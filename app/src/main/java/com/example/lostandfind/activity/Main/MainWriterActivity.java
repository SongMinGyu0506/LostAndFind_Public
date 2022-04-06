package com.example.lostandfind.activity.Main;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lostandfind.R;
import com.example.lostandfind.adapter.MainAdapter;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class MainWriterActivity extends AppCompatActivity {
    UserData tmp_user;
    FirebaseFirestore db;
    FirebaseUser user;
    EditText etDate,etTitle,etText,etPlace;
    Button btnAdd;
    Calendar myCalendar = Calendar.getInstance();
    Intent intent;

    //분실,습득 날짜 지정 다이얼로그 관련 함수
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            myCalendar.set(Calendar.YEAR,i);
            myCalendar.set(Calendar.MONTH,i1);
            myCalendar.set(Calendar.DAY_OF_MONTH,i2);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_writer);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("분실물 작성");
        //Toast.makeText(this, user.getEmail().toString(), Toast.LENGTH_SHORT).show();

        etDate = (EditText)findViewById(R.id.etDate);
        etDate.setKeyListener(null);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                inputManager.hideSoftInputFromWindow(etDate.getWindowToken(),0);
                new DatePickerDialog(MainWriterActivity.this,myDatePicker,myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etTitle = (EditText)findViewById(R.id.etTitle);
        etText = (EditText)findViewById(R.id.etText);
        etPlace = (EditText)findViewById(R.id.etPlace);
        btnAdd = (Button)findViewById(R.id.btnAdd);

        //작성자 유저 데이터 데이터베이스에서 가져옴
        db.collection("Users")
                .whereEqualTo("uid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            tmp_user = document.toObject(UserData.class);
                        }
                    }
                });

        //등록 버튼 클릭할 때
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //게시글 객체 생성
                Post post = new Post(etTitle.getText().toString(),
                        etText.getText().toString(),
                        etDate.getText().toString()
                        ,user.getEmail(),
                        tmp_user.getName(),
                        user.getUid(),
                        etPlace.getText().toString());

                //디비에 등록함
                db.collection("Posts").add(post);

                //다시 액티비티 돌아가야하는데 리사이클러 뷰 업데이트 시켜줘야함
                intent = new Intent();
                intent.putExtra("post",post);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    //날짜 다이얼로그 글 양식 맞추기
    private void updateLabel() {
        String myFormat = "yyyy/MM/dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        EditText et_date = (EditText)findViewById(R.id.etDate);
        et_date.setText(sdf.format(myCalendar.getTime()));
    }

    //액션바 돌아가기 기능
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}