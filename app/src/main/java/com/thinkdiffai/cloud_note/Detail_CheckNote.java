package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterCheckList;
import com.thinkdiffai.cloud_note.Model.GET.ModelCheckList;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetCheckList;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.PATCH.ModelPutCheckList;
import com.thinkdiffai.cloud_note.Model.POST.ModelCheckListPost;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_CheckNote extends AppCompatActivity {
    private CardView cardView;
    private ImageButton back;
    private ImageButton menuDetailCheckList;
    private EditText title;
    private ImageButton done;
    private String color_background;
    //Adapter and Recyler View
    private RecyclerView recyclerView;
    // dữ liệu intent gửi sang
    int idNote;
    float colorA;
    int colorR;
    int colorG;
    int colorB;
    AdapterCheckList adapterCheckList;
    private ImageButton btnDetailChecklistDone;
    private Button btnAddCheckList;
KProgressHUD isloading;
    List<ModelCheckListPost> checkListUpdate = new ArrayList<>();
    int notePublic;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_check_note);
        Intent intent = getIntent();
        //Ánh xạ
        cardView = findViewById(R.id.cardview_detail_checklist);
        back = findViewById(R.id.back_from_detail_check_note);
        title = findViewById(R.id.title_detail_checklist);
        recyclerView = findViewById(R.id.recycler_checklist);
        menuDetailCheckList = (ImageButton) findViewById(R.id.menu_detail_check_list);
        btnDetailChecklistDone = (ImageButton) findViewById(R.id.btn_detail_checklist_done);
        btnAddCheckList = (Button) findViewById(R.id.btn_addCheckList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        getData(intent);
        if(notePublic==0){
            btnAddCheckList.setVisibility(View.VISIBLE);
            title.setEnabled(true);
            btnDetailChecklistDone.setVisibility(View.VISIBLE);
        }else{
            btnAddCheckList.setVisibility(View.INVISIBLE);
            title.setEnabled(false);
            btnDetailChecklistDone.setVisibility(View.INVISIBLE);
        }
        String hex = ChuyenMau(colorA, colorR, colorG, colorB);
        cardView.setCardBackgroundColor(Color.parseColor(hex));
        ModelGetCheckList obj = new ModelGetCheckList();
        isloading= new KProgressHUD(Detail_CheckNote.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        menuDetailCheckList.setOnClickListener(view->{
            Menu_Dialog(Gravity.BOTTOM);
        });
        APINote.apiService.getNoteByIdTypeCheckList(idNote).enqueue(new Callback<ModelGetCheckList>() {
            @Override
            public void onResponse(Call<ModelGetCheckList> call, Response<ModelGetCheckList> response) {
                isloading.show();
                if (response.body() != null && response.isSuccessful()) {
                    isloading.dismiss();
                    obj.setModelTextNoteCheckList(response.body().getModelTextNoteCheckList());
                    title.setText(obj.getModelTextNoteCheckList().getTitle());
                    adapterCheckList = new AdapterCheckList(obj.getModelTextNoteCheckList().getData(), true);
                    recyclerView.setAdapter(adapterCheckList);
                    btnAddCheckList.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialogAddCheckList(Detail_CheckNote.this, obj.getModelTextNoteCheckList().getData());
                        }
                    });
                    btnDetailChecklistDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ModelPutCheckList update = new ModelPutCheckList();
                            update.setTitle(title.getText().toString());
                            if (cardView.getCardBackgroundColor().getDefaultColor() == Color.parseColor(hex)) {
                                Log.d("TAG", "onCreate:Color1: k thay đổi màu  ");
                                update.setColor(new com.thinkdiffai.cloud_note.Model.Color(colorA, colorB, colorG, colorR));
                            } else {
                                Log.d("TAG", "onCreate:Color2: thay đổi màu  ");
                                update.setColor(chuyenMauARGB(color_background));
                            }


                            for (ModelCheckList x : obj.getModelTextNoteCheckList().getData()) {
                                ModelCheckListPost item = new ModelCheckListPost();
                                item.setContent(x.getContent());
                                if (x.getStatus() == 1) {
                                    item.setStatus(true);
                                } else {
                                    item.setStatus(false);
                                }
                                checkListUpdate.add(item);
                            }
                            update.setData(checkListUpdate);
                            update.setType("checklist");
                            update.setPinned(0);
                            update.setReminAt("");
                            update.setDuaAt("");
                            update.setLock("");
                            pathCheckList(update);
                        }
                    });
                }

            }

            @Override
            public void onFailure(Call<ModelGetCheckList> call, Throwable t) {
                Toast.makeText(Detail_CheckNote.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onFailure: " + t.getMessage());
                isloading.dismiss();
            }
        });
        Back();
    }
    public void dialogAddCheckList(Context context, List<ModelCheckList> list) {
        final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_add_check_list);
        TextInputLayout inputCheckList = (TextInputLayout) dialog.findViewById(R.id.input_checkList);
        Button btnAdd = (Button) dialog.findViewById(R.id.btn_add);
        CheckBox cbStatus = dialog.findViewById(R.id.cb_status);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputCheckList.getEditText().getText().toString() != "") {
                    ModelCheckList obj = new ModelCheckList();
                    obj.setContent(inputCheckList.getEditText().getText().toString());

                    if(cbStatus.isChecked()==true){
                        obj.setStatus(1);
                    }else{
                        obj.setStatus(0);
                    }
                    list.add(obj);
                    AdapterCheckList adapterCheckList1 = new AdapterCheckList(list, true);
                    recyclerView.setAdapter(adapterCheckList1);
                    inputCheckList.setError("");
                    dialog.dismiss();
                } else {
                    inputCheckList.setError("Không được để trống");
                }

            }
        });
        dialog.show();
    }

    private void pathCheckList(ModelPutCheckList obj) {
        isloading.show();
        ModelReturn r = new ModelReturn();
        APINote.apiService.patch_Check_list(idNote, obj).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelReturn>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ModelReturn modelReturn) {
                        r.setStatus(modelReturn.getStatus());
                        r.setMessage(modelReturn.getMessage());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage());
                        isloading.dismiss();
                    }

                    @Override
                    public void onComplete() {
                        if (r.getStatus() == 200) {
                            isloading.dismiss();
//                            Intent intent = new Intent(Detail_CheckNote.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   onBackPressed();
                               }
                           });
                        }
                    }
                });
    }

    private void getData(Intent intent) {
        idNote = intent.getIntExtra("id", -1);
        colorA = intent.getFloatExtra("colorA", 0);
        colorR = intent.getIntExtra("colorR", 0);
        colorG = intent.getIntExtra("colorG", 0);
        colorB = intent.getIntExtra("colorB", 0);


    }

    private String ChuyenMau(float alpha, float red, float green, float blue) {
        // chuyển đổi giá trị của từng kênh màu sang giá trị thập lục phân
        String alphaHex = Integer.toHexString((int) alpha);
        String redHex = Integer.toHexString((int) red);
        String greenHex = Integer.toHexString((int) green);
        String blueHex = Integer.toHexString((int) blue);
// ghép các giá trị thập lục phân lại với nhau theo thứ tự ARGB
        String hex = "#" + redHex + greenHex + blueHex;
        Log.d("TAG", "ChuyenMau: " + hex);
        return hex;
    }

    public com.thinkdiffai.cloud_note.Model.Color chuyenMauARGB(String hexColor) {
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

    public void Back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void Menu_Dialog(int gravity){
        final Dialog dialog = new Dialog(this);
        //Truyền layout cho dialog.
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_select_color);

        //Xác định vị trí cho dialog

        Window window = dialog.getWindow();
        if(window == null){

        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else {
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
        TextView tvShare = dialog.findViewById(R.id.tv_share);
        tvShare.setOnClickListener(view->{

        });
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