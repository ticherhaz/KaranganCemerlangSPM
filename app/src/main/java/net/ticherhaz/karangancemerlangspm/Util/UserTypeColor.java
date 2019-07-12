package net.ticherhaz.karangancemerlangspm.Util;

import android.app.Activity;
import android.graphics.Typeface;

import net.ticherhaz.karangancemerlangspm.Model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.ViewHolder.OnlineStatusViewHolder;
import net.ticherhaz.karangancemerlangspm.ViewHolder.UmumDetailHolder;

public class UserTypeColor {

    public UserTypeColor() {
    }

    public void setTextColorUserUmumDetail(RegisteredUser registeredUser, UmumDetailHolder holder, Activity activity) {
        switch (registeredUser.getTypeUser()) {
            case "admin":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorAdmin));
                break;
            case "moderator":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorModerator));
                break;
            case "cikgu":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorCikgu));
                break;
            case "ahliPremium":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorAhliPremium));
                break;
        }
    }

    public void setTextColorUserOnlineUserActivity(RegisteredUser registeredUser, OnlineStatusViewHolder holder, Activity activity) {
        switch (registeredUser.getTypeUser()) {
            case "admin":
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorAdmin));
                break;
            case "moderator":
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorModerator));
                break;
            case "cikgu":
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorCikgu));
                break;
            case "ahliPremium":
                holder.getTextViewUsername().setTextColor(activity.getResources().getColor(R.color.colorAhliPremium));
                break;
        }
    }

}
