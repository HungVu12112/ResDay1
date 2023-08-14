package com.thinkdiffai.cloud_note.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.APIs.APINote;
import com.thinkdiffai.cloud_note.Detail_CheckNote;
import com.thinkdiffai.cloud_note.Detail_Note;
import com.thinkdiffai.cloud_note.Detail_Note_ImageActivity;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetCheckList;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetImageNote;
import com.thinkdiffai.cloud_note.Model.GET.ModelGetNoteText;
import com.thinkdiffai.cloud_note.Model.GET.ModelReturn;
import com.thinkdiffai.cloud_note.Model.Model_List_Note;
import com.thinkdiffai.cloud_note.TextNoteActvity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterNote extends RecyclerView.Adapter<AdapterNote.ViewHoderItemNote> {

    Context context;
    List<Model_List_Note> list;
    AdapterCheckList adapterCheckList;
    boolean home;


    public AdapterNote(List<Model_List_Note> list, boolean home) {
        this.list = list;
        this.home = home;
    }

    @NonNull
    @Override
    public ViewHoderItemNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        context = parent.getContext();
        return new ViewHoderItemNote(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHoderItemNote holder, int position) {
        final int index = position;
        Model_List_Note list_note = list.get(index);
        Log.e("TAG", "onBindViewHolder: IdNote" + list_note.getCreateAt());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss ");
        holder.titleHeader.setText(list_note.getTitle());
        holder.createDate.setText(list_note.getCreateAt()+ "");
        holder.dueDate.setText(list_note.getDuaAt() + "");
        if (list_note.getNotePublic() == 1) {
            holder.imgActive1.setVisibility(View.VISIBLE);
            holder.imgActive2.setVisibility(View.GONE);
            holder.imgDelete.setVisibility(View.GONE);
            holder.imgInfor.setVisibility(View.VISIBLE);
            holder.imgInfor.setImageResource(R.drawable.info);
            holder.imgActive1.setImageResource(R.drawable.info);
        } else {
            holder.imgActive1.setVisibility(View.VISIBLE);
            holder.imgActive2.setVisibility(View.VISIBLE);
            holder.imgDelete.setVisibility(View.VISIBLE);
            holder.imgInfor.setVisibility(View.VISIBLE);
            holder.imgInfor.setImageResource(R.drawable.info);
            holder.imgActive1.setImageResource(R.drawable.baseline_edit_24);
        }
        String hex = ChuyenMau(list_note.getColor().getA(), list_note.getColor().getR(), list_note.getColor().getG(), list_note.getColor().getB());
        if (!hex.equalsIgnoreCase("#000")) {
            holder.RecyclerCardview.setCardBackgroundColor(Color.parseColor(hex + ""));
        } else {
            holder.RecyclerCardview.setCardBackgroundColor(Color.parseColor("#ffffff"));
        }
        if (list_note.getDoneNote() == 0) {
            holder.state.setText("Not Done");
            holder.state.setVisibility(View.INVISIBLE);
        } else if (list_note.getDoneNote() == 1) {
            holder.state.setText("Done");
            holder.state.setVisibility(View.INVISIBLE);
        }
        if (home == true) {
            if(list_note.getNotePublic()==0){
                holder.imgActive1.setImageResource(R.drawable.baseline_edit_24);
            }else{
                holder.imgActive1.setImageResource(R.drawable.info);
            }
            holder.imgActive2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDelete(context, list_note.getId(), index);
                }
            });
        } else if (home == false) {
            if(list_note.getNotePublic()==0){
                holder.imgActive1.setImageResource(R.drawable.baseline_restore_24);
            }
            holder.imgActive2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogDelete2(context, list_note.getId(), index);
                }
            });
        }
        if (list_note.getType().equalsIgnoreCase("text")) {
            holder.rcvCheckList.setVisibility(View.GONE);
            holder.contentText.setVisibility(View.VISIBLE);
            holder.layoutNote.setVisibility(View.VISIBLE);
            holder.layoutNoteImage.setVisibility(View.GONE);
            Intent intent = new Intent(context, Detail_Note.class);
            intent.putExtra("id", list_note.getId());
            intent.putExtra("colorA", list_note.getColor().getA());
            intent.putExtra("colorR", list_note.getColor().getR());
            intent.putExtra("colorG", list_note.getColor().getG());
            intent.putExtra("colorB", list_note.getColor().getB());
            intent.putExtra("notePublic", list_note.getNotePublic());

            APINote.apiService.getNoteByIdTypeText(list_note.getId()).enqueue(new Callback<ModelGetNoteText>() {
                @Override
                public void onResponse(Call<ModelGetNoteText> call, Response<ModelGetNoteText> response) {
                    if (response.body() != null && response.isSuccessful()) {
                        ModelGetNoteText obj = response.body();
                        if (obj.getModelTextNote().getData().length() > 150) {
                            String result = "";
                            for (int i = 0; i < 150; i++) {
                                result += obj.getModelTextNote().getData().charAt(i);
                            }
                            result += "............";
                            holder.contentText.setText(result);
                        } else {
                            holder.contentText.setText(obj.getModelTextNote().getData());
                        }
                        intent.putExtra("data", obj.getModelTextNote().getData());
                    }

                }

                @Override
                public void onFailure(Call<ModelGetNoteText> call, Throwable t) {
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
            if (home == true) {
                if(list_note.getNotePublic()==0){
                    holder.imgActive1.setImageResource(R.drawable.baseline_edit_24);
                }else{
                    holder.imgActive1.setImageResource(R.drawable.info);
                }

                holder.imgActive1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(intent);
                    }
                });
                holder.RecyclerCardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(intent);
                    }
                });
            } else if (home == false) {
                holder.imgActive1.setImageResource(R.drawable.baseline_restore_24);
                holder.imgActive1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restore(context, list_note.getId(), index);
                    }
                });

            }


        } else if (list_note.getType().equalsIgnoreCase("checklist")) {
            holder.rcvCheckList.setVisibility(View.VISIBLE);
            holder.contentText.setVisibility(View.GONE);
            holder.layoutNote.setVisibility(View.VISIBLE);
            holder.layoutNoteImage.setVisibility(View.GONE);

            Intent intent = new Intent(context, Detail_CheckNote.class);
            intent.putExtra("id", list_note.getId());

            intent.putExtra("colorA", list_note.getColor().getA());
            intent.putExtra("colorR", list_note.getColor().getR());
            intent.putExtra("colorG", list_note.getColor().getG());
            intent.putExtra("colorB", list_note.getColor().getB());
            intent.putExtra("notePublic", list_note.getNotePublic());
            APINote.apiService.getNoteByIdTypeCheckList(list_note.getId()).enqueue(new Callback<ModelGetCheckList>() {
                @Override
                public void onResponse(Call<ModelGetCheckList> call, Response<ModelGetCheckList> response) {
                    ModelGetCheckList obj = response.body();
                    adapterCheckList = new AdapterCheckList(obj.getModelTextNoteCheckList().getData(), false);
                    holder.rcvCheckList.setAdapter(adapterCheckList);
                    // intent.putExtra("data", new ArrayList<>(obj.getModelTextNoteCheckList().getData()));
                }

                @Override
                public void onFailure(Call<ModelGetCheckList> call, Throwable t) {
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
            if (home == true) {
                if(list_note.getNotePublic()==0){
                    holder.imgActive1.setImageResource(R.drawable.baseline_edit_24);
                }else{
                    holder.imgActive1.setImageResource(R.drawable.info);
                }

                holder.imgActive1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(intent);
                    }
                });
                holder.RecyclerCardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(intent);
                    }
                });
            } else if (home == false) {
                holder.imgActive1.setImageResource(R.drawable.baseline_restore_24);
                holder.imgActive1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restore(context, list_note.getId(), index);
                    }
                });

            }

        } else if (list_note.getType().equalsIgnoreCase("image")) {
            holder.layoutNote.setVisibility(View.GONE);
            holder.layoutNoteImage.setVisibility(View.VISIBLE);
            holder.tvTitle.setText(list_note.getTitle());
            holder.tvCreateAt.setText("CreateAt: "+list_note.getCreateAt());
            Intent intent = new Intent(context, Detail_Note_ImageActivity.class);
            intent.putExtra("id", list_note.getId());
            intent.putExtra("colorA", list_note.getColor().getA());
            intent.putExtra("colorR", list_note.getColor().getR());
            intent.putExtra("colorG", list_note.getColor().getG());
            intent.putExtra("colorB", list_note.getColor().getB());
            intent.putExtra("type", list_note.getType());
            intent.putExtra("notePublic", list_note.getNotePublic());
            APINote.apiService.getNoteByIdTypeImage(list_note.getId()).enqueue(new Callback<ModelGetImageNote>() {

                @SuppressLint("ResourceAsColor")
                @Override
                public void onResponse(Call<ModelGetImageNote> call, Response<ModelGetImageNote> response) {
                    if (response.isSuccessful() & response.body() != null) {
                        ModelGetImageNote obj = response.body();
                        if (obj.getNote().getData().length() > 150) {
                            String result = "";
                            for (int i = 0; i < 150; i++) {
                                result += obj.getNote().getData().charAt(i);
                            }
                            result += "............";
                            holder.tvData.setText(result);
                        } else {
                            holder.tvData.setText(obj.getNote().getData());
                        }
                        if (obj.getNote().getMetaData() != null && obj.getNote().getMetaData() != "") {

//                            holder.titleHeader.setTextColor(R.color.white);
//                            holder.contentText.setTextColor(R.color.white);
//                            holder.dueDate.setTextColor(R.color.white);
//                            holder.createDate.setTextColor(R.color.white);
//                            holder.Due.setTextColor(R.color.white);
//                            holder.Created.setTextColor(R.color.white);
                            Glide.with(holder.imgScreenShot).load(obj.getNote().getMetaData()).into(holder.imgScreenShot);
                        } else {
                            holder.imgScreenShot.setVisibility(View.GONE);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ModelGetImageNote> call, Throwable t) {
                    Log.e("TAG", "onFailure: " + t.getMessage());
                }
            });
            if (home == true) {
                holder.imgInfor.setImageResource(R.drawable.info);
                holder.imgInfor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.startActivity(intent);
                    }
                });
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(intent);
                    }
                });
                holder.imgDelete.setOnClickListener(view->{
                    dialogDelete(context, list_note.getId(), index);
                });
            } else if (home == false) {
                holder.imgInfor.setImageResource(R.drawable.baseline_restore_24);
                holder.imgActive1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        restore(context, list_note.getId(), index);
                    }
                });
                holder.imgDelete.setOnClickListener(view->{
                    dialogDelete2(context, list_note.getId(), index);
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
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

    private void dialogDelete2(Context context, int idNote, int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Bạn có muốn xóa vĩ viễn note này không ");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                APINote.apiService.deleteNote(idNote).enqueue(new Callback<ModelReturn>() {
                    @Override
                    public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                        if (response.isSuccessful() & response.body() != null) {
                            ModelReturn obj = response.body();
                            if (obj.getStatus() == 200) {
                                list.remove(index);
                                notifyDataSetChanged();
                                notifyItemRangeRemoved(index, list.size());
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
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void restore(Context context, int idNote, int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("RESTORE");
        builder.setMessage("Bạn có muốn khôi phục lại note này? ");
        builder.setPositiveButton("Restore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                APINote.apiService.restore(idNote).enqueue(new Callback<ModelReturn>() {
                    @Override
                    public void onResponse(Call<ModelReturn> call, Response<ModelReturn> response) {
                        if (response.isSuccessful() & response.body() != null) {
                            ModelReturn obj = response.body();
                            if (obj.getStatus() == 200) {
                                list.remove(index);
                                notifyDataSetChanged();
                                notifyItemRangeRemoved(index, list.size());
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private String ChuyenMau(float alpha, float red, float green, float blue) {
        // chuyển đổi giá trị của từng kênh màu sang giá trị thập lục phân
        String alphaHex = Integer.toHexString((int) alpha);
        String redHex = Integer.toHexString((int) red);
        String greenHex = Integer.toHexString((int) green);
        String blueHex = Integer.toHexString((int) blue);
// ghép các giá trị thập lục phân lại với nhau theo thứ tự ARGB
        String hex = "#" + redHex + greenHex + blueHex;
        Log.d("TAG", "ChuyenMau: " + hex);
        return hex;
    }

    public class ViewHoderItemNote extends RecyclerView.ViewHolder {
        private CardView RecyclerCardview;
        private ConstraintLayout layoutNoteImage;
        private ImageView imgScreenShot;
        private TextView tvTitle;
        private ImageView imgInfor;
        private ImageView imgDelete;
        private TextView tvData;
        private TextView tvCreateAt;
        private ConstraintLayout layoutNote;
        private TextView titleHeader;
        private TextView state;
        private ImageView imgActive1;
        private ImageView imgActive2;
        private TextView contentText;
        private RecyclerView rcvCheckList;
        private TextView Created;
        private TextView createDate;
        private TextView Due;
        private TextView dueDate;


        public ViewHoderItemNote(@NonNull View itemView) {
            super(itemView);


            RecyclerCardview = (CardView) itemView.findViewById(R.id.Recycler_Cardview);
            layoutNoteImage = (ConstraintLayout) itemView.findViewById(R.id.layout_note_image);
            imgScreenShot = (ImageView) itemView.findViewById(R.id.img_screenShot);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            imgInfor = (ImageView) itemView.findViewById(R.id.img_infor);
            imgDelete = (ImageView) itemView.findViewById(R.id.img_delete);
            tvData = (TextView) itemView.findViewById(R.id.tv_data);
            tvCreateAt = (TextView) itemView.findViewById(R.id.tv_createAt);
            layoutNote = (ConstraintLayout) itemView.findViewById(R.id.layout_note);
            titleHeader = (TextView) itemView.findViewById(R.id.title_header);
            state = (TextView) itemView.findViewById(R.id.state);
            imgActive1 = (ImageView) itemView.findViewById(R.id.img_Active1);
            imgActive2 = (ImageView) itemView.findViewById(R.id.img_Active2);
            contentText = (TextView) itemView.findViewById(R.id.content_text);
            rcvCheckList = (RecyclerView) itemView.findViewById(R.id.rcv_checkList);
            Created = (TextView) itemView.findViewById(R.id.Created);
            createDate = (TextView) itemView.findViewById(R.id.create_date);
            Due = (TextView) itemView.findViewById(R.id.Due);
            dueDate = (TextView) itemView.findViewById(R.id.due_date);


        }
    }
}
