package com.example.schoolproj.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproj.TeacherMark;
import com.example.schoolproj.R;
import com.example.schoolproj.TeacherMark;
import com.example.schoolproj.classes.Student;
import com.example.schoolproj.classes.Subject;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> studentList;
    private int subjectId;
    private Context context;

    public StudentAdapter(List<Student> studentList, int subjectId, Context context) {
        this.studentList = studentList;
        this.subjectId = subjectId;
        this.context = context;
    }

    @Override
    public StudentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.studentcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StudentAdapter.ViewHolder holder, int position) {
        Student student = studentList.get(position);

        holder.txtName.setText(student.getName());
        holder.txtId.setText(String.valueOf(student.getId()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, TeacherMark.class);
            intent.putExtra("student_id", student.getId());
            intent.putExtra("subject_id", subjectId);
            context.startActivity(intent);
        });



    }
    public void setFilter(List<Student> filteredList) {
        this.studentList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtId;

        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.text_student_name);
            txtId = itemView.findViewById(R.id.text_student_id);
        }
    }
}
