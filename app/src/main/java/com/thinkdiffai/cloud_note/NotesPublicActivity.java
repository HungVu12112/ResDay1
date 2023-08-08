package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cloud_note.R;

public class NotesPublicActivity extends AppCompatActivity {
    private ImageView imgBack;
    private TextView tvLogin;
    private RecyclerView rcvListNotePublic;
    Intent intentGetData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_public);
        intentGetData = getIntent();

        imgBack = (ImageView) findViewById(R.id.img_back);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        rcvListNotePublic = (RecyclerView) findViewById(R.id.rcv_listNotePublic);
        imgBack.setOnClickListener( view->{
            onBackPressed();
        });
        tvLogin.setOnClickListener(view->{
            Intent intent = new Intent(NotesPublicActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}