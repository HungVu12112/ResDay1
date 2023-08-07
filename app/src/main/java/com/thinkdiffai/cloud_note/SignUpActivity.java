package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.POST.RegiterReq;
import com.google.android.material.textfield.TextInputLayout;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {
    private ImageView imgBack;
    private TextInputLayout inputYourName;
    private TextInputLayout inputUsername;
    private TextInputLayout inputEmail;
    private TextInputLayout inputPasswd;
    private TextInputLayout inputRetypePasswd;
    private TextView tvSignIn;
    private Button btnRegiter;
    private ModelReturn modelReturnm;
    KProgressHUD isloading;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        imgBack = (ImageView) findViewById(R.id.img_back);
        inputYourName = findViewById(R.id.input_yourName);
        inputUsername = (TextInputLayout) findViewById(R.id.input_username);
        inputEmail = (TextInputLayout) findViewById(R.id.input_email);
        inputPasswd = (TextInputLayout) findViewById(R.id.input_passwd);
        inputRetypePasswd = (TextInputLayout) findViewById(R.id.input_RetypePasswd);
        tvSignIn = (TextView) findViewById(R.id.tv_signIn);


        btnRegiter = (Button) findViewById(R.id.btn_regiter);
        isloading = new KProgressHUD(SignUpActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnRegiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate() == true) {
                    isloading.show();

                    Regiter();
                }
            }
        });

    }

    private void Regiter() {
        RegiterReq regiterReq = new RegiterReq(inputUsername.getEditText().getText().toString(), inputPasswd.getEditText().getText().toString(), inputYourName.getEditText().getText().toString(), inputEmail.getEditText().getText().toString());
        APINote.apiService.Regiter(regiterReq)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelReturn>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ModelReturn modelReturn) {
                        modelReturnm = modelReturn;
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isloading.dismiss();
                        Log.e("TAG", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("TAG", "onComplete: Đăng ký thành công");
                        if (modelReturnm.getStatus() == 200) {
                            isloading.dismiss();
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }

    private boolean validate() {
        String rex = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        if (inputYourName.getEditText().getText().toString() != ""
                && inputUsername.getEditText().getText().toString() != ""
                && inputPasswd.getEditText().getText().toString() != ""
                && inputEmail.getEditText().getText().toString() != ""
                && inputRetypePasswd.getEditText().getText().toString() != ""
                && inputPasswd.getEditText().getText().toString().length() >= 8
//                && inputPasswd.getEditText().getText().toString() == inputRetypePasswd.getEditText().getText().toString()
                && inputEmail.getEditText().getText().toString().matches(rex) == true) {
            return true;
        } else {
            if (inputYourName.getEditText().getText().toString() == "") {
                inputYourName.setError("Your Name không được để trống");
            } else {
                inputYourName.setError("");
            }
            if (inputEmail.getEditText().getText().toString() == "") {
                inputEmail.setError("Email không được để trống");
            } else if (inputEmail.getEditText().getText().toString().matches(rex) != true) {
                inputEmail.setError("Email không đúng định dạng");
            } else {
                inputEmail.setError("");
            }
            if (inputUsername.getEditText().getText().toString() == "") {
                inputUsername.setError("Username không được để trống");
            } else {
                inputUsername.setError("");
            }
            if (inputPasswd.getEditText().getText().toString() == "") {
                inputPasswd.setError("Password không được để trống");
            } else if (inputPasswd.getEditText().getText().toString().length() < 8) {
                inputPasswd.setError("Password phải ít nhất 8 ký tự");
            } else {
                inputPasswd.setError("");
            }
            if (inputRetypePasswd.getEditText().getText().toString() == "") {
                inputRetypePasswd.setError("Retype Password không được để trống");
            } else if (inputPasswd.getEditText().getText().toString().trim() != inputRetypePasswd.getEditText().getText().toString().trim()) {
                inputRetypePasswd.setError("Mật khẩu không trùng khớp");
            } else {
                inputRetypePasswd.setError("");
            }
            return false;
        }
    }
}