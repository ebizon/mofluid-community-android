package com.mofluid.magento2.custome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.GridView;

/**
 * Created by ebizon on 20/11/15.
 */
public class CustomePullGridView extends GridView {

    private static final int MAX_Y_OVERSCROLL_DISTANCE = 100;

    private final Context mContext;
    private int mMaxYOverscrollDistance;

    public CustomePullGridView(Context context) {
        super(context);
        mContext = context;
        initBounceListView();
    }

    public CustomePullGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initBounceListView();
    }

    public CustomePullGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initBounceListView();
    }

    /*public CustomePullGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }*/

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance, isTouchEvent);
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
