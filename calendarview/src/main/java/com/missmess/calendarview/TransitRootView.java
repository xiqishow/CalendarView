package com.missmess.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * A TransitRootView should contains two direct children, the first child contains an YearView, and
 * the second contains a MonthView.
 * This view can help display Y and M transition animation and the animations of your other elements.
 *
 * @author wl
 * @since 2016/08/25 11:33
 */
public class TransitRootView extends ScrollView {
    private FrameLayout frameLayout;
    View child1;
    View child2;
    boolean mReceiveEvent = true;
    private MonthView transitView;

    public TransitRootView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        setFillViewport(true);

        frameLayout = new FrameLayout(getContext());
        transitView = new MonthView(getContext());
        transitView.showMonthTitle(false);
        transitView.showWeekLabel(false);
        transitView.setVisibility(View.GONE);
        transitView.setEnabled(false);
        frameLayout.addView(transitView);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if(getChildCount() == 0) {
            super.addView(frameLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        addChild(child);
    }

    private void addChild(View child) {
        if(getChildCount() > 2) {
            throw new IllegalStateException("TransitRootView can host only two direct children");
        }
        if(child1 == null) {
            child1 = child;
        } else {
            child2 = child;
            changeChildrenVisibility();
        }

        frameLayout.addView(child, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    // just one child visible at once, another set to gone.
    private void changeChildrenVisibility() {
        if(child1.getVisibility() == View.VISIBLE) {
            child2.setVisibility(View.GONE);
            return;
        }
        if(child2.getVisibility() == View.VISIBLE) {
            child1.setVisibility(View.GONE);
            return;
        }
        // no child is visible
        child1.setVisibility(View.VISIBLE);
        child2.setVisibility(View.GONE);
    }

    public void setReceiveEvent(boolean receive) {
        mReceiveEvent = receive;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return !mReceiveEvent || super.dispatchTouchEvent(ev);
    }

    public MonthView useTransitView() {
        transitView.setVisibility(View.VISIBLE);
        transitView.setEnabled(true);
        return transitView;
    }

    public void recycleTransitView() {
        transitView.setVisibility(View.GONE);
        transitView.setEnabled(false);
    }
}
