package app.socialgps.db.dto;

import android.util.Log;
import app.socialgps.db.getJson;
import app.socialgps.db.dao.user_pass_dao;

public class user_pass_dto {
	getJson gj;
	user_pass_dao upd_admin;
	String query;
	public user_pass_dto()
	{
		upd_admin= new user_pass_dao();
		upd_admin.set_user_id("Admin_dinesh_balaji");//("Admin_dinesh_balaji"); //default user id and password management
		upd_admin.set_passwd("smile_always");//("smile_always");
	}
	public int insert(user_pass_dao udo)
	{
		try
		{
			query= "insert into user_pass values ('"+udo.get_user_id()+"','"+udo.get_passwd()+"')";
			System.out.print(query);
		//	query= Mcrypt.bytesToHex(new Mcrypt().encrypt(query, upd_admin.get_passwd()));
		//	System.out.print(query+","+new Mcrypt().decrypt(query, upd_admin.get_passwd()));
			gj= new getJson(upd_admin.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in user_pass dao insertion", e.toString());
			return 105;
		}
	}
	public int delete(user_pass_dao udo)
	{
		try
		{
			query="delete from user_pass where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd_admin.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in user_pass dao Deletion", e.toString());
			return 105;
		}
	}
	public int update(user_pass_dao udo)
	{
		try
		{
			query="update user_pass set passwd='"+udo.get_passwd()+"' where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd_admin.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in user_pass dao Updation", e.toString());
			return 105;
		}
	}
	public user_pass_dao select(user_pass_dao udo)
	{
		try
		{
			query="select * from user_pass where user_id='"+udo.get_user_id()+"'";
			gj= new getJson(upd_admin.get_user_id(),query);
			System.out.print("testcode");
		//	user_pass_dao testObj = new user_pass_dao();
		//	testObj = gj.get_user_pass_result();
		//	System.out.print(gj.get_user_pass_result().get_user_id()+"in select dao");
			
			return gj.get_user_pass_result();
		}
		catch(Exception e)
		{
			Log.e("Error in user_pass dao Selection", e.toString());
			return null;
		}
	}
	
	public int get_status()
	{
		Log.d("DTO", "getting code");
		return gj.get_res_code();
	}
	
	
}