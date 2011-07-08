package com.mscg.virgilio;

import java.util.LinkedList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mscg.virgilio.adapters.ListTypeItemAdapter;
import com.mscg.virgilio.programs.ListType;

public class ListTypeSelection extends GenericActivity implements OnItemClickListener {

    private ListView typeSelectionList;
    private ArrayAdapter<ListType> typeSelectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.list_type_selection);

        List<ListType> listTypes = new LinkedList<ListType>();
        listTypes.add(new ListType(getString(R.string.list_type_channel_selection),
                                   VirgilioGuidaTvChannelSelection.class));

        typeSelectionList = (ListView)findViewById(R.id.listTypeSelectionList);
        typeSelectionAdapter = new ListTypeItemAdapter(this, R.layout.list_type_layout, listTypes);
        typeSelectionList.setAdapter(typeSelectionAdapter);
        typeSelectionList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ListType item = typeSelectionAdapter.getItem(position);
        Intent intent = new Intent(this, item.getListItemActivity());
        Bundle extras = getIntent().getExtras();
        if(extras != null)
            intent.putExtras(extras);
        startActivity(intent);
    }

}
