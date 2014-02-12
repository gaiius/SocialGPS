package app.socialgps.ui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.UserInfo;

public class ContactActivity extends Activity {

	TextView name;
	TextView number;
	TextView status;
	Button addFriendButton;
	String[] contactNames;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		System.out.println("layout set");

		try {
			Intent i = getIntent();
			System.out.println("intent created");
			user_detail_dao uio = (user_detail_dao) i
					.getSerializableExtra("user_detail");
			System.out.println("obtained serialized object");

			name = (TextView) findViewById(R.id.contactName);
			number = (TextView) findViewById(R.id.contactNumber);
			status = (TextView) findViewById(R.id.contactStatus);
			addFriendButton = (Button) findViewById(R.id.contactButton);
			name.setText(uio.get_display_name());
			number.setText(uio.get_user_id());
			status.setText(uio.get_status());
		} catch (Exception e) {
			e.printStackTrace();
		}

		addFriendButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(),
						"FriendButton onClickListener activated",
						Toast.LENGTH_LONG).show();
				// TODO Auto-generated method stub

			}
		});
	}

	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.contact, menu);
	// return true;
	// }

}
