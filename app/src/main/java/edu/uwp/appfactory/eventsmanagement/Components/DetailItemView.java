package edu.uwp.appfactory.eventsmanagement.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import edu.uwp.appfactory.eventsmanagement.R;

/**
 * Created by hanh on 3/27/17.
 */

public class DetailItemView extends RelativeLayout {


    private final static int TYPE_TEXT = 0;
    private final static int TYPE_VIDEO = 1;
    private final static int TYPE_SCHEDULE = 2;
    private Context context;
    View rootView;
    TextView titleView;
    ImageView iconView;
    LinearLayout contentContainerView;
    View contentView;


    private String title;
    private int type = TYPE_TEXT;
    private Drawable icon;


    public DetailItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public DetailItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public void setContent(Object content){
        switch (type){
            case TYPE_TEXT:
                setTextContent((String) content);
                break;
            case TYPE_SCHEDULE:
                if (content instanceof String) {
                    setSchedule((String) content);
                } else if (content instanceof HashMap) {
                    setSchedule((HashMap<String,String>) content);
                }
                break;
            case TYPE_VIDEO:
                break;
        }
    }
    private void init(Context context,AttributeSet attrs){
        rootView = inflate(context, R.layout.view_item_detail, this);
        titleView = (TextView)rootView.findViewById(R.id.title);
        iconView = (ImageView)rootView.findViewById(R.id.icon);
        contentContainerView = (LinearLayout) rootView.findViewById(R.id.content);
        this.context = context;

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DetailItemView, 0, 0);


        title = a.getString(R.styleable.DetailItemView_title);
        icon = a.getDrawable(R.styleable.DetailItemView_header_icon);
        type = a.getInt(R.styleable.DetailItemView_type,TYPE_TEXT);

        a.recycle();
        if (icon!=null){
            iconView.setBackground(icon);
        }

        titleView.setText(title);

        switch (type){

            case TYPE_TEXT:
                 contentView =  inflate(context,R.layout.detail_content_text,null);
                break;

            case TYPE_VIDEO:
                contentView = inflate(context,R.layout.detail_content_video,null);
                break;
        }

        if(contentView!=null){
            contentContainerView.addView(contentView);
        }

    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setTextContent(String textContent){
        TextView textView = (TextView)contentView.findViewById(R.id.content_text);
        textView.setText(textContent);
    }

    public void setSchedule(HashMap<String,String> schedules){

        for (Map.Entry<String, String> entry : schedules.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            View scheduleItem =  inflate(context,R.layout.detail_content_schedule_item,null);

            TextView time = (TextView)scheduleItem.findViewById(R.id.time);
            TextView description = (TextView)scheduleItem.findViewById(R.id.description);
            time.setText(key);
            description.setText(value);

            contentContainerView.addView(scheduleItem);

        }


    }

    public void setSchedule(String contents) {
        View scheduleItem =  inflate(context,R.layout.detail_content_schedule_item,null);

        TextView time = (TextView)scheduleItem.findViewById(R.id.time);
        TextView description = (TextView)scheduleItem.findViewById(R.id.description);
        time.setVisibility(View.GONE);
        description.setText(contents);

        contentContainerView.addView(scheduleItem);
    }
}
