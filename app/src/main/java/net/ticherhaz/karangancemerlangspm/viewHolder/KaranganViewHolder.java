package net.ticherhaz.karangancemerlangspm.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ticherhaz.karangancemerlangspm.R;

public class KaranganViewHolder extends RecyclerView.ViewHolder {

    private final View view;
    private final TextView textViewTajuk;
    private final TextView textViewDeskripsi;
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
