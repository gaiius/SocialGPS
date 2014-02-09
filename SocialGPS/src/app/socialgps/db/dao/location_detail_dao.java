package app.socialgps.db.dao;

public class location_detail_dao
{
	private String user_id, location, tyme;
	public void set_user_id(String uid)
	{
		this.user_id=uid;
	}
	public void set_location(String loc)
	{
		this.location=loc;
	}
	public void set_tyme(String tym)
	{
		this.tyme=tym;
	}
	public String get_location()
	{
		return this.location;
	}
	public String get_user_id()
	{
		return this.user_id;
	}
	public String get_tyme()
	{
		return this.tyme;
	}
}