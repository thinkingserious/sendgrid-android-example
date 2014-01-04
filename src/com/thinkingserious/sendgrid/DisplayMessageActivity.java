package com.thinkingserious.sendgrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.thinkingserious.sendgrid.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

@SuppressLint("NewApi")
public class DisplayMessageActivity extends Activity {

	
	// Get statistics from SendGrid using the Web API, returns a JSON object as described in the URL below
	// http://sendgrid.com/docs/API_Reference/Web_API/Statistics/index.html
	class StatsFromSendGrid extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
		    HttpClient httpclient = new DefaultHttpClient();
		    Utils creds = new Utils();
		    // Reference: http://sendgrid.com/docs/API_Reference/Web_API/Statistics/index.html
		    String url = "https://api.sendgrid.com/api/stats.get.json?api_user=" + creds.getUsername() + "&api_key=" + creds.getPassword();
		    // Prepare a request object
		    HttpGet httpget = new HttpGet(url); 

		    // Execute the request
		    HttpResponse response = null;
		    String result = null;
		    try {
		        response = httpclient.execute(httpget);
		        HttpEntity entity = response.getEntity();
		        InputStream instream = entity.getContent();
	            result = convertStreamToString(instream);
	            // now you have the string representation of the HTML request
	            instream.close();
		    } catch (Exception e) {}
		    return result;
		}
		
		// Code taken from here: http://stackoverflow.com/questions/4457492/simple-http-client-example-in-android
		private String convertStreamToString(InputStream is) {
		    /*
		     * To convert the InputStream to String we use the BufferedReader.readLine()
		     * method. We iterate until the BufferedReader return null which means
		     * there's no more data to read. Each line will appended to a StringBuilder
		     * and returned as String.
		     */
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		    StringBuilder sb = new StringBuilder();

		    String line = null;
		    try {
		        while ((line = reader.readLine()) != null) {
		            sb.append(line + "\n");
		        }
		    } catch (IOException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            is.close();
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		    return sb.toString();
		}
	}
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_message);
		
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
		if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
		// Get the message from the intent
		Intent intent = getIntent();
		String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
		
		// Update the display with the response code from the mail end point
		TextView testView = (TextView) findViewById(R.id.response);
		message = "Email Send Status: " + message + "\n";
		testView.setText(message);
		
		// Get the stats from the SendGrid Web API
		StatsFromSendGrid stats = new StatsFromSendGrid();
		String result = null;
		try {
			result = stats.execute().get();
		} catch (InterruptedException e) {
			// TODO Implement exception handling
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Implement exception handling
			e.printStackTrace();
		}
		
		// Make the returned JSON readable
		JSONParser parser=new JSONParser();
		try{
	         Object obj = parser.parse(result);
	         JSONArray array = (JSONArray)obj;
	         JSONObject obj2 = (JSONObject)array.get(0);
	         result = "Requests: " + obj2.get("requests").toString();
	         result = result + "\nDelivered: " + obj2.get("delivered").toString();
	         result = result + "\nOpens: " + obj2.get("opens").toString();
	      }catch(ParseException pe){
	         result = pe.toString();
	      }
		
		// Update the display with data from the stats end point
		testView = (TextView) findViewById(R.id.statistics);
		testView.setText(result);
		
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
