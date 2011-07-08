package com.mscg.virgilio.adapters;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.util.DayLinearLayout;
import com.mscg.virgilio.util.Util;

public class DayListItemAdapter extends GenericListItemAdapter<Integer> {

    private SimpleDateFormat dateFormat;
    private SimpleDateFormat dayFormat;

    public DayListItemAdapter(Context context, int textViewResourceId, Integer[] objects) {
        this(context, textViewResourceId, Arrays.asList(objects));
    }

    public DayListItemAdapter(Context context, int textViewResourceId, List<Integer> objects) {
        super(context, textViewResourceId, objects);
        dateFormat = new SimpleDateFormat(getContext().getString(R.string.select_day_format));
        dayFormat = new SimpleDateFormat("EEEEE");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DayLinearLayout dayView = null;
        Integer dayOffset = getItem(position);

        InfoHolder holder = null;

        if (convertView == null) {
            dayView = new DayLinearLayout(getContext());
            vi.inflate(textViewResourceId, dayView, true);
            holder = new InfoHolder();
            holder.dayName = (TextView) dayView.findViewById(R.id.dayName);
            holder.dayNumber = (TextView) dayView.findViewById(R.id.dayNumber);
            dayView.setTag(holder);
        } else {
            dayView = (DayLinearLayout) convertView;
            holder = (InfoHolder) dayView.getTag();
        }

        Calendar now = new GregorianCalendar();
        now.add(Calendar.DAY_OF_MONTH, dayOffset);

        Calendar dayCal = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        dayCal.setTimeInMillis(now.getTimeInMillis());
        dayView.setDay(dayCal);

        // if(position == 0)
        // dayView.setBackgroundColor(getContext().getResources().getColor(R.color.actual_day));
        // else
        // dayView.setBackgroundColor(getContext().getResources().getColor(R.color.default_day));

        holder.dayName.setText(Util.capitalize(dayFormat.format(now.getTime())));
        holder.dayNumber.setText(dateFormat.format(now.getTime()));

        return dayView;
    }

    private class InfoHolder {
        TextView dayName;
        TextView dayNumber;
    }

}
