package app.socialgps.db.dto;

import java.util.List;

import android.util.Log;
import app.socialgps.db.getJson;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;

public class user_detail_dto {
	getJson gj;
	user_pass_dao upd;
	String query;
	public user_detail_dto(user_pass_dao upd1)
	{
		upd= new user_pass_dao();
		upd=upd1;
	}
	public int insert(user_detail_dao udo)
	{
		try
		{
			query= "insert into user_detail values ('"+udo.get_user_id()+"','"+udo.get_user_name()+"','"+udo.get_phone()+"','"+udo.get_email_id()+"','"+udo.get_status()+"')";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in user_detail dao insertion", e.toString());
			return 105;
		}
	}
	public int delete(user_detail_dao udo)
	{
		try
		{
			query="delete from user_detail where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in user_detail dao Deletion", e.toString());
			return 105;
		}
	}
	public int update(user_detail_dao udo)
	{
		try
		{
			query="update user_detail set user_name='"+udo.get_user_name()+"',phone='"+udo.get_phone()+"',email_id='"+udo.get_email_id()+"',status='"+udo.get_status()+"' where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd.get_user_id(),query); //aware in changing phone number also change phone no
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in user_detail dao Updation", e.toString());
			return 105;
		}
	}
	public user_detail_dao select(user_detail_dao udo)
	{
		try
		{
			query="select * from user_detail where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_user_detail_result();
		}
		catch(Exception e)
		{
			Log.e("Error in user_detail dao Selection", e.toString());
			return null;
		}
	}
	public List select_all(String c)
	{
		try
		{
			query="select * from user_detail where "+c;
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_user_detailss();
		}
		catch(Exception e)
		{
			Log.e("Error in user_detail dao All Selection", e.toString());
			return null;
		}
	}
	public int get_status()
	{
		return gj.get_res_code();
	}

}