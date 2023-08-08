package com.thinkdiffai.cloud_note.APIs;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface API_UploadImage {
    // khởi tạo Gson
//    Gson gson = new GsonBuilder()
//            .setDateFormat("yyyy-MM-dd HH:mm:ss")
//            .create();
    // khởi tạo retrofit
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder okbilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor);
    API_UploadImage  api = new Retrofit.Builder()
            // là dumain cảu api
            .baseUrl("http://14.225.7.221:18011/")
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okbilder.build())
            .build().create(API_UploadImage.class);
    @GET("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<Object> getListNoteByUser2(@Path("id") int id);

}
