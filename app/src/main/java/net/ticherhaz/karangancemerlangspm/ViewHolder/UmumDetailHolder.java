package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.ticherhaz.karangancemerlangspm.R;

public class UmumDetailHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView textViewMasaDibalasOleh;
    private ImageView imageViewProfile;
    private TextView textViewUsername;
    private TextView textViewUserTitle;
    private TextView textViewSekolah;
    private TextView textViewUserJoinDate;
    private TextView textViewGender;
    private TextView textViewPos;
    private TextView textViewReputation;
    private TextView textViewDeskripsi;
    private TextView textViewGiveReputation;
    private TextView textViewMembalas;
    private TextView textViewStatus;
    private TextView textViewState;

    public UmumDetailHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        textViewMasaDibalasOleh = itemView.findViewById(R.id.text_view_masa_dibalas_oleh);
        imageViewProfile = itemView.findViewById(R.id.image_view_profile);
        textViewUsername = itemView.findViewById(R.id.text_view_username);
        textViewUserTitle = itemView.findViewById(R.id.text_view_user_title);
        textViewSekolah = itemView.findViewById(R.id.text_view_sekolah);
        textViewUserJoinDate = itemView.findViewById(R.id.text_view_user_join_date);
        textViewGender = itemView.findViewById(R.id.text_view_gender);
        textViewPos = itemView.findViewById(R.id.text_view_pos);
        textViewReputation = itemView.findViewById(R.id.text_view_reputation);
        textViewDeskripsi = itemView.findViewById(R.id.text_view_deskripsi);
        textViewGiveReputation = itemView.findViewById(R.id.text_view_give_reputation);
        textViewMembalas = itemView.findViewById(R.id.text_view_membalas);
        textViewStatus = itemView.findViewById(R.id.text_view_status);
        textViewState = itemView.findViewById(R.id.text_view_state);
    }

    public TextView getTextViewState() {
        return textViewState;
    }

    public void setTextViewState(TextView textViewState) {
        this.textViewState = textViewState;
    }

    public TextView getTextViewStatus() {
        return textViewStatus;
    }

    public void setTextViewStatus(TextView textViewStatus) {
        this.textViewStatus = textViewStatus;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTextViewMasaDibalasOleh() {
        return textViewMasaDibalasOleh;
    }

    public void setTextViewMasaDibalasOleh(TextView textViewMasaDibalasOleh) {
        this.textViewMasaDibalasOleh = textViewMasaDibalasOleh;
    }

    public ImageView getImageViewProfile() {
        return imageViewProfile;
    }

    public void setImageViewProfile(ImageView imageViewProfile) {
        this.imageViewProfile = imageViewProfile;
    }

    public TextView getTextViewUsername() {
        return textViewUsername;
    }

    public void setTextViewUsername(TextView textViewUsername) {
        this.textViewUsername = textViewUsername;
    }

    public TextView getTextViewUserTitle() {
        return textViewUserTitle;
    }

    public void setTextViewUserTitle(TextView textViewUserTitle) {
        this.textViewUserTitle = textViewUserTitle;
    }

    public TextView getTextViewSekolah() {
        return textViewSekolah;
    }

    public void setTextViewSekolah(TextView textViewSekolah) {
        this.textViewSekolah = textViewSekolah;
    }

    public TextView getTextViewUserJoinDate() {
        return textViewUserJoinDate;
    }

    public void setTextViewUserJoinDate(TextView textViewUserJoinDate) {
        this.textViewUserJoinDate = textViewUserJoinDate;
    }

    public TextView getTextViewGender() {
        return textViewGender;
    }

    public void setTextViewGender(TextView textViewGender) {
        this.textViewGender = textViewGender;
    }

    public TextView getTextViewPos() {
        return textViewPos;
    }

    public void setTextViewPos(TextView textViewPos) {
        this.textViewPos = textViewPos;
    }

    public TextView getTextViewReputation() {
        return textViewReputation;
    }

    public void setTextViewReputation(TextView textViewReputation) {
        this.textViewReputation = textViewReputation;
    }

    public TextView getTextViewDeskripsi() {
        return textViewDeskripsi;
    }

    public void setTextViewDeskripsi(TextView textViewDeskripsi) {
        this.textViewDeskripsi = textViewDeskripsi;
    }

    public TextView getTextViewGiveReputation() {
        return textViewGiveReputation;
    }

    public void setTextViewGiveReputation(TextView textViewGiveReputation) {
        this.textViewGiveReputation = textViewGiveReputation;
    }

    public TextView getTextViewMembalas() {
        return textViewMembalas;
    }

    public void setTextViewMembalas(TextView textViewMembalas) {
        this.textViewMembalas = textViewMembalas;
    }
}
