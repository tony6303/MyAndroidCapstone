package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //싱글턴 패턴
    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();

    private EditText mMainSearchAreaText;
    private String id;  //파이어베이스 primary key 설정용

    //리사이클러뷰 = 게시판 나오는 레이아웃
    private RecyclerView mMainRecyclerview;

    //리사이클러뷰 어댑터
    private MainAdapter mAdapter;
    private List<Board> mBoardList;

    //내가만든 클래스의 생성자 사용
    //이미지 슬라이드용 (뷰페이저)
    MainImageAdapter mImageAdapter; //
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewPager = findViewById(R.id.viewpager);
        mImageAdapter = new MainImageAdapter(this); //new 로 인스턴스 생성
        mViewPager.setAdapter(mImageAdapter);

        mMainRecyclerview = findViewById(R.id.main_recyclerview); //MainActivity RecyclerView
        mBoardList = new ArrayList<>();  //Board 객체를 담을 ArrayList

        findViewById(R.id.writebutton).setOnClickListener(this); // 이 버튼의 이벤트는 OnClick 함수에 있습니다

            // 파이어베이스에서 실시간으로 데이터 읽어오기
            mStore.collection("board").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()){
                    String id = (String)dc.getDocument().getData().get("id");
                    String title = (String)dc.getDocument().getData().get("title");
                    String contents = (String)dc.getDocument().getData().get("contents");
                    String name = (String)dc.getDocument().getData().get("name");
                    String area = (String) dc.getDocument().getData().get("firstArea");
                    String work = (String)dc.getDocument().getData().get("worklist");
                    String day = (String)dc.getDocument().getData().get("weekendorweekday");


                    Board data = new Board(id,title,contents,name,area,work,day); //생성자 호출

                    mBoardList.add(data);
                }
                mAdapter = new MainAdapter(mBoardList);
                mMainRecyclerview.setAdapter(mAdapter);
            }
        });

        mMainSearchAreaText = findViewById(R.id.search_main_area_text); //MainActivity EditText
        Button mainSearchButton = findViewById(R.id.search_main_button); //MainActivity Button(검색)

        //검색 버튼을 누르면 발동하는 이벤트, 파이어베이스에서 title(Editext에 쓴 내용)으로 검색한다
        mainSearchButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사용자 함수 바로밑에있음
                searchData(mMainSearchAreaText.getText().toString());
            }
        });
    }  // Oncreate 끝

    //Query문을 사용하여 데이터를 검색하는 사용자 함수
    public void searchData(String s){
        //FireStore에서 Query Like문을 사용하고 싶었는데 어떻게 쓰는지 알 수가 없었습니다. 자료가 없네요..
        mStore.collection("board").whereEqualTo("firstArea",s) //검색하는 글자가 정확히 일치해야한다.
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() { //스냅샷 리스너가 아니라 컴플리트 리스너입니다. 차이는 모름
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        mBoardList.clear();  //원래 있었던 어댑터를 초기화시킵니다.
                        for(DocumentSnapshot dc : task.getResult()){
                            String id = dc.getString("id");
                            String title = dc.getString("title");
                            String contents = dc.getString("contents");
                            String name = dc.getString("name");
                            String area = dc.getString("firstArea");
                            String work = dc.getString("worklist");
                            String day = dc.getString("weekendorweekday");

                            Board data = new Board(id,title,contents,name,area,work,day);

                            mBoardList.add(data);
                        }
                        mAdapter = new MainAdapter(mBoardList);
                        mMainRecyclerview.setAdapter(mAdapter);
                        Toast.makeText(MainActivity.this,"검색완료",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }



    @Override
    public void onClick(View v) { //연필 버튼 누르면 글 작성 액티비티로 이동함
        startActivity(new Intent(this,WriteActivity.class));
    }
}
