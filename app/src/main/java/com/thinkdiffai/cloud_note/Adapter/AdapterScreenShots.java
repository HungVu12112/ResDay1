package com.thinkdiffai.cloud_note.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Detail_Note_ImageActivity;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.GET.ModelScreenShots;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterScreenShots extends RecyclerView.Adapter<AdapterScreenShots.ViewHoderScreenShot> {
    Context context;
    List<ModelScreenShots> list;
    String TAG="aaaaaaa";

    public AdapterScreenShots(List<ModelScreenShots> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoderScreenShot onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_screen_shots, parent, false);
        context = parent.getContext();
        return new ViewHoderScreenShot(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoderScreenShot holder, int position) {
        final int index = position;
        ModelScreenShots obj = list.get(index);
        holder.tvTitle.setText(obj.getTitle());
        if(obj.getType().equalsIgnoreCase("screenshot")){
            Glide.with(holder.imgScreenShot).load(obj.getMetaData()).into(holder.imgScreenShot);
            if(obj.getData().length() > 120){
                String result = "";
                for (int i = 0; i < 120; i++) {
                    result += obj.getData().charAt(i);
                }
                result += "............";
                holder.tvData.setText(result);
            } else {
                holder.tvData.setText(obj.getData());
            }
            holder.tvCreateAt.setText("CreateAt: "+obj.getCreateAt());
            holder.imgInfor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Detail_Note_ImageActivity.class);
                    intent.putExtra("id", obj.getId());
                    intent.putExtra("colorA", obj.getColor().getA());
                    intent.putExtra("colorR", obj.getColor().getR());
                    intent.putExtra("colorG", obj.getColor().getG());
                    intent.putExtra("colorB", obj.getColor().getB());
                    intent.putExtra("type", obj.getType());
                    context.startActivity(intent);
                }
            });
            holder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDelete(context, obj.getId(), index);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0: list.size();
    }
    private void dialogDelete(Context context, int id, int index) {
        final Dialog dialog = new Dialog(context, androidx.appcompat.R.style.Theme_AppCompat_Light_Dialog_Alert);
        dialog.setContentView(R.layout.dialog_delete_note);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
        Button btn_delete = dialog.findViewById(R.id.btn_delete);
        Button btn_move_trash = dialog.findViewById(R.id.btn_move_trash);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APINote.apiService.deleteNote(id).enqueue(new Callback<ModelReturn>() {
                    @Override
                    public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                        if (response.isSuccessful() & response.body() != null) {
                            ModelReturn r = response.body();
                            if (r.getStatus() == 200) {
                                list.remove(index);
                                notifyDataSetChanged();
                                notifyItemRangeRemoved(index, list.size());
                                Toast.makeText(context, r.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ModelReturn> call, Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                });
            }
        });
        btn_move_trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                APINote.apiService.moveToTrash(id).enqueue(new Callback<ModelReturn>() {
                    @Override
                    public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                        if (response.isSuccessful() & response.body() != null) {
                            ModelReturn r = response.body();
                            if (r.getStatus() == 200) {
                                list.remove(index);
                                notifyDataSetChanged();
                                notifyItemRangeRemoved(index, list.size());
                                Toast.makeText(context, r.getMessage(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ModelReturn> call, Throwable t) {
                        Log.e("TAG", "onFailure: " + t.getMessage());
                    }
                });
            }
        });

        dialog.show();
    }

    public class ViewHoderScreenShot extends RecyclerView.ViewHolder {
        private CardView RecyclerCardview;
        private ImageView imgScreenShot;
        private TextView tvTitle;
        private ImageView imgInfor;
        private ImageView imgDelete;
        private TextView tvData;
        private TextView tvCreateAt;
        public ViewHoderScreenShot(@NonNull View itemView) {
            super(itemView);

            RecyclerCardview = (CardView) itemView.findViewById(R.id.Recycler_Cardview);
            imgScreenShot = (ImageView) itemView.findViewById(R.id.img_screenShot);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            imgInfor = (ImageView) itemView.findViewById(R.id.img_infor);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);
            tvData = (TextView) itemView.findViewById(R.id.tv_data);
            tvCreateAt = (TextView) itemView.findViewById(R.id.tv_createAt);

        }
    }
}
