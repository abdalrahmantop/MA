package com.example.schoolproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {


    Button btnViewSchedule, btnPublishMarks, btnSendAssignment;
    private   int teacherid = 2;

    TextView txtTeacherName, txtTeacherId;
    ImageView imgTeacher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnViewSchedule = findViewById(R.id.btnViewSchedule);
        btnPublishMarks = findViewById(R.id.btnPublishMarks);
         txtTeacherName = findViewById(R.id.txtTeacherName);
        txtTeacherId = findViewById(R.id.txtTeacherId);
        imgTeacher = findViewById(R.id.imgTeacher);

        getTeacher();

        btnViewSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Intent intent = new Intent(MainActivity.this, ViewTeacherScheduleActivity.class);
                // startActivity(intent);
            }
        });


        btnPublishMarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TeacherSubjects.class);
                intent.putExtra("teacher_id", teacherid);
                startActivity(intent);
            }
        });



    }


    public void getTeacher() {
        String url = "http://10.0.2.2/get_Teacher.php?teacher_id=" + teacherid;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.getString("status").equals("success")) {
                            JSONObject teacher = response.getJSONObject("teacher");
                            txtTeacherName.setText(teacher.getString("name"));
                            txtTeacherId.setText("ID: " + teacher.getInt("teacher_id"));


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

}
