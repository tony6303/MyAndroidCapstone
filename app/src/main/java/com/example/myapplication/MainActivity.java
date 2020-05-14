package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseFirestore mStore = FirebaseFirestore.getInstance();
    private EditText mMainWriteTitleText;
    private String id;

    private RecyclerView mMainRecyclerview;

    private MainAdapter mAdapter;
    private List<Board> mBoardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMainRecyclerview = findViewById(R.id.main_recyclerview); //MainActivity RecyclerView

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


                    Board data = new Board(id,title,contents,name,area,work,day);

                    mBoardList.add(data);
                }
                mAdapter = new MainAdapter(mBoardList);
                mMainRecyclerview.setAdapter(mAdapter);
            }
        });
        // Read firebase
        /*mStore.collection("board").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String id = (String)document.getData().get("id");
                                String title = (String)document.getData().get("title");
                                String contents = (String)document.getData().get("contents");
                                String name = (String)document.getData().get("name");

                                Board data = new Board(id,title,contents,name);

                                mBoardList.add(data);

                            }
                        }
                    }
                });
*/
        mMainWriteTitleText = findViewById(R.id.write_main_title_text); //MainActivity EditText
        Button mainWriteButton = (Button)findViewById(R.id.write_main_button); //MainActivity Button(작성)

        //작성 버튼을 누르면 발동하는 이벤트, 파이어베이스에 id, title(Editext에 쓴 내용)을 저장한다
        mainWriteButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = mStore.collection("board").document().getId();

                Map<String, Object> post = new HashMap<>();
                post.put("id", id);
                //post.put("title", mWriteTitleText.getText().toString());
                post.put("title", mMainWriteTitleText.getText().toString());  // title이 MainActivity에 보이는 속성입니다 contetns는 안보여요
                //post.put("name", mWriteNameText.getText().toString());

                mStore.collection("board").document().set(post)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this,"업로드성공",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"업로드실패",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        mBoardList = new ArrayList<>();
        //mBoardList.add(new Board(null,"안녕하세요",null,"android"));
        //mBoardList.add(new Board(null,"ㅋㅋㅋ",null,"python"));
        //mBoardList.add(new Board(null,"어려워요",null,"java"));

        //mAdapter = new MainAdapter(mBoardList);
        //mMainRecyclerview.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) { //연필 버튼 누르면 글 작성 액티비티로 이동함
        startActivity(new Intent(this,WriteActivity.class));
    }
    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainviewHolder>{

        private List<Board> mBoardList;

        public MainAdapter(List<Board> mBoardList) {
            this.mBoardList = mBoardList;
        }

        @NonNull
        @Override
        public MainviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainviewHolder holder, int position) {
            Board data = mBoardList.get(position);
            holder.mTitleTextView.setText(data.getTitle());
            holder.mContentsTextView.setText(data.getContents());
            holder.mNameTextView.setText(data.getName());
            holder.mAreaTextView.setText(data.getArea());
            holder.mWorkTextView.setText(data.getWork());
            holder.mDayTextView.setText(data.getDay()); //Board 클래스 미완성
        }

        @Override
        public int getItemCount() {  //보드 리스트 사이즈만큼
            return mBoardList.size();
        }

        class MainviewHolder extends RecyclerView.ViewHolder{

            private TextView mTitleTextView;
            private TextView mContentsTextView;
            private TextView mNameTextView;
            private TextView mAreaTextView;
            private TextView mWorkTextView;
            private TextView mDayTextView;

            public MainviewHolder(@NonNull View itemView) {
                super(itemView);

                mTitleTextView = itemView.findViewById(R.id.item_title_text);
                mContentsTextView = itemView.findViewById(R.id.item_contents_text);
                mNameTextView = itemView.findViewById(R.id.item_name_text);
                mAreaTextView = itemView.findViewById(R.id.item_area_text);
                mWorkTextView = itemView.findViewById(R.id.item_work_text);
                mDayTextView = itemView.findViewById(R.id.item_day_text);

            }
        }
    }
}
