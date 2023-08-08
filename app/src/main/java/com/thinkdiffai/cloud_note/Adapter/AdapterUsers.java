package com.thinkdiffai.cloud_note.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.Model.GET.ModelUser;
import com.thinkdiffai.cloud_note.NotesPublicActivity;

import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.ViewHoderUser> {
    List<ModelUser> list;
    Context context;

    public AdapterUsers(List<ModelUser> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHoderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.item_user, parent, false);
        context = parent.getContext();
        return new ViewHoderUser(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoderUser holder, int position) {
        final int index =position;
        ModelUser userModel = list.get(index);
        holder.tvUsername.setText(userModel.getName().trim());
        Glide.with(holder.imgAvatar).load(userModel.getLinkAvatar()).into(holder.imgAvatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NotesPublicActivity.class);
                intent.putExtra("idUser", userModel.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0: list.size();
    }

    public class ViewHoderUser extends RecyclerView.ViewHolder {
        private TextView tvUsername;
        private ImageView imgAvatar;
        public ViewHoderUser(@NonNull View itemView) {
            super(itemView);


            tvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            imgAvatar = itemView.findViewById(R.id.img_avatar);

        }
    }
}
