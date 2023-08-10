package com.thinkdiffai.cloud_note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.Adapter.Fragment_Adapter;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.services.ScreenShot;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewpager2;
    private Fragment_Adapter fragment_adapter;
    public BottomNavigationView bottomNavigationView;
    private ImageButton btnAdd;
    Login daoLogin;
    Model_State_Login user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottombar);
        viewpager2 = findViewById(R.id.viewpager);
        btnAdd = findViewById(R.id.btn_add);
        daoLogin = new Login(getApplicationContext());
        user=daoLogin.getLogin();
        if(user.getState()==0){
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requesPermisstion();
            } else {
                allet();
            }
        }
        init_Viewpager();
        SetUpViewpager();
        add();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent Main = new Intent(Intent.ACTION_MAIN);
        Main.addCategory(Intent.CATEGORY_HOME);
        startActivity(Main);
        finish();
    }

    private void init_Viewpager() {
        fragment_adapter = new Fragment_Adapter(MainActivity.this);
        viewpager2.setAdapter(fragment_adapter);
    }

    private void SetUpViewpager() {
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.note).setChecked(true);
                        btnAdd.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.calendar).setChecked(true);
                        btnAdd.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.archived).setChecked(true);
                        btnAdd.setVisibility(View.VISIBLE);
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.deleted).setChecked(true);
                        btnAdd.setVisibility(View.VISIBLE);
                        break;
                    case 4:
                        bottomNavigationView.getMenu().findItem(R.id.groups).setChecked(true);
                        btnAdd.setVisibility(View.VISIBLE);

                        break;
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.note).setChecked(true);
                        btnAdd.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.note:
                        viewpager2.setCurrentItem(0);

                        btnAdd.setImageResource(R.drawable.add);
                        return true;
                    case R.id.calendar:
                        viewpager2.setCurrentItem(1);
                        btnAdd.setImageResource(R.drawable.add);
                        return true;
                    case R.id.archived:
                        viewpager2.setCurrentItem(2);
                        btnAdd.setImageResource(R.drawable.add);
                        return true;
                    case R.id.deleted:
                        viewpager2.setCurrentItem(3);
                        btnAdd.setImageResource(R.drawable.add);
                        return true;
                    case  R.id.groups:
                        viewpager2.setCurrentItem(4);
                        btnAdd.setImageResource(R.drawable.new_group);
                        return true;
                }
                return false;
            }
        });
    }

    public void add() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenDialog(Gravity.BOTTOM);
            }
        });
    }

    private void allet() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alet");
        builder.setMessage("Bạn có muốn lưu trũ ảnh Screenshort không" );
        builder.setPositiveButton("Đóng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                ArrayList<File> listFile = getScreenshotFiles();
//                for (File x : listFile){
//                    Log.e("TAG", "onStartCommand: "+ x.getPath());
//                }
                Log.e("TAG", "onClick: start service" );
                startService(new Intent(getApplicationContext(), ScreenShot.class));

            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void requesPermisstion() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999 & grantResults.length>0 & grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // đồng ý
                allet();
        } else {
            // không đồng ý
            Toast.makeText(MainActivity.this, "Do bạn không đồng ý !!!", Toast.LENGTH_SHORT).show();
        }
    }


    public void OpenDialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        //Truyền layout cho dialog.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pop_up_layout);

        //Xác định vị trí cho dialog

        Window window = dialog.getWindow();
        if (window == null) {

        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true);
        } else {
            dialog.setCancelable(false);
        }
        //Ánh xạ
        Button button_checkList = dialog.findViewById(R.id.btn_new_checked_list);
        Button button_note = dialog.findViewById(R.id.btn_new_text_note);
        Button button_cancel = dialog.findViewById(R.id.btn_Cancel);
        Button btn_image_note = dialog.findViewById(R.id.btn_new_image_note);
        button_checkList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CheckedList_Activity.class);
                startActivity(intent);
                dialog.dismiss();

            }
        });
        button_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TextNoteActvity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btn_image_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteImageActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

}