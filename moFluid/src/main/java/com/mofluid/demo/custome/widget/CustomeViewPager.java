package com.mofluid.magento2.custome.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import com.mofluid.magento2.R;

/**
 * Created by ebizon on 24/11/15.
 */
public class CustomeViewPager extends ViewPager {
    private static  int MAX_Y_OVERSCROLL_DISTANCE = 200;

    private final Context mContext;
    private int mMaxYOverscrollDistance;

    public CustomeViewPager(Context context) {

        super(context);
        mContext = context;
        MAX_Y_OVERSCROLL_DISTANCE=(context.getResources().getInteger(R.integer.over_scroll_distance_));
        initBounceListView();
    }

    public CustomeViewPager(Context context, AttributeSet attrs) {
        super(context);
        mContext = context;
        MAX_Y_OVERSCROLL_DISTANCE=(context.getResources().getInteger(R.integer.over_scroll_distance_));
        initBounceListView();
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, mMaxYOverscrollDistance, maxOverScrollY, isTouchEvent);
    }


    private void initBounceListView()
    {
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size

        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }
}
