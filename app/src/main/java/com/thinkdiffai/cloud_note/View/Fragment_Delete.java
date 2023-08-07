package com.thinkdiffai.cloud_note.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterNote;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.DAO.Sort;
import com.thinkdiffai.cloud_note.Model.GET.Model_Notes;
import com.thinkdiffai.cloud_note.Model.Model_List_Note;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;
import com.thinkdiffai.cloud_note.Model.Setting_Sort;
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

public class Fragment_Delete extends Fragment {
    private ImageButton btnSetting;
    private SearchView search;


    private ImageButton buttonSortby;
    private RecyclerView recyclerView;
    Context context;
    AdapterNote adapterNote;
    Login daoLogin;
    Model_State_Login user;
    Sort daoSort;
    Setting_Sort sort;
    boolean isSort = true;
    KProgressHUD isloading;
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deleted_page, container, false);
        btnSetting = (ImageButton) view.findViewById(R.id.btnSetting);
        buttonSortby = (ImageButton) view.findViewById(R.id.button_sortby);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_View);
        search = (SearchView) view.findViewById(R.id.search);
        context = getContext();
        daoLogin = new Login(context);
        daoSort = new Sort(context);
        isloading = new KProgressHUD(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = daoLogin.getLogin();
        sort = daoSort.getNameSort();
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SettingActivity.class);
                getActivity().startActivity(intent);
            }
        });
        getData(user.getIdUer());
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(user.getIdUer());
        user = daoLogin.getLogin();
        sort = daoSort.getNameSort();
    }

    private void getData(int idUser) {
        isloading.show();
        Model_Notes obj = new Model_Notes();
        APINote.apiService.getListTrash(idUser).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Model_Notes>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Model_Notes model_notes) {
                        obj.setList(model_notes.getList());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.e("TAG", "onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        isloading.dismiss();
                        adapterNote = new AdapterNote(obj.getList(), false);
                        recyclerView.setAdapter(adapterNote);

                        buttonSortby.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                List<Model_List_Note> listSort = sort(sort.getSortName(), obj.getList());
                                Log.e("TAG", "onClick: sắp sếp theo ngày ");
                                adapterNote = new AdapterNote(listSort, true);
                                recyclerView.setAdapter(adapterNote);
//                                if(isSort==true){
//                                    isSort=false;
//
//                                }else {
//                                    Log.e("TAG", "onClick: trở lại ban đầu " );
//                                    isSort=true;
//                                    adapterNote = new AdapterNote(obj.getList(), true);
//                                    recyclerView.setAdapter(adapterNote);
//                                }
                            }
                        });
                        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                if (newText.trim() != "") {
                                    List<Model_List_Note> listQuery = search(newText, obj.getList());
                                    adapterNote = new AdapterNote(listQuery, false);
                                    recyclerView.setAdapter(adapterNote);
                                } else {
                                    adapterNote = new AdapterNote(obj.getList(), false);
                                    recyclerView.setAdapter(adapterNote);
                                }
                                return true;
                            }
                        });

                    }
                });
    }

    private List<Model_List_Note> search(String query, List<Model_List_Note> list) {
        List<Model_List_Note> listQuery = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getTitle().toLowerCase().contains(query.toLowerCase())) {
                listQuery.add(list.get(i));
                Log.d("TAG", "search: Title:" + list.get(i).getTitle());
//                Log.d("TAG", "search: color:"+list.get(i).getColor().getG());
//                Log.d("TAG", "search: color:"+list.get(i).getColor().getB());
            }
        }
        return listQuery;
    }

    private List<Model_List_Note> sort(String querySort, List<Model_List_Note> list) {
        List<Model_List_Note> listSort = new ArrayList<>();
        switch (querySort) {
            case "title":
                Comparator<Model_List_Note> compTitle = new Comparator<Model_List_Note>() {
                    @Override
                    public int compare(Model_List_Note model_list_note, Model_List_Note t1) {
                        String title1 = model_list_note.getTitle().split(" ")[model_list_note.getTitle().split(" ").length - 1];
                        String title2 = t1.getTitle().split(" ")[t1.getTitle().split(" ").length - 1];
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
                        } catch (ParseException e) {
                            Log.e("TAG", "compare: " + e.getMessage());
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
                        String title1 = model_list_note.getTitle().split(" ")[model_list_note.getTitle().split(" ").length - 1];
                        String title2 = t1.getTitle().split(" ")[t1.getTitle().split(" ").length - 1];
                        return title1.compareToIgnoreCase(title2);
                    }
                };
                Collections.sort(list, compTitle2);
                break;


        }
        return list;
    }
}
