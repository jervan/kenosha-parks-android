package edu.uwp.appfactory.eventsmanagement.ReminderScreen;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uwp.appfactory.eventsmanagement.R;
import edu.uwp.appfactory.eventsmanagement.model.Realm.ReminderInfo;
import edu.uwp.appfactory.eventsmanagement.util.ReminderUtil;
import io.realm.Realm;


/**
 * A placeholder fragment containing a simple view.
 */
public class ReminderFragment extends Fragment {

    View rootView;
    private Realm mRealm;
    private RecyclerView mRecyclerView;

    public ReminderFragment() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.eventList);
        // set the adapter here
        mRecyclerView.setAdapter(new ReminderAdapter(ReminderUtil.verifyReminderSet(mRealm.where(ReminderInfo.class).findAll(), getActivity()), getActivity()));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mRecyclerView != null) {
            ReminderUtil.verifyReminderSet(mRealm.where(ReminderInfo.class).findAll(), getActivity());
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mRealm != null) {
            mRealm.close();
        }
    }
}
