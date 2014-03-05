package app.socialgps.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.gps_details;
import app.socialgps.db.dao.location_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.location_sync;

public class FriendActivity extends Activity {
	TextView name;
	TextView[] loc = new TextView[5];
	TextView[] time = new TextView[5];
	user_detail_dao udd = new user_detail_dao();
	DatabaseHandler d;
	List<gps_details> templist;
	List<String> timelst;
	Button showInMap;

	location_detail_dao l;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
		try {
			Intent i = getIntent();
			System.out.println("intent created");
			user_detail_dao uio = (user_detail_dao) i
					.getSerializableExtra("user_detail");
			System.out.println("obtained serialized object");

			name = (TextView) findViewById(R.id.frdname);
			loc[0] = (TextView) findViewById(R.id.loc1);
			loc[1] = (TextView) findViewById(R.id.loc2);
			loc[2] = (TextView) findViewById(R.id.loc3);
			loc[3] = (TextView) findViewById(R.id.loc4);
			loc[4] = (TextView) findViewById(R.id.loc5);
			time[0] = (TextView) findViewById(R.id.time1);
			time[1] = (TextView) findViewById(R.id.time2);
			time[2] = (TextView) findViewById(R.id.time3);
			time[3] = (TextView) findViewById(R.id.time4);
			time[4] = (TextView) findViewById(R.id.time5);
			showInMap = (Button) findViewById(R.id.showInMapButton);
			showInMap.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getApplicationContext(),
							FriendMapActivity.class);
					i.putExtra("user_detail", l.get_location());
					System.out.println("Friend activity intent created");
						//	i.putExtra("user_detail", notifications.get(position));
					startActivity(i);
				}
			});

			System.out.print("Name " + uio.get_display_name());
			name.setText(uio.get_display_name());
			d = new DatabaseHandler(this);
			l = new location_detail_dao();
			l.set_user_id(uio.get_user_id());
			l = d.select(l);
			if (l != null) {
				Log.d("Friend info", l.toString());
				if (l.get_status() == null || l.get_status() != "off") // privacy
				{
					templist = new ArrayList<gps_details>();
					timelst = new ArrayList<String>();
					templist = location_sync.stringtoloc(l.get_location());
					timelst = location_sync.stringtotym(l.get_tyme());
					Geocoder gc = new Geocoder(this);
					for (int j = 0; j < 5; j++) {

						if (j < templist.size()) {
							// try {
							// List<Address> addresses
							// =gc.getFromLocation(templist.get(j).getLat(),
							// templist.get(j).getLng(), 1);
							//
							// if(addresses.size()>0 && addresses!=null)
							// {
							// loc[j].setText(addresses.get(0).getAddressLine(0)+", "+addresses.get(0).getCountryName());
							// Address address = addresses.get(0);
							// Log.d("getAddressLine(0) = " ,
							// address.getAddressLine(0));
							// Log.d("getAddressLine(1) = ",
							// address.getAddressLine(1));
							// Log.d("getAddressLine(2) = " ,
							// address.getAddressLine(2));
							//
							// }
							// else
							loc[j].setText("Lat: " + templist.get(j).getLat()
									+ ", Lon: " + templist.get(j).getLng());
							time[j].setText(timelst.get(j));
							// } catch (IOException e) {
							// Log.e("Exception Location setting frd activity",
							// e.toString());
							// }
						} else {
							loc[j].setText("Not Avail");
							time[j].setText("...");
						}

					}
				}
			}
		} catch (Exception e) {
			Log.d("Friend activity on create()", e.toString());
		} finally {
			d.close();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		// getMenuInflater().inflate(R.menu.friend, menu);
		return false;
	}

}
