package app.socialgps.db.dto;

import java.util.List;

import android.util.Log;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.getJson;
import app.socialgps.db.dao.user_pass_dao;

public class friend_detail_dto  {
	getJson gj;
	user_pass_dao upd;
	String query;
	public friend_detail_dto(user_pass_dao upd1)
	{
		upd= new user_pass_dao();
		upd=upd1;
	}
	public int insert(friend_detail_dao udo)
	{
		try
		{
			query= "insert into friend_detail values ('"+udo.get_user_id()+"','"+udo.get_friend_id()+"','"+udo.get_status()+"')";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in friend_detail dao insertion", e.toString());
			return 105;
		}
	}
	public int delete(friend_detail_dao udo)
	{
		try
		{
			System.out.println("Status : "+udo.get_status());
			if(udo.get_status()!=null)
				query="delete from friend_detail where user_id='"+udo.get_user_id()+"' and friend_id='"+udo.get_friend_id()+"' and status='"+udo.get_status()+"'";
			else
				query="delete from friend_detail where user_id='"+udo.get_user_id()+"' and friend_id='"+udo.get_friend_id()+"'";
			gj= new getJson(upd.get_user_id(),query);
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in friend_detail dao Deletion", e.toString());
			return 105;
		}
	}
	public int update(friend_detail_dao udo)
	{
		try
		{
			query="update friend_detail set status='"+udo.get_status()+"' where user_id='"+udo.get_user_id()+"' and friend_id='"+udo.get_friend_id()+"'";
			System.out.print(query);
			
			gj= new getJson(upd.get_user_id(),query); 
			return gj.get_res_code();
		}
		catch(Exception e)
		{
			Log.e("Error in friend_detail dao Updation", e.toString());
			return 105;
		}
	}
	public List<friend_detail_dao> select(friend_detail_dao udo)
	{
		try
		{
			query="select * from friend_detail where user_id='"+udo.get_user_id()+"'";  // and friend_id='"+udo.get_friend_id()+"'";
			System.out.print(query);
				gj= new getJson(upd.get_user_id(),query);
			return gj.get_friend_detail_result();
		}
		catch(Exception e)
		{
			Log.e("Error in friend_detail dao Selection", e.toString());
			return null;
		}
	}
	
	public List<friend_detail_dao> selectbyfrdid(friend_detail_dao udo) //check user friend with who and status
	{
		try
		{	
			if(udo.get_status()==null)
				query="select * from friend_detail where friend_id='"+udo.get_user_id()+"'";  // and friend_id='"+udo.get_friend_id()+"'";
			else
				query="select * from friend_detail where friend_id='"+udo.get_user_id()+"' and status='"+udo.get_status()+"'";
			System.out.print(query);
				gj= new getJson(upd.get_user_id(),query);
			return gj.get_friend_detail_result();
		}
		catch(Exception e)
		{
			Log.e("Error in friend_detail dao Frd_id_Selection", e.toString());
			return null;
		}
	}
	public int get_status()
	{
		return gj.get_res_code();
	}
}