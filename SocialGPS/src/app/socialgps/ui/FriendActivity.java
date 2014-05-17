package app.socialgps.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
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
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
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
	friend_detail_dao frd = new friend_detail_dao();
	List<gps_details> templist;
	List<String> timelst;
	Button showInMap, route;

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
			
			route=(Button) findViewById(R.id.routeButton);
			route.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getApplicationContext(),
							FriendMapActivity.class);
					i.putExtra("user_detail", l.get_location());
					i.putExtra("type", "route");
					System.out.println("Friend activity intent created");
					// i.putExtra("user_detail", notifications.get(position));
					startActivity(i);
				}});
				
			showInMap = (Button) findViewById(R.id.showInMapButton);
			showInMap.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getApplicationContext(),
							FriendMapActivity.class);
					i.putExtra("user_detail", l.get_location());
					i.putExtra("user_timing", l.get_tyme());
					i.putExtra("type", "marker");
					System.out.println("Friend activity intent created");
					// i.putExtra("user_detail", notifications.get(position));
					startActivity(i);
				}
			});

			System.out.print("Name " + uio.get_display_name());
			name.setText(uio.get_display_name());
			d = new DatabaseHandler(this);
			l = new location_detail_dao();
			l.set_user_id(uio.get_user_id());
			frd.set_friend_id(uio.get_user_id());
			System.out.println(frd);
			frd= d.selectbyfrdid(frd);
			System.out.println("after :"+frd);
			l = d.select(l);
			if (l != null && frd!=null) {
				Log.d("Friend info", l.toString()+ " " +frd.toString());
				if ((l.get_status() == null || l.get_status().equals("off")) && (frd.get_status()!=null) && frd.get_status().equals("accepted") ) // privacy
				{
					templist = new ArrayList<gps_details>();
					timelst = new ArrayList<String>();
					System.out.println("Accepted contact");
					templist = location_sync.stringtoloc(l.get_location());
					timelst = location_sync.stringtotym(l.get_tyme());
					Geocoder geocoder;
					List<Address> addresses;
					geocoder = new Geocoder(this, Locale.getDefault());
					for (int j = 0; j < 5; j++) {

						if (j < templist.size()) {

							try {
								Log.d("Geocode", templist.get(j).getLat() + " "
										+ templist.get(j).getLng());
								// System.out.println(templist.get(j));
								System.out.println("addresses 1");
								
								addresses = geocoder.getFromLocation(templist
										.get(j).getLat(), templist.get(j)
										.getLng(), 1);
								// addresses = geocoder.getFromLocation(47.656275, -122.303135, 1);
						System.out.println("addresses 2");
								
								if(addresses!=null)// && addresses.get(0)!=null)
								{
									System.out.println("addresses 3");
									String address = addresses.get(0)
											.getAddressLine(0)+", "+addresses.get(0)
											.getAddressLine(1)+", "+addresses.get(0)
											.getAddressLine(2);
									loc[j].setText(address);
								}
								//if address is null time not assigned
							} catch (Exception e) {
								// TODO Auto-generated catch block
								loc[j].setText("Lat: "
										+ templist.get(j).getLat()
										+ ", Lon: "
										+ templist.get(j).getLng());
					
								Log.e("GeoCode Exception", e.toString());
							}
							time[j].setText(timelst.get(j));
						} else {
							loc[j].setText("Not Avail");
							time[j].setText("...");
						}
					}
				}
			else
				for (int j = 0; j < 5; j++) {
					loc[j].setText("Not Avail");
					time[j].setText("...");
				}
			}
		} catch (Exception e) {
			Log.d("Friend activity on create()", e.toString());
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		// getMenuInflater().inflate(R.menu.friend, menu);
		return false;
	}

}
