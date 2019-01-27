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

    public KaranganViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewTajuk = itemView.findViewById(R.id.text_view_tajuk);
        textViewDeskripsi = itemView.findViewById(R.id.text_view_deskripsi);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextViewTajuk() {
        return textViewTajuk;
    }

    public void setTextViewTajuk(TextView textViewTajuk) {
        this.textViewTajuk = textViewTajuk;
    }

    public TextView getTextViewDeskripsi() {
        return textViewDeskripsi;
    }

    public void setTextViewDeskripsi(TextView textViewDeskripsi) {
        this.textViewDeskripsi = textViewDeskripsi;
    }
}
