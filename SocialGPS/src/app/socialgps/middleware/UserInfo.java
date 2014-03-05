package app.socialgps.middleware;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", name=" + name + ", status="
				+ status + ", laln=" + laln + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public LatLng getLaln() {
		return laln;
	}
	public void setLaln(LatLng laln) {
		this.laln = laln;
	}
	private String name;
	private String phone;
	private String status;
	private LatLng laln;
	/**
	 * @param args
	 */

}
