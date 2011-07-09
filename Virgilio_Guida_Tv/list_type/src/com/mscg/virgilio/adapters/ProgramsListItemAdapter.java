package com.mscg.virgilio.adapters;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.programs.TVProgram;
import com.mscg.virgilio.util.ProgramLinearLayout;

public class ProgramsListItemAdapter extends GenericListItemAdapter<TVProgram> {

    private SimpleDateFormat hourFormatter;
    private Drawable playingBg;
    private Drawable normalBg;

    public ProgramsListItemAdapter(Context context, int textViewResourceId, TVProgram[] objects) {
        this(context, textViewResourceId, Arrays.asList(objects));
    }

    public ProgramsListItemAdapter(Context context, int textViewResourceId, List<TVProgram> objects) {
        super(context, textViewResourceId, objects);

        hourFormatter = new SimpleDateFormat(context.getString(R.string.hours_format));

        playingBg = getContext().getResources().getDrawable(R.drawable.list_element_playing_bg);
        normalBg = getContext().getResources().getDrawable(R.drawable.list_element_bg);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProgramLinearLayout itemView = null;

        InfoHolder holder = null;

        if (convertView == null) {
            itemView = new ProgramLinearLayout(getContext());
            vi.inflate(textViewResourceId, itemView, true);
            holder = new InfoHolder();
            holder.startHour = (TextView) itemView.findViewById(R.id.startHour);
            holder.endHour = (TextView) itemView.findViewById(R.id.endHour);
            holder.programName = (TextView) itemView.findViewById(R.id.programName);
            holder.programCategory = (TextView) itemView.findViewById(R.id.programCategory);
            itemView.setTag(holder);
        } else {
            itemView = (ProgramLinearLayout) convertView;
            holder = (InfoHolder) itemView.getTag();
        }

        TVProgram program = getItem(position);
        itemView.setTvProgram(program);

        // Calendar day = new GregorianCalendar();
        // day.setTime(program.getChannel().getPrograms().getDate());
        //
        // Calendar startTime = new
        // GregorianCalendar(TimeZone.getTimeZone("GMT"));
        // startTime.setTimeInMillis(day.getTimeInMillis() +
        // program.getStartTime().getTime());
        //
        // Calendar endTime = new
        // GregorianCalendar(TimeZone.getTimeZone("GMT"));
        // endTime.setTimeInMillis(day.getTimeInMillis() +
        // program.getEndTime().getTime());
        //
        // Calendar now = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        // if(now.compareTo(startTime) >= 0 && now.compareTo(endTime) <= 0)
        // itemView.setBackgroundColor(R.color.actual_program);
        // else
        // itemView.setBackgroundColor(R.color.default_program);

        holder.startHour.setText(hourFormatter.format(program.getStartTime()));
        holder.endHour.setText(hourFormatter.format(program.getEndTime()));
        holder.programName.setText(program.getName());
        holder.programCategory.setText(program.getCategory());

        if(program.isPlaying()) {
            if(itemView.getBackground() != playingBg)
                itemView.setBackgroundDrawable(playingBg);
        }
        else {
            if(itemView.getBackground() != normalBg)
                itemView.setBackgroundDrawable(normalBg);
        }

        return itemView;
    }

    private class InfoHolder {
        TextView startHour;
        TextView endHour;
        TextView programName;
        TextView programCategory;
    }

}
