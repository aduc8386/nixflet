package dhcnhn.aduc8386.nixflet.controller.adapter;


import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import dhcnhn.aduc8386.nixflet.R;
import dhcnhn.aduc8386.nixflet.model.UserComment;

public class UserCommentAdapter extends RecyclerView.Adapter<UserCommentAdapter.UserCommentHolder> {

    private List<UserComment> userComments;

    public UserCommentAdapter(List<UserComment> userComments) {
        this.userComments = userComments;
    }

    @NonNull
    @Override
    public UserCommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_comment, parent, false);

        return new UserCommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserCommentHolder holder, int position) {
        UserComment userComment = userComments.get(position);

        String userEmail = userComment.getUser().getEmail();
        String[] arr = userEmail.split("@");

        String userEmailPrefix = arr[0];
        String userCommentContent = userComment.getComment();

        String userCommentText = String.format("@%s %s", userEmailPrefix, userCommentContent);

        SpannableString comment = new SpannableString(userCommentText);

        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);

        comment.setSpan(styleSpan, 0, userEmailPrefix.length()+1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.textViewUserComment.setText(comment);
        Glide.with(holder.itemView.getContext())
                .load(userComment.getUser().getProfilePicture())
                .error(R.drawable.user_avatar_holder)
                .centerCrop()
                .into(holder.imageViewUserProfile);
    }

    @Override
    public int getItemCount() {
        return userComments.size();
    }

    static class UserCommentHolder extends RecyclerView.ViewHolder {

        private final TextView textViewUserComment;
        private final ImageView imageViewUserProfile;

        public UserCommentHolder(@NonNull View itemView) {
            super(itemView);

            textViewUserComment = itemView.findViewById(R.id.textview_item_user_comment_user_comment);
            imageViewUserProfile = itemView.findViewById(R.id.circle_imageview_item_user_comment_user_avatar);

        }
    }
}
