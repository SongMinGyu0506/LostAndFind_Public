package com.example.lostandfind.activity.Main;

import static android.content.ContentValues.TAG;
import static android.widget.Toast.makeText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfind.R;
import com.example.lostandfind.data.Post;
import com.example.lostandfind.data.UserData;
import com.example.lostandfind.query.main.MainCreateQuery;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class MainCreateActivity extends AppCompatActivity {
    Toolbar toolbar;
    UserData tmp_user;
    FirebaseFirestore db;
    FirebaseUser user;
    ImageView img;
    String imageName; // 파일명
    EditText etTitle, etPlace, etDetails;
    TextView etDate;
    RadioGroup radiogroup; // 상태(보관 중/완료)
    String Status;
    Spinner spinner; // 카테고리
    Button btnAdd;
    Intent intent;
    private static final int REQUEST_CODE = 0; // 이미지 사진 요청코드
    String user_name;
    MainCreateQuery mainCreateQuery;
    Uri uri;

//    private FirebaseStorage storage;

    // menu_activity_main.xml를 inflate
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main_create, menu);
        
        // "등록" 메뉴 색상 설정
        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); // fix the color to white
//            spanString.setSpan(new AbsoluteSizeSpan(18, true), 0, spanString.length(), 0); // fix the text size to 18dp
            item.setTitle(spanString);
        }
        return true ;
    }

    // 메뉴 클릭 이벤트 처리
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // "등록" 메뉴 클릭 시
            case R.id.action_create :
                String title = etTitle.getText().toString();
                String category = spinner.getSelectedItem().toString();
                String place = etPlace.getText().toString();
                String date = etDate.getText().toString();
                String status = Status;
                String details = etDetails.getText().toString();
                String user_email = mainCreateQuery.getUserEmail();
                String user_uid = mainCreateQuery.getUserUid();
//                String user_email = user.getEmail().toString();
//                String user_uid = user.getUid().toString();
                if (uri != null && imageName != null) {
                    mainCreateQuery.imageUpload(uri,imageName);
                }
//                StorageReference storageRef = storage.getReference();
//                StorageReference riverRef = storageRef.child("photo/"+imageName);
//                UploadTask uploadTask = riverRef.putFile(uri);
//
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e(TAG,"Developer: Image Upload Error:",e);
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        makeText(MainCreateActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
//                    }
//                });


                //Post temp_post = new Post(imageName, title, category, place, date, status, details, user_email, tmp_user.getName(), user_uid);

                mainCreateQuery.registerPost(new Post(imageName, title, category, place, date, status, details, user_email, tmp_user.getName(), user_uid));
//                db.collection("Posts").add(temp_post)
//                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                Log.d(TAG,"add data");
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG,"Error!",e);
//                    }
//                });
                Intent intent = new Intent(MainCreateActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;

            // "X" 클릭 시
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_create);

        // toolbar 생성, 타이틀 설정
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); // toolbar를 액티비티의 App Bar로 지정
        ActionBar appbar = getSupportActionBar(); // toolbar에 대한 참조 획득

        // toolbar - 뒤로 가기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        mainCreateQuery = new MainCreateQuery(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
//        storage = FirebaseStorage.getInstance();

        //user_name = mainCreateQuery.getUserName();

        db.collection("Users")
                .whereEqualTo("uid",user.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                tmp_user = documentSnapshot.toObject(UserData.class);
                            }
                        }
                    }
                });

        etTitle = (EditText)findViewById(R.id.c_title);
        etPlace = (EditText)findViewById(R.id.c_place);
        etDate = (TextView)findViewById(R.id.c_date);
        radiogroup = (RadioGroup)findViewById(R.id.radio_status); // 상태(보관 중/완료)
        etDetails = (EditText)findViewById(R.id.c_details);

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

        // 카테고리
        ArrayList<String> category = new ArrayList<String>();
        category.add("가방");
        category.add("귀금속");
        category.add("도서");
        category.add("스포츠용품");
        category.add("악기");
        category.add("의류");
        category.add("전자기기");
        category.add("지갑");
        category.add("카드");
        category.add("현금");
        category.add("휴대폰");
        category.add("기타");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_text, category);
        ((Spinner)findViewById(R.id.c_category)).setAdapter(adapter);
        spinner = (Spinner)findViewById(R.id.c_category);

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

        //작성자 유저 데이터 데이터베이스에서 가져옴
//        tmp_user = mainCreateQuery.getUserData();
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
    }

    // 이미지 관련
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                try {
                    uri = data.getData();
                    imageName = uri.getLastPathSegment(); // 파일path에서 파일명만 가져오기
                    Glide.with(getApplicationContext()).load(uri).into(img); // 다이얼로그 이미지 사진에 넣기
                } catch (Exception e) {
                    Log.e(TAG,"Developer Log: Error",e);
                }
            } else if (resultCode == RESULT_CANCELED) { // 취소 시 호출할 행동

            }
        }
    }

    // 체크된 라디오 버튼 값을 String인 Status에 저장
    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch(v.getId()){
            case R.id.radio_keep:
                if(checked) {
                    Status="보관 중";
                }
                break;

            case R.id.radio_complete:
                if(checked) {
                    Status="완료";
                }
                break;
        }
    }
}