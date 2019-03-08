package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.ticherhaz.karangancemerlangspm.R;

public class JenisViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewTitle;
    private TextView textViewDescription;

    public JenisViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewTitle = itemView.findViewById(R.id.text_view_jenis_title);
        textViewDescription = itemView.findViewById(R.id.text_view_jenis_description);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextViewTitle() {
        return textViewTitle;
    }

    public void setTextViewTitle(TextView textViewTitle) {
        this.textViewTitle = textViewTitle;
    }

    public TextView getTextViewDescription() {
        return textViewDescription;
    }

    public void setTextViewDescription(TextView textViewDescription) {
        this.textViewDescription = textViewDescription;
    }
}
