package net.ticherhaz.karangancemerlangspm.util;

import android.app.Activity;
import android.graphics.Typeface;

import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.viewHolder.OnlineStatusViewHolder;
import net.ticherhaz.karangancemerlangspm.viewHolder.UmumDetailHolder;

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
