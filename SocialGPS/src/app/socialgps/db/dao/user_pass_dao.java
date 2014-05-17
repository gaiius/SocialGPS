package app.socialgps.db.dao;

public class user_pass_dao
{
	@Override
	public String toString() {
		return "user_pass_dao [user_id=" + user_id + ", passwd=" + passwd + "]";
	}
	private String user_id, passwd;
	public void set_user_id(String uid)
	{
		user_id=uid;
	}
	public void set_passwd(String pwd)
	{
		this.passwd=pwd;
	}
	public String get_user_id()
	{
		return this.user_id;
	}
	public String get_passwd()
	{
		return this.passwd;
	}
}


