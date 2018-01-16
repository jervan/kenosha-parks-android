package edu.uwp.appfactory.eventsmanagement.ReminderScreen;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.model.Realm.ReminderInfo;
import edu.uwp.appfactory.eventsmanagement.util.DateUtils;
import edu.uwp.appfactory.eventsmanagement.util.ReminderUtil;

/**
 * Created by ChenMingxi on 3/31/17.
 */

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder>{

    private List<ReminderInfo> reminderList;
    private Activity mContext;

    public ReminderAdapter(List<ReminderInfo> reminderList, Activity context) {
        this.reminderList = reminderList;
        this.mContext = context;
    }

    @Override
    public int getItemCount(){
        return reminderList.size();
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reminder,parent,false);
        return new ReminderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        final ReminderInfo ri = reminderList.get(position);
        final long[] times = ReminderUtil.getEventTimesOnCalendar(ri.getCalendarEventId(), mContext);
        holder.vName.setText(ri.eventName);
        holder.vDate.setText(DateUtils.formatDateForReminderCard(new Date(times[0])));
        holder.vTime.setText(DateUtils.formatDateForReminderRange(new Date(times[0]), new Date(times[1])));
        String reminderTime = DateUtils.formatReminderTime(ReminderUtil.getFirstReminderForEvent(ri.getCalendarEventId(), mContext), new Date(times[0]));
        holder.vReminderTime.setText(reminderTime);
        holder.vCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setData(Uri.parse("content://com.android.calendar/events/" + String.valueOf(ri.getCalendarEventId())));
                mContext.startActivity(intent);
            }
        });
    }

    public static class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView vName;
        public TextView vDate;
        public TextView vTime;
        public TextView vReminderTime;
        public CardView vCardView;

        public ReminderViewHolder(View v){
            super(v);
            vName = (TextView) v.findViewById(R.id.eventName);
            vDate = (TextView) v.findViewById(R.id.date);
            vTime = (TextView) v.findViewById(R.id.time);
            vCardView = (CardView) v.findViewById(R.id.card_view);
            vReminderTime = (TextView) v.findViewById(R.id.reminderTime);
        }
    }
}
