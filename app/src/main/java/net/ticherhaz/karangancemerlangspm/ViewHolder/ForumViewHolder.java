package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.ticherhaz.karangancemerlangspm.R;

public class ForumViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewForumTitle;
    private TextView textViewUserViewing;
    private TextView textViewForumDescription;
    private TextView textViewForumViews;
    private TextView textViewThreads;
    private TextView textViewPostThreadsCount;
    private TextView textViewLastThreadPost;
    private TextView textViewLastThreadByUser;

    public ForumViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewForumTitle = itemView.findViewById(R.id.text_view_forum_title);
        textViewUserViewing = itemView.findViewById(R.id.text_view_forum_user_viewing);
        textViewForumDescription = itemView.findViewById(R.id.text_view_forum_description);
        textViewForumViews = itemView.findViewById(R.id.text_view_forum_views);
        textViewThreads = itemView.findViewById(R.id.text_view_threads);
        textViewPostThreadsCount = itemView.findViewById(R.id.text_view_posts_thread_count);
        textViewLastThreadPost = itemView.findViewById(R.id.text_view_last_thread_post);
        textViewLastThreadByUser = itemView.findViewById(R.id.text_view_last_thread_by_user);

    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextViewForumTitle() {
        return textViewForumTitle;
    }

    public void setTextViewForumTitle(TextView textViewForumTitle) {
        this.textViewForumTitle = textViewForumTitle;
    }

    public TextView getTextViewUserViewing() {
        return textViewUserViewing;
    }

    public void setTextViewUserViewing(TextView textViewUserViewing) {
        this.textViewUserViewing = textViewUserViewing;
    }

    public TextView getTextViewForumDescrption() {
        return textViewForumDescription;
    }

    public void setTextViewForumDescrption(TextView textViewForumDescrption) {
        this.textViewForumDescription = textViewForumDescrption;
    }

    public TextView getTextViewForumViews() {
        return textViewForumViews;
    }

    public void setTextViewForumViews(TextView textViewForumViews) {
        this.textViewForumViews = textViewForumViews;
    }

    public TextView getTextViewThreads() {
        return textViewThreads;
    }

    public void setTextViewThreads(TextView textViewThreads) {
        this.textViewThreads = textViewThreads;
    }

    public TextView getTextViewPostThreadsCount() {
        return textViewPostThreadsCount;
    }

    public void setTextViewPostThreadsCount(TextView textViewPostThreadsCount) {
        this.textViewPostThreadsCount = textViewPostThreadsCount;
    }

    public TextView getTextViewLastThreadPost() {
        return textViewLastThreadPost;
    }

    public void setTextViewLastThreadPost(TextView textViewLastThreadPost) {
        this.textViewLastThreadPost = textViewLastThreadPost;
    }

    public TextView getTextViewLastThreadByUser() {
        return textViewLastThreadByUser;
    }

    public void setTextViewLastThreadByUser(TextView textViewLastThreadByUser) {
        this.textViewLastThreadByUser = textViewLastThreadByUser;
    }


}
