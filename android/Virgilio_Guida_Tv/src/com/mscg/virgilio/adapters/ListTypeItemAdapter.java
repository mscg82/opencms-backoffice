package com.mscg.virgilio.adapters;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mscg.virgilio.R;
import com.mscg.virgilio.programs.ListType;

public class ListTypeItemAdapter extends GenericListItemAdapter<ListType> {

    public ListTypeItemAdapter(Context context, int textViewResourceId, List<ListType> objects) {
        super(context, textViewResourceId, objects);
    }

    public ListTypeItemAdapter(Context context, int textViewResourceId, ListType[] objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = null;
        ListType item = getItem(position);

        InfoHolder holder = null;
        if(convertView == null) {
            layout = new LinearLayout(getContext());
            vi.inflate(textViewResourceId, layout, true);
            holder = new InfoHolder();
            holder.listTypeName = (TextView)layout.findViewById(R.id.listTypeName);
            layout.setTag(holder);
        }
        else {
            layout = (LinearLayout) convertView;
            holder = (InfoHolder) layout.getTag();
        }

        holder.listTypeName.setText(item.getListItemName());

        return layout;
    }

    private class InfoHolder {
        TextView listTypeName;
    }

}
