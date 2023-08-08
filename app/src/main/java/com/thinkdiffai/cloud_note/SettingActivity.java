package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.DAO.Sort;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.Model.Setting_Sort;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {
    private ImageView imgBack;
    private Button btnLogout;
    Login daoLogin;
    Model_State_Login user;
    private Spinner spinnerSort;
    Sort sortTB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        imgBack = (ImageView) findViewById(R.id.img_back);
        btnLogout = (Button) findViewById(R.id.btn_logout);
        sortTB = new Sort(SettingActivity.this);

        ArrayList<String> listNameSort = new ArrayList<>();
        listNameSort.add("title");
        listNameSort.add("createAt");
        ArrayAdapter<String > adapterSort = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,listNameSort );
        adapterSort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerSort = (Spinner) findViewById(R.id.spinner_sort);
        spinnerSort.setAdapter(adapterSort);
        Setting_Sort obj = sortTB.getNameSort();
        for (int i=0;i<listNameSort.size();i++){
            if(listNameSort.get(i).equalsIgnoreCase(obj.getSortName())){
                spinnerSort.setSelection(i);
                spinnerSort.setSelected(true);
            }
        }
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                obj.setSortName(listNameSort.get(i));
                    int res =sortTB.update(obj);
                    if(res>0){

                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        daoLogin = new Login(SettingActivity.this);
        user = daoLogin.getLogin();
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_Logout();
            }
        });
    }

    private void dialog_Logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SettingActivity.this);
        builder.setTitle("SIGN OUT");
        builder.setMessage("Do you want to sign out of this account" );
        builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int res = daoLogin.delete(user);
                if (res > 0) {
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}