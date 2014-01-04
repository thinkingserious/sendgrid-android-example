package com.thinkingserious.sendgrid;

import java.util.Hashtable;
import java.util.concurrent.ExecutionException;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.github.sendgrid.SendGrid;
import com.thinkingserious.sendgrid.R;

public class MainActivity extends Activity{

	public final static String EXTRA_MESSAGE = "com.thinkingserious.sendgrid.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	// Send an email with SendGrid's Web API, using our SendGrid Java Library
	// https://github.com/sendgrid/sendgrid-java
	private class SendEmailWithSendGrid extends AsyncTask<Hashtable<String,String>, Void, String> {

		@Override
		protected String doInBackground(Hashtable<String,String>... params) {
			Hashtable<String,String> h = params[0];
			Utils creds = new Utils();
			SendGrid sendgrid = new SendGrid(creds.getUsername(),creds.getPassword());
			sendgrid.addTo(h.get("to"));
			sendgrid.setFrom(h.get("from"));
			sendgrid.setSubject(h.get("subject"));
			sendgrid.setText(h.get("text"));
			String response = sendgrid.send();
			return response;
		}
	}
	
	// Called when the user clicks the Send button
	@SuppressWarnings("unchecked")
	public void sendMessage(View view) {
		Hashtable<String,String> params = new Hashtable<String,String>();
		String result = null;
		
		// Get the values from the form
		EditText editTo = (EditText) findViewById(R.id.edit_to);
		String to = editTo.getText().toString();
		params.put("to", to);	
		
		EditText editFrom = (EditText) findViewById(R.id.edit_from);
		String from = editFrom.getText().toString();
		params.put("from", from);
		
		EditText editSubject = (EditText) findViewById(R.id.edit_subject);
		String subject = editSubject.getText().toString();
		params.put("subject", subject);
		
		EditText editText = (EditText) findViewById(R.id.edit_text);
		String text = editText.getText().toString();
		params.put("text", text);
		
		// Send the Email
		SendEmailWithSendGrid email = new SendEmailWithSendGrid();
		try {
			result = email.execute(params).get();
		} catch (InterruptedException e) {
			// TODO Implement exception handling
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Implement exception handling
			e.printStackTrace();
		}
		
		// Display the result of the email send
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		intent.putExtra(EXTRA_MESSAGE, result);
		startActivity(intent);
	}

}
