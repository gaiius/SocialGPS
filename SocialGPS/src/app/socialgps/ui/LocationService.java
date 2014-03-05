package app.socialgps.ui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import app.socialgps.middleware.contact_sync;
import app.socialgps.middleware.friend_sync;
import app.socialgps.middleware.location_sync;

public class LocationService extends Service {
	location_sync gps;
	contact_sync cs;
	friend_sync frd;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d("LocationService", "onBind");		
		return null;
	}

	public LocationService() {
		super();
		Log.d("LocationService", "LocationService constructor()");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("LocationService", "GPS service onCreate()");
		//	return null;
		}	

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.d("LocationService", "async task refresh onStart()");
		try {
			Log.d("LocationService", "LocationService contact refresh Started");
			 cs = new contact_sync(this);
			// update contact details
			System.out.println("affected row :"+ cs.sync_contact_db(this.getContentResolver()));			
			//insert are update new contacts from online to local db
			frd= new friend_sync(this);
			frd.sync_ol_sql();			
			//refresh location details
			gps= new location_sync(this);
			
			gps.refresh();
			gps.insert();
			Log.d("LocationService", "LocationService contact refresh finished");
		//	return null;
		} catch (Exception e) {
			Log.e("Err in onStart service", e.toString());
		}
		
	
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);
		Log.d("LocationService", "onStartCommand()");
		return  START_NOT_STICKY ;
		
	}

	/**
	 * @param args
	 */

}
