package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.example.cloud_note.R;

public class LoadingPageActivity extends AppCompatActivity {
    private CardView btnLogin;
    private CardView btnSignin;
    private CardView btnUserGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_page);
        btnLogin = (CardView) findViewById(R.id.btn_login);
        btnSignin = (CardView) findViewById(R.id.btn_Signin);
        btnUserGuest = (CardView) findViewById(R.id.btn_user_guest);
        btnLogin.setOnClickListener(view -> {
            Intent intent = new Intent(LoadingPageActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        btnSignin.setOnClickListener(view -> {
            Intent intent = new Intent(LoadingPageActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
        btnUserGuest.setOnClickListener(view -> {
            Intent intent = new Intent(LoadingPageActivity.this, HomeActivity.class);
            startActivity(intent);
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent Main = new Intent(Intent.ACTION_MAIN);
        Main.addCategory(Intent.CATEGORY_HOME);
        startActivity(Main);
        finish();
    }
}