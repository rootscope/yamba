package com.rootscope.yamba7;

import android.os.Bundle;

public class SettingsActivity extends SubActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			SettingsFragment fragment = new SettingsFragment();
			getFragmentManager()
				.beginTransaction()
				.add(android.R.id.content, fragment, fragment.getClass().getSimpleName()).commit();
		}
	}
}
