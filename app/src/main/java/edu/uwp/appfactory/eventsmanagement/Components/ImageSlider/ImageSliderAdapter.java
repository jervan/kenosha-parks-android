package edu.uwp.appfactory.eventsmanagement.Components.ImageSlider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import edu.uwp.appfactory.eventsmanagement.model.Realm.RealmString;
import io.realm.RealmList;

/**
 * Created by hanh on 3/27/17.
 */

public class ImageSliderAdapter extends FragmentStatePagerAdapter{

    private RealmList<RealmString> images;

    public void setImages(RealmList<RealmString> images) {
        this.images = images;
    }

    public ImageSliderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ImageSliderFragment fragment = new ImageSliderFragment();
        fragment.setImage(images.get(position).toString());
        return fragment;
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
