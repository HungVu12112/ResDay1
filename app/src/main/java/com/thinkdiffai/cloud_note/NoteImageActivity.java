package com.thinkdiffai.cloud_note;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.Model.POST.ModelPostImageNote;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

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
import java.util.Date;
import java.util.UUID;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NoteImageActivity extends AppCompatActivity {
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
    String color_background = "#FF7D7D";
    String imageBase64 = "";
    Login daoUser;
    Model_State_Login user;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    String currentPhotoPath="";
    KProgressHUD hud;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_image);
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
        daoUser = new Login(NoteImageActivity.this);
        user = daoUser.getLogin();
        imgBackground.setVisibility(View.GONE);
        hud = KProgressHUD.create(NoteImageActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        cardViewTextnote.setCardBackgroundColor(Color.parseColor(color_background));
        backFromTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                opendDialogImage();

            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hud.show();
                String title = titleName.getText().toString();
                String content = addContentText.getText().toString();
                ModelPostImageNote obj = new ModelPostImageNote();
                obj.setTitle(title);
                obj.setData(content);
                obj.setType("image");
                obj.setColor(chuyenMau(color_background));
                obj.setPinned(false);
                obj.setLock("");
                obj.setShare("");
                obj.setDuaAt("");
                obj.setReminAt("");
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
                            if (title != "" && content != "") {
                                postImageNote(obj);
                            }

                        }
                    };
                    async.execute();


                } else {
                    obj.setMetaData("");
                    if (title != "" && content != "") {
                        postImageNote(obj);
                    }
                }

            }
        });
        menuTextNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu_Dialog(Gravity.BOTTOM);
            }
        });


    }

    private void postImageNote(ModelPostImageNote obj) {
        hud.show();
        ModelReturn r = new ModelReturn();
        APINote.apiService.post_image_note(user.getIdUer(), obj).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ModelReturn>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull ModelReturn modelReturn) {
                        r.setStatus(modelReturn.getStatus());
                        r.setMessage(modelReturn.getMessage());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        if (r.getStatus() == 200) {
                            hud.dismiss();
                            Toast.makeText(NoteImageActivity.this, r.getMessage(), Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    }
                });
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
            Response response = client.newCall(request).execute();
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
                Log.e("ERROR", "opendCamera: "+ex );

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
    private Bitmap setPic() {
        String filePath  = currentPhotoPath.replace("/emulated/0/","/self/primary/");
        // Get the dimensions of the View
//        int targetW = imgBackground.getWidth();
//        int targetH = imgBackground.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, bmOptions);

//        int photoW = bmOptions.outWidth;
//        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
//        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmOptions);
       // imgBackground.setImageBitmap(bitmap);
        return  bitmap;
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


    private void requesPermisstionGallery() {
        ActivityCompat.requestPermissions(NoteImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_GALLERY);
    }

    private void requesPermisstionCamera() {
        ActivityCompat.requestPermissions(NoteImageActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == REQUEST_IMAGE_GALLERY & grantResults[0] == 0) || (requestCode == REQUEST_IMAGE_CAPTURE & grantResults[0] == 0)) {
            // đồng ý
            opendDialogImage();
        } else {
            // không đồng ý
            Toast.makeText(NoteImageActivity.this, "Do bạn không đồng ý !!!", Toast.LENGTH_SHORT).show();
        }
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

                addContentText.setText(result);
                if(currentPhotoPath!=""){
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
                }else{
                    hud.dismiss();
                }

                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onSuccess: " + result);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NoteImageActivity.this, "Fail to detect text from image.." + e.getMessage(), Toast.LENGTH_SHORT).show();
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



    private void opendDialogImage() {
        final Dialog dialog = new Dialog(NoteImageActivity.this, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_menu_image);
        TextView tv_camera = dialog.findViewById(R.id.tv_camera);
        TextView tv_gallery = dialog.findViewById(R.id.tv_gallery);
        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(NoteImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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
                if (ActivityCompat.checkSelfPermission(NoteImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requesPermisstionGallery();
                } else {
                    pickImage();

                }
                dialog.dismiss();


            }
        });
        dialog.show();

    }


}