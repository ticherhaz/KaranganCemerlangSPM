package net.ticherhaz.karangancemerlangspm.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.ticherhaz.karangancemerlangspm.R;

public class HubungiKamiViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private TextView tvDate;
    private TextView tvMessage;

    public HubungiKamiViewHolder(@NonNull View itemView) {
        super(itemView);
        view = itemView;
        tvDate = view.findViewById(R.id.tv_date);
        tvMessage = view.findViewById(R.id.tv_message);
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public TextView getTvDate() {
        return tvDate;
    }

    public void setTvDate(TextView tvDate) {
        this.tvDate = tvDate;
    }

    public TextView getTvMessage() {
        return tvMessage;
    }

    public void setTvMessage(TextView tvMessage) {
        this.tvMessage = tvMessage;
    }
}
