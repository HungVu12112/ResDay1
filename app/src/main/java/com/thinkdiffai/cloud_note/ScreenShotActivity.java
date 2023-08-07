package com.thinkdiffai.cloud_note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterScreenShots;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.DAO.Sort;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetScreenShots;
import com.thinkdiffai.cloud_note.Model.GET.ModelScreenShots;
import com.thinkdiffai.cloud_note.Model.GET.Model_Notes;
import com.thinkdiffai.cloud_note.Model.Model_List_Note;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.Model.Setting_Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenShotActivity extends AppCompatActivity {
    private ImageButton back;
    private ImageButton preferences;
    private SearchView search;
    private RecyclerView recyclerView;
    AdapterScreenShots adapterScreenShots;
    Model_Notes note;
    Login daoLogin;
    Sort daoSort;
    Setting_Sort sort;
    Model_State_Login user;
    String TAG="zzzzzzzzzz";
    KProgressHUD isloading;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shot);
        back = (ImageButton) findViewById(R.id.back);
        preferences = (ImageButton) findViewById(R.id.preferences);
        search = (SearchView) findViewById(R.id.search);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        daoLogin = new Login(ScreenShotActivity.this);
        daoSort = new Sort(ScreenShotActivity.this);
        user = daoLogin.getLogin();
        sort = daoSort.getNameSort();
        isloading = new KProgressHUD(ScreenShotActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScreenShotActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getListScreenShots();
        //getList();
    }
    private void getListScreenShots(){
        isloading.show();
       Model_Notes notes = new Model_Notes();
        APINote.apiService.getListNoteByUser(user.getIdUer()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model_Notes>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Model_Notes model_notes) {
                        notes.setList(model_notes.getList());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: "+e.getMessage() );
                    }

                    @Override
                    public void onComplete() {
                        AsyncTask<Void, Void, List<ModelScreenShots>> asyncTask = new AsyncTask<Void, Void, List<ModelScreenShots>>() {
                            @Override
                            protected List<ModelScreenShots> doInBackground(Void... voids) {
                                return getListItem(notes.getList());
                            }

                            @Override
                            protected void onPostExecute(List<ModelScreenShots> list) {
                                super.onPostExecute(list);
                                isloading.dismiss();
                                Log.e(TAG, "onPostExecute: "+list.size());
                                adapterScreenShots = new AdapterScreenShots(list);
                                recyclerView.setAdapter(adapterScreenShots);
                                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                    @Override
                                    public boolean onQueryTextSubmit(String s) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onQueryTextChange(String s) {
                                        if(s!=""){
                                            adapterScreenShots = new AdapterScreenShots(search(s, list));
                                            recyclerView.setAdapter(adapterScreenShots);
                                        }else{
                                            adapterScreenShots = new AdapterScreenShots(list);
                                            recyclerView.setAdapter(adapterScreenShots);
                                        }
                                        return true;
                                    }
                                });

                            }
                        };
                        asyncTask.execute();

                    }
                });

    }
    private List<ModelScreenShots> getListItem(List<Model_List_Note> list){
        List<ModelScreenShots> screenShots = new ArrayList<>();
        CountDownLatch latch = new CountDownLatch(list.size());
        for (Model_List_Note x : list){
            if(x.getType().equalsIgnoreCase("screenshot")){
                APINote.apiService.getNoteByIdTypeScreenshot(x.getId()).enqueue(new Callback<ModelGetScreenShots>() {
                    @Override
                    public void onResponse(Call<ModelGetScreenShots> call, Response<ModelGetScreenShots> response) {
                        if(response.isSuccessful()&response.body()!=null){
                            ModelScreenShots obj = response.body().getNotes();
                            screenShots.add(obj);

                        }
                        latch.countDown();
                    }
                    @Override
                    public void onFailure(Call<ModelGetScreenShots> call, Throwable t) {
                        Log.e(TAG, "onFailure: "+t.getMessage() );
                        latch.countDown();
                    }
                });
            }else{
                latch.countDown();
            }
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Comparator<ModelScreenShots> compTitle = new Comparator<ModelScreenShots>() {
            @Override
            public int compare(ModelScreenShots model_list_note, ModelScreenShots t1) {
                String title1 = model_list_note.getData().split(" ")[model_list_note.getData().split(" ").length-1];
                String title2 = t1.getData().split(" ")[t1.getData().split(" ").length-1];
                return title1.compareToIgnoreCase(title2);
            }
        };
        Collections.sort(screenShots, compTitle);
        return screenShots;
    }
    private List<ModelScreenShots> search(String query,  List<ModelScreenShots> list ){
        List<ModelScreenShots> listQuery = new ArrayList<>();
        for (ModelScreenShots item : list){
            if(item.getData().toLowerCase().contains(query.toLowerCase())){
                listQuery.add(item);
            }
        }
        return  listQuery;
    }


}