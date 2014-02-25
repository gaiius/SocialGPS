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
import android.widget.Toast;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.middleware.UserInfo;

//connects with the adapter class and sets the view in main class
public class FriendListFragment extends ListFragment {
	// In this class constructor we have to retrieve all matching contacts from
	// server and assign it to a class object or list type


		user_detail_dao udd;
	DatabaseHandler d;	
	List<user_detail_dao> contacts= new ArrayList<user_detail_dao>();			// list for user detail table
	
	
	// listener for contact click
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Toast.makeText(getActivity().getApplicationContext(),
				"FriendListItem onClickListener position " + position,
				Toast.LENGTH_LONG).show();
		// Intent i = new Intent(getActivity().getApplicationContext(),
		// ContactActivity.class);
		// UserInfo uio = new UserInfo();
		// uio.name = "First Last";
		// uio.phone = "9999999999";
		// uio.status = "Hello this is my status";
		// i.putExtra("userinfoobj", uio);
		// startActivity(i);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		contacts = get_list();
		System.out.println("contacts obtained");
		
		FriendListCustomAdaptor adapter = new FriendListCustomAdaptor(
				getActivity().getApplicationContext(), contacts);
		setListAdapter(adapter);
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
				if(friends.get(i).get_status()!=null && friends.get(i).get_status().equals("accepted"))				//get only accepted contact
				{
					udd= new user_detail_dao();
					udd.set_user_id(friends.get(i).get_friend_id());
					udd= d.select(udd);											//get display name from user detail table
					if(udd!=null)
						contacts.add(udd);
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