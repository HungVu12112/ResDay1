package com.thinkdiffai.cloud_note.services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.Model.Color;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.Model.POST.ModelPostScreenShot;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class ScreenShot extends Service {
    Login daoLogin;
    Model_State_Login user;
    String TAG="zzzzzzzz";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);
        daoLogin = new Login(getApplicationContext());
        user = daoLogin.getLogin();
        Log.e(TAG, "onStartCommand: start service" );

        new AsyncTask<Void, Void , List<String>>(){
            @Override
            protected List<String> doInBackground(Void... voids) {
                List<String> list = convertScreenshotsToBase64();
                Log.e(TAG, "doInBackground: "+list.size() );
                return list;
            }

            @Override
            protected void onPostExecute(List<String> strings) {
                super.onPostExecute(strings);
                List<String > listBase64 = strings;
                Log.e(TAG, "onStartCommand: getList String base64 "+listBase64.size() );
                for (String item : listBase64){

                    Log.e(TAG, "onStartCommand: join loop " );
                    new AsyncTask<Void, Void, String>() {
                        @Override
                        protected String doInBackground(Void... voids) {
                            Log.e(TAG, "doInBackground: join doIn" );
                            String linkImage = sendBase64(item);
                            return linkImage;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            super.onPostExecute(s);
                            Log.e("TAG", "onPostExecute: link image"+s );
                            ModelPostScreenShot obj = new ModelPostScreenShot();
                            obj.setTitle("");
                            obj.setColor(new Color(0,0,0,0));
                            obj.setType("screenshot");
                            obj.setImages(s);
                            obj.setNotePublic(0);
                            obj.setPinned(false);
                            obj.setLock("");
                            obj.setDuaAt("");
                            obj.setShare("");
                            obj.setReminAt("");
                            scanlTextImage(item, obj);
                        }
                    }.execute();
                }
            }
        }.execute();

        return  START_STICKY;

    }
    private void postScreenShot (ModelPostScreenShot obj){
        APINote.apiService.postScreenShot(user.getIdUer(),obj ).enqueue(new Callback<ModelReturn>() {
            @Override
            public void onResponse(Call<ModelReturn> call, retrofit2.Response<ModelReturn> response) {
                if(response.isSuccessful()&response.body()!=null){
                    ModelReturn re = response.body();
                    if(re.getStatus()==200){
                        Log.e("TAG", "onResponse: "+re.getMessage() );
                        Model_State_Login obj = user;
                        obj.setState(1);
                        int res = daoLogin.update(obj);
                        if(res>0){
                            Log.e("TAG", "onResponse: "+"lưu trữ ảnh thành công" );
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelReturn> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t);
            }
        });
    }

    private  List<String> convertScreenshotsToBase64() {
        List<String> listBase64 = new ArrayList<>();
        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA
        };
        String selection = MediaStore.Images.ImageColumns.DATA + " like ?";
        String[] selectionArgs = new String[]{"%/Screenshots/%"};

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            //int idColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID);
            int dataColumn = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                String imagePath = cursor.getString(dataColumn);
               String image2 =  imagePath.replace("/emulated/0/","/self/primary/");
                Log.e("TAG", "convertScreenshotsToBase642: "+image2);
                Bitmap bm = BitmapFactory.decodeFile(image2);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // nén ảnh
                byte[] b = baos.toByteArray();
                String encodedImage = Base64.encodeToString(b, Base64.DEFAULT); // chuyển thành chuỗi base64
                listBase64.add(encodedImage);
                cursor.moveToNext();
                // Làm gì đó với chuỗi base64 tại đây, ví dụ như lưu trữ vào cơ sở dữ liệu hoặc gửi qua mạng
            }

            cursor.close();
        }
        return listBase64;
    }
    private void scanlTextImage(String base64, ModelPostScreenShot obj) {
        final String[] textScanl = {""};
        Log.e(TAG, "scanlTextImage: join scanle text image start" );
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        InputImage inputImage = InputImage.fromBitmap(decodedByte, 0);
        TextRecognizer textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {

                StringBuilder result = new StringBuilder();
                String blockText;
                for (Text.TextBlock block : text.getTextBlocks()) {
                    blockText = block.getText();
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
                textScanl[0] = String.valueOf(result);
                Log.e(TAG, "scanlTextImage: join scanle text image end" );
                obj.setData(textScanl[0]);
                postScreenShot(obj);
                Log.e("TAG", "onSuccess: trả về text trong image:  "+textScanl[0] );
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "onFailure: "+e.getMessage() );
               // Toast.makeText(getApplicationContext(), "Fail to detect text from image.." + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });





    }

    private String sendBase64(String base64String) {
        Log.e(TAG, "sendBase64: join send image" );
        OkHttpClient client = new OkHttpClient();
        String key ="6374d7c9cfa9f0cb372098bdf76d806e";
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
        String imageUrl="";
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
    }
}
