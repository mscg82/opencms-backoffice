package com.mscg.emule.listener;

import android.view.View;
import android.widget.AdapterView;

import com.mscg.emule.DownloadList;
import com.mscg.emule.bean.CategoryBean;

public class CategorySelectionListener extends PreventingUpdateItemSelectionListener {

	public CategorySelectionListener(DownloadList context) {
		super(context);
	}

	@Override
	protected void innerOnItemSelected(AdapterView<?> parent, View view, int position, long id) {
		CategoryBean category = (CategoryBean)parent.getItemAtPosition(position);
		context.getParameters().put("cat", category.getId().toString());
		context.update();
	}

	@Override
	protected void innerOnNothingSelected(AdapterView<?> parent) {
		context.getParameters().remove("cat");
		context.update();
	}

}
