package app.socialgps.db.dao;

import java.io.Serializable;

public class user_detail_dao implements Serializable
{
	private String user_id, user_name, email_id, status, dispaly_name;
	private Long phone;
	public void set_user_id(String user_id)
	{
		this.user_id=user_id;
	}
	public void set_user_name(String name)
	{
		this.user_name=name;
	}
	public void set_email_id(String eid)
	{
		this.email_id=eid;
	}
	@Override
	public String toString() {
		return "user_detail_dao [user_id=" + user_id + ", user_name="
				+ user_name + ", email_id=" + email_id + ", status=" + status
				+ ", dispaly_name=" + dispaly_name + ", phone=" + phone + "]";
	}
	public void set_status(String s)
	{
		this.status=s;
	}
	public void set_phone(Long ph)
	{
		this.phone= ph;
	}
	public void set_display_name(String s)
	{
		this.dispaly_name=s;
	}
	
	public String get_user_id()
	{
		return this.user_id;
	}
	public String get_user_name()
	{
		return this.user_name;
	}
	public String get_email_id()
	{
		return this.email_id;
	}
	public String get_status()
	{
		return this.status;
	}
	public Long get_phone()
	{
		return this.phone;
	}
	public String get_display_name()
	{
		return this.dispaly_name;
	}
	
}
