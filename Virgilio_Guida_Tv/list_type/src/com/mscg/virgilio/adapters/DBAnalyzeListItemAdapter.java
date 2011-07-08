package com.mscg.virgilio.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ResourceCursorAdapter;

import com.mscg.virgilio.R;
import com.mscg.virgilio.VirgilioGuidaTvDbAnalyze;
import com.mscg.virgilio.database.ProgramsDB;
import com.mscg.virgilio.listener.AnalyzeDBClickListener;

public class DBAnalyzeListItemAdapter extends ResourceCursorAdapter {

    private SimpleDateFormat dateFormat;

    public DBAnalyzeListItemAdapter(Context context, int layout, Cursor c, boolean autoRequery) {
        super(context, layout, c, autoRequery);
        dateFormat = new SimpleDateFormat(context.getString(R.string.complete_day_format));
    }

    public DBAnalyzeListItemAdapter(Context context, int layout, Cursor c) {
        this(context, layout, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = super.newView(context, cursor, parent);
        Holder holder = new Holder();
        holder.listener = new AnalyzeDBClickListener(context, ((VirgilioGuidaTvDbAnalyze) context).getGuiHandler());
        holder.dayName = (Button) view.findViewById(R.id.analyze_db_elem_day);
        holder.dayName.setOnClickListener(holder.listener);
        holder.selected = (CheckBox) view.findViewById(R.id.analyze_db_elem_select);
        holder.selected.setOnCheckedChangeListener(holder.listener);
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Holder holder = (Holder) view.getTag();
        holder.id = cursor.getLong(ProgramsDB.PROGRAMS_CONSTS.ID_COL_INDEX);
        long time = cursor.getLong(ProgramsDB.PROGRAMS_CONSTS.DATE_COL_INDEX);
        holder.date = new Date(time);
        holder.dayName.setText(dateFormat.format(holder.date));
        holder.dayName.setTag(holder);
        boolean checked = ((VirgilioGuidaTvDbAnalyze) context).isElementChecked(holder.id);
        holder.selected.setChecked(checked);
        holder.listener.setProgramID(holder.id);
    }

    public class Holder {
        public Button dayName;
        public CheckBox selected;
        public long id;
        public Date date;
        public AnalyzeDBClickListener listener;
    }

}
