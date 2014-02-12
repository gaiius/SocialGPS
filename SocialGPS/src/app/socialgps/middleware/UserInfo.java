package app.socialgps.middleware;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

public class UserInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String userId;
	public String name;
	public String phone;
	public String status;
	public LatLng laln;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
