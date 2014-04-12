package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.UserInfo;
import app.socialgps.middleware.contact_manage;
import android.widget.Toast;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.friend_detail_dto;
import app.socialgps.middleware.UserInfo;

public class ContactActivity extends Activity {

	TextView name;
	TextView number;
	TextView emailid;
	TextView status;
	DatabaseHandler d;
	Button addFriendButton;
	String[] contactNames;
	String frd_id;
	List<friend_detail_dao> frdlist = new ArrayList<friend_detail_dao>();
	user_pass_dao upd = new user_pass_dao();;
	user_detail_dao udd = new user_detail_dao();
	user_detail_dao uio;
	friend_detail_dao frd = new friend_detail_dao();
	friend_detail_dto frt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		System.out.println("layout set");

		try {
			Intent i = getIntent();
			System.out.println("intent created");
			uio = (user_detail_dao) i.getSerializableExtra("user_detail");
			System.out.println("obtained serialized object");
			d = new DatabaseHandler(getApplicationContext());
			name = (TextView) findViewById(R.id.contactName);
			number = (TextView) findViewById(R.id.contactNumber);
			emailid = (TextView) findViewById(R.id.frdname);
			status = (TextView) findViewById(R.id.contactStatus);
			addFriendButton = (Button) findViewById(R.id.contactButton);
			name.setText(uio.get_display_name());
			number.setText(uio.get_user_id());
			emailid.setText(uio.get_email_id());
			this.frd_id = uio.get_user_id();
			status.setText(uio.get_status());

			frd.set_friend_id(this.frd_id);
			frd = d.selectbyfrdid(frd);
			System.out.println(frd);

			
			final contact_manage cm = new contact_manage(frd_id,
					getApplicationContext());
			if (frd==null || (frd.get_status() != null && !frd.get_status().equals("blocked"))) {
				if (cm.already_there().equals("nothing same")) {
					addFriendButton.animate();
				} else if (cm.already_there().equals("pending")) {
					addFriendButton.setText("Delete Request");
				} else if (cm.already_there().equals("accepted")) {
					addFriendButton.setText("Remove Friend");
				}
			} else
				addFriendButton.setVisibility(View.GONE); // for blocked contact

			addFriendButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if(testnet())
					{
					addFriendButton.setEnabled(false);
					upd = d.check_record();
					frt = new friend_detail_dto(upd);
					int t = 0, t1 = 0;

					if (addFriendButton.getText().equals("Add Friend")) {
						System.out.println("Add Friend");

						if (cm.send_req() != 105) {
							Toast.makeText(getApplicationContext(),
									"Request sent", Toast.LENGTH_LONG).show();
							addFriendButton.setText("Delete Request");
						} else
							Toast.makeText(
									getApplicationContext(),
									uio.get_user_id()
											+ "Sorry,can't convience a server. Please try again later",
									Toast.LENGTH_SHORT).show();

					} else if (addFriendButton.getText().equals(
							"Delete Request")) {
						frd = new friend_detail_dao();
						System.out.println("In Remove Req");

						frd.set_user_id(upd.get_user_id());
						frd.set_friend_id(frd_id);
						frd.set_status("pending");
						t = frt.delete(frd);

						if (t != 105) {
							frd.set_friend_id(frd_id);
							frd.set_status("null");
							frd.set_notify("null");
							d.update(frd);
							System.out.println("onclick Exit");
							addFriendButton.setText("Add Friend");
							Toast.makeText(getApplicationContext(),
									uio.get_user_id() + " request canceled",
									Toast.LENGTH_LONG).show();
						} else
							Toast.makeText(
									getApplicationContext(),
									uio.get_user_id()
											+ "Sorry,can't convience a server. Please try again later",
									Toast.LENGTH_SHORT).show();

					} else if (addFriendButton.getText()
							.equals("Remove Friend")) {

						frd = new friend_detail_dao();
						System.out.println("In Remove frd");

						frd.set_user_id(frd_id);
						frd.set_friend_id(upd.get_user_id());
						t = frt.delete(frd);
						System.out.println("1 " + frd);

						frd.set_user_id(upd.get_user_id());
						frd.set_friend_id(frd_id);
						t1 = frt.delete(frd);
						System.out.println("2 " + frd);

						if (t != 105 && t1 != 105) {
							frd.set_friend_id(frd_id);
							frd.set_status("null");
							frd.set_notify("null");
							d.update(frd);
							addFriendButton.setText("Add Friend");
							System.out.println("3 " + frd);
							Toast.makeText(getApplicationContext(),
									uio.get_user_id() + " removed",
									Toast.LENGTH_LONG).show();
						} else
							Toast.makeText(
									getApplicationContext(),
									uio.get_user_id()
											+ "Sorry,can't convience a server. Please try again later",
									Toast.LENGTH_SHORT).show();

					}
					addFriendButton.setEnabled(true);

				}
					else
						Toast.makeText(getApplicationContext(),
								"No internet connection, Can't make your request",
								Toast.LENGTH_LONG).show();
					}
			});
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			d.close();
		}

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

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.contact, menu);
	// return true;
	// }

}
