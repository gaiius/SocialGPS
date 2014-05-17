package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;

public class NotificationActivity extends Activity {
	Context context;
	private ListView notificationList;
	user_detail_dao udd=new user_detail_dao();
	DatabaseHandler d;
	List<user_detail_dao> contacts = new ArrayList<user_detail_dao>(); // list
																		// for
																		// user
																		// detail
																		// table

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// System.out.println("notification activity entered oncreate");
		setContentView(R.layout.activity_notification_);
//		 System.out.println("notification activity contentviewset");

//		 System.out.println("notification activity items added");
		notificationList = (ListView) findViewById(R.id.notification_list_view);
//		 System.out.println("notification activity listview assigned");
		contacts = get_list();
		System.out.println("checking empty");
		if(contacts==null || contacts.size()<=0)
			{
			user_detail_dao ud1=new user_detail_dao();
			System.out.println("user detail obj");
			ud1.set_display_name("Your contact is empty");
		//	contacts = new ArrayList<user_detail_dao>(); 
			contacts.add(ud1);
			System.out.println("in empty");
			}
		System.out.println("adapter calling");
		NotificationListAdaptor nla = new NotificationListAdaptor(
				getApplicationContext(), R.layout.notification_fragment,
				contacts);
		 System.out.println("adapter created");
		notificationList.setAdapter(nla);
		 System.out.println("adapter set");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.notification, menu);
		return false;
	}

	public List<user_detail_dao> get_list() {
		int l=0;
		try {
			
			d = new DatabaseHandler(this);
			System.out.println("record checked");
			List<friend_detail_dao> friends = new ArrayList<friend_detail_dao>();
			friends = d.select_all_friend_detail();
			if (friends != null)
				for (int i = 0; i < friends.size(); i++) {
					System.out.println("Loop enterd" + i);
					System.out.println(friends.get(i).get_notify()
							+ friends.get(i).get_friend_id());
					if (friends.get(i).get_notify() != null
							&& friends.get(i).get_notify().equals("pending")) {
						// get only pending contact
						udd = new user_detail_dao();
						udd.set_user_id(friends.get(i).get_friend_id());
						System.out.println("preparig pending");
						// get display name from user detail table
						udd = d.select(udd);
						if (udd != null)
							contacts.add(udd); 
					}
				}
			System.out.println("end of loop" +contacts.size());

			return contacts;
		} catch (Exception e) {
			Log.d("notificationlistadapter getlist exception", e.toString());
			return null;
		} finally {
			d.close();
		}
	}

}
