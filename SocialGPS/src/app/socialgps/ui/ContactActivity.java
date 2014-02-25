package app.socialgps.ui;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
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
	List<friend_detail_dao> frdlist= new ArrayList<friend_detail_dao>();
	user_pass_dao upd= new user_pass_dao();;
	user_detail_dao udd= new user_detail_dao();
	friend_detail_dao frd= new friend_detail_dao(); 
	friend_detail_dto frt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		System.out.println("layout set");

		try {
			Intent i = getIntent();
			System.out.println("intent created");
			user_detail_dao uio = (user_detail_dao) i.getSerializableExtra("user_detail");
			System.out.println("obtained serialized object");

			name = (TextView) findViewById(R.id.contactName);
			number = (TextView) findViewById(R.id.contactNumber);
			emailid= (TextView) findViewById(R.id.frdname);
			status = (TextView) findViewById(R.id.contactStatus);
			addFriendButton = (Button) findViewById(R.id.contactButton);
			name.setText(uio.get_display_name());
			number.setText(uio.get_user_id());
			emailid.setText(uio.get_email_id());
			this.frd_id= uio.get_user_id();
			status.setText(uio.get_status());
			final contact_manage cm= new contact_manage(frd_id, getApplicationContext());
			
			if(cm.already_there().equals("nothing same"))
			{
				addFriendButton.animate();
			}
			else if (cm.already_there().equals("pending") || cm.already_there().equals("accepted"))
			{
				addFriendButton.setVisibility(View.INVISIBLE);
			}
		
		
		addFriendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(cm.send_req()!=106)
				{
					Toast.makeText(getApplicationContext(),	"Request sent",	Toast.LENGTH_LONG).show();
					addFriendButton.setVisibility(View.INVISIBLE);
				}
			}
		});
		} catch (Exception e) {
			e.printStackTrace();
		} finally { }

		
	}

	
	
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.contact, menu);
	// return true;
	// }

}
