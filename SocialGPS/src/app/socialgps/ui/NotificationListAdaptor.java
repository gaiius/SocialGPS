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
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.friend_detail_dto;
import app.socialgps.middleware.contact_manage;


	public class NotificationListAdaptor extends ArrayAdapter<user_detail_dao> {

		private List<user_detail_dao>  items;
		private int layoutResourceId;
		private Context context;
		List<friend_detail_dao> olfrdlist= new ArrayList<friend_detail_dao>();
		user_detail_dao udd= new user_detail_dao();
		friend_detail_dao frd; 
		friend_detail_dto frt;
		TextView notificationName;
		Button acceptButton;
		Button denyButton;
		DatabaseHandler d;
		user_pass_dao upd;
		contact_manage cm;
		public NotificationListAdaptor(Context context, int layoutResourceId, List<user_detail_dao>  items) {
			super(context, layoutResourceId, items);
			System.out.println("noti list adaptor constructor entered");
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.items = items;
			d=new DatabaseHandler(context);
			upd= new user_pass_dao();;
			upd= d.check_record();
			this.frt= new  friend_detail_dto(upd);
			System.out.println("noti list adaptor constructor end");
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View row = convertView;
			try {
			//AtomPaymentHolder holder = null;
			System.out.println("getview entered");
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("inflating");
			row = inflater.inflate(layoutResourceId, parent, false);
			System.out.println("inflated");

			notificationName = (TextView)row.findViewById(R.id.notification_name);
			acceptButton = (Button)row.findViewById(R.id.notification_accept_button);
			denyButton = (Button)row.findViewById(R.id.notification_deny_button);
//			notificationName.setText("Hello hi");
			notificationName.setText(items.get(position).get_display_name());
			acceptButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//adding friend
					frd= new friend_detail_dao(); 	
					frd.set_friend_id(items.get(position).get_user_id());
					frd.set_notify("accepted");
					frd.set_status("accepted");
				
						d.update(frd);
					
					
					// making another user as frd
					frd= new friend_detail_dao(); 	
					frd.set_user_id(upd.get_user_id());
					frd.set_friend_id(items.get(position).get_user_id());
					frd.set_status("accepted");
					cm= new contact_manage(items.get(position).get_user_id(),context);
					if(cm.already_there().equals("pending")) //req recvd have to update
					{
						frt.update(frd);
					}
					else
					{
						frt.insert(frd);
					}
					
					System.out.println("user pass :"+upd.get_user_id());
					frd.set_user_id(items.get(position).get_user_id());
					frd.set_friend_id(upd.get_user_id());
					frd.set_status("accepted");
					frt.update(frd);

					System.out.println("onclick Exit");
					Toast.makeText(context,
							items.get(position).get_user_id()+" request accepted",
							Toast.LENGTH_LONG).show();
				}
			});
			
			denyButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					System.out.println("onclick entered");
					frd= new friend_detail_dao(); 
					frd.set_friend_id(items.get(position).get_user_id());
					System.out.println("user pass :"+upd.get_user_id());
					frd.set_notify("null");
					d.update(frd);
				
					frd.set_user_id(items.get(position).get_user_id());
					frd.set_friend_id(upd.get_user_id());
					frd.set_status("pending");
					frt.delete(frd);
					System.out.println("onclick Exit");
					
					Toast.makeText(context,
							items.get(position).get_user_id() + " request canceled",
							Toast.LENGTH_LONG).show();
				
				}
			});
			
			
			
		}
			catch(Exception e) { Log.e("Notify updation", e.toString());	}
			finally { d.close(); }
			return row;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if(items == null)
				return 0;
			return items.size();
		}

	}
