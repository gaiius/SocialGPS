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
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.middleware.UserInfo;

//connects with the adapter class and sets the view in main class
public class ContactListFragment extends ListFragment {

	// In this class constructor we have to retrieve all matching contacts from
	// server and assign it to a class object or list type

	List<user_detail_dao> contacts = new ArrayList<user_detail_dao>();
	DatabaseHandler d;	

	// listener for contact click
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent i = new Intent(getActivity().getApplicationContext(),
				ContactActivity.class);
		i.putExtra("user_detail", contacts.get(position));
		startActivity(i);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contacts = get_list();
		System.out.println("contacts obtained");
		ContactListArrayAdapter adapter = new ContactListArrayAdapter(
				getActivity().getApplicationContext(), contacts); // pass the class object/list type instead of contactNames
		System.out.println("contactlistadapter created");
			setListAdapter(adapter);				
			System.out.println("contactlistadapter set");
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	public List<user_detail_dao> get_list() {
		try {
			d = new DatabaseHandler(getActivity().getApplicationContext());
			user_pass_dao udd = new user_pass_dao();
			udd = d.check_record();
			System.out.println("record checked");
			contacts = d.select_all();
			System.out.println("contacts selected");
			contacts= remove_mine(udd.get_user_id(), contacts);
			System.out.println("mine removed");
			System.out.println("Frien siz " + contacts.size() + " "
					+ contacts.get(0).get_display_name() + contacts.get(0).get_phone());
			return contacts;
		} catch (Exception e) {
			Log.d("getlist exception", "ContactListFragment getlist function \n" + e.toString());
			return null;
		} finally {
			d.close();
		}
	}

	public List<user_detail_dao> remove_mine(String id, List<user_detail_dao> a) {
		if (a != null)
			for (int i = 0; i < a.size(); i++) {
				if (a.get(i).get_user_id().equals(id)) {
					a.remove(i);
				}
			}
		return a;
	}
}
