/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.socialgps.ui;

import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.user_pass_dto;
import app.socialgps.ui.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	private DrawerLayout mDrawerLayout; 
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	user_pass_dao upd;
	user_pass_dto upt;
	DatabaseHandler d;
	private String[] menuItems;
	int newPosition = 0;
	private boolean doubleBackToExitPressedOnce = false;
	public Context context;
	LocationServiceAsyncTask lsat;
 
   
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context = getApplicationContext();
		
		//Location retrieving service execution
		Log.d("LocationService", "call to service");
		//try to reuse same object
		lsat = new LocationServiceAsyncTask(context);
		lsat.execute();
		Log.d("Main activity", "continue after gps_details service call");
//		Log.d("LocationService", "lsat called and proceed main activity");
//		new Thread(new Runnable() { 
//            public void run(){
//            	
//            	Calendar cal = Calendar.getInstance();
//        		cal.add(Calendar.SECOND, 10);
//        		Intent service = new Intent(context, LocationService.class);
//        		PendingIntent pintent = PendingIntent
//        				.getService(context, 0, service, 0);
//
//        		//Every 5 mins LocationService service will be called
//        		AlarmManager alarm = (AlarmManager) context
//        				.getSystemService(Context.ALARM_SERVICE);
//        		// for 60 min 60*60*1000
//        		alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
//        				5 * 60 * 1000, pintent);        		
//        		startService(service);
//        		Log.d("LocationService", "service started");
//
////            	LocationServiceAsyncTask lsat = new LocationServiceAsyncTask(context);
////        		lsat.execute();
//            }
//    }).start();
		
//		d = new DatabaseHandler(this);
//		d.close();
//		try {
//			//callAsynchronousTask();
//
//			 } catch (Exception e) {
//			Log.d("main activitiy on create()", e.toString());
//		}

		mTitle = mDrawerTitle = getTitle();
		menuItems = getResources().getStringArray(R.array.menu_items);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, menuItems));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description for accessibility */
		R.string.drawer_close /* "close drawer" description for accessibility */
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d("Main Activity", "onStart()");		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		menu.findItem(R.id.action_notifications).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.action_settings:
		{
			System.out.println("mainactivity settings selected");
			Intent i = new Intent(getApplicationContext(),
					SettingsActivity.class);
			System.out.println("intent created");
				//	i.putExtra("user_detail", notifications.get(position));
					startActivity(i);	
					System.out.println("Settings activity started");
					break;
		}
		case R.id.action_notifications:
		{
			System.out.println("mainactivity notifications selected");
			Intent i = new Intent(getApplicationContext(),
					NotificationActivity.class);
			System.out.println("intent created");
				//	i.putExtra("user_detail", notifications.get(position));
					startActivity(i);	
					System.out.println("notification activity started");
					break;
		}
			// Toast.makeText(getApplicationContext(),
			// "Yet to design Contacts view", Toast.LENGTH_LONG).show();
			//ft.replace(R.id.content_frame, nlf).commit();

			// // create intent to perform web search for this planet
			// Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
			// intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
			// // catch event that there's no activity to handle intent
			// if (intent.resolveActivity(getPackageManager()) != null) {
			// startActivity(intent);
			// } else {
			// Toast.makeText(this, R.string.app_not_available,
			// Toast.LENGTH_LONG).show();
			// }
			// return true;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	private void selectItem(int position) {
		// update the main content by replacing fragments

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(menuItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
		// Fragment mapfragment = new MapsFragment();
		// MapsFragment defined at top
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		switch (position) {
		case 0:
			// googleMap = (GoogleMap) mapFragment.getMap();
			MapsFragment mf = new MapsFragment();
			ft.replace(R.id.content_frame, mf).commit();
			break;
		case 1:
			FriendListFragment flf = new FriendListFragment();
			// Toast.makeText(getApplicationContext(),
			// "Yet to design Contacts view", Toast.LENGTH_LONG).show();
			ft.replace(R.id.content_frame, flf).commit();
			break;

		case 2:
			ContactListFragment clf = new ContactListFragment();
			// Toast.makeText(getApplicationContext(),
			// "Yet to design Contacts view", Toast.LENGTH_LONG).show();
			ft.replace(R.id.content_frame, clf).commit();
			break;

		case 3:
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("Logout");
			alertDialog.setMessage("Are you sure you want Logout?");

			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							d= new DatabaseHandler(getApplicationContext());
							Toast.makeText(
									getApplicationContext()," logged out "+ d.truncate(),
									Toast.LENGTH_SHORT).show();
							finish();
						}
					});

			// Showing Alert Message
			alertDialog.show();
			break; // comment

		}

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Press Back again to exit", Toast.LENGTH_SHORT)
				.show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}
	
	//not used
	
}


