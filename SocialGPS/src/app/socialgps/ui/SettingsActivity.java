package app.socialgps.ui;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.location_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.location_detail_dto;
import app.socialgps.db.dto.user_detail_dto;

public class SettingsActivity extends Activity {
	Button b1;
	EditText name, emailid;
	Switch privacy;
	EditText status;
	DatabaseHandler d;
	user_detail_dao udd;
	user_pass_dao upd = new user_pass_dao();
	location_detail_dao ldd;
	user_detail_dto udt;
	location_detail_dto ldt;
	protected LocationManager locationManager;
	boolean isNetworkEnabled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		b1 = new Button(this);
		b1 = (Button) findViewById(R.id.editorsubmit);
		name = (EditText) findViewById(R.id.myname);
		emailid = (EditText) findViewById(R.id.myemailid);
		privacy = (Switch) findViewById(R.id.privacy);
		status = (EditText) findViewById(R.id.status1);
		d = new DatabaseHandler(this); // pass context
		upd = d.check_record();
		udt = new user_detail_dto(upd);
		ldt = new location_detail_dto(upd);

		try {
			System.out.println(upd);
			
			udd = new user_detail_dao();
			udd.set_user_id(upd.get_user_id());
			udd = d.select(udd);
			System.out.println(udd);
			
			ldd = new location_detail_dao();
			ldd.set_user_id(upd.get_user_id());
			ldd = d.select(ldd);
			Log.d("settings", "getting data");
			System.out.println(ldd);

			
			if (udd != null && ldd != null) {
				System.out.println(ldd.get_status());

				Log.d("detais", udd.toString());
				name.setText(udd.get_user_name());
				emailid.setText(udd.get_email_id());
				status.setText(udd.get_status());

				if (ldd.get_status() != null && ldd.get_status().equals("off"))
					privacy.setChecked(false);
				else
					privacy.setChecked(true);
				System.out.println("detais"+
						udd.get_user_name() + udd.get_status()
								+ udd.get_email_id()+ ldd.get_status());
			}

			name.setEnabled(false);
			emailid.setEnabled(false);
			status.setEnabled(false);
			privacy.setEnabled(false);

			b1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					event_invoked();
				}
			});

		} catch (Exception e) {
			Log.e("[Exception in oncreateoption Homeactivitiy]: ", e.toString());

		} finally {
			d.close();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		//getMenuInflater().inflate(R.menu.home, menu);
		return false;
	}

	public void event_invoked() {

		if(testnet())
		{
		int t=0, t1=0;
		if (b1.getText().equals("Edit")) {
			b1.setText("Submit");
			name.setEnabled(true);
			emailid.setEnabled(true);
			status.setEnabled(true);
			privacy.setEnabled(true);
		} else if (b1.getText().equals("Submit")) {
			b1.setEnabled(false);
			name.setEnabled(false);
			emailid.setEnabled(false);
			status.setEnabled(false);
			privacy.setEnabled(false);

			udd = new user_detail_dao();
			udd.set_user_id(upd.get_user_id());
			udd.set_user_name(name.getText().toString());
			udd.set_email_id(emailid.getText().toString());
			udd.set_status(status.getText().toString());

			
			t=udt.update(udd);
			if(t!=105)
			{
			d.update(udd);
			
			ldd = new location_detail_dao();
			ldd.set_user_id(upd.get_user_id());
			if (privacy.isChecked())
				ldd.set_status("on");
			else
				ldd.set_status("off");

			t=ldt.update(ldd);
			if(t!=105)
			{
				d.update(ldd);
				Toast.makeText(getApplicationContext(),
						"Settings saved",
						Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(getApplicationContext(),
						"Can't Convience a server, Please try again",
						Toast.LENGTH_LONG).show();
		}
			else
				Toast.makeText(getApplicationContext(),
						"Can't Convience a server, Please try again",
						Toast.LENGTH_LONG).show();
			b1.setEnabled(true);
			b1.setText("Edit");
		
		}
	}
		else
			Toast.makeText(getApplicationContext(),
					"No internet connection, Can't make your request",
					Toast.LENGTH_LONG).show();
}
	 public boolean testnet(){
	        ConnectivityManager connectivity = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	          if (connectivity != null) 
	          {
	              NetworkInfo[] info = connectivity.getAllNetworkInfo();
	              if (info != null) 
	                  for (int i = 0; i < info.length; i++) 
	                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
	                      {
	                          return true;
	                      }
	          }
	          return false;
	    }
}