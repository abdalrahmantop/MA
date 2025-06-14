package com.example.schoolproj.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproj.R;
 import com.example.schoolproj.TeacherStudent;
import com.example.schoolproj.classes.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder> {

    private List<Subject> subjectList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Subject subject);
    }

    public SubjectAdapter(List<Subject> subjectList, Context context ) {
        this.subjectList = subjectList;
        this.context = context;

    }

    @NonNull
    @Override
    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.subjectcard, parent, false);
        return new SubjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.subjectName.setText(subject.getName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TeacherStudent.class);
            intent.putExtra("subject_id", subject.getId());
            intent.putExtra("teacher_id", subject.getTeacherid());
            intent.putExtra("subject_name" , subject.getName());
            context.startActivity(intent);
        });
    }

    public void setFilter(List<Subject> filteredList) {
        this.subjectList = filteredList;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectName;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectName = itemView.findViewById(R.id.tv_subject_name);
        }
    }
}
