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

public class friend_sync {
	
	DatabaseHandler d;
	String[] frdnames;
	String frd_id;
	List<friend_detail_dao> olfrdlist= new ArrayList<friend_detail_dao>();
	List<friend_detail_dao> locfrdlist= new ArrayList<friend_detail_dao>();
	Context c;
	user_detail_dao udd= new user_detail_dao();
	friend_detail_dao frd; 
	friend_detail_dto frt;

	public friend_sync(Context x)
	{
		try {
			this.c= x;
			} 
		catch (Exception e) {
			Log.e("friend_sync main", e.toString());
		}
		
	}
	
	public int sync_ol_sql()
	{
		int l=0;
		try	{
		frd= new friend_detail_dao(); 
		user_pass_dao upd= new user_pass_dao();;
		d=new DatabaseHandler(this.c);
		upd= d.check_record();
		this.frt= new  friend_detail_dto(upd);
		frd.set_user_id(upd.get_user_id());
		olfrdlist= frt.select(frd);
		
		//locfrdlist= d.select(frd);
		
		if(olfrdlist!=null)											//get all online frds to local friends
			for(int i=0;i<olfrdlist.size();i++ )
			{
				if(d.selectbyfrdid(olfrdlist.get(i))==null)
				{
					d.insert(olfrdlist.get(i));
					Log.d("Frd local sync added "+i, olfrdlist.get(i).get_friend_id()+ " "+olfrdlist.get(i).get_status());
				}
				else
				{
					d.update(olfrdlist.get(i));
					Log.d("Frd local sync updated " +i, olfrdlist.get(i).get_friend_id()+ " "+olfrdlist.get(i).get_status());
				}
				l++;
			}
		
		frd= new friend_detail_dao(); 
		frd.set_user_id(upd.get_user_id());
		frd.set_status("blocked");
		olfrdlist= frt.selectbyfrdid(frd);
		if(olfrdlist!=null)											//get all frds, who all are friend with me 
			for(int i=0;i<olfrdlist.size();i++ )
			{
				frd= new friend_detail_dao(); 
				if(olfrdlist.get(i).get_status().equals("blocked"))
				{
				frd.set_friend_id(olfrdlist.get(i).get_user_id());		//get user who frd wid you
				frd.set_visible("false");
				
				if(d.selectbyfrdid(frd)==null)
				{
					d.insert(frd);
					Log.d("Frd local block list added " +i, frd.get_friend_id()+ " "+ frd.get_status()+ " "+ frd.get_visible());
				}
				else
				{
					d.update(frd);
					Log.d("Frd local block list updated " +i, frd.get_friend_id()+ " "+ frd.get_status()+ " "+ frd.get_visible());
				}
		
				}
			}
		
		
		}
		catch(Exception e) { Log.e("Sync ol sqllite frd list", e.toString());	}
		finally { d.close(); }
		return l;
		}
	}


	