package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.friend_detail_dto;

public class FriendListCustomAdaptor extends BaseAdapter implements OnClickListener,OnCheckedChangeListener {

	/**
	 * @param args
	 */
	DatabaseHandler d;
	Context context;
	//String[] values;  
	List<user_detail_dao> values = new ArrayList<user_detail_dao>();
	friend_detail_dao frd;
	friend_detail_dto fdt;
	user_pass_dao upd;
	
	
   // private static LayoutInflater inflater = null;

	public FriendListCustomAdaptor(Context context, List values) {
		//super();
		try {
		this.context = context;
		this.values = values;
		d= new DatabaseHandler(this.context);
		this.upd= new user_pass_dao();
		this.upd=d.check_record();
		this.fdt= new friend_detail_dto(this.upd);
	
		}
		 catch(Exception e) { Log.e("main friend adapter", e.toString());	}
		finally { d.close(); }
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub					
		int pos = (Integer)v.getTag();		 
	
		Intent i = new Intent(context, FriendActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra("user_detail", values.get(pos));
		context.startActivity(i);
	
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (values==null)
			return 0;
		
		return values.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return values.get(position).get_display_name();
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		 try {
		if (convertView == null) {
		        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        convertView = inflater.inflate(R.layout.friends_fragment, null);
		    }
		 TextView friendName = (TextView) convertView.findViewById(R.id.friend_name);
		 Switch friendViewToggle = (Switch) convertView.findViewById(R.id.friend_view_toggle);
		
		 if(getCount()==1 && values.get(0).get_display_name().equals("Your contact is empty") )
			{
			 friendName.setText("  Your friend list is empty");
				System.out.println("  Doing for empty");
				friendName.setTextSize(15);
				friendName.setEnabled(false);
				friendViewToggle.setVisibility(View.GONE);
			}
		  else
		 {
		 friendName.setText(values.get(position).get_display_name());
		 friendName.setTag(new Integer(position));
		 friendViewToggle.setTag(new Integer(position));
		 friendName.setOnClickListener(this);	
		 }
		 frd= new friend_detail_dao();
		 d= new DatabaseHandler(this.context);
		 frd.set_friend_id(values.get(position).get_user_id());
			
		 frd= d.selectbyfrdid(frd);
			
		 if(frd!=null)
		 {
			 if(frd.get_visible()==null || frd.get_visible().equals("true"))	{
				friendViewToggle.setChecked(false); // not blocked 
				
		 }else
			 friendViewToggle.setChecked(true); //blocked
			 System.out.println("Friend blocked"+ frd.get_visible());
		 }
		 
			friendViewToggle.setOnCheckedChangeListener(this);
			 if(values.get(position).get_display_name().equals("Tap for more detail") )
			 {
				 friendName.setText("     Switch button to invisible from your friend");
				 friendName.setTextSize(15);
				friendName.setEnabled(false);
				friendViewToggle.setVisibility(View.GONE);
			 }
			  
		 }
		 catch(Exception e) { Log.e("getview friend adapter", e.toString());	}
			finally { d.close(); }
		 
		return convertView;
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		try {
		int pos = (Integer)buttonView.getTag();
		System.out.println("in block ");
		
		 d= new DatabaseHandler(this.context);
			
		if(isChecked)									// if enable toggle button make friend block
		{
			if(testnet())
			{
			frd=  new friend_detail_dao();
			frd.set_friend_id(values.get(pos).get_user_id());		//get user who frd wid you
			frd.set_visible("false");
			if(d.selectbyfrdid(frd)==null)
				{
					d.insert(frd);						//local db insert 
					Log.d("Frd local block list added " , frd.get_friend_id()+ " "+ frd.get_status());
					frd=  new friend_detail_dao();						//for block list interchange user id and friend id and put block
					frd.set_user_id(values.get(pos).get_user_id());		//current friend id
					frd.set_friend_id(this.upd.get_user_id());	// user id
					frd.set_status("blocked");		
					this.fdt.insert(frd);			
				}
		
			else
				{
					d.update(frd);					//local db update
					Log.d("Frd local block list updated " , frd.get_friend_id()+ " "+ frd.get_status()+ " ");
					frd=  new friend_detail_dao();						//for block list interchange user id and friend id and put block
					frd.set_user_id(values.get(pos).get_user_id());		//current friend id
					frd.set_friend_id(this.upd.get_user_id());	// user id
					frd.set_status("blocked");		
					this.fdt.update(frd);			
				}
			
								//insert into online DB	
			Toast.makeText(buttonView.getContext(), values.get(pos).get_display_name()+" is Blocked, Now he can't see you",
		               Toast.LENGTH_SHORT).show();
		
			Log.d("Online frd id blocked" , frd.get_user_id()+" "+ frd.get_friend_id()+" "+ frd.get_status());
			}
			else
				{
				Toast.makeText(buttonView.getContext(),
						"No internet connection, Can't make your request",
						Toast.LENGTH_LONG).show();
				buttonView.setChecked(false);
				}
			
			
		}
		else
		{
			if(testnet())
			{
			frd=  new friend_detail_dao();
			frd.set_friend_id(values.get(pos).get_user_id());		//get user who frd wid you
			frd.set_visible(null);
			if(d.selectbyfrdid(frd)==null)
				{
					d.delete(frd);						//local db insert 
					Log.d("Frd local block list deleted " , frd.get_friend_id()+ " "+ frd.get_status()+ " "+ frd.get_visible());
				}
			else
				{
					frd.set_visible("true");
					d.update(frd);					//local db update
					Log.d("Frd local block list updated " , frd.get_friend_id()+ " "+ frd.get_status()+ " "+ frd.get_visible());
				}
			
			frd=  new friend_detail_dao();						//for block list interchange user id and friend id and put block
			frd.set_user_id(values.get(pos).get_user_id());		//current friend id
			frd.set_friend_id(this.upd.get_user_id());			// user id
			frd.set_status("accepted");		
			Log.d("Frd online blocking list updated " , frd.get_friend_id()+ " "+ frd.get_status());
			
			this.fdt.update(frd);								//delete into online DB
			Toast.makeText(buttonView.getContext(), values.get(pos).get_display_name()+"  is unblocked, Now he can see you",
		               Toast.LENGTH_SHORT).show();
			Log.d("Online frd id released" , frd.get_user_id()+" "+ frd.get_friend_id()+" "+ frd.get_status());
		}
		
		else
			{
			Toast.makeText(buttonView.getContext(),
					"No internet connection, Can't make your request",
					Toast.LENGTH_LONG).show();
			buttonView.setChecked(true);
			}
		}
		
		}catch(Exception e) { Log.e("onClick friend adapter", e.toString());	}
		finally { d.close(); }
		}
	
	
	}
	

