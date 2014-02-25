package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.middleware.UserInfo;

//connects with the adapter class and sets the view in main class
public class NotificationListFragment extends ListFragment {

	// In this class constructor we have to retrieve all matching contacts from
	// server and assign it to a class object or list type
	user_detail_dao udd = new user_detail_dao();
	DatabaseHandler d;	
	String[] sampleData = {"BalaG", "Dinesh", "Thilak"};
	List<user_detail_dao> contacts= new ArrayList<user_detail_dao>();			// list for user detail table
	

	// listener for contact click
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(getActivity().getApplicationContext(),
		NotificationActivity.class);
		//i.putExtra("user_detail", notifications.get(position));
		startActivity(i);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//contacts = get_list();
		//System.out.println("contacts obtained");
//		udd.set_display_name("Balaji");
//		notifications.add(udd);
		NotificationListArrayAdapter adapter = new NotificationListArrayAdapter(
				getActivity().getApplicationContext(), contacts); // pass the class object/list type instead of contactNames
		System.out.println("notificationslistadapter created");
			setListAdapter(adapter);				
			System.out.println("notificationslistadapter set");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public List<user_detail_dao> get_list() {
		try {
			d = new DatabaseHandler(getActivity().getApplicationContext());
			System.out.println("record checked");
			List<friend_detail_dao> friends = new ArrayList<friend_detail_dao>();
			friends = d.select_all_friend_detail();
			if(friends!=null)
			for(int i=0; i<friends.size();i++)						
			{
				Log.d(friends.get(i).get_friend_id(),friends.get(i).get_status());
				if(friends.get(i).get_status().equals("pending"))				//get only accepted contact
				{
					udd= new user_detail_dao();
					udd.set_user_id(friends.get(i).get_friend_id());
					udd= d.select(udd);											//get display name from user detail table
					contacts.add(udd);
					Log.d("Display name",udd.get_display_name());
				}
			}
			System.out.println("contacts selected");
			return contacts;
		} catch (Exception e) {
			Log.d("getlist frd exception",  e.toString());
			return null;
		} finally {
			d.close();
		}
	}
}