package com.thinkdiffai.cloud_note.APIs;

import com.thinkdiffai.cloud_note.Model.GET.ModelGetImageNote;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetScreenShots;
import com.thinkdiffai.cloud_note.Model.LoginModel;
import com.thinkdiffai.cloud_note.Model.ModelListLastUser;
import com.thinkdiffai.cloud_note.Model.PATCH.ModelPutCheckList;
import com.thinkdiffai.cloud_note.Model.PATCH.ModelPutTextNote;
import com.thinkdiffai.cloud_note.Model.POST.LoginReq;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetCheckList;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetNoteText;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.POST.ModelPostImageNote;
import com.thinkdiffai.cloud_note.Model.POST.ModelPostScreenShot;
import com.thinkdiffai.cloud_note.Model.POST.ModelTextNoteCheckListPost;
import com.thinkdiffai.cloud_note.Model.POST.ModelTextNotePost;
import com.thinkdiffai.cloud_note.Model.GET.Model_Notes;
import com.thinkdiffai.cloud_note.Model.POST.Public.ModelCheckListPublic;
import com.thinkdiffai.cloud_note.Model.POST.Public.ModelTextNotePublic;
import com.thinkdiffai.cloud_note.Model.POST.RegiterReq;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APINote {
    // khởi tạo Gson
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();
    // khởi tạo retrofit
    HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient.Builder okbilder = new OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(httpLoggingInterceptor);
    APINote apiService = new Retrofit.Builder()
            // là dumain cảu api
            .baseUrl("http://14.225.7.221:18011/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okbilder.build())
            .build().create(APINote.class);


    @POST("login")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<LoginModel> login(@Body LoginReq loginReq);


    @POST("register")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> Regiter(@Body RegiterReq regiterReq);

    @GET("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<Model_Notes> getListNoteByUser(@Path("id") int id);
    @GET("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<Objects> getListNoteByUser2(@Path("id") int id);
    ///
    @POST("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> post_text_note(@Path("id") int id, @Body ModelTextNotePost modelTextNotePost);

    @POST("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> post_Check_list(@Path("id") int id, @Body ModelTextNoteCheckListPost modelTextNoteCheckList);
    @POST("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> post_image_note(@Path("id") int id , @Body ModelPostImageNote modelPostImageNote);
    //
    @PATCH("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> patch_text_note(@Path("id") int id, @Body ModelPutTextNote modelPutTextNote);

    @PATCH("notes/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Observable<ModelReturn> patch_Check_list(@Path("id") int id, @Body ModelPutCheckList modelPutCheckList);

    @GET("only/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelGetNoteText> getNoteByIdTypeText(@Path("id") int id);

    @GET("only/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelGetCheckList> getNoteByIdTypeCheckList(@Path("id") int id);
    @GET("only/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelGetImageNote> getNoteByIdTypeImage(@Path("id") int id);
    @GET("only/{id}")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelGetScreenShots>getNoteByIdTypeScreenshot(@Path("id") int id);

    @DELETE("trunc-notes/{id}")
    Call<ModelReturn> deleteNote(@Path("id") int id);

    @DELETE("notes/{id}")
    Call<ModelReturn> moveToTrash(@Path("id") int id);

    @GET("trash/{id}")
    Observable<Model_Notes> getListTrash(@Path("id") int id);
    @POST("trash-res/{id}")
    Call<ModelReturn> restore(@Path("id") int id);

    @POST("notes/{id}")
    Call<ModelReturn> postScreenShot(@Path("id") int id,@Body ModelPostScreenShot image);
    @GET("lastUser")
    Call<ModelListLastUser> getListLastUser();
    @GET("notes_public")
    Call<Model_Notes> getNotePublic();

    @POST("notes/10")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelReturn> post_text_note_public( @Body ModelTextNotePublic modelTextNotePost);
    @POST("notes/10")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelReturn> post_checklist_public( @Body ModelCheckListPublic modelCheckListPublic);
    @POST("notes/10")
    @Headers({
            "Content-type: Application/json"
    })
    Call<ModelReturn> post_image_note_public( @Body ModelPostImageNote modelPostImageNote);
}
