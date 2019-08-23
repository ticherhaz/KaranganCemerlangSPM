package net.ticherhaz.karangancemerlangspm.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    private ImageView imageViewProfile;

    public UmumHolder(@NonNull View itemView) {
        super(itemView);
        this.view = itemView;
        textViewUmumTitle = itemView.findViewById(R.id.text_view_umum_title);
        textViewUmumViews = itemView.findViewById(R.id.text_view_umum_views);
        textViewJumlahBalas = itemView.findViewById(R.id.text_view_jumlah_balas);
        textViewKedudukan = itemView.findViewById(R.id.text_view_kedudukan);
        textViewDimulaiOleh = itemView.findViewById(R.id.text_view_dimulai_oleh);
        textViewDibalasOleh = itemView.findViewById(R.id.text_view_dibalas_oleh);
        textViewMasaDibalasOleh = itemView.findViewById(R.id.text_view_masa_dibalas_oleh);
        imageViewProfile = itemView.findViewById(R.id.iv_profile);
    }

    public ImageView getImageViewProfile() {
        return imageViewProfile;
    }

    public void setImageViewProfile(ImageView imageViewProfile) {
        this.imageViewProfile = imageViewProfile;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextViewUmumTitle() {
        return textViewUmumTitle;
    }

    public void setTextViewUmumTitle(TextView textViewUmumTitle) {
        this.textViewUmumTitle = textViewUmumTitle;
    }

    public TextView getTextViewUmumViews() {
        return textViewUmumViews;
    }

    public void setTextViewUmumViews(TextView textViewUmumViews) {
        this.textViewUmumViews = textViewUmumViews;
    }

    public TextView getTextViewJumlahBalas() {
        return textViewJumlahBalas;
    }

    public void setTextViewJumlahBalas(TextView textViewJumlahBalas) {
        this.textViewJumlahBalas = textViewJumlahBalas;
    }

    public TextView getTextViewKedudukan() {
        return textViewKedudukan;
    }

    public void setTextViewKedudukan(TextView textViewKedudukan) {
        this.textViewKedudukan = textViewKedudukan;
    }

    public TextView getTextViewDimulaiOleh() {
        return textViewDimulaiOleh;
    }

    public void setTextViewDimulaiOleh(TextView textViewDimulaiOleh) {
        this.textViewDimulaiOleh = textViewDimulaiOleh;
    }

    public TextView getTextViewDibalasOleh() {
        return textViewDibalasOleh;
    }

    public void setTextViewDibalasOleh(TextView textViewDibalasOleh) {
        this.textViewDibalasOleh = textViewDibalasOleh;
    }

    public TextView getTextViewMasaDibalasOleh() {
        return textViewMasaDibalasOleh;
    }

    public void setTextViewMasaDibalasOleh(TextView textViewMasaDibalasOleh) {
        this.textViewMasaDibalasOleh = textViewMasaDibalasOleh;
    }
}
