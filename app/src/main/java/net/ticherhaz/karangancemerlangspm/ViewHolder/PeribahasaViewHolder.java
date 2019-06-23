package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ticherhaz.karangancemerlangspm.R;

public class PeribahasaViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewTitle;
    private TextView textViewDescription;

    public PeribahasaViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewTitle = view.findViewById(R.id.text_view_peribahasa_title);
        textViewDescription = view.findViewById(R.id.text_view_peribahasa_description);
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
