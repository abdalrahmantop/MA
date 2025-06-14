package com.example.schoolproj;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.schoolproj.Adapters.MarkAdapter;
import com.example.schoolproj.classes.Mark;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherMark extends AppCompatActivity {
    private static final String TAG = "TeacherMark";


    private EditText markTypeEditText;
    private Button addMarkTypeButton;
    private Button btnSearch;
    private EditText editTextSearch;
    private RecyclerView recyclerView;

    private List<Mark> Marklist;
    private MarkAdapter adapter;
    private RequestQueue requestQueue;

    private int studentId;
    private int subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.markpage);

        markTypeEditText = findViewById(R.id.markTypeEditText);
        addMarkTypeButton = findViewById(R.id.addMarkTypeButton);
        btnSearch = findViewById(R.id.btn_search);
        editTextSearch = findViewById(R.id.edt_search);
        recyclerView = findViewById(R.id.recycler_students);

        studentId = getIntent().getIntExtra("student_id", -1);
        subjectId = getIntent().getIntExtra("subject_id", -1);

        Log.d(TAG, "onCreate: Student ID = " + studentId + ", Subject ID = " + subjectId);

        if (studentId == -1 || subjectId == -1) {
            Log.e(TAG, "onCreate: Invalid IDs received");
            Toast.makeText(this, "خطأ في البيانات المستلمة", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Marklist = new ArrayList<>();
        adapter = new MarkAdapter(Marklist, this);
        recyclerView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        loadMark();

        btnSearch.setOnClickListener(v -> {
            String text = editTextSearch.getText().toString();
            List<Mark> filtered = filterMarks(text);
            adapter.setFilter(filtered);
        });

        addMarkTypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String markType = markTypeEditText.getText().toString().trim();
                if (!markType.isEmpty()) {
                    Log.d(TAG, "Adding new mark type: " + markType);
                    AddMark(markType);
                } else {
                    Toast.makeText(TeacherMark.this, "أدخل نوع العلامة", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMark() {
        String url = "http://10.0.2.2/get_mark.php?student_id=" + studentId + "&subject_id=" + subjectId;
        Log.d(TAG, "loadMark: Fetching marks from URL: " + url);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "loadMark: Received response: " + response.toString());
                        Marklist.clear();
                        try {
                            if (response.length() == 0) {
                                Log.w(TAG, "loadMark: No marks found for student_id=" + studentId + " and subject_id=" + subjectId);
                                Toast.makeText(TeacherMark.this, "لا توجد علامات لهذا الطالب", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject markObject = response.getJSONObject(i);
                                Log.d(TAG, "loadMark: Processing mark object: " + markObject.toString());

                                try {
                                    int markId = markObject.getInt("mark_id");
                                    String type = markObject.getString("mark_type");
                                    String value = markObject.getString("mark_value");

                                    Log.d(TAG, String.format("loadMark: Parsed mark - ID: %d, Student: %d, Subject: %d, Type: %s, Value: %s",
                                            markId, studentId, subjectId, type, value));

                                    Mark mark = new Mark(
                                            studentId,
                                            subjectId,
                                            markId,
                                            value,
                                            type
                                    );
                                    Marklist.add(mark);
                                } catch (JSONException e) {
                                    Log.e(TAG, "loadMark: Error parsing mark object at index " + i, e);
                                    Log.e(TAG, "loadMark: Problematic JSON object: " + markObject.toString());
                                }
                            }

                            Log.d(TAG, "loadMark: Total marks loaded: " + Marklist.size());
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            Log.e(TAG, "loadMark: Error parsing JSON", e);
                            Toast.makeText(TeacherMark.this, "Error parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "loadMark: Error fetching data", error);
                Toast.makeText(TeacherMark.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    public List<Mark> filterMarks(String text) {
        List<Mark> filteredList = new ArrayList<>();
        for (Mark mark : Marklist) {
            if (mark.getMarkType().toLowerCase().contains(text.toLowerCase()) ||
                mark.getMarkValue().toString().contains(text)) {
                filteredList.add(mark);
            }
        }
        return filteredList;
    }

    public void AddMark(String namemark) {
        String url = "http://10.0.2.2/AddMark.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Done add ", Toast.LENGTH_SHORT).show();
                    markTypeEditText.setText("");
                    loadMark();
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", String.valueOf(studentId));
                params.put("subject_id", String.valueOf(subjectId));
                params.put("mark_type", namemark);
                params.put("mark_value", "0");
                return params;
            }
        };

        requestQueue.add(request);
    }


    public void DeleteMark(int markId) {
        String url = "http://10.0.2.2/DeleteMark.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, "Done: " + response, Toast.LENGTH_SHORT).show();
                    loadMark();
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mark_id", String.valueOf(markId));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


    public void updateMark(int markId, String newValue) {
        String url = "http://10.0.2.2/UpdateMark.php";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                response -> {
                    Toast.makeText(this, response, Toast.LENGTH_SHORT).show();
                    loadMark();
                    adapter.notifyDataSetChanged();
                },
                error -> {
                    Toast.makeText(this, "Error ", Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("mark_id", String.valueOf(markId));
                params.put("mark_value", newValue);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }


}
