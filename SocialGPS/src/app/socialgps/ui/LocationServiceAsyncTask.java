package app.socialgps.ui;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import app.socialgps.middleware.contact_sync;
import app.socialgps.middleware.friend_sync;

public class LocationServiceAsyncTask extends AsyncTask<Void, Void, Void> {
	Context context;

	public LocationServiceAsyncTask(Context context) {
		super();
		this.context = context;		
		Log.d("LocationService", "lsat constructor() executed");

	}

	@Override
	protected Void doInBackground(Void... params) {
		
		Log.d("LocationService", "lsat doinbackground()");
		
//		Calendar cal = Calendar.getInstance();
//		cal.add(Calendar.SECOND, 10);
		Intent service = new Intent(context, LocationService.class);
		PendingIntent pintent = PendingIntent
				.getService(context, 0, service, 0);
//		PendingIntent pintent = PendingIntent
//				.getService(context, 0, service, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		// for 60 min 60*60*1000
		alarm.setRepeating(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime(),
				2 * 60 * 1000, pintent);
		Log.d("LocationService", "service start before");
		context.startService(service); 
		Log.d("LocationService", "service start after");
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
//		try {
//			Log.d("LocationService", "async task refresh Started");
//			contact_sync cs = new contact_sync(context);
//			System.out.println("affected row :"+ cs.sync_contact_db(context.getContentResolver()));			// update contact details
//			new friend_sync(context).sync_ol_sql();				//insert are update new contacts from online to local db 
//		
//			Log.d("Async task", "refresh finished");
//		//	return null;
//		} catch (Exception e) {
//			Log.e("Err in onPostexe async task", e.toString());
//		//	return null;
//		}
	}
}