package com.thinkdiffai.cloud_note.View;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Adapter.AdapterNote;
import com.thinkdiffai.cloud_note.DAO.Login;
import com.thinkdiffai.cloud_note.Model.GET.Model_Notes;
import com.thinkdiffai.cloud_note.Model.Model_List_Note;
import com.thinkdiffai.cloud_note.Model.Model_State_Login;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.rupinderjeet.kprogresshud.KProgressHUD;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class Fragment_Calendar extends Fragment {
    private CalendarView calenderView;
    private RecyclerView rcvLitsNote;
    Context context;
    Login daoLogin;
    Model_State_Login user;
    Calendar calendar;
    private Date datePick;
    AdapterNote adapterNote;
    KProgressHUD isloading;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment__calendar, container, false);


        calenderView = (CalendarView) view.findViewById(R.id.calender);
        rcvLitsNote = (RecyclerView) view.findViewById(R.id.rcv_litsNote);
        context = getContext();
        daoLogin = new Login(context);
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
        calendar = Calendar.getInstance();


        calendar.set(Calendar.MONTH, Calendar.NOVEMBER);
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        calendar.set(Calendar.YEAR, 2012);


        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.YEAR, 1);
        getListNote(user.getIdUer());

    }

    @Override
    public void onResume() {
        super.onResume();
        user = daoLogin.getLogin();
    }

    private void getListNote(int idUser) {
        Model_Notes obj = new Model_Notes();
        APINote.apiService.getListNoteByUser(idUser).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
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
//                        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
//                        List<Model_List_Note> listByDate = new ArrayList<>();
//                        Date dateNow = new Date();
//                        try {
//                           Date today = simpleDateFormat.parse(String.valueOf(dateNow));
//                            //Toast.makeText(context, datePick+"", Toast.LENGTH_SHORT).show();
//                            for (Model_List_Note x : obj.getList()) {
//                                Date date = simpleDateFormat.parse(x.getCreateAt());
//                                if(today.compareTo(date)==0) {
//                                    listByDate.add(x);
//                                    Log.e("TAG", "onSelectedDayChange: add list" );
//                                }
//                            }
//                        }catch (ParseException e){
//                            Log.e("TAG", "onSelectedDayChange: "+e.getMessage() );
//                        }
                      //  Toast.makeText(context, "listDateSize: "+listByDate.size() , Toast.LENGTH_SHORT).show();
//                        adapterNote = new AdapterNote(listByDate, true);
//                        rcvLitsNote.setAdapter(adapterNote);
                        calenderView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                                isloading.show();
                                DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
                                String dateClick = i + "-" + (i1 + 1) + "-" + i2;
                                List<Model_List_Note> listByDate = new ArrayList<>();
                                try {
                                    datePick = simpleDateFormat.parse(dateClick);
                                    //Toast.makeText(context, datePick+"", Toast.LENGTH_SHORT).show();
                                    for (Model_List_Note x : obj.getList()) {
                                        Date date = simpleDateFormat.parse(x.getCreateAt());
                                        if(datePick.compareTo(date)==0) {
                                            if(!x.getType().equalsIgnoreCase("screenshot")){
                                                listByDate.add(x);
                                                Log.e("TAG", "onSelectedDayChange: add list" );
                                            }
                                        }
                                    }
                                }catch (ParseException e){
                                    Log.e("TAG", "onSelectedDayChange: "+e.getMessage() );
                                }
                                adapterNote = new AdapterNote(listByDate, true);
                                rcvLitsNote.setAdapter(adapterNote);
                                isloading.dismiss();

                            }
                        });

                    }
                });
    }
}