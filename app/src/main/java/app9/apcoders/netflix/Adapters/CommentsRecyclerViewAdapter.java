package app9.apcoders.netflix.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import app9.apcoders.netflix.Modal.CommentsModal;
import app9.apcoders.netflix.R;

public class CommentsRecyclerViewAdapter extends RecyclerView.Adapter<CommentsRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<CommentsModal> commentsList = new ArrayList<>();

    public CommentsRecyclerViewAdapter(Context context, ArrayList<CommentsModal> commentsList) {

        this.context = context;
        this.commentsList = commentsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.commenttext.setText(commentsList.get(position).getCommenttext());
        holder.username.setText(commentsList.get(position).getUsername());
        SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy" );
        String formatedDate = format.format(commentsList.get(position).getCommentDate());
        holder.commentDate.setText(formatedDate);
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView commenttext, username, commentDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            commenttext = itemView.findViewById(R.id.commenttext);
            username = itemView.findViewById(R.id.username);
            commentDate = itemView.findViewById(R.id.commentDate);
        }
    }
}
