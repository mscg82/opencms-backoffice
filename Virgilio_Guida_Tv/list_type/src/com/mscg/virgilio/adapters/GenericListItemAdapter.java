package com.mscg.virgilio.adapters;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public class GenericListItemAdapter<T> extends ArrayAdapter<T> {

    protected int textViewResourceId;
    protected LayoutInflater vi;

    public GenericListItemAdapter(Context context, int textViewResourceId, T[] objects) {
        this(context, textViewResourceId, Arrays.asList(objects));
    }

    public GenericListItemAdapter(Context context, int textViewResourceId, List<T> objects) {
        super(context, textViewResourceId, objects);

        this.textViewResourceId = textViewResourceId;
        String inflater = Context.LAYOUT_INFLATER_SERVICE;
        vi = (LayoutInflater) getContext().getSystemService(inflater);
    }

}
