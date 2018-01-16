package edu.uwp.appfactory.eventsmanagement.infoscreen;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.ReminderScreen.ReminderAdapter;

/**
 * Created by hanh on 4/25/17.
 */

public class LinkFragment extends Fragment {

    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_link, container, false);


        View.OnClickListener listener1 = getClickListener("https://www.facebook.com/City-of-Kenosha-Parks-Alliance-1264534383627514/");
        View.OnClickListener listener2 = getClickListener("https://www.mykpl.info/");
        View.OnClickListener listener3 = getClickListener("http://kenoshaymca.org/");
        View.OnClickListener listener4 = getClickListener("http://www.bgckenosha.org/");

        ImageView logo1 = (ImageView)rootView.findViewById(R.id.logo1);
        ImageView logo2 = (ImageView)rootView.findViewById(R.id.logo2);
        ImageView logo3 = (ImageView)rootView.findViewById(R.id.logo3);
        ImageView logo4 = (ImageView)rootView.findViewById(R.id.logo4);

        TextView tv1= (TextView)rootView.findViewById(R.id.link1);
        TextView tv2= (TextView)rootView.findViewById(R.id.link2);
        TextView tv3= (TextView)rootView.findViewById(R.id.link3);
        TextView tv4= (TextView)rootView.findViewById(R.id.link4);

        logo1.setOnClickListener(listener1);
        logo2.setOnClickListener(listener2);
        logo3.setOnClickListener(listener3);
        logo4.setOnClickListener(listener4);

        tv1.setOnClickListener(listener1);
        tv2.setOnClickListener(listener2);
        tv3.setOnClickListener(listener3);
        tv4.setOnClickListener(listener4);



        return rootView;
    }



    private View.OnClickListener getClickListener(final String url){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        };
    }
}
