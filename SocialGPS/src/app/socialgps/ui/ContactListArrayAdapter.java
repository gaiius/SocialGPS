package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.friend_sync;

//sets the views in contact_fragment for each contact in a listview
public class ContactListArrayAdapter extends ArrayAdapter<String> {
	private final Context context;// declare class object/list type here
	private List<user_detail_dao> contacts;
	//DatabaseHandler d;
	friend_detail_dao fdd;
	DatabaseHandler d;
	public ContactListArrayAdapter(Context context,
			List<user_detail_dao> contacts) {		
		super(context, R.layout.contact_fragment); 		
		this.context = context;
		this.contacts = contacts;
	//	System.out.println("ContactListFragment constructor "+ contacts.get(0).get_display_name()+contacts.get(0).get_phone());
	}

	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			System.out.println("getview first line");
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("layout defined");

			View rowView = inflater.inflate(R.layout.contact_fragment, parent,
					false);
			System.out.println("view inflated");

			TextView contactName = (TextView) rowView
					.findViewById(R.id.contact_name);

			TextView friendStatus = (TextView) rowView
					.findViewById(R.id.contact_friend_status);
			System.out.println("views created");
			// get position in list and assign object and use it to set values
			// set contactname
			contactName.setText(contacts.get(position).get_display_name());
			
			System.out.println("name set");
			System.out.println("arrayadapterclass "+contacts.get(position).get_display_name());

			this.d= new DatabaseHandler(this.context); 
			fdd= new friend_detail_dao();
			fdd.set_friend_id(contacts.get(position).get_user_id());
			fdd = this.d.selectbyfrdid(fdd);
			if(fdd==null)
			{
				friendStatus.setText("");
			}
			else if(fdd.get_status().equals("pending"))
			{
				friendStatus.setText("Request sent");
			}		
			else if (fdd.get_status().equals("accepted"))
			{
				friendStatus.setText("Friend");
			}
			else if(fdd.get_status().equals("blocked"))
			{
				friendStatus.setText("");			//not yet decided
			}
			
			return rowView;
		} catch (Exception e) {
			Log.d("Exception in getview contact array adapter", e.toString());
			return null;
		}finally
		{ d.close(); }

	}
	public int getCount() {
		if (contacts==null)
			return 0;
		
		   return contacts.size();
		}
}
