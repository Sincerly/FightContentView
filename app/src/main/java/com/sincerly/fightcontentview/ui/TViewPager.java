package com.sincerly.fightcontentview.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2018/4/16 0016.
 */

public class TViewPager extends ViewPager {
	private boolean isCanScroll=false;

	public TViewPager(@NonNull Context context) {
		super(context);
	}

	public TViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return isCanScroll && super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return isCanScroll && super.onTouchEvent(ev);
	}
}
