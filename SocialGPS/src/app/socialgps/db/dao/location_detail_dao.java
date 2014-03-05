package app.socialgps.db.dao;

public class location_detail_dao
{
	private String user_id, location, tyme, status;
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
	public void set_status(String status)
	{
		this.status=status;
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
	public String get_status()
	{
		return this.status;
	}
	@Override
	public String toString() {
		return "location_detail_dao [user_id=" + user_id + ", location="
				+ location + ", tyme=" + tyme + ", status=" + status + "]";
	}	
	
}