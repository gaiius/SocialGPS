package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.friend_sync;

//sets the views in contact_fragment for each contact in a listview
public class NotificationListArrayAdapter extends ArrayAdapter<String> {
	private final Context context;// declare class object/list type here
	private List<user_detail_dao> contacts;
	//DatabaseHandler d;
	friend_detail_dao fdd;
	DatabaseHandler d;
	
	TextView notificatonName;
	Button acceptButton;
	Button denyButton;
	
	
	public NotificationListArrayAdapter(Context context,
			List<user_detail_dao> contacts) {		
		super(context, R.layout.notification_fragment); 		
		this.context = context;
		this.contacts = contacts;
		new friend_sync(context).sync_ol_sql();				//insert are update new contacts from online to local db 
	//	System.out.println("ContactListFragment constructor "+ contacts.get(0)
	//			.get_display_name()+contacts.get(0).get_phone());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			System.out.println("getview first line");
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("layout defined");

			View rowView = inflater.inflate(R.layout.notification_fragment, parent,
					false);
			System.out.println("view inflated");

			notificatonName = (TextView) rowView
					.findViewById(R.id.notification_name);
			acceptButton = (Button) rowView.findViewById(R.id.notification_accept_button);
			denyButton = (Button) rowView.findViewById(R.id.notification_deny_button);
			acceptButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 Toast.makeText(context,
								"Yet to design Contacts view", Toast.LENGTH_LONG).show();
				}
			});

			System.out.println("views created");
			// get position in list and assign object and use it to set values
			// set contactname
			notificatonName.setText(contacts.get(position).get_display_name());
			
			System.out.println("name set");
			System.out.println("arrayadapterclass "+contacts.get(position).get_display_name());		
			
			
			return rowView;
		} catch (Exception e) {
			Log.d("NotificatioAdaptor Exception", e.toString());
			return null;
		}

	}
//	public int getCount() {
//		   return contacts.size();
//		}
}
