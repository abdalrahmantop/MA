package com.example.schoolproj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproj.Adapters.SubjectAdapter;
import com.example.schoolproj.classes.Subject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeacherSubjects extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubjectAdapter adapter;
    private List<Subject> subjectList;

    private int teacherId;
    private Button searchbtn;

    private EditText editText ;

    private static final String BASE_URL = "http://10.0.2.2/get_subjects.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subjectpage);


        teacherId = getIntent().getIntExtra("teacher_id", -1);
        if (teacherId == -1) {
            Toast.makeText(this, "خطأ في معرف المعلم", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recycler_subjects);
        searchbtn = findViewById(R.id.btn_search);
        editText = findViewById(R.id.edt_search);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        subjectList = new ArrayList<>();
        loadSubjects();

        searchbtn.setOnClickListener(e -> {
            String text = editText.getText().toString().trim();
            List<Subject> filtered = Filter(text);
            adapter.setFilter(filtered);
        });


    }


    public List<Subject> Filter (String text){
        List<Subject> subjectfilter = new ArrayList<>();
        for (Subject s : subjectList){
            if (s.getName().contains(text)){
                subjectfilter.add(s);
            }
        }

        return subjectfilter;
    }
    private void loadSubjects() {
        String url = BASE_URL + "?teacher_id=" + teacherId;
        
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONObject teacherObj = response.getJSONObject("teacher");
                        String teacherName = teacherObj.getString("name");
                        String teacherPhone = teacherObj.getString("phone");
                        String teacherEmail = teacherObj.getString("email");
                        
                         setTitle("المواد - " + teacherName);
                        

                        JSONArray subjectsArray = response.getJSONArray("subjects");
                        subjectList.clear();
                        for (int i = 0; i < subjectsArray.length(); i++) {
                            JSONObject object = subjectsArray.getJSONObject(i);
                            int id = object.getInt("subject_id");
                            String name = object.getString("name");
                            int teacherId = object.getInt("teacher_id");
                            
                            Subject subject = new Subject(id, name);
                            subject.setTeacherid(teacherId);
                            subjectList.add(subject);
                        }
                        
                        adapter = new SubjectAdapter(subjectList, TeacherSubjects.this);
                        recyclerView.setAdapter(adapter);
                        
                    } catch (JSONException e) {
                        Log.e("TeacherSubjects", "Error parsing JSON: " + e.getMessage());
                        Toast.makeText(TeacherSubjects.this, "error", Toast.LENGTH_SHORT).show();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("TeacherSubjects", "Error: " + error.getMessage());
                    Toast.makeText(TeacherSubjects.this, "خطأ في الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                }
            }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsonObjectRequest);
    }
}
