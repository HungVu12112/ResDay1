package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterNote;
import com.thinkdiffai.cloud_note.Adapter.AdapterUsers;
import com.thinkdiffai.cloud_note.Model.GET.Model_Notes;
import com.thinkdiffai.cloud_note.Model.ModelListLastUser;
import com.thinkdiffai.cloud_note.Model.POST.ModelPostImageNote;
import com.thinkdiffai.cloud_note.Model.POST.ModelTextNoteCheckListPost;
import com.thinkdiffai.cloud_note.Model.POST.ModelTextNotePost;

import java.util.ArrayList;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private TextView tvLogin;
    private RecyclerView rcvListUser;
    KProgressHUD isLoading;
    AdapterUsers adapterUsers;
    private RecyclerView rcvListNotePublic;
    private Button btnCreateNotePublic;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        tvLogin = (TextView) findViewById(R.id.tv_login);
        rcvListUser = (RecyclerView) findViewById(R.id.rcv_listUser);


        btnCreateNotePublic = (Button) findViewById(R.id.btn_createNotePublic);


        rcvListNotePublic = (RecyclerView) findViewById(R.id.rcv_listNotePublic);

        tvLogin.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
        isLoading = new KProgressHUD(HomeActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        getListUser();
        btnCreateNotePublic.setOnClickListener(view->{
            Intent intent = new Intent(HomeActivity.this, CreateNotePublicActivity.class);
            startActivity(intent);
        });

    }

    private void getListUser() {
        isLoading.show();
        APINote.apiService.getListLastUser().enqueue(new Callback<ModelListLastUser>() {

            @Override
            public void onResponse(Call<ModelListLastUser> call, Response<ModelListLastUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isLoading.dismiss();
                    ModelListLastUser listLastUser = response.body();
                    Log.e("TAG", "onResponse: "+listLastUser.getModelUser());
                    adapterUsers = new AdapterUsers(listLastUser.getModelUser());
                    rcvListUser.setAdapter(adapterUsers);

                }
            }

            @Override
            public void onFailure(Call<ModelListLastUser> call, Throwable t) {
                Log.e("TAG", "onFailure: " + t);
            }
        });
        APINote.apiService.getNotePublic().enqueue(new Callback<Model_Notes>() {
            @Override
            public void onResponse(Call<Model_Notes> call, Response<Model_Notes> response) {
                if (response.isSuccessful() & response.body() != null) {
                    isLoading.dismiss();
                    Model_Notes obj = response.body();
                    AdapterNote adapterNote = new AdapterNote(obj.getList(), true);
                    rcvListNotePublic.setAdapter(adapterNote);
                }
            }

            @Override
            public void onFailure(Call<Model_Notes> call, Throwable t) {
                Log.e("TAG", "onFailure: "+t );
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        getListUser();
    }

    private void opendDialogCreateNote(Context context) {
        final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_add_note_public);
        Spinner spinnerTypeNote = (Spinner) dialog.findViewById(R.id.spinner_typeNote);
        LinearLayout layoutTextNote = (LinearLayout) dialog.findViewById(R.id.layout_textNote);
        EditText titleTextNote = (EditText) dialog.findViewById(R.id.title_text_note);
        EditText addContentText = (EditText) dialog.findViewById(R.id.add_content_text);
        LinearLayout layoutCheckList = (LinearLayout) dialog.findViewById(R.id.layout_checkList);
        EditText titleChecklist = (EditText) dialog.findViewById(R.id.title_checklist);
        RecyclerView rcvCheckList = (RecyclerView) dialog.findViewById(R.id.rcv_checkList);
        Button btnAddCheckList = (Button) dialog.findViewById(R.id.btn_addCheckList);
        LinearLayout layoutNoteImage = (LinearLayout) dialog.findViewById(R.id.layout_noteImage);
        Button btnUpload = (Button) dialog.findViewById(R.id.btn_upload);
        ImageView imgBackground = (ImageView) dialog.findViewById(R.id.img_background);
        EditText titleImage = (EditText) dialog.findViewById(R.id.title_image);
        EditText addContentImage = (EditText) dialog.findViewById(R.id.add_content_image);
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
                switch (i){
                    case 0:
                        ModelTextNotePost text = new ModelTextNotePost();
                        layoutTextNote.setVisibility(View.VISIBLE);
                        layoutNoteImage.setVisibility(View.GONE);
                        layoutCheckList.setVisibility(View.GONE);
                        break;
                    case 1:
                        ModelTextNoteCheckListPost checklist = new ModelTextNoteCheckListPost();
                        layoutTextNote.setVisibility(View.GONE);
                        layoutNoteImage.setVisibility(View.GONE);
                        layoutCheckList.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        ModelPostImageNote image = new ModelPostImageNote();
                        layoutTextNote.setVisibility(View.GONE);
                        layoutNoteImage.setVisibility(View.VISIBLE);
                        layoutCheckList.setVisibility(View.GONE);
                        break;
                    default:
                        ModelTextNotePost text1 = new ModelTextNotePost();
                        layoutTextNote.setVisibility(View.VISIBLE);
                        layoutNoteImage.setVisibility(View.GONE);
                        layoutCheckList.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        dialog.show();
    }
    private void postTextNotePublic(ModelTextNotePost obj){

    }
}