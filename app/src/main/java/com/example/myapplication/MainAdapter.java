package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainviewHolder>{

    private List<Board> mBoardList;

    public MainAdapter(List<Board> mBoardList) {
        this.mBoardList = mBoardList;
    }

    @NonNull
    @Override
    public MainAdapter.MainviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainAdapter.MainviewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.MainviewHolder holder, int position) {
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
