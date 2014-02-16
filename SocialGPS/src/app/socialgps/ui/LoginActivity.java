package app.socialgps.ui;

import app.socialgps.ui.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.*;
import app.socialgps.db.dto.*;

public class LoginActivity extends Activity {
	Button b1;
	boolean update = false;
	TextView t1;
	EditText user_name;
	EditText password;
	String userid, passwd, ph_no;
	user_pass_dao upd;
	user_pass_dto upt;
	DatabaseHandler d;
	TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		try {
			tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
			ph_no= tm.getLine1Number();
			user_name = (EditText) findViewById(R.id.editText1);
			user_name.setText(ph_no);
			//user_name.setEnabled(false);
			b1 = new Button(this);
			b1 = (Button) findViewById(R.id.button);
			t1 = new TextView(this);
			t1 = (TextView) findViewById(R.id.textView5);
			d = new DatabaseHandler(this);
			if(init())		//checking db
			{
				Intent i = new Intent(getApplicationContext(), 	MainActivity.class); 	//redirecting to main page
				startActivity(i);
				finish();
				Log.d("already there", "forwarding");
			}
			else
				Log.d("Nothing match","try again");
			Log.d("Event Started", "Button");
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			getMenuInflater().inflate(R.menu.main, menu);
			b1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					update = false;
					upt = new user_pass_dto();
					if (!local_check_now()) {
						if (check_now()) {
							if (update)	{
								d.update(upd);
								d.close(); }
							else {
								 d.insert(upd);
								 d.close(); }
							//System.out.print("Test case" + t);
							// continue next activity
							Intent i = new Intent(getApplicationContext(),
									MainActivity.class);
							startActivity(i);
							finish();
						} else
							Toast.makeText(getApplicationContext(),
									"Incorrect User name password Set",
									Toast.LENGTH_LONG).show();
					} else {
						// continue next activity
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						startActivity(i);
						finish();
					}

				}
			});

			t1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent i = new Intent(getApplicationContext(),
							Registration.class);
					startActivity(i);
					finish();
				}
			});

			return true;
		}

		catch (Exception e) {
			Log.e("Main", e.toString());
			return true;
		}
	}
	public boolean init()
	{
		try	{
		user_pass_dao upp= new user_pass_dao(); 
		user_pass_dao nu_upp= new user_pass_dao(); 
		user_pass_dto uppt = new user_pass_dto();
		
		nu_upp=d.check_record();
		if(nu_upp!=null)	{
		upp = uppt.select(nu_upp);
		if (uppt.get_status() == 3)	{ // check user_name
			if (upp.get_passwd().equals(nu_upp.get_passwd()))	{
				return true;	}
			else {
				return false;
			}
		
		} else {
		Log.d("checking APP", "OUT");
		return false;
	}
		}
		else
			return false;
		}

		catch (Exception e) {
			Log.e("Main", e.toString());
			return true;
		}
		finally{ d.close(); }
	}

	public boolean check_empty() {
		Log.d("Event Running", "Checking empty");

		user_name = new EditText(this);
		password = new EditText(this);
		user_name = (EditText) findViewById(R.id.editText1);
		password = (EditText) findViewById(R.id.editText2);
		userid = user_name.getText().toString();
		passwd = password.getText().toString();

		if (userid.equals("") || passwd.equals("")) {
			Log.d("Event Running", "False");
			return false;
		} else {
			upd = new user_pass_dao();
			upd.set_user_id(userid);
			upd.set_passwd(passwd);
			return true;
		}
	}

	public boolean local_check_now() {
		try{
		if (check_empty()) {
			Log.d("Event Running", "checking local");
			// System.out.println("inserting"+d.insert(upd));
			upd = d.select(upd);
			if (upd != null) {
				if (upd.get_passwd().equals(passwd)) {
					Log.d("checking local", "True");
					return true;
				} else {
					update = true;
					Log.d("checking local1", "Wrong password");
					return false;
				}
			} else {
				Log.d("checking local2", "False");
				return false;
			}
		} else {
			Log.d("checking local", "no entry");
			return false;
		}
		} catch(Exception e) { Log.d("login db", e.toString()); return false; }
		finally{ d.close(); }
	}

	public boolean check_now() {
		try {
			if (check_empty()) {
				Log.d("checking Net", "IN");

				upd = upt.select(upd);
				if (upt.get_status() == 3) // check user_name
					if (upd.get_passwd().equals(passwd))
						return true;
					else {
						return false;
					}
				else {
					return false;
				}
			} else {
				Log.d("checking Net", "OUT");
				Toast.makeText(getApplicationContext(),
						"Empty set- Please enter some values",
						Toast.LENGTH_LONG).show();
				return false;
			}
		} catch (Exception e) {
		  //	Log.e("Main", e.toString());
			return false;
		}
	}
}
