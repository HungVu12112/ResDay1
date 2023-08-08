package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterCheckListPost;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.Model.POST.ModelCheckListPost;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.POST.ModelTextNoteCheckListPost;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CheckedList_Activity extends AppCompatActivity {
    private CardView cardView;
    private ImageButton back;
    private ImageButton done;
    private ImageButton calendar;
    private EditText title;
    private RecyclerView rcvCheckList;
    private Button btnAddCheckList;

    private TextView tvDateCreate;
    private ImageView imgDateCreate;
    private ImageButton menu;
    private String Date_created;
    private TextView tvTimeCreate;
    private ImageView imgTimeCreate;
    private String color_background = "#8FD2EF";
    List<ModelCheckListPost> list = new ArrayList<>();

    AdapterCheckListPost adapterCheckList;

    Login daoLogin;
    Model_State_Login user;
    KProgressHUD isloading;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checked_list);
        init();
        daoLogin = new Login(CheckedList_Activity.this);
        user = daoLogin.getLogin();
        Save();
        OpenMenu();
        adapterCheckList = new AdapterCheckListPost(list);
        rcvCheckList.setAdapter(adapterCheckList);
      isloading = new KProgressHUD(CheckedList_Activity.this)
              .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
              .setLabel("Please wait")
              .setDetailsLabel("")
              .setCancellable(true)
              .setAnimationSpeed(2)
              .setDimAmount(0.5f);

        btnAddCheckList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddCheckList(CheckedList_Activity.this);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void Save() {
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title_values = title.getText().toString();
//                Date date = new Date();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a");
//                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ha_Noi"));
//                Date_created = simpleDateFormat.format(date);
                ModelTextNoteCheckListPost obj = new ModelTextNoteCheckListPost();
                obj.setTitle(title_values);
                obj.setData(list);
                obj.setType("checklist");
                obj.setPinned(false);
                obj.setColor(chuyenMau(color_background));
                obj.setLock("");
                obj.setReminAt("");
                obj.setShare("");
                obj.setDuaAt("");
                if (obj.getTitle() != null && obj.getData() != null) {
                    postAPI(obj);
                } else {
                    if (obj.getTitle() == null) {
                        Toast.makeText(CheckedList_Activity.this, "Title không được để trống", Toast.LENGTH_SHORT).show();
                    }
                    if (obj.getData() == null) {
                        Toast.makeText(CheckedList_Activity.this, "Content không được để trống", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void init() {
        cardView = findViewById(R.id.cardview_checkList);
        back = findViewById(R.id.back_from_check_note);
        done = findViewById(R.id.btn_checklist_done);
        rcvCheckList = (RecyclerView) findViewById(R.id.rcv_checkList);
        btnAddCheckList = (Button) findViewById(R.id.btn_addCheckList);
        title = findViewById(R.id.title_checklist);
        menu = findViewById(R.id.menu_checked_list);
        tvDateCreate = (TextView) findViewById(R.id.tv_dateCreate);
        imgDateCreate = (ImageView) findViewById(R.id.img_dateCreate);
        tvTimeCreate = (TextView) findViewById(R.id.tv_timeCreate);
        imgTimeCreate = (ImageView) findViewById(R.id.img_timeCreate);
    }


    public void dialogDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(CheckedList_Activity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int days = dayOfMonth;
                int months = month;
                int years = year;
                tvDateCreate.setText(days + "/" + (months + 1) + "/" + years);
            }
        }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    public void dialogTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(CheckedList_Activity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                int hour = i;
                int minute = i1;
                tvTimeCreate.setText(hour + ":" + minute);
            }
        }, hourOfDay, minute, false);
        timePickerDialog.show();
    }

    public void postAPI(ModelTextNoteCheckListPost obj) {
        isloading.show();
        ModelReturn modelReturn = new ModelReturn();
        APINote.apiService.post_Check_list(user.getIdUer(), obj)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelReturn>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ModelReturn modelReturn) {
                        modelReturn.setStatus(modelReturn.getStatus());
                        modelReturn.setMessage(modelReturn.getMessage());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isloading.dismiss();
                        Log.e("TAG", "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        isloading.dismiss();
                        Toast.makeText(CheckedList_Activity.this, "Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CheckedList_Activity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });
    }

    public com.thinkdiffai.cloud_note.Model.Color chuyenMau(String hexColor) {
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
        com.thinkdiffai.cloud_note.Model.Color color = new com.thinkdiffai.cloud_note.Model.Color();
        color.setA((float) 0.87);
        color.setB(blue);
        color.setG(green);
        color.setR(red);
        return color;
    }

    public void dialogAddCheckList(Context context) {
        final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_add_check_list);
        TextInputLayout inputCheckList = (TextInputLayout) dialog.findViewById(R.id.input_checkList);
        Button btnAdd = (Button) dialog.findViewById(R.id.btn_add);
        CheckBox cbStatus = dialog.findViewById(R.id.cb_status);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputCheckList.getEditText().getText().toString() != "") {
                    ModelCheckListPost obj = new ModelCheckListPost();
                    obj.setContent(inputCheckList.getEditText().getText().toString());
                    obj.setStatus(cbStatus.isChecked());
                    list.add(obj);
                    adapterCheckList = new AdapterCheckListPost(list);
                    rcvCheckList.setAdapter(adapterCheckList);
                    inputCheckList.setError("");
                    dialog.dismiss();
                } else {
                    inputCheckList.setError("Không được để trống");
                }

            }
        });
        dialog.show();
    }

    public void OpenMenu() {
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_Dialog(Gravity.BOTTOM);
            }
        });
    }

    public void Menu_Dialog(int gravity) {
        final Dialog dialog = new Dialog(this);
        //Truyền layout cho dialog.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_select_color);

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
        ImageButton red = dialog.findViewById(R.id.color_red);
        ImageButton orange = dialog.findViewById(R.id.color_orange);
        ImageButton yellow = dialog.findViewById(R.id.color_yellow);
        ImageButton green1 = dialog.findViewById(R.id.color_green1);
        ImageButton green2 = dialog.findViewById(R.id.color_green2);
        ImageButton mint = dialog.findViewById(R.id.color_mint);
        ImageButton blue = dialog.findViewById(R.id.color_blue);
        ImageButton purple = dialog.findViewById(R.id.color_purple);
        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FF7D7D";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FFBC7D";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FAE28C";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#D3EF82";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#A5EF82";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        mint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82EFBB";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82C8EF";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#8293EF";
                cardView.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        dialog.show();
    }
}