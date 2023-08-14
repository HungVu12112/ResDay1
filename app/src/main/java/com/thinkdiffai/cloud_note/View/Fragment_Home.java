package com.thinkdiffai.cloud_note.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterNote;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.DAO.Sort;
import com.thinkdiffai.cloud_note.Model.Model_List_Note;
import com.thinkdiffai.cloud_note.Model.GET.Model_Notes;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.Model.Setting_Sort;
import com.thinkdiffai.cloud_note.ScreenShotActivity;
import com.thinkdiffai.cloud_note.SettingActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment_Home extends Fragment {
    private Activity mActivity;
    Context context;
    private CardView homePage;
    private ImageButton preferences;
    private SearchView search;
    private ImageButton buttonSortby;
    private RecyclerView recyclerView;
    private ImageButton screenShot;



    AdapterNote adapterNote;
    Model_Notes note;
    Login daoLogin;
    Sort daoSort;
    Setting_Sort sort;
    Model_State_Login user;
    boolean isSort;
    KProgressHUD isloading;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_page, container, false);
        mActivity = (Activity) getActivity();

        homePage = (CardView) view.findViewById(R.id.home_page);
        preferences = (ImageButton) view.findViewById(R.id.preferences);
        screenShot = (ImageButton) view.findViewById(R.id.screenShot);

        search = (SearchView) view.findViewById(R.id.search);
        buttonSortby = (ImageButton) view.findViewById(R.id.button_sortby);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        //====================================================
        context = getContext();
        daoLogin = new Login(context);
        daoSort = new Sort(context);
        isloading= new KProgressHUD(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDimAmount(0.5f).setCancellable(true)
                .setAnimationSpeed(2);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = daoLogin.getLogin();
        sort = daoSort.getNameSort();
        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingActivity.class);
                mActivity.startActivity(intent);
            }
        });

        getListNote();
        screenShot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScreenShotActivity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
//        getListNote();
        user = daoLogin.getLogin();
        sort= daoSort.getNameSort();
        Log.e("TAG", "onResume: sort name"+sort.getSortName() );
    }

    @Override
    public void onStart() {
        super.onStart();
        getListNote();
    }

    public void getListNote() {
        isloading.show();
        Log.d("TAG", "getListNote: Step1");
        APINote.apiService.getListNoteByUser(user.getIdUer()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model_Notes>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Model_Notes model_notes) {
                        note = model_notes;
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        isloading.dismiss();
//                        Toast.makeText(context, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                        List<Model_List_Note> list = new ArrayList<>();
                        for (Model_List_Note x : note.getList()){
                            if(!x.getType().equalsIgnoreCase("screenshot")){
                                list.add(x);
                            }
                        }

                        adapterNote = new AdapterNote(list, true);
                        recyclerView.setAdapter(adapterNote);

                        buttonSortby.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                List<Model_List_Note> listSort = sort(sort.getSortName(), list);
                                Log.e("TAG", "onClick: sắp sếp theo ngày " );
                                adapterNote = new AdapterNote(listSort, true);
                                recyclerView.setAdapter(adapterNote);


                                if(isSort==true){


                                }else {
                                    isSort=true;
                                    Log.e("TAG", "onClick: list đầu " );
                                    adapterNote = new AdapterNote(note.getList(), true);
                                    recyclerView.setAdapter(adapterNote);
                                }

                            }
                        });
                        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String s) {

                                return true;
                            }

                            @Override
                            public boolean onQueryTextChange(String s) {
                                Log.e("TAG", "onQueryTextSubmit: query :" +s);
                                if(s.trim()!=""){
                                    List<Model_List_Note> listQuery = search(s, note.getList());
                                    adapterNote= new AdapterNote(listQuery, true);
                                    recyclerView.setAdapter(adapterNote);
                                }else{
                                    Log.e("TAG", "onQueryTextSubmit: query trống" );
                                    List<Model_List_Note> list =new ArrayList<>();
                                    list.addAll(note.getList());
                                    adapterNote= new AdapterNote(list, true);
                                    recyclerView.setAdapter(adapterNote);
                                }
                                return true;
                            }
                        });
                    }
                });
    }
    private List<Model_List_Note> search(String query,  List<Model_List_Note> list ){
        List<Model_List_Note> listQuery = new ArrayList<>();
        for (int i=0;i< list.size();i++){
            if(list.get(i).getType().equalsIgnoreCase("image")||list.get(i).getType().equalsIgnoreCase("text")){
                if(list.get(i).getTitle().toLowerCase().contains(query.toLowerCase()) || list.get(i).getCreateAt().toLowerCase().contains(query.toLowerCase())){
                    listQuery.add(list.get(i));
                }
            }else if(list.get(i).getType().equalsIgnoreCase("checklist")){
                if(list.get(i).getTitle().toLowerCase().contains(query.toLowerCase()) || list.get(i).getCreateAt().toLowerCase().contains(query.toLowerCase())){
                    listQuery.add(list.get(i));
                }
            }


        }
        return  listQuery;
    }
    private List<Model_List_Note> sort (String querySort, List<Model_List_Note> list){
        List<Model_List_Note> listSort = new ArrayList<>();
        switch (querySort){
            case "title":
                Comparator<Model_List_Note> compTitle = new Comparator<Model_List_Note>() {
                    @Override
                    public int compare(Model_List_Note model_list_note, Model_List_Note t1) {
                        String title1 = model_list_note.getTitle().split(" ")[model_list_note.getTitle().split(" ").length-1];
                        String title2 = t1.getTitle().split(" ")[t1.getTitle().split(" ").length-1];
                        return title1.compareToIgnoreCase(title2);
                    }
                };
                Collections.sort(list, compTitle);
                break;
            case "createAt":
                Comparator<Model_List_Note> compDate = new Comparator<Model_List_Note>() {
                    @Override
                    public int compare(Model_List_Note model_list_note, Model_List_Note t1) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
                        try {
                            Date date1 = simpleDateFormat.parse(model_list_note.getCreateAt());
                            Date date2 = simpleDateFormat.parse(t1.getCreateAt());
                            return date2.compareTo(date1);
                        }catch (ParseException e){
                            Log.e("TAG", "compare: "+e.getMessage() );
                        }
                        return 0;

                    }
                };
                Collections.sort(list, compDate);
                break;
            default:
                Comparator<Model_List_Note> compTitle2 = new Comparator<Model_List_Note>() {
                    @Override
                    public int compare(Model_List_Note model_list_note, Model_List_Note t1) {
                        String title1 = model_list_note.getTitle().split(" ")[model_list_note.getTitle().split(" ").length-1];
                        String title2 = t1.getTitle().split(" ")[t1.getTitle().split(" ").length-1];
                        return title1.compareToIgnoreCase(title2);
                    }
                };
                Collections.sort(list, compTitle2);
                break;


        }
        return list;
    }


}