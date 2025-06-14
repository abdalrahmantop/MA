package com.example.schoolproj;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.database.Cursor;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class TeacherHomework extends AppCompatActivity {

    private static final int FILE_REQUEST_CODE = 100;
    int SubjectId;
    EditText etTitle, etDescription, etDueDate;
    Button btnChooseFile, btnSaveHomework;
    TextView tvFileName;

    Uri selectedFileUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework);

        etTitle = findViewById(R.id.et_title);
        etDescription = findViewById(R.id.et_description);
        etDueDate = findViewById(R.id.et_due_date);
        btnChooseFile = findViewById(R.id.btn_choose_file);
        tvFileName = findViewById(R.id.tv_file_name);
        btnSaveHomework = findViewById(R.id.btn_save_homework);
        SubjectId = getIntent().getIntExtra("subject_id", -1);

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        btnSaveHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendHomework(SubjectId,etTitle.getText().toString() , etDescription.getText().toString() , etDueDate.getText().toString() , tvFileName.getText().toString());
            }
        });


        EditText etDueDate = findViewById(R.id.et_due_date);

        etDueDate.setOnClickListener(v -> {

            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> {

                        String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                        etDueDate.setText(selectedDate);
                    }, year, month, day);

            datePickerDialog.show();
        });

    }


    public void sendHomework(int subjectId, String title, String description, String dueDate, String filePath) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://10.0.2.2/AddHomework.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Homework added successfully", Toast.LENGTH_SHORT).show();

                },
                error -> {
                    Toast.makeText(this, "Error adding homework", Toast.LENGTH_SHORT).show();
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("subject_id", String.valueOf(subjectId));
                params.put("title", title);
                params.put("description", description);
                params.put("due_date", dueDate);
                params.put("file_path", filePath);
                return params;
            }
        };

        requestQueue.add(request);
    }

}
