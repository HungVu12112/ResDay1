package com.thinkdiffai.cloud_note.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cloud_note.R;
import com.thinkdiffai.cloud_note.Model.Model_List_Note;


import java.util.ArrayList;

public class Genereal_Note_Adapter extends RecyclerView.Adapter<ViewHolder> {

    private Activity activity;
    private ArrayList<Model_List_Note> noteList;

    public Genereal_Note_Adapter(Activity activity, ArrayList<Model_List_Note> noteList) {
        this.activity = activity;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_List_Note note = noteList.get(position);
//        holder.cardView.setCardBackgroundColor(Color.parseColor(note.getColor()));

        //Title
        holder.title.setText(note.getTitle());
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(note.getGenre().equals("Note")) {
//                    Intent intent = new Intent(activity, Detail_Note.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Bundle bundle = new Bundle();
//                    bundle.putInt("Id_Note", note.getId());
//                    bundle.putString("Title_Note", note.getTitle());
//                    bundle.putString("Content_Note", note.getContent());
//                    bundle.putString("Date_Note", note.getCreated_date());
//                    bundle.putString("Color_Note", note.getColor());
//                    intent.putExtras(bundle);
//                    activity.startActivity(intent);
//                }else if(note.getGenre().equals("Checked List")){
//                    Intent intent = new Intent(activity, Detail_CheckNote.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("CheckedList_Title", note.getTitle());
//                    bundle.putString("CheckedList_Content", note.getContent());
//                    bundle.putString("Color_Note_Checklist", note.getColor());
//                    bundle.putString("Date_Checked_List", note.getCreated_date());
//                    bundle.putInt("Id_Check_Note", note.getId());
//                    intent.putExtras(bundle);
//                    activity.startActivity(intent);
//                }
//            }
//        });
        //Content
//        if(note.getContent().length() > 300){
//            String result = "";
//            for(int i = 0 ;i < 300; i++){
//                result += note.getContent().charAt(i);
//            }
//            result +="............";
//            holder.content.setText(result);
//        }else {
//            holder.content.setText(note.getContent());
//        }
//        holder.date_created.setText(note.getCreated_date());
//        holder.date_due.setText(note.getDue_date());
//        holder.state.setText(note.getState());
//        if(note.getGenre().equals("Note")){
//            holder.Due.setVisibility(View.INVISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
class ViewHolder extends RecyclerView.ViewHolder{
    CardView cardView;
    TextView title;
    TextView content;
    TextView Created;
    TextView Due;
    TextView date_created;
    TextView date_due;
    TextView state;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        cardView = itemView.findViewById(R.id.Recycler_Cardview);
        title = itemView.findViewById(R.id.title_header);
        content = itemView.findViewById(R.id.content_text);
        Created = itemView.findViewById(R.id.Created);
        Due = itemView.findViewById(R.id.Due);
        date_created = itemView.findViewById(R.id.create_date);
        date_due = itemView.findViewById(R.id.due_date);
        state = itemView.findViewById(R.id.state);
    }
}
