package com.example.schoolproj.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.schoolproj.R;
import com.example.schoolproj.TeacherMark;
import com.example.schoolproj.classes.Mark;

import java.util.List;

public class MarkAdapter extends RecyclerView.Adapter<MarkAdapter.ViewHolder> {
    private static final String TAG = "MarkAdapter";

    private List<Mark> markList;
    private Context context;

    public MarkAdapter(List<Mark> markList, Context context) {
        this.markList = markList;
        this.context = context;
        Log.d(TAG, "MarkAdapter: Created with " + markList.size() + " marks");
    }

    @Override
    public MarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.markcard, parent, false);
        Log.d(TAG, "onCreateViewHolder: Created new view holder");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarkAdapter.ViewHolder holder, int position) {
        Mark mark = markList.get(position);
        Log.d(TAG, "onBindViewHolder: Binding mark at position " + position + ": " + mark.getMarkType());

        holder.txtMarkName.setText(mark.getMarkType());
        holder.edtMark.setText(String.valueOf(mark.getMarkValue()));

        holder.btnSave.setOnClickListener(v -> {
            String markValueStr = holder.edtMark.getText().toString();

            if (!markValueStr.isEmpty()) {

                ((TeacherMark) context).updateMark(mark.getMarkId(), markValueStr);
            } else {
                Toast.makeText(context, "please insert the value ", Toast.LENGTH_SHORT).show();
            }
        });



        holder.btnDelete.setOnClickListener(v -> {

            ((TeacherMark) context).DeleteMark(mark.getMarkId());
        });

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: Returning " + markList.size() + " items");
        return markList.size();
    }

    public void setFilter(List<Mark> filteredList) {
        this.markList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMarkName;
        EditText edtMark;
        Button btnSave;

        Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMarkName = itemView.findViewById(R.id.name_mark);
            edtMark = itemView.findViewById(R.id.Edit_mark);
            btnSave = itemView.findViewById(R.id.ButtonSave);
            btnDelete = itemView.findViewById(R.id.deleteMarkButton);
            Log.d(TAG, "ViewHolder: Initialized views");
        }
    }
}