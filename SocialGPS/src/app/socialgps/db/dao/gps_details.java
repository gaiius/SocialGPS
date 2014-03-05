package app.socialgps.db.dao;

import java.io.Serializable;

public class gps_details implements Serializable {
	double lng, lat;
	String address;
	public double getLng()
	{
		return this.lng;
	}
	public double getLat()
	{
		return this.lat;
	}
	public String getAddress()
	{
		return this.getAddress();
	}

	@Override
	public String toString() {
		return "gps_details [lng=" + lng + ", lat=" + lat + ", address="
				+ address + "]";
	}
	public void setLng(double l)
	{
		this.lng=l;
	}
	public void setLat(double l)
	{
		this.lat= l;
	}
	public void setAddress(String s)
	{
		this.address= s;
	}
}
