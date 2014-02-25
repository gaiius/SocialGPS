package app.socialgps.db.dao;

public class friend_detail_dao
{
	private String user_id, friend_id, status, visible, notify;
	public void set_user_id(String uid)
	{
		this.user_id=uid;
	}
	public void set_friend_id(String fid)
	{
		this.friend_id=fid;
	}
	public void set_status(String status)
	{
		this.status=status;
	}
	public void set_visible(String visible)
	{
		this.visible=visible;
	}
	public void set_notify(String notify)
	{
		this.notify=notify;
	}
	public String get_user_id()
	{
		return this.user_id;
	}
	public String get_friend_id()
	{
		return this.friend_id;
	}
	public String get_status()
	{
		return this.status;
	}	
	public String get_visible()
	{
		return this.visible;
	}
	public String get_notify()
	{
		return this.notify;
	}
}