package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ticherhaz.karangancemerlangspm.R;

public class JenisViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewTitle;
    private TextView textViewDescription;
    private TextView textViewViewer;

    public JenisViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewTitle = itemView.findViewById(R.id.text_view_jenis_title);
        textViewDescription = itemView.findViewById(R.id.text_view_jenis_description);
        textViewViewer = itemView.findViewById(R.id.text_view_viewer);
    }

    public TextView getTextViewViewer() {
        return textViewViewer;
    }

    public void setTextViewViewer(TextView textViewViewer) {
        this.textViewViewer = textViewViewer;
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
