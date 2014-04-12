package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
		
		 public boolean testnet(){
		        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		          if (connectivity != null) 
		          {
		              NetworkInfo[] info = connectivity.getAllNetworkInfo();
		              if (info != null) 
		                  for (int i = 0; i < info.length; i++) 
		                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
		                      {
		                          return true;
		                      }
		 
		          }
		          return false;
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
			if(getCount()==1 && items.get(0).get_display_name().equals("Your contact is empty") )
			{
				notificationName.setText("No new notification");
				System.out.println("Doing for empty");
			//	notificationName.setTextSize(30);
			
				acceptButton.setVisibility(View.GONE);
				denyButton.setVisibility(View.GONE);
				return row;
			}
			notificationName.setText(items.get(position).get_display_name());
			acceptButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//adding friend
					if(testnet())
					{
					denyButton.setEnabled(false);
					// making another user as frd
					frd= new friend_detail_dao(); 	
					frd.set_user_id(upd.get_user_id());
					frd.set_friend_id(items.get(position).get_user_id());
					frd.set_status("accepted");
					cm= new contact_manage(items.get(position).get_user_id(),context);
					Log.d("already there", cm.already_there());
					int t=0, t2=0;
					friend_detail_dao frd1= new friend_detail_dao();
					frd1.set_friend_id(items.get(position).get_user_id());
					frd1= d.selectbyfrdid(frd1);
					System.out.println(frd1.get_status());
					
					if(frd1.get_status()== null || !frd1.get_status().equals("pending")) //req recvd have to update
					{
						t2=frt.insert(frd);
					}
					else if(t2!=105)
					{
						t2=frt.update(frd);
					}
					
					System.out.println("user pass :"+upd.get_user_id());
					frd.set_user_id(items.get(position).get_user_id());
					frd.set_friend_id(upd.get_user_id());
					frd.set_status("accepted");
					t=frt.update(frd);

				if(t!=105 && t2!=105)
				{
					frd= new friend_detail_dao(); 	
					frd.set_friend_id(items.get(position).get_user_id());
					frd.set_notify("accepted");
					frd.set_status("accepted");
					d.update(frd);
					System.out.println("onclick Exit");
					Toast.makeText(context,
							items.get(position).get_user_id()+" request accepted",
							Toast.LENGTH_LONG).show();
				}
				else
					Toast.makeText(context,
							"Can't Make request, Try Again",
							Toast.LENGTH_LONG).show();
			
				}
			
				else
					Toast.makeText(context,
							"No internet connection, Can't make your request",
							Toast.LENGTH_LONG).show();
				}
			});
			
			denyButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int t=0;
					if(testnet())
					{
					
					frd.set_user_id(items.get(position).get_user_id());
					frd.set_friend_id(upd.get_user_id());
					frd.set_status("pending");
					t=frt.delete(frd);
					System.out.println("onclick Exit");
					if(t!=105)
					{
					System.out.println("onclick entered");
					frd= new friend_detail_dao(); 
					frd.set_friend_id(items.get(position).get_user_id());
					System.out.println("user pass :"+upd.get_user_id());
					frd.set_notify("null");
					d.update(frd);
					Toast.makeText(context,
							items.get(position).get_user_id() + " request canceled",
							Toast.LENGTH_LONG).show();
					}
					else
						Toast.makeText(context,
								"Can't cancel the request, Try Again",
								Toast.LENGTH_LONG).show();
					}
					
					else
						Toast.makeText(context,
								"No internet connection, Can't make your request",
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
