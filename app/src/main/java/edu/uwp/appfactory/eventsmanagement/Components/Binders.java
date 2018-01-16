package edu.uwp.appfactory.eventsmanagement.Components;

import android.databinding.BindingAdapter;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.HashMap;

/**
 * Created by hanh on 3/29/17.
 */

public class Binders {

    @android.databinding.BindingAdapter("content")
    public static void setContent(DetailItemView detailItemView, Object object){
        detailItemView.setContent(object);
    }

    @BindingAdapter("imageName")
    public static void setImage(ImageView imageView, String name){
        if(name!=null && name.length()>0){
            Glide.with(imageView.getContext()).load(Uri.parse("file:///android_asset/images/"+name)).centerCrop().into(imageView);
        }
        else {
            Glide.with(imageView.getContext()).load(Uri.parse("file:///android_asset/baker1.jpg")).centerCrop().into(imageView);
        }

    }



}
