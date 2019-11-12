package com.mofluid.magento2.custome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

import com.mofluid.magento2.R;

/**
 * Created by ebizon on 20/11/15.
 */
public class CustomePullListview extends ListView
{
    private static  int MAX_Y_OVERSCROLL_DISTANCE = 200;

    private final Context mContext;
    private int mMaxYOverscrollDistance;

    public CustomePullListview(Context context)
    {
        super(context);
        mContext = context;
        MAX_Y_OVERSCROLL_DISTANCE=(context.getResources().getInteger(R.integer.over_scroll_distance_));
        initBounceListView();
    }

    public CustomePullListview(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        MAX_Y_OVERSCROLL_DISTANCE=(context.getResources().getInteger(R.integer.over_scroll_distance_));
        initBounceListView();
    }

    public CustomePullListview(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        mContext = context;
        MAX_Y_OVERSCROLL_DISTANCE=(context.getResources().getInteger(R.integer.over_scroll_distance_));
        initBounceListView();
    }

    private void initBounceListView()
    {
        //get the density of the screen and do some maths with it on the max overscroll distance
        //variable so that you get similar behaviors no matter what the screen size

        final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        final float density = metrics.density;

        mMaxYOverscrollDistance = (int) (density * MAX_Y_OVERSCROLL_DISTANCE);
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent)
    {
        //This is where the magic happens, we have replaced the incoming maxOverScrollY with our own custom variable mMaxYOverscrollDistance;
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
    }

}