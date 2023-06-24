package com.example.instagramclone.Adapters;

import static java.text.DateFormat.getDateTimeInstance;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagramclone.Models.Comment;
import com.example.instagramclone.Models.User;
import com.example.instagramclone.R;
import com.example.instagramclone.databinding.CommentSampleBinding;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>
{
    Context context;
    ArrayList<Comment>list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.comment_sample,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {

        Comment comment=list.get(position);
//        holder.comment.setText(comment.getCommentBody());
        String text = TimeAgo.using(comment.getCommentedAt());
        holder.commentedAt.setText(text);
        String commentedBy=comment.getCommentedBy();

        FirebaseDatabase.getInstance().getReference().child("Users")
                .child(commentedBy).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user=snapshot.getValue(User.class);
                        Picasso.get().load(user.getProfilePhoto()).placeholder(R.drawable.default_profile).into(holder.profile);

                        holder.comment.setText(Html.fromHtml("<b>" +user.getName() +"</b> " + "    "+comment.getCommentBody()));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView comment,commentedAt;
        ImageView profile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
           comment=itemView.findViewById(R.id.comment);
           commentedAt=itemView.findViewById(R.id.commentedAt);
           profile=itemView.findViewById(R.id.profilePic);
        }
    }

    public static String getTimeDate(long timestamp){
        try{
            DateFormat dateFormat = getDateTimeInstance();
            Date netDate = (new Date(timestamp));
            return dateFormat.format(netDate);
        } catch(Exception e) {
            return "date";
        }
    }
}
