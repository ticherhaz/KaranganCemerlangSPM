package net.ticherhaz.karangancemerlangspm.Util;

import android.graphics.Color;
import android.graphics.Typeface;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.ViewHolder.OnlineStatusViewHolder;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumDetailHolder;

public class UserTypeColor {

    public UserTypeColor() {
    }

    public void setTextColorUserUmumDetail(RegisteredUser registeredUser, UmumDetailHolder holder) {
        switch (registeredUser.getTypeUser()) {
            case "admin":
                holder.getTextViewUserTitle().setTextColor(Color.RED);
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(Color.RED);
                break;
            case "cikgu":
                holder.getTextViewUserTitle().setTextColor(Color.CYAN);
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(Color.RED);
                break;
            case "ahliPremium":
                holder.getTextViewUserTitle().setTextColor(Color.GREEN);
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(Color.GREEN);
                break;
        }
    }

    public void setTextColorUserOnlineUserActivity(RegisteredUser registeredUser, OnlineStatusViewHolder holder) {
        switch (registeredUser.getTypeUser()) {
            case "admin":
                holder.getTextViewUsername().setTextColor(Color.RED);
                break;
            case "cikgu":
                holder.getTextViewUsername().setTextColor(Color.CYAN);
                break;
            case "ahliPremium":
                holder.getTextViewUsername().setTextColor(Color.GREEN);
                break;
        }
    }

}
