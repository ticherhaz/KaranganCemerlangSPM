package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.ticherhaz.karangancemerlangspm.R;

public class OnlineStatusViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewUsername;
    private TextView textViewSekolah;
    private TextView textViewReputation;

    public OnlineStatusViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewUsername = itemView.findViewById(R.id.text_view_username);
        textViewSekolah = itemView.findViewById(R.id.text_view_sekolah);
        textViewReputation = itemView.findViewById(R.id.text_view_reputation);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextViewUsername() {
        return textViewUsername;
    }

    public void setTextViewUsername(TextView textViewUsername) {
        this.textViewUsername = textViewUsername;
    }

    public TextView getTextViewSekolah() {
        return textViewSekolah;
    }

    public void setTextViewSekolah(TextView textViewSekolah) {
        this.textViewSekolah = textViewSekolah;
    }

    public TextView getTextViewReputation() {
        return textViewReputation;
    }

    public void setTextViewReputation(TextView textViewReputation) {
        this.textViewReputation = textViewReputation;
    }
}
