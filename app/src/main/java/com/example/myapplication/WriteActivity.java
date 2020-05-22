package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1beta1.Write;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private EditText mWriteNameText;

    private String[] ArrayFirstArea = {"부산", "김해", "창원"}; //Spinner에 들어갈 배열
    private Spinner mFirstArea;

    private String[] ArrayWorklist_busan = {"부산작물재배", "작물심기"};
    private String[] ArrayWorklist_kimhae = {"김해작물재배", "작물심기"};
    private String[] ArrayWorklist_chngwon = {"창원작물재배", "작물심기"};
    private Spinner mWorkList;

    private String[] ArrayWeek = {"주말", "평일"};
    private Spinner mWeekend;

    private Button button; //플로팅액션 버튼 (파란연필)
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentsText = findViewById(R.id.write_contents_text);
        mWriteNameText = findViewById(R.id.write_name_text);

        mFirstArea = (Spinner)findViewById(R.id.first_area);
        mFirstArea.setPrompt("지역 선택");
        ArrayAdapter<String> arealist;
        arealist = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, ArrayFirstArea);
        mFirstArea.setAdapter(arealist);

        //스피너에서 부산 골랐을때 나머지 스피너 변경하게 해주는 함수
        mFirstArea.setOnItemSelectedListener(this);  //이게 있어야 override한 onItemSelect 함수를 쓸 수 있는것같습니다.

        //FirstArea에 따라 바뀌게 하기위해 setAdapter함수는 아래에 override 해놓았습니다.
        mWorkList = (Spinner)findViewById(R.id.work_list);
        mWorkList.setPrompt("작업 선택");

        mWeekend = (Spinner)findViewById(R.id.weekend_or_weekday);
        mWeekend.setPrompt("날짜 선택");
        ArrayAdapter<String> weeklist;
        weeklist = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, ArrayWeek);
        mWeekend.setAdapter(weeklist);

        button = findViewById(R.id.write_upload_button);
        button.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //id는 Primary Key 와 같다. 랜덤으로 하나 줍니다.
                id = mStore.collection("board").getId();

                Map<String, Object> post = new HashMap<>();
                //파이어베이스 데이터베이스에 데이터를 put 합니다.
                post.put("id", id);

                //스피너 배열 속 아이템
                post.put("firstArea",mFirstArea.getSelectedItem().toString());
                post.put("worklist",mWorkList.getSelectedItem().toString());
                post.put("weekendorweekday",mWeekend.getSelectedItem().toString());

                //EditText 속 문자
                post.put("title", mWriteTitleText.getText().toString());
                post.put("contents", mWriteContentsText.getText().toString());
                post.put("name", mWriteNameText.getText().toString());

                mStore.collection("board").add(post)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(WriteActivity.this,"업로드성공",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(WriteActivity.this,"업로드실패",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    //스피너가 선택되었을 때 동작하는 이벤트 함수
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ArrayAdapter<String> worklist;
        if(mFirstArea.getSelectedItem().toString().equals("부산")) { //지역 스피너가 부산이면 작업 스피너를 알맞게 setAdapt 한다. 이하동일
            worklist = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ArrayWorklist_busan);
            mWorkList.setAdapter(worklist); //작업선택 스피너에 어댑터 연결
        } else if(mFirstArea.getSelectedItem().toString().equals("김해")){
            worklist = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ArrayWorklist_kimhae);
            mWorkList.setAdapter(worklist);
        } else if(mFirstArea.getSelectedItem().toString().equals("창원")){
            worklist = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, ArrayWorklist_chngwon);
            mWorkList.setAdapter(worklist);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
