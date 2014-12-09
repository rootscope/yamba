package com.marakana.yamba1;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.marakana.android.yamba.clientlib.YambaClientException;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class StatusActivity extends ActionBarActivity implements OnClickListener {
	
	private static final String MYTAG = "StatusActivity";
	
	private EditText editStatus;
	
	private Button buttonTweet;
	private Button buttonClear;
	private Button buttonSmiley;
	
	private TextView textCount;
	private int defaultTextColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		
		editStatus = (EditText) findViewById(R.id.editStatus);
		textCount = (TextView) findViewById(R.id.textCount);
		
		buttonTweet = (Button) findViewById(R.id.buttonTweet);
		buttonClear = (Button) findViewById(R.id.buttonClear);
		buttonSmiley = (Button) findViewById(R.id.buttonSmiley);
		
		defaultTextColor = textCount.getTextColors().getDefaultColor();
		
		buttonTweet.setOnClickListener(this);
		buttonClear.setOnClickListener(clear);
		buttonSmiley.setOnClickListener(smiley);
		
		editStatus.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				int count = 140 - s.length();
				textCount.setText(Integer.toString(count));
				
				if (count <= 70 && count > 35) {
					textCount.setTextColor(Color.YELLOW);
				}
				else if (count <= 35) {
					textCount.setTextColor(Color.RED);
				}
				else {
					textCount.setTextColor(Color.GREEN);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	public OnClickListener clear = new OnClickListener() {
	    public void onClick(View v) {
	        editStatus.setText("");
	    }
	};
	
	public OnClickListener smiley = new OnClickListener() {
		public void onClick(View v) {
			String status = editStatus.getText().toString();
			if (status.length() + 2 > 140) {
				Log.e(MYTAG, "No room to smile in here.");
			}
			else {
				editStatus.setText(status + ":)");
				editStatus.setSelection(status.length() + 2);
			}
		}
	};
	
	public void onClick(View v) {
		String status = editStatus.getText().toString();
		PostTask postTask = new PostTask();
		postTask.execute(status);
		Log.d(MYTAG, "onClicked with status: " + status);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.status, menu);
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
				Log.d(MYTAG, "Successfully posted to the cloud: " + params[0]);
				return "Successfully posted";
			} catch (YambaClientException e) {
				Log.e(MYTAG, "Failed to post to the cloud", e);
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
