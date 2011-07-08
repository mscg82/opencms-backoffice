package com.mscg.virgilio.util;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.mscg.virgilio.programs.TVProgram;

public class ProgramLinearLayout extends LinearLayout {

    private TVProgram tvProgram;

    public ProgramLinearLayout(TVProgram tvProgram, Context context) {
        super(context);
        setTvProgram(tvProgram);
    }

    public ProgramLinearLayout(TVProgram tvProgram, Context context, AttributeSet attrs) {
        super(context, attrs);
        setTvProgram(tvProgram);
    }

    public ProgramLinearLayout(Context context) {
        this(null, context);
    }

    public TVProgram getTvProgram() {
        return tvProgram;
    }

    public void setTvProgram(TVProgram tvProgram) {
        this.tvProgram = tvProgram;
    }

}
