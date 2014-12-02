package com.rootscope.yamba3;

import android.os.Bundle;

public class StatusActivity extends SubActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(savedInstanceState == null) {
			StatusFragment fragment = new StatusFragment();
			getFragmentManager()
				.beginTransaction()
				.add(android.R.id.content, fragment, fragment.getClass().getSimpleName()).commit();
			System.out.println(fragment);
		}
		
	}
}
