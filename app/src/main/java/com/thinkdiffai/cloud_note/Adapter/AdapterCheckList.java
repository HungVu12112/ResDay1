package com.thinkdiffai.cloud_note.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.Model.GET.ModelCheckList;

import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class AdapterCheckList extends RecyclerView.Adapter<AdapterCheckList.ViewHoderCheckList> {
    List<ModelCheckList> list;
    Context context;
    boolean edit;

    public AdapterCheckList(List<ModelCheckList> list, boolean edit) {
        this.list = list;
        this.edit = edit;
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
        final int index = position;
        ModelCheckList checkList = list.get(index);
        holder.tvCheckNote.setText(checkList.getContent());
        if (checkList.getStatus() == 1) {
            holder.checkbox.setChecked(true);

        } else {
            holder.checkbox.setChecked(false);
        }
        if (edit == true) {
            holder.tvCheckNote.setEnabled(true);
            holder.tvCheckNote.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    Log.e("TAG", "afterTextChanged: "+editable.toString() );
                    checkList.setContent(String.valueOf(editable));
                    list.set(index,checkList );
                }
            });
            holder.checkbox.setClickable(true);
            holder.imgEdit.setVisibility(View.INVISIBLE);
            ModelCheckList obj = new ModelCheckList();
            obj.setId(checkList.getId());
//            if(holder.checkbox.isChecked()==true){
//                obj.setStatus(1);
//            }else if(holder.checkbox.isChecked()==false){
//                obj.setStatus(0);
//            }
            obj.setContent(checkList.getContent());
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    Log.e("TAG", "onCheckedChanged: "+b );
                    if(b==true){
                        Log.e("TAG", "onCheckedChanged: 1");
                        checkList.setStatus(1);
                        list.set(index,checkList );

                    }else{
                        Log.e("TAG", "onCheckedChanged: 1");
                        checkList.setStatus(0);
                        list.set(index,checkList );
                    }
                }
            });
//            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    dialogupdate(context, obj, index, holder);
//                }
//            });

        } else if (edit == false) {
            holder.tvCheckNote.setEnabled(false);
            holder.checkbox.setClickable(false);
            holder.imgEdit.setVisibility(View.INVISIBLE);
        }

    }

    private void dialogupdate(Context context, ModelCheckList obj, int index,ViewHoderCheckList holder) {
        final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_put_check_list);
        CheckBox cbStasus = dialog.findViewById(R.id.cb_status);
        TextInputLayout inputCheckList = dialog.findViewById(R.id.input_checkList);
        Button btn_put = dialog.findViewById(R.id.btn_put);
        inputCheckList.getEditText().setText(obj.getContent());
        btn_put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj.setContent(inputCheckList.getEditText().getText().toString());
                if(cbStasus.isChecked()==true){
                    obj.setStatus(1);
                }else {
                    obj.setStatus(0);
                }
                Log.e("TAG", "onClick: input:"+obj.getContent() );
                list.set(index, obj);
                holder.tvCheckNote.setText(obj.getContent());
                if(obj.getStatus()==1){
                    holder.checkbox.setChecked(true);
                }else if(obj.getStatus()==0){
                    holder.checkbox.setChecked(false);
                }
                Log.e("TAG", "onClick: update content "+list.get(index).getContent());
                dialog.dismiss();

            }
        });
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHoderCheckList extends RecyclerView.ViewHolder {
        private CheckBox checkbox;
        private EditText tvCheckNote;
        private ImageView imgEdit;

        public ViewHoderCheckList(@NonNull View itemView) {
            super(itemView);
            checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
            tvCheckNote = (EditText) itemView.findViewById(R.id.tv_checkNote);


            imgEdit = (ImageView) itemView.findViewById(R.id.img_edit);


        }
    }
}
