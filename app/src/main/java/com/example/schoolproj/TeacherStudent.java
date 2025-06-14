package com.example.schoolproj;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproj.Adapters.StudentAdapter;
import com.example.schoolproj.classes.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherStudent extends AppCompatActivity {

    private RecyclerView recyclerView;

    private StudentAdapter adapter;
    private List<Student> studentList;
    private int subjectId;

    private String nameS;
    private int teacher_id;

    private Button btnSearch;

    private EditText editText;

    private TextView NameSubjects;
    FloatingActionButton fabAddHomework;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentpage);

        subjectId = getIntent().getIntExtra("subject_id", -1);
        teacher_id = getIntent().getIntExtra("teacher_id", -1);
        nameS = getIntent().getStringExtra("subject_name");

        Toast.makeText(this, "Subject ID: " + subjectId + ", Teacher ID: " + teacher_id, Toast.LENGTH_LONG).show();

        recyclerView = findViewById(R.id.recycler_students);
        btnSearch = findViewById(R.id.btnSearch);
        editText = findViewById(R.id.editTextSearch);
        NameSubjects = findViewById(R.id.subject_name);
        NameSubjects.setText(nameS);
          fabAddHomework = findViewById(R.id.fab_add_homework);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        studentList = new ArrayList<>();
        loadStudents(subjectId);


        btnSearch.setOnClickListener(v -> {
            String text = editText.getText().toString();
            List<Student> filtered = filterStudents(text);
            adapter.setFilter(filtered);
        });


        fabAddHomework.setOnClickListener(e ->{

            Intent intent = new Intent(this, TeacherHomework.class);
             intent.putExtra("subject_id", subjectId);
            this.startActivity(intent);
        });

    }

    private void loadStudents(int subjectId) {
        String url = "http://10.0.2.2/get_student.php?subject_id=" + subjectId;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    studentList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject studentObject = response.getJSONObject(i);
                            int id = studentObject.getInt("student_id");
                            String name = studentObject.getString("name");

                            studentList.add(new Student(id, name));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    adapter = new StudentAdapter(studentList, subjectId, this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }, error -> {
            error.printStackTrace();
        });

        queue.add(request);
    }

    public List<Student> filterStudents(String text) {
        List<Student> filteredList = new ArrayList<>();
        for (Student s : studentList) {
            if (s.getName().contains(text) || String.valueOf(s.getId()).contains(text)) {
                filteredList.add(s);
            }
        }
        return filteredList;
    }

}
