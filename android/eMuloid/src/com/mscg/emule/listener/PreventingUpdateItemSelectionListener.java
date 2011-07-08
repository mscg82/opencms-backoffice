package com.mscg.emule.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.mscg.emule.GenericSpeedInfoActivity;
import com.mscg.emule.util.ContextAware;

public abstract class PreventingUpdateItemSelectionListener extends ContextAware<GenericSpeedInfoActivity>
                                                            implements OnItemSelectedListener {

	protected int updatesToIgnore;
	protected boolean preventTrigger;

	public PreventingUpdateItemSelectionListener(GenericSpeedInfoActivity context) {
		super(context);
		updatesToIgnore = 0;
		preventTrigger = false;
	}

	public int getUpdatesToIgnore() {
		return updatesToIgnore;
	}

	public void setUpdatesToIgnore(int updatesToIgnore) {
		this.updatesToIgnore = updatesToIgnore;
	}

	public boolean isPreventTrigger() {
		return preventTrigger;
	}

	public void setPreventTrigger(boolean preventTrigger) {
		this.preventTrigger = preventTrigger;
	}

	protected abstract void innerOnItemSelected(AdapterView<?> parent, View view, int position, long id);

	protected abstract void innerOnNothingSelected(AdapterView<?> parent);

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		if(!preventTrigger) {
			if(updatesToIgnore == 0)
				innerOnItemSelected(parent, view, position, id);
			else
				updatesToIgnore --;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		if(!preventTrigger) {
			if(updatesToIgnore == 0)
				innerOnNothingSelected(parent);
			else
				updatesToIgnore--;
		}
	}

}
