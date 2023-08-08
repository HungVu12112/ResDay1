package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;

public class LoadingActivity extends AppCompatActivity {
    Login dao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Handler handler = new Handler();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dao = new Login(LoadingActivity.this);
                Model_State_Login obj = dao.getLogin();
                Log.d("TAG", "run:IdUser: "+obj.getIdUer());
                if(obj.getIdUer()!=0){
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(getBaseContext(), LoadingPageActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        },3000);
        setContentView(R.layout.loading_page);
    }
}