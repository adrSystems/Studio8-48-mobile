package com.systems.adr_.studio8_48_mobile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

/**
 * Created by luis_ on 01/05/2017.
 */

public class DatePickerScrolling extends DatePicker {
    public DatePickerScrolling(Context context) {
        super(context);
    }
    public DatePickerScrolling(Context context, AttributeSet attrs) {
        super(context,attrs);
    }
    public DatePickerScrolling(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            ViewParent p = getParent();
            if (p != null)
                p.requestDisallowInterceptTouchEvent(true);
        }

        return false;
    }
}
