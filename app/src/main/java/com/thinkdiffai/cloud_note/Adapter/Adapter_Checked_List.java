package com.thinkdiffai.cloud_note.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cloud_note.R;

import java.util.ArrayList;

public class Adapter_Checked_List extends RecyclerView.Adapter<ViewHolder_Checked_List > {
    private Activity mActivity;
    private ArrayList<String> checkList;

    public Adapter_Checked_List(Activity activity, ArrayList<String> checkList){
        this.mActivity = activity;
        this.checkList = checkList;
    }

    @NonNull
    @Override
    public ViewHolder_Checked_List onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder_Checked_List(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_checknote, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_Checked_List holder, int position) {
        holder.editText.setText(checkList.get(position));
    }

    @Override
    public int getItemCount() {
        return checkList.size();
    }
}
class ViewHolder_Checked_List extends RecyclerView.ViewHolder{

    CheckBox checkbox;
    TextView editText;

    public ViewHolder_Checked_List(@NonNull View itemView) {
        super(itemView);
        checkbox = itemView.findViewById(R.id.checkbox);
        editText = itemView.findViewById(R.id.tv_checkNote);
    }
}
