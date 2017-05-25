package com.crush.calender;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Crush on 5/25/17.
 */

public class CalenderLayoutManager extends GridLayoutManager {

    public CalenderLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public CalenderLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, final int widthSpec, final int heightSpec) {
//        try {
//            //不能使用   View view = recycler.getViewForPosition(0);
//            //measureChild(view, widthSpec, heightSpec);
//            // int measuredHeight  view.getMeasuredHeight();  这个高度不准确
//            if (adapter != null && adapter.getItemHeight() > 0) {
//                int measuredWidth = View.MeasureSpec.getSize(widthSpec);
//                int measuredHeight = adapter.getItemHeight() + rvPhotos.getPaddingBottom() + rvPhotos.getPaddingTop();
//                int line = getItemCount() / getSpanCount();
//                if (getItemCount() % getSpanCount() > 0) line++;
//                setMeasuredDimension(measuredWidth, measuredHeight * line);
//            } else {
//                super.onMeasure(recycler, state, widthSpec, heightSpec);
//            }
//
//        } catch (Exception e) {
//            super.onMeasure(recycler, state, widthSpec, heightSpec);
//        }
    }
}
