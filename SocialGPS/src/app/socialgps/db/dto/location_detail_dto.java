package app.socialgps.db.dto;

import android.util.Log;
import app.socialgps.db.getJson;
import app.socialgps.db.dao.location_detail_dao;
import app.socialgps.db.dao.user_pass_dao;

public class location_detail_dto {
	getJson gj;
	user_pass_dao upd;
	String query;
	public location_detail_dto(user_pass_dao upd1)
	{
		upd= new user_pass_dao();
		upd=upd1;
	}
	public int insert(location_detail_dao udo)
	{
		try
		{
			query= "insert into location_detail values ('"+udo.get_user_id()+"','"+udo.get_location()+"','"+udo.get_tyme()+"')";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in location_detail dao insertion", e.toString());
			return 105;
		}
	}
	public int delete(location_detail_dao udo)
	{
		try
		{
			query="delete from location_detail where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in location_detail dao Deletion", e.toString());
			return 105;
		}
	}
	public int update(location_detail_dao udo)
	{
		try
		{
			query="update location_detail set location='"+udo.get_location()+"',time='"+udo.get_tyme()+"' where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd.get_user_id(),query); 
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in location_detail dao Updation", e.toString());
			return 105;
		}
	}
	public location_detail_dao select(location_detail_dao udo)
	{
		try
		{
			query="select * from location_detail where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_location_detail_result();
		}
		catch(Exception e)
		{
			Log.e("Error in location_detail dao Selection", e.toString());
			return null;
		}
	}
	public int get_status()
	{
		return gj.get_res_code();
	}
}