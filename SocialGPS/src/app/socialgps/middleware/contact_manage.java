package app.socialgps.middleware;


import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.friend_detail_dto;
public class contact_manage  {

	DatabaseHandler d;
	String[] contactNames;
	String frd_id;
	List<friend_detail_dao> frdlist= new ArrayList<friend_detail_dao>();
	user_pass_dao upd= new user_pass_dao();;
	user_detail_dao udd= new user_detail_dao();
	friend_detail_dao frd= new friend_detail_dao(); 
	friend_detail_dto frt;
	Context c;
	public contact_manage(String frd_id1,Context x) {		//getApplicationContext()
		try {
			this.c=x;
			this.d=new DatabaseHandler(c);
			this.upd= d.check_record();
			this.frd_id= frd_id1;
			this.frt= new  friend_detail_dto(upd);
			} 
		catch (Exception e) {
			Log.e("contact_manage main", e.toString());
		}
		finally {d.close();	}
		
	}

	public String already_there()
	{
		try
		{
		String s="nothing same";						//empty msg is nothing same , so set these as default
		this.d=new DatabaseHandler(this.c);
		frd.set_user_id(upd.get_user_id());				//setting friend dao
		frdlist= d.select_all_friend_detail();	
		
		if(frdlist!=null) {
			s=check_db(frdlist);
			System.out.print("1 Friend list not empty");
		}
		if(s.equals("accepted") || s.equals("pending") )
			return s;									//return current status
		else
			return "nothing same";

		}
		catch(Exception e)
		{
			Log.d("Already there()", e.toString());
		}
		
		finally { d.close(); }
		return  "nothing same";
		}
	
	
	public int send_req()
	{
		try
		{
		int t=0;
		//String s = number.toString();
		this.d=new DatabaseHandler(this.c);
		frd.set_user_id(upd.get_user_id());
		frd.set_friend_id(this.frd_id);
		frd.set_status("pending");
	
		friend_detail_dao frd1= new friend_detail_dao();
		frd1.set_friend_id(this.frd_id);
		frd1= this.d.selectbyfrdid(frd1);
		t=frt.insert(frd);
		if(t!=105)
			if(frd1==null)
				t=this.d.insert(frd);
			else
				t=this.d.update(frd);
		
		Log.d("Request status", String.valueOf(t));
		return t;
		}
		
		catch(Exception e) { Log.d("send req func", e.toString()); return 106;	}
		finally { d.close(); }
	}
	
	public String check_db(List<friend_detail_dao> frdlist)		//comparing current friend already in db
	{
		if(frdlist!=null)
		{
		for(int t=0; t<frdlist.size();t++)
			{
				if(frdlist.get(t).get_friend_id().equals(this.frd_id))
				{
					Log.d("Friend details check db",frdlist.get(t).get_friend_id() +frdlist.get(t).get_status() );
					return frdlist.get(t).get_status();  
				}
			}
		Log.d("Friend details","Nothing same for "+this.frd_id );
		}
		return  "nothing same";
	}
	
	
	
	
	// @Override
	// public boolean onCreateOptionsMenu(Menu menu) {
	// // Inflate the menu; this adds items to the action bar if it is present.
	// getMenuInflater().inflate(R.menu.contact, menu);
	// return true;
	// }

}
