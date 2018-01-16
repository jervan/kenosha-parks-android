package edu.uwp.appfactory.eventsmanagement.Components.ImageSlider;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import edu.uwp.appfactory.eventsmanagement.R;

/**
 * Created by hanh on 3/27/17.
 */

public class ImageSliderFragment extends Fragment {

    private View rootView;
    private String image;


    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_image_slider,container,false);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.image) ;

        Glide.with(this).load(Uri.parse("file:///android_asset/images/"+image)).centerCrop().into(imageView);




        return rootView;
    }
}
