package com.rootscope.yamba6;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
		case R.id.action_settings:
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		case R.id.action_tweet:
			startActivity(new Intent(this, StatusActivity.class));
			return true;
		case R.id.action_refresh:
			startService(new Intent(this, RefreshService.class));
			return true;
		case R.id.action_purge:
			int rows = getContentResolver().delete(StatusContract.CONTENT_URI, null, null);
			Toast.makeText(this, "deleted "+rows+" rows", Toast.LENGTH_LONG).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
