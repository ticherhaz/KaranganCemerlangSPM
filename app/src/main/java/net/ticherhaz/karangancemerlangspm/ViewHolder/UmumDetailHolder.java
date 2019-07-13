package net.ticherhaz.karangancemerlangspm.ViewHolder;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    private TextView textViewStatus;
    private TextView textViewState;
    private TextView textViewEditReply;
    private EditText editTextEdit;
    private TextView textViewEditYes;
    private TextView textViewEditCancel;

    @SuppressLint("ClickableViewAccessibility")
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
        textViewStatus = itemView.findViewById(R.id.text_view_status);
        textViewState = itemView.findViewById(R.id.text_view_state);
        textViewEditReply = itemView.findViewById(R.id.text_view_edit_reply);
        editTextEdit = itemView.findViewById(R.id.edit_text_deskripsi);
        textViewEditYes = itemView.findViewById(R.id.text_view_edit_reply_submit);
        textViewEditCancel = itemView.findViewById(R.id.text_view_edit_reply_cancel);

        textViewDeskripsi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Disallow the touch request for parent scroll on touch of child view
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        //  textViewDeskripsi.setMovementMethod(new ScrollingMovementMethod());
    }

    public TextView getTextViewEditYes() {
        return textViewEditYes;
    }

    public void setTextViewEditYes(TextView textViewEditYes) {
        this.textViewEditYes = textViewEditYes;
    }

    public TextView getTextViewEditCancel() {
        return textViewEditCancel;
    }

    public void setTextViewEditCancel(TextView textViewEditCancel) {
        this.textViewEditCancel = textViewEditCancel;
    }

    public EditText getEditTextEdit() {
        return editTextEdit;
    }

    public void setEditTextEdit(EditText editTextEdit) {
        this.editTextEdit = editTextEdit;
    }

    public TextView getTextViewEditReply() {
        return textViewEditReply;
    }

    public void setTextViewEditReply(TextView textViewEditReply) {
        this.textViewEditReply = textViewEditReply;
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

}
