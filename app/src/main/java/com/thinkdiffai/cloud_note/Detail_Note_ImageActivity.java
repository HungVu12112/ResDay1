package com.thinkdiffai.cloud_note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetImageNote;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetScreenShots;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Detail_Note_ImageActivity extends AppCompatActivity {
    private ImageButton backFromTextNote;
    private ImageButton btnDone;
    private CardView cardViewTextnote;
    private Button btnUpload;
    private ImageView imgBackground;
    private EditText titleName;
    private TextView tvDateCreate;
    private ImageView imgDateCreate;
    private TextView tvTimeCreate;
    private ImageView imgTimeCreate;
    private EditText addContentText;
    private ImageButton menuTextNote;
    String color_background;
    String imageBase64;
    int idNote;
    float colorA;
    int colorR;
    int colorG;
    int colorB;
    String type;
    KProgressHUD isloading;
    int notePublic;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note_image);
        Intent intentData = getIntent();
        backFromTextNote = (ImageButton) findViewById(R.id.back_from_text_note);
        btnDone = (ImageButton) findViewById(R.id.btn_done);
        cardViewTextnote = (CardView) findViewById(R.id.cardView_textnote);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        imgBackground = (ImageView) findViewById(R.id.img_background);
        titleName = (EditText) findViewById(R.id.title_name);
        tvDateCreate = (TextView) findViewById(R.id.tv_dateCreate);
        imgDateCreate = (ImageView) findViewById(R.id.img_dateCreate);
        tvTimeCreate = (TextView) findViewById(R.id.tv_timeCreate);
        imgTimeCreate = (ImageView) findViewById(R.id.img_timeCreate);
        addContentText = (EditText) findViewById(R.id.add_content_text);
        menuTextNote = (ImageButton) findViewById(R.id.menu_text_note);
        btnDone.setVisibility(View.INVISIBLE);
        btnUpload.setVisibility(View.INVISIBLE);
        isloading = new KProgressHUD(Detail_Note_ImageActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        getData(intentData);
        String hex = ChuyenMau(colorA, colorR, colorB, colorB);
        if(!hex.equalsIgnoreCase("#000")){
            cardViewTextnote.setCardBackgroundColor(Color.parseColor(hex+""));
        }

        backFromTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(Detail_Note_ImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    requesPermisstion();
                }else{
                    pickImage();
                }
            }
        });
        menuTextNote.setVisibility(View.INVISIBLE);
        menuTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_Dialog(Gravity.BOTTOM);
            }
        });
        if(type.equalsIgnoreCase("image")){
            APINote.apiService.getNoteByIdTypeImage(idNote).enqueue(new Callback<ModelGetImageNote>() {
                @Override
                public void onResponse(Call<ModelGetImageNote> call, Response<ModelGetImageNote> response) {
                    isloading.show();
                    if(response.isSuccessful()&response.body()!=null){
                        isloading.dismiss();
                        ModelGetImageNote obj = response.body();
                        titleName.setText(obj.getNote().getTitle());
                        addContentText.setText(obj.getNote().getData());
                        if(obj.getNote().getMetaData()!=null){
                            imgBackground.setVisibility(View.VISIBLE);
                            Glide.with(imgBackground).load(obj.getNote().getMetaData()).into(imgBackground);
                        }else{
                            imgBackground.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelGetImageNote> call, Throwable t) {
                    Log.e("TAG", "onFailure: "+t.getMessage() );
                    isloading.dismiss();
                }
            });
        }else{
            APINote.apiService.getNoteByIdTypeScreenshot(idNote).enqueue(new Callback<ModelGetScreenShots>() {
                @Override
                public void onResponse(Call<ModelGetScreenShots> call, Response<ModelGetScreenShots> response) {
                    isloading.show();
                    if(response.isSuccessful()&response.body()!=null){
                        isloading.dismiss();
                        ModelGetScreenShots item = response.body();
                        titleName.setText(item.getNotes().getTitle());
                        addContentText.setText(item.getNotes().getData());
                        Glide.with(imgBackground).load(item.getNotes().getMetaData()).into(imgBackground);

                    }
                }

                @Override
                public void onFailure(Call<ModelGetScreenShots> call, Throwable t) {
                    Log.e("TAG", "onFailure: "+t.getMessage() );
                    isloading.dismiss();
                }
            });
        }




    }
    private void getData(Intent intent){
        idNote = intent.getIntExtra("id", -1);
        colorA = intent.getFloatExtra("colorA",0);
        colorR = intent.getIntExtra("colorR", 0);
        colorG = intent.getIntExtra("colorG", 0);
        colorB = intent.getIntExtra("colorB", 0);
        type = intent.getStringExtra("type");
        notePublic= intent.getIntExtra("notePublic", 0);


    }
    private void pickImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),999);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==999&&resultCode ==RESULT_OK&& data!=null&&data.getData()!=null){
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                byte[] decodedString = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgBackground.setVisibility(View.VISIBLE);
                imgBackground.setImageBitmap(decodedByte);
            }catch (IOException e){
                Log.e("TAG", "onActivityResult: "+e.getMessage() );
            }
        }
    }

    private void requesPermisstion(){
        ActivityCompat.requestPermissions(Detail_Note_ImageActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 999 & grantResults[0] == 0) {
            // đồng ý

        } else {
            // không đồng ý
            Toast.makeText(Detail_Note_ImageActivity.this, "Do bạn không đồng ý !!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog datePickerDialog = new DatePickerDialog(Detail_Note_ImageActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                int days = dayOfMonth;
                int months = month;
                int years = year;
                tvDateCreate.setText(days + "-" + (months + 1) + "-" + years);
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
        TimePickerDialog timePickerDialog = new TimePickerDialog(Detail_Note_ImageActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                int hour = i;
                int minute = i1;
                tvTimeCreate.setText(hour + ":" + minute);
            }
        }, hourOfDay, minute, false);
        timePickerDialog.show();
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

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FFBC7D";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FAE28C";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#D3EF82";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#A5EF82";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        mint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82EFBB";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82C8EF";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#8293EF";

                cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        dialog.show();
    }
    public com.thinkdiffai.cloud_note.Model.Color chuyenMau(String hexColor) {
        Log.e("TAG", "chuyenMau: "+hexColor);
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
        Log.e("TAG", "chuyenMau:R "+red);
        Log.e("TAG", "chuyenMau: G"+green);
        Log.e("TAG", "chuyenMau: B"+blue);
        com.thinkdiffai.cloud_note.Model.Color color = new com.thinkdiffai.cloud_note.Model.Color();
        color.setA((float) 0.87);
        color.setB(blue);
        color.setG(green);
        color.setR(red);
        return color;
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
}