package com.mscg.virgilio.util;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class DayLinearLayout extends LinearLayout {

    private Calendar day;

    public DayLinearLayout(Calendar day, Context context) {
        super(context);
        setDay(day);
    }

    public DayLinearLayout(Calendar day, Context context, AttributeSet attrs) {
        super(context, attrs);
        setDay(day);
    }

    public DayLinearLayout(Context context) {
        this(null, context);
    }

    public DayLinearLayout(Context context, AttributeSet attrs) {
        this(null, context, attrs);
    }

    public Calendar getDay() {
        return day;
    }

    public void setDay(Calendar day) {
        this.day = day;
    }

}
