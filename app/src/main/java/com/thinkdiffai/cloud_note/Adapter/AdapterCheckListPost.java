package com.thinkdiffai.cloud_note.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.Model.POST.ModelCheckListPost;


import java.util.List;

public class AdapterCheckListPost extends RecyclerView.Adapter<AdapterCheckListPost.ViewHoderCheckList> {
    List<ModelCheckListPost> list;
    Context context;

    public AdapterCheckListPost(List<ModelCheckListPost> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoderCheckList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.recycler_checknote, parent, false);
        context = parent.getContext();
        return new ViewHoderCheckList(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoderCheckList holder, int position) {
        final  int index = position;
        ModelCheckListPost checkList = list.get(index);
        holder.tvCheckNote.setText(checkList.getContent());
        holder.checkbox.setChecked(checkList.isStatus());
        if(checkList.isStatus()==true){

        }else{

        }
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0: list.size();
    }

    public class ViewHoderCheckList extends RecyclerView.ViewHolder {
        private CheckBox checkbox;
        private TextView tvCheckNote;
        public ViewHoderCheckList(@NonNull View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvCheckNote = (TextView) itemView.findViewById(R.id.tv_checkNote);

        }
    }
}
