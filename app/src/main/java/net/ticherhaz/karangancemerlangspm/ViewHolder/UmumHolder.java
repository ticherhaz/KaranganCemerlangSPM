package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.ticherhaz.karangancemerlangspm.R;

public class UmumHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewUmumTitle;
    private TextView textViewUmumViews;
    private TextView textViewJumlahBalas;
    private TextView textViewKedudukan;
    private TextView textViewDimulaiOleh;
    private TextView textViewDibalasOleh;
    private TextView textViewMasaDibalasOleh;

    public UmumHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        textViewUmumTitle = itemView.findViewById(R.id.text_view_umum_title);
        textViewUmumViews = itemView.findViewById(R.id.text_view_umum_views);
        textViewJumlahBalas = itemView.findViewById(R.id.text_view_jumlah_balas);
        textViewKedudukan = itemView.findViewById(R.id.text_view_kedudukan);

    }
}
