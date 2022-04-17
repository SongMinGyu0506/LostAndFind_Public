package com.example.lostandfind.activity.Main;

import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.data.UserData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.Calendar;

public class MainCreateActivity extends AppCompatActivity {
    Toolbar toolbar;
    UserData tmp_user;
    FirebaseFirestore db;
    FirebaseUser user;
    EditText etTitle, etCategory, etType, etPlace, etDate, etStatus,etDetails;
    Button btnAdd;
    Intent intent;
    ImageView img;
    private static final int REQUEST_CODE = 0; // 이미지 사진 요청코드

    // menu_activity_main.xml를 inflate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main_create, menu) ;

        return true ;
    }

    // 메뉴 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create : // "등록" 메뉴 클릭 시
                makeText(getApplicationContext(), "등록 등록 등록",Toast.LENGTH_SHORT).show();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_create);

        // toolbar 생성, 타이틀 설정
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("습득물/분실물 등록");

        setSupportActionBar(toolbar); // toolbar를 액티비티의 App Bar로 지정
        ActionBar appbar = getSupportActionBar(); // toolbar에 대한 참조 획득

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        // 이미지
        img = findViewById(R.id.btn_img);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // 습득 및 분실 날짜
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        TextView date = (TextView)findViewById(R.id.c_date); // 날짜 표시할 텍스트뷰
        ImageView btn_date = findViewById(R.id.btn_calendar); // 달력 아이콘

        // 날짜 지정 다이얼로그 관련 함수
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int c_year, int c_month, int c_day) {
                // 달력 아이콘 눌러서 선택한 날짜를 텍스트뷰에 바인딩
                date.setText(c_year + "/" + c_month + "/" + c_day);
            }
        }, year, month, day);

        // 달력 아이콘 클릭 이벤트
        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_date.isClickable()) {
                    datePickerDialog.show();
                }
            }
        });


        EditText etTitle, etCategory, etType, etPlace, etDate, etStatus,etDetails;




//        etDate = (EditText)findViewById(R.id.c_date);
//        etDate.setKeyListener(null);
//        etDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
////                inputManager.hideSoftInputFromWindow(etDate.getWindowToken(),0);
//                new DatePickerDialog(MainCreateActivity.this,myDatePicker,myCalendar.get(Calendar.YEAR),
//                        myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//            }
//        });
//        etTitle = (EditText)findViewById(R.id.c_title);
//        etText = (EditText)findViewById(R.id.etText);
//        etPlace = (EditText)findViewById(R.id.etPlace);
//        btnAdd = (Button)findViewById(R.id.btnAdd);
//
//
//
//        //작성자 유저 데이터 데이터베이스에서 가져옴
//        db.collection("Users")
//                .whereEqualTo("uid",user.getUid())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        for (QueryDocumentSnapshot document : task.getResult()) {
//                            tmp_user = document.toObject(UserData.class);
//                        }
//                    }
//                });
//
//        //등록 버튼 클릭할 때
//        btnAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // 게시글 객체 생성
//                Post post = new Post(etTitle.getText().toString(),
//                        etText.getText().toString(),
//                        etDate.getText().toString()
//                        ,user.getEmail(),
//                        tmp_user.getName(),
//                        user.getUid(),
//                        etPlace.getText().toString());
//
//                // 디비에 등록함
//                db.collection("Posts").add(post);
//
//                // 다시 액티비티 돌아가야 하는데 리사이클러 뷰 업데이트 시켜줘야함
//                intent = new Intent();
//                intent.putExtra("post",post);
//                setResult(Activity.RESULT_OK,intent);
//                finish();
//            }
//        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    Glide.with(getApplicationContext()).load(uri).into(img); // 다이얼로그 이미지 사진에 넣기
                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) { // 취소 시 호출할 행동

            }
        }
    }



}