package com.mofluid.magento2;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by ebizon201 on 20/9/16.
 */
public class LoadImageWithPicasso {
    private static LoadImageWithPicasso ourInstance = new LoadImageWithPicasso();

    public static LoadImageWithPicasso getInstance() {
        return ourInstance;
    }

    private LoadImageWithPicasso() {
    }
    public void loadImage(ImageView img,String url)
    {
        int height = MainActivity.INSTANCE.getResources().getDimensionPixelSize(R.dimen.comman_img_height);
        if(url!=null && !url.equalsIgnoreCase("null") && img!=null ) {
            Picasso.with(MainActivity.INSTANCE)
                    .load(url)
                    .placeholder(R.drawable.default_mofluid)
                    .error(R.drawable.default_mofluid)
                  /*  .resize(200, 200)*/
                    .into(img);
        }
        else
            if(img!=null)
                img.setImageResource(R.drawable.default_mofluid);

    }
}
