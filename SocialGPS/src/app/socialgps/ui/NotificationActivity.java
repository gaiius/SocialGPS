package app.socialgps.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class NotificationActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notification);
//		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//		NotificationListFragment nlf = new NotificationListFragment();
//		ft.replace(R.id.notification_fragment_id, nlf).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notification, menu);
		return true;
	}

}
