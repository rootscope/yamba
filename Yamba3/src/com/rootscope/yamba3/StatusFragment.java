package com.rootscope.yamba3;

import com.marakana.android.yamba.clientlib.YambaClient;
import com.rootscope.yamba3.R;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusFragment extends Fragment {
	private static final String TAG = StatusFragment.class.getSimpleName();

	private Button buttonTweet;
	private Button buttonClear;
	private Button buttonSmiley;
	private EditText textStatus;
	private TextView textCount;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.activity_status, null, false);
		System.out.println("did we inflate?");
		
		buttonTweet = (Button) v.findViewById(R.id.status_button_tweet);
		buttonClear = (Button) v.findViewById(R.id.status_button_clear);
		buttonSmiley = (Button) v.findViewById(R.id.status_button_smiley);
		textStatus = (EditText) v.findViewById(R.id.status_text);
		textCount = (TextView) v.findViewById(R.id.status_text_count);
		textCount.setText(Integer.toString(140));
		
		buttonTweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String status = textStatus.getText().toString();
				PostTask postTask = new PostTask();
				postTask.execute(status);
				Log.d(TAG, "onClicked");
			}
		});

		buttonClear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
		        textStatus.setText("");
		    }	
		});
		
		buttonSmiley.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String status = textStatus.getText().toString();
				if (status.length() + 2 > 140) {
					Log.e("WTF", "No room to smile in here.");
				}
				else {
					textStatus.setText(status + ":)");
					textStatus.setSelection(status.length() + 2);
				}
		    }	
		});
		
		textStatus.addTextChangedListener(new TextWatcher() {
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
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		
		//return super.onCreateView(inflater, container, savedInstanceState);
		return v;
	}

	class PostTask extends AsyncTask<String, Void, String> {

		private ProgressDialog progress;
		
		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(getActivity(), "Posting", "Please wait...");
			progress.setCancelable(true);
		}
		
		@Override
		protected String doInBackground(String... params) {
			try {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
				String username = prefs.getString("username", "");
				String password = prefs.getString("password", "");
				
				if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
					getActivity().startActivity(new Intent(getActivity(), SettingsActivity.class));
					return "Please set a username and password";
				}

				YambaClient cloud = new YambaClient(username, password);
				cloud.postStatus(params[0]);
				Log.d(TAG, "Successfully posted to the cloud: " + params[0]);
				return "Successfully posted";
			} catch (Exception e) {
				Log.e(TAG, "Failed to post to the cloud", e);
				e.printStackTrace();
				return "Failed to post";
			}
		}
		
		@Override
		protected void onPostExecute(String result) {
			progress.dismiss();
			if (getActivity() != null && result != null) {
				Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
			}
		}
		
	}

}
