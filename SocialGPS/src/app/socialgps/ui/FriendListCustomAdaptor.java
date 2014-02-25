package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
		Toast.makeText(v.getContext(), "Friend position is " + pos,
	               Toast.LENGTH_SHORT).show();

		
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
		 friendName.setText(values.get(position).get_display_name());
		 friendName.setTag(new Integer(position));
		 friendViewToggle.setTag(new Integer(position));
		 friendName.setOnClickListener(this);	
		 
		 frd= new friend_detail_dao();
		 d= new DatabaseHandler(this.context);
		 frd.set_friend_id(values.get(position).get_user_id());
			
		 frd= d.selectbyfrdid(frd);
			
		 if(frd!=null)
		 {
			 if(frd.get_visible()!=null)	{
				friendViewToggle.setChecked(true);//blocked 
				System.out.println("Friend blocked"+ frd.get_visible());
		 }else
			 friendViewToggle.setChecked(false);
		 }
		 
			friendViewToggle.setOnCheckedChangeListener(this);
		 
		 }
		 catch(Exception e) { Log.e("getview friend adapter", e.toString());	}
			finally { d.close(); }
		 
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		try {
		int pos = (Integer)buttonView.getTag();
		
		 d= new DatabaseHandler(this.context);
		
		if(isChecked)									// if enable toggle button make friend block
		{
			frd=  new friend_detail_dao();
			frd.set_friend_id(values.get(pos).get_user_id());		//get user who frd wid you
			frd.set_visible("false");
			if(d.selectbyfrdid(frd)==null)
				{
					d.insert(frd);						//local db insert 
					Log.d("Frd local block list added " , frd.get_friend_id()+ " "+ frd.get_status());
				}
			else
				{
					d.update(frd);					//local db update
					Log.d("Frd local block list updated " , frd.get_friend_id()+ " "+ frd.get_status()+ " ");
				}
			
			frd=  new friend_detail_dao();						//for block list interchange user id and friend id and put block
			frd.set_user_id(values.get(pos).get_user_id());		//current friend id
			frd.set_friend_id(this.upd.get_user_id());	// user id
			frd.set_status("blocked");		
			this.fdt.insert(frd);								//insert into online DB	
			Toast.makeText(buttonView.getContext(), values.get(pos).get_display_name()+" Blocked",
		               Toast.LENGTH_SHORT).show();
		
			Log.d("Online frd id blocked" , frd.get_user_id()+" "+ frd.get_friend_id()+" "+ frd.get_status());
		}
		else
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
			frd.set_status("blocked");		
			this.fdt.delete(frd);								//delete into online DB
			Toast.makeText(buttonView.getContext(), values.get(pos).get_display_name()+" Unblocked",
		               Toast.LENGTH_SHORT).show();
			Log.d("Online frd id released" , frd.get_user_id()+" "+ frd.get_friend_id()+" "+ frd.get_status());
		}
		
		
		}catch(Exception e) { Log.e("onClick friend adapter", e.toString());	}
		finally { d.close(); }
		}
	
	
	}
	

