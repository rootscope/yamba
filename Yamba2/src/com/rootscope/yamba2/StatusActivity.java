package com.rootscope.yamba2;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {
	
	private static final String WTF = "WTF";
	
	private EditText editStatus;
	
	private Button buttonTweet;
	
	private TextView textCount;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(savedInstanceState);
		
		editStatus = (EditText) findViewById(R.id.editStatus);
		textCount = (TextView) findViewById(R.id.textCount);
		
		buttonTweet = (Button) findViewById(R.id.buttonTweet);
		
		buttonTweet.setOnClickListener(this);
		
		Configuration config = getResources().getConfiguration();
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			LM_Fragment ls_fragment = new LM_Fragment();
			fragmentTransaction.replace(android.R.id.content, ls_fragment);
		}
		else {
			PM_Fragment pm_fragment = new PM_Fragment();
			fragmentTransaction.replace(android.R.id.content, pm_fragment);
		}
		fragmentTransaction.commit();
	}
	
	public void onClick(View v) {
		String status = editStatus.getText().toString();
		PostTask postTask = new PostTask();
		postTask.execute(status);
		Log.e(WTF, "onClicked with status: " + status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class PostTask extends AsyncTask<String, Void, String> {

		private ProgressDialog progress;
		
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(StatusActivity.this, "Posting", "Please wait...");
			progress.setCancelable(true);
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(StatusActivity.this);
				String username = prefs.getString("username", "student");
				String password = prefs.getString("password", "password");
				YambaClient cloud = new YambaClient(username, password);
				cloud.postStatus(params[0]);
				Log.d(WTF, "Successfully posted to the cloud: " + params[0]);
				return "Successfully posted";
			} catch (YambaClientException e) {
				Log.e(WTF, "Failed to post to the cloud", e);
				e.printStackTrace();
				return "Failed to post";
			}
		}
		
		protected void onPostExecute(String result) {
			progress.dismiss();
			if (this != null && result != null) {
				Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
			}
		}
		
	}

}
