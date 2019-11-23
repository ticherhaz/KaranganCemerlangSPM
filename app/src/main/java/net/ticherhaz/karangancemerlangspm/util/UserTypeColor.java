package net.ticherhaz.karangancemerlangspm.util;

import android.content.Context;
import android.graphics.Typeface;

import net.ticherhaz.karangancemerlangspm.R;
import net.ticherhaz.karangancemerlangspm.model.RegisteredUser;
import net.ticherhaz.karangancemerlangspm.viewHolder.OnlineStatusViewHolder;
import net.ticherhaz.karangancemerlangspm.viewHolder.UmumDetailHolder;

public class UserTypeColor {

    public UserTypeColor() {
    }

    public static void setTextColorUserUmumDetail(RegisteredUser registeredUser, UmumDetailHolder holder, Context context) {
        switch (registeredUser.getTypeUser()) {
            case "ahli":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorAhli));
                break;
            case "admin":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorAdmin));
                break;
            case "moderator":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorModerator));
                break;
            case "cikgu":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorCikgu));
                break;
            case "ahliPremium":
                holder.getTextViewUserTitle().setTypeface(null, Typeface.BOLD);
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorAhliPremium));
                break;
        }
    }

    public static void setTextColorUserOnlineUserActivity(RegisteredUser registeredUser, OnlineStatusViewHolder holder, Context context) {
        switch (registeredUser.getTypeUser()) {
            case "ahli":
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorAhli));
                break;
            case "admin":
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorAdmin));
                break;
            case "moderator":
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorModerator));
                break;
            case "cikgu":
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorCikgu));
                break;
            case "ahliPremium":
                holder.getTextViewUsername().setTextColor(context.getResources().getColor(R.color.colorAhliPremium));
                break;
        }
    }

}
