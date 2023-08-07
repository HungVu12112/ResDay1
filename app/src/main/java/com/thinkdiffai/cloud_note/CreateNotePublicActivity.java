package com.thinkdiffai.cloud_note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterCheckListPost;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.POST.ModelCheckListPost;
import com.thinkdiffai.cloud_note.Model.POST.ModelPostImageNote;
import com.thinkdiffai.cloud_note.Model.POST.Public.ModelCheckListPublic;
import com.thinkdiffai.cloud_note.Model.POST.Public.ModelTextNotePublic;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateNotePublicActivity extends AppCompatActivity {
    private Spinner spinnerTypeNote;
    private LinearLayout layoutTextNote;
    private EditText titleTextNote;
    private EditText addContentText;
    private LinearLayout layoutCheckList;
    private EditText titleChecklist;
    private RecyclerView rcvCheckList;
    private Button btnAddCheckList;
    private LinearLayout layoutNoteImage;
    private Button btnUpload;
    private ImageView imgBackground;
    private EditText titleImage;
    private EditText addContentImage;
    private ImageButton backFromCheckNote;
    private ImageButton btnChecklistDone;
    String type;
    private CardView cardView;
    String color_background = "#8FD2EF";
    private ImageButton menuTextNote;
    List<ModelCheckListPost> checklists = new ArrayList<>();
    AdapterCheckListPost adapterCheckListPost;
    String imageBase64 = "";
    private final int REQUEST_IMAGE_CAPTURE = 1;
    private final int REQUEST_IMAGE_GALLERY = 2;
    private String currentPhotoPath = "";
    KProgressHUD hud;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_note_public);
        spinnerTypeNote = (Spinner) findViewById(R.id.spinner_typeNote);
        layoutTextNote = (LinearLayout) findViewById(R.id.layout_textNote);
        titleTextNote = (EditText) findViewById(R.id.title_text_note);
        addContentText = (EditText) findViewById(R.id.add_content_text);
        layoutCheckList = (LinearLayout) findViewById(R.id.layout_checkList);
        titleChecklist = (EditText) findViewById(R.id.title_checklist);
        rcvCheckList = (RecyclerView) findViewById(R.id.rcv_checkList);
        btnAddCheckList = (Button) findViewById(R.id.btn_addCheckList);
        layoutNoteImage = (LinearLayout) findViewById(R.id.layout_noteImage);
        btnUpload = (Button) findViewById(R.id.btn_upload);
        imgBackground = (ImageView) findViewById(R.id.img_background);
        titleImage = (EditText) findViewById(R.id.title_image);
        addContentImage = (EditText) findViewById(R.id.add_content_image);
        menuTextNote = (ImageButton) findViewById(R.id.menu_text_note);

        backFromCheckNote = (ImageButton) findViewById(R.id.back_from_check_note);
        btnChecklistDone = (ImageButton) findViewById(R.id.btn_checklist_done);
        hud = KProgressHUD.create(CreateNotePublicActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        backFromCheckNote.setOnClickListener(view -> {
            onBackPressed();
        });
        btnChecklistDone.setOnClickListener(view -> {
            hud.show();
            switch (type) {
                case "text":
                    ModelTextNotePublic text = new ModelTextNotePublic();
                    text.setType(type);
                    text.setData(addContentText.getText().toString());
                    text.setTitile(titleTextNote.getText().toString());
                    text.setNotePublic(1);
                    text.setColor(chuyenMau(color_background));
                    text.setLock("");
                    text.setPinned(false);
                    text.setShare("");
                    text.setDueAt("");
                    text.setRemindAt("");
                    postTextNotePublulic(text);
                    break;
                case "checklist":
                    ModelCheckListPublic checkListPublic = new ModelCheckListPublic();
                    checkListPublic.setNotePublic(1);
                    checkListPublic.setDuaAt("");
                    checkListPublic.setColor(chuyenMau(color_background));
                    checkListPublic.setReminAt("");
                    checkListPublic.setTitle(titleChecklist.getText().toString());
                    checkListPublic.setLock("");
                    checkListPublic.setType(type);
                    checkListPublic.setPinned(false);
                    checkListPublic.setShare("");
                    checkListPublic.setData(checklists);
                    potsChecklistPublic(checkListPublic);
                    break;
                case "image":
                    ModelPostImageNote obj = new ModelPostImageNote();
                    obj.setTitle(titleImage.getText().toString());
                    obj.setData(addContentImage.getText().toString());
                    obj.setType(type);
                    obj.setColor(chuyenMau(color_background));
                    obj.setPinned(false);
                    obj.setLock("");
                    obj.setShare("");
                    obj.setDuaAt("");
                    obj.setReminAt("");
                    obj.setNotePublic(1);
                    if (imageBase64 != "") {
                        AsyncTask<Void, Void, String> async = new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... voids) {
                                return img();
                            }

                            @Override
                            protected void onPostExecute(String s) {
                                super.onPostExecute(s);
                                Log.e("TAG", "onPostExecute: " + s);
                                obj.setMetaData(s);
                                if (titleImage.getText().toString() != "" && addContentImage.getText().toString() != "") {
                                    postImageNotePublic(obj);
                                }

                            }
                        };
                        async.execute();


                    } else {
                        obj.setMetaData("");
                        if (titleImage.getText().toString() != "" && addContentImage.getText().toString() != "") {
                            postImageNotePublic(obj);
                        }
                    }
                    break;

            }
        });
        ArrayList<String> listType = new ArrayList<>();
        listType.add("text");
        listType.add("checklist");
        listType.add("image");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, listType);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeNote.setAdapter(arrayAdapter);
        spinnerTypeNote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type = listType.get(i);
                switch (i) {
                    case 0:
                        layoutTextNote.setVisibility(View.VISIBLE);
                        layoutNoteImage.setVisibility(View.GONE);
                        layoutCheckList.setVisibility(View.GONE);
                        break;
                    case 1:

                        layoutTextNote.setVisibility(View.GONE);
                        layoutNoteImage.setVisibility(View.GONE);
                        layoutCheckList.setVisibility(View.VISIBLE);
                        adapterCheckListPost = new AdapterCheckListPost(checklists);
                        rcvCheckList.setAdapter(adapterCheckListPost);
                        btnAddCheckList.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogAddCheckList(CreateNotePublicActivity.this);
                            }
                        });

                        break;
                    case 2:

                        layoutTextNote.setVisibility(View.GONE);
                        layoutNoteImage.setVisibility(View.VISIBLE);
                        layoutCheckList.setVisibility(View.GONE);
                        btnUpload.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                opendDialogImage();
                            }
                        });
                        break;
//                    default:
//                        ModelTextNotePost text1 = new ModelTextNotePost();
//                        layoutTextNote.setVisibility(View.VISIBLE);
//                        layoutNoteImage.setVisibility(View.GONE);
//                        layoutCheckList.setVisibility(View.GONE);
//                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        menuTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_Dialog(Gravity.BOTTOM);
            }
        });


    }

    private void postTextNotePublulic(ModelTextNotePublic obj) {

        APINote.apiService.post_text_note_public(obj).enqueue(new Callback<ModelReturn>() {
            @Override
            public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                if (response.isSuccessful() & response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        hud.dismiss();
                        onBackPressed();
                        Toast.makeText(CreateNotePublicActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        hud.dismiss();
                        onBackPressed();
                        Toast.makeText(CreateNotePublicActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<ModelReturn> call, Throwable t) {
                hud.dismiss();
                Log.e("TAG", "onFailure: " + t);
            }
        });
    }

    private void potsChecklistPublic(ModelCheckListPublic checklist) {
        APINote.apiService.post_checklist_public(checklist).enqueue(new Callback<ModelReturn>() {
            @Override
            public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                if (response.body() != null && response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        hud.dismiss();
                        onBackPressed();
                        Toast.makeText(CreateNotePublicActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelReturn> call, Throwable t) {
                hud.dismiss();
                Log.e("TAG", "onFailure: " + t);
            }
        });
    }

    private void postImageNotePublic(ModelPostImageNote imageNote) {
        APINote.apiService.post_image_note_public(imageNote).enqueue(new Callback<ModelReturn>() {
            @Override
            public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 200) {
                        hud.dismiss();
                        onBackPressed();
                        Toast.makeText(CreateNotePublicActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelReturn> call, Throwable t) {
                hud.dismiss();
                Log.e("TAG", "onFailure: "+t );
            }
        });
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
                    checklists.add(obj);
                    adapterCheckListPost = new AdapterCheckListPost(checklists);
                    rcvCheckList.setAdapter(adapterCheckListPost);
                    inputCheckList.setError("");
                    dialog.dismiss();
                } else {
                    inputCheckList.setError("Không được để trống");
                }

            }
        });
        dialog.show();
    }

    private void opendDialogImage() {
        final Dialog dialog = new Dialog(CreateNotePublicActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_menu_image);
        TextView tv_camera = dialog.findViewById(R.id.tv_camera);
        TextView tv_gallery = dialog.findViewById(R.id.tv_gallery);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(CreateNotePublicActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requesPermisstionCamera();
                } else {
                    opendCamera();

                }
                dialog.dismiss();


            }
        });
        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(CreateNotePublicActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requesPermisstionGallery();
                } else {
                    pickImage();

                }
                dialog.dismiss();


            }
        });
        dialog.show();

    }

    private Bitmap setPic() {
        String filePath = currentPhotoPath.replace("/emulated/0/", "/self/primary/");
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        return bitmap;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        Log.e("TAG", "createImageFile: " + image);
        Log.e("TAG", "createImageFile: " + currentPhotoPath);
        return image;
    }

    private void scanlTextImage(String base64) {
        hud.show();
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        InputImage inputImage = InputImage.fromBitmap(decodedByte, 0);
        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
                StringBuilder result = new StringBuilder();
                for (Text.TextBlock block : text.getTextBlocks()) {
                    String blockText = block.getText();
                    Point[] blockCornerPoint = block.getCornerPoints();
                    Rect blockFrame = block.getBoundingBox();
                    for (Text.Line line : block.getLines()) {
                        String lineTExt = line.getText();
                        Point[] lineCornerPoint = line.getCornerPoints();
                        Rect lineRect = line.getBoundingBox();
                        for (Text.Element element : line.getElements()) {
                            String elementText = element.getText();
                            result.append(elementText);
                        }


                    }

                }

                addContentImage.setText(result);
                if (currentPhotoPath != "") {
                    File file = new File(currentPhotoPath);
                    if (file.exists()) {
                        // Xóa file
                        boolean isDeleted = file.delete();

                        if (isDeleted) {
                            hud.dismiss();
                            System.out.println("File đã được xóa thành công.");
                        } else {
                            System.out.println("Không thể xóa file.");
                        }
                    } else {
                        System.out.println("File không tồn tại.");
                    }
                } else {
                    hud.dismiss();
                }

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onSuccess: " + result);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateNotePublicActivity.this, "Fail to detect text from image.." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void requesPermisstionCamera() {
        ActivityCompat.requestPermissions(CreateNotePublicActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
    }

    private void requesPermisstionGallery() {
        ActivityCompat.requestPermissions(CreateNotePublicActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);
    }

    private String img() {
        OkHttpClient client = new OkHttpClient();
        String base64String = imageBase64;
        String key = "6374d7c9cfa9f0cb372098bdf76d806e";
        String boundary = "Boundary-" + UUID.randomUUID().toString();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=" + boundary);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("key", key)
                .addFormDataPart("image", base64String)
                .build();
        Request request = new Request.Builder()
                .url("https://api.imgbb.com/1/upload")
                .post(requestBody)
                .build();
        String imageUrl = "";
        try {
            // Thực hiện yêu cầu và lấy phản hồi trả về
            okhttp3.Response response = client.newCall(request).execute();
            String responseBody = response.body().string();
            response.close();
            // Trích xuất URL của hình ảnh từ phản hồi JSON của ImgBB
            JSONObject jsonObject = new JSONObject(responseBody);
            imageUrl = jsonObject.getJSONObject("data").getString("url");
            // In ra URL của hình ảnh đã tải lê


        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;

    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_GALLERY);
    }

    public void opendCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                Log.e("TAG", "opendCamera: " + photoFile);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e("ERROR", "opendCamera: " + ex);

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                        "com.example.cloud_note.fileprovider",
                        photoFile);


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_GALLERY:
                    Uri uri = data.getData();
                    Log.e("TAG", "onActivityResult: Ảnh từ thư viện " + data.getData());
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
                        scanlTextImage(imageBase64);
                        Log.e("TAG", "onActivityResult: Step1 Complate ");
                    } catch (IOException e) {
                        Log.e("TAG", "onActivityResult: " + e.getMessage());
                    }
                    break;
                case REQUEST_IMAGE_CAPTURE:
                    Bitmap bitmap = setPic();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    imageBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    imgBackground.setVisibility(View.VISIBLE);
                    imgBackground.setImageBitmap(bitmap);
                    scanlTextImage(imageBase64);
                    break;
            }


        }
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
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

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

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FFBC7D";

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#FAE28C";

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#D3EF82";

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        green2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#A5EF82";

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        mint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82EFBB";

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#82C8EF";

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                color_background = "#8293EF";

                cardView.setCardBackgroundColor(android.graphics.Color.parseColor(color_background));
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public com.thinkdiffai.cloud_note.Model.Color chuyenMau(String hexColor) {
        Log.e("TAG", "chuyenMau: " + hexColor);
        int red = Integer.parseInt(hexColor.substring(1, 3), 16);
        int green = Integer.parseInt(hexColor.substring(3, 5), 16);
        int blue = Integer.parseInt(hexColor.substring(5, 7), 16);
        Log.e("TAG", "chuyenMau:R " + red);
        Log.e("TAG", "chuyenMau: G" + green);
        Log.e("TAG", "chuyenMau: B" + blue);
        com.thinkdiffai.cloud_note.Model.Color color = new com.thinkdiffai.cloud_note.Model.Color();
        color.setA((float) 0.87);
        color.setB(blue);
        color.setG(green);
        color.setR(red);
        return color;
    }

}