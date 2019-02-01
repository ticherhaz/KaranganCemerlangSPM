package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.ticherhaz.karangancemerlangspm.R;

public class KaranganViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewTajuk;
    private TextView textViewDeskripsi;
    private TextView textViewViewer;
    private TextView textViewFav;

    public KaranganViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewTajuk = itemView.findViewById(R.id.text_view_tajuk);
        textViewDeskripsi = itemView.findViewById(R.id.text_view_deskripsi);
        textViewViewer = itemView.findViewById(R.id.text_view_viewer);
        textViewFav = itemView.findViewById(R.id.text_view_fav);
    }

    public View getView() {
        return view;
    }

    public TextView getTextViewTajuk() {
        return textViewTajuk;
    }

    public TextView getTextViewDeskripsi() {
        return textViewDeskripsi;
    }

    public TextView getTextViewViewer() {
        return textViewViewer;
    }

    public void setTextViewViewer(TextView textViewViewer) {
        this.textViewViewer = textViewViewer;
    }

    public TextView getTextViewFav() {
        return textViewFav;
    }

    public void setTextViewFav(TextView textViewFav) {
        this.textViewFav = textViewFav;
    }
}
