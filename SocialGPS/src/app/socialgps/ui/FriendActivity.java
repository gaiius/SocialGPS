package app.socialgps.ui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import app.socialgps.db.dao.user_detail_dao;

public class FriendActivity extends Activity {
	TextView name;
	user_detail_dao udd= new user_detail_dao();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Intent i = getIntent();
		System.out.println("intent created");
		user_detail_dao uio = (user_detail_dao) i.getSerializableExtra("user_detail");
		System.out.println("obtained serialized object");

		name = (TextView) findViewById(R.id.frdname);
		System.out.print("Name "+uio.get_display_name());
		name.setText(uio.get_display_name());
		
		getMenuInflater().inflate(R.menu.friend, menu);
		return true;
	}

}
