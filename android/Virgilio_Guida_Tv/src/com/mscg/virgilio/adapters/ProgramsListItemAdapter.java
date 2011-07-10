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

    public ProgramsListItemAdapter(Context context, int textViewResourceId, TVProgram[] objects) {
        this(context, textViewResourceId, Arrays.asList(objects));
    }

    public ProgramsListItemAdapter(Context context, int textViewResourceId, List<TVProgram> objects) {
        super(context, textViewResourceId, objects);

        hourFormatter = new SimpleDateFormat(context.getString(R.string.hours_format));
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
            holder.playingBg = getContext().getResources().getDrawable(R.drawable.list_element_playing_bg);
            holder.normalBg = getContext().getResources().getDrawable(R.drawable.list_element_bg);
            itemView.setTag(holder);
        } else {
            itemView = (ProgramLinearLayout) convertView;
            holder = (InfoHolder) itemView.getTag();
        }

        TVProgram program = getItem(position);
        itemView.setTvProgram(program);

        holder.startHour.setText(hourFormatter.format(program.getStartTime()));
        holder.endHour.setText(hourFormatter.format(program.getEndTime()));
        holder.programName.setText(program.getName());
        holder.programCategory.setText(program.getCategory());

        if(program.isPlaying()) {
            if(itemView.getBackground() != holder.playingBg)
                itemView.setBackgroundDrawable(holder.playingBg);
        }
        else {
            if(itemView.getBackground() != holder.normalBg)
                itemView.setBackgroundDrawable(holder.normalBg);
        }

        return itemView;
    }

    private class InfoHolder {
        TextView startHour;
        TextView endHour;
        TextView programName;
        TextView programCategory;

        Drawable playingBg;
        Drawable normalBg;
    }

}
