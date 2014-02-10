package app.socialgps.ui;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.widget.Toast;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.*;
import app.socialgps.db.dto.*;

public class Registration extends Activity {
	Button b1;
	EditText e1,e2,e3, e4, e5;
	String s1, s2,s3,s4, s5, ph_no;
	TelephonyManager tm;
	user_detail_dao udd;
	user_detail_dto udt;
	user_pass_dao upd;
	user_pass_dto upt;
	DatabaseHandler d;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		tm = (TelephonyManager)getSystemService(TELEPHONY_SERVICE); 
		ph_no= tm.getLine1Number();
		e3=(EditText) findViewById(R.id.editText3);
		e3.setText(ph_no);
	
		b1= new Button(this);
		b1=(Button) findViewById(R.id.button);
		upd = new user_pass_dao();
		udd=new user_detail_dao();
		d= new DatabaseHandler(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registration, menu);
		try	{
		
		b1.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View view) {
				if(local_check())
				{					
					upd= new user_pass_dao();
					
					udd.set_user_id(s3);
					udd.set_user_name(s1);
					udd.set_email_id(s2);
					udd.set_phone(Long.parseLong(s3));
					udd.set_status("on");
					
					upd.set_user_id(s3);
					upd.set_passwd(s5);
				
					upt = new user_pass_dto();				//storing in online db
					upt.insert(upd);
					
					udt = new user_detail_dto(upd);			
					udt.insert(udd);
					System.out.println("upd insert");
					
					
					d.insert(upd);							//storing in offline db
					d.insert(udd);
					Toast.makeText(getApplicationContext(), "User Registered, please continue by login", Toast.LENGTH_LONG).show();
					
					//Redirecting to Login Screen
					Intent i = new Intent(getApplicationContext(),
							LoginActivity.class);
					startActivity(i);
					finish();
				}
			}
			});
		
		return true;
		}
		catch(Exception e) {
			Log.e("[Exception]",e.toString());
			return true;
		}
	}
	public boolean is_avail()
	{
		try	{
		upd= new user_pass_dao();
		upd.set_user_id(s3);
		upt= new user_pass_dto();
		upd= upt.select(upd);
		if(upt.get_status()!=101)
		{
			return false;
		}
		else	{
	//		Toast.makeText(getApplicationContext(), "User Email id not exist", Toast.LENGTH_LONG).show();
			upd= null;
			return true;
		}
	}
		catch(Exception e) {
			Log.e("[Exception]",e.toString());
			return false;
		}
	}
	
	public boolean local_check()
	{
		try	{
		e1=(EditText)  findViewById(R.id.editText1);
		e2=(EditText)  findViewById(R.id.editText2);
		e3=(EditText)  findViewById(R.id.editText3);
		e4=(EditText)  findViewById(R.id.editText4);
		e5=(EditText)  findViewById(R.id.editText5);
		s1=e1.getText().toString();
		s2=e2.getText().toString();
		s3=e3.getText().toString();
		s4=e4.getText().toString();
		s5=e5.getText().toString();
		String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";
		 
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s2);
		
		
		if(s1.equals("")|s2.equals("")|s3.equals("")|s4.equals("")|s5.equals(""))
		{
			Toast.makeText(getApplicationContext(), "Please enter all values", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(!m.find())
		{
			Toast.makeText(getApplicationContext(), "Email id is invalid format", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(!(s4.equals(s5)))
		{
			Toast.makeText(getApplicationContext(), "Password is not same", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(s1.length()>10)
		{
			Toast.makeText(getApplicationContext(), "User name size should be within 10", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(s3.length()!=10)
		{
			Toast.makeText(getApplicationContext(), "Phone number size should be 10 not "+s3.length(), Toast.LENGTH_LONG).show();
			return false;
		}
		else if((s4.length()<5) | s4.length()>20)
		{
			Toast.makeText(getApplicationContext(), "Password size must within 5 to 20", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(!is_avail())
		{
			Toast.makeText(getApplicationContext(), "User Email id already exist", Toast.LENGTH_LONG).show();
			return false;
		}
		else
		{
			Log.d("s1 s2 s3 s4 s5", s1+s2+s3+s4+s5);
					
			return true;
		}
			
		
	}
	catch(Exception e)
	{
		Log.e("[Exception]: ",e.toString());
		return false;
	}
	}

}
