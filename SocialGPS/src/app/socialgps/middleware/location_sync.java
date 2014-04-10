package app.socialgps.middleware;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.android.gms.location.LocationStatusCodes;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.gps_details;
import app.socialgps.db.dao.location_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.friend_detail_dto;
import app.socialgps.db.dto.location_detail_dto;
import app.socialgps.db.dto.user_detail_dto;
import app.socialgps.ui.GPSTracker;

public class location_sync {
	DatabaseHandler d;
	List<gps_details> templist;
	List<String> timelst;
	List<location_detail_dao> lddlist;
	SimpleDateFormat sdf = new SimpleDateFormat("dd:MMM:yyyy hh:mm:ss a");
	Context c;
	location_detail_dao ldd;
	location_detail_dto ldt;
	user_pass_dao upd;
	gps_details gd;
	int n;
	double lat;
	double lon;
	GPSTracker gps;

	public location_sync(Context c) {
		try {
			d = new DatabaseHandler(c);
			upd = new user_pass_dao();
			upd = d.check_record();
			ldt = new location_detail_dto(upd);
			gps = new GPSTracker(c);
			lat = gps.getLatitude();
			lon = gps.getLongitude();
			this.c = c;
		} catch (Exception e) {
			Log.e("Exception Location sync construtor", e.toString());
		} finally {
			d.close();
		}
	}

	public location_detail_dao getMine() {
		try {
			ldd = new location_detail_dao();
			ldd.set_user_id(upd.get_user_id());
			return ldt.select(ldd);
		} catch (Exception e) {
			Log.e("Exception Location sync getMine", e.toString());
			return null;
		}
	}

	public void insert() {
		try {
			d = new DatabaseHandler(this.c);
			ldd = new location_detail_dao();
			ldd.set_user_id(upd.get_user_id());
			ldd = d.select(ldd);
			if (ldd == null) { // loc db is empty
				Log.d("Location", "Local DB own id empty");
				ldd = getMine(); // get from online frd location
				if (ldd == null) // none in online
				{
					Log.d("Location", "ol DB own id empty");
					templist = new ArrayList<gps_details>();
					gd = new gps_details();
					gd.setLat(lat);
					gd.setLng(lon);
					Log.d("Location", " Current location: " + gd);
					templist.add(gd);

					timelst = new ArrayList<String>();
					String datetym = sdf.format(new Date());
					timelst.add(datetym);

					ldd = new location_detail_dao(); // storin new record in
														// local
														// and online
					ldd.set_user_id(upd.get_user_id());
					ldd.set_location(loctostring(templist));
					ldd.set_tyme(tymtostring(timelst));
					ldd.set_status("on");
	
					ldt.insert(ldd);
					Log.d("Location", " ol Record first copie inserted "
							+ loctostring(templist));
					d.insert(ldd);
					Log.d("Location", " loc Record first copie inserted  "
							+ loctostring(templist));
				} else
					d.insert(ldd);

			} else {
				Log.d("Location", "IN Local DB ");
				templist = new ArrayList<gps_details>();
				templist = stringtoloc(ldd.get_location());
				timelst = stringtotym(ldd.get_tyme());
				n = templist.size() - 1; // current entry in array
				float dis = distFrom(templist.get(n).getLat(), templist.get(n)
						.getLng(), lat, lon);
				gd = new gps_details();
				gd.setLat(lat);
				gd.setLng(lon);
				Log.d("Location", "size" + n + " Current location:  " + gd);

				if (n <= 3) {
					Log.d("Location " + n, "Size is less than limit");
					if (dis > 200) {
						Log.d("Location", "Distance hi ");
						templist.add(n + 1, gd);
						// added to next location
						timelst.add(n + 1, sdf.format(new Date()));

						// update both time and location
						ldd = new location_detail_dao();
						ldd.set_user_id(upd.get_user_id());
						ldd.set_location(loctostring(templist));
						ldd.set_tyme(tymtostring(timelst));
						ldt.update(ldd);
						d.update(ldd);
					} else {
						Log.d("Location " + n, "changes in same row change");
						Log.d("Location", "Distance not hi ");
						templist.remove(n);
						timelst.remove(n);
						
						templist.add(n, gd);
						timelst.add(n, sdf.format(new Date()));
						// update only time for previous checked location

						ldd = new location_detail_dao();
						ldd.set_user_id(upd.get_user_id());
						ldd.set_location(loctostring(templist));
						ldd.set_tyme(tymtostring(timelst));
						
						
						
						
						ldt.update(ldd);
						d.update(ldd);

					}
				} else if (n >= 4) {
					Log.d("Location " + n, " Size is more than limit");
					if (dis > 200) {

						Log.d("Location " + templist.size(), "Distance hi ");
						templist.remove(0); // now size become 3
						timelst.remove(0);

						templist.add(n, gd); // here n is 4.
						timelst.add(n, sdf.format(new Date()));
						Log.d("Location " + templist.size(), "Replacing");

						// update both time and location
						ldd = new location_detail_dao();
						ldd.set_user_id(upd.get_user_id());
						ldd.set_location(loctostring(templist));
						ldd.set_tyme(tymtostring(timelst));
						ldt.update(ldd);
						d.update(ldd);
					} else {
						Log.d("Location " + templist.size(), "Distance not hi ");
						
						Log.d("Location " + n, "changes in same row change");
						
						templist.remove(n);
						timelst.remove(n); // n should be 4
						
						templist.add(n, gd);
						timelst.add(n, sdf.format(new Date()));

						// update both time and location
						ldd = new location_detail_dao();
						ldd.set_user_id(upd.get_user_id());
						ldd.set_location(loctostring(templist));
						ldd.set_tyme(tymtostring(timelst));
						ldt.update(ldd);
						d.update(ldd);
					}
				}
			}
		} catch (Exception e) {
			Log.e("Exception Location sync insert", e.toString());
		} finally {
			d.close();
		}
	}

	// static functions

	public static float distFrom(double lat1, double lng1, double lat2,
			double lng2) { // returns distance between two points in meters
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;
		Log.d("Location", "latlng, latlng"+lat1+ lng1+" "+lat2+ lng2+" Distance" + (dist * meterConversion));
		return (float) (dist * meterConversion);
	}

	public static String loctostring(List<gps_details> l) { // Location object
															// format to DB
															// format
		StringBuffer s = new StringBuffer();
		try {
			for (int i = 0; i < 5; i++) {
				if (i < l.size())
					s.append(String.valueOf(l.get(i).getLat() + "_"
							+ l.get(i).getLng()));
				else
					s.append("0");

				if (i != 4)
					s.append(",");
			}
			Log.d("Location", "loctostring" + s.toString());
		} catch (Exception e) {
			Log.e("Exception Location sync loctostring", e.toString());
		}
		return s.toString();
	}

	public static List<gps_details> stringtoloc(String t) { // location DB
															// format to list
															// object format
		List<gps_details> temp = new ArrayList<gps_details>();
		try {
			String[] tem = t.split(",", 5);
			for (int i = 0; i < 5; i++) {
				if (!tem[i].equals("0")) {
					gps_details gd = new gps_details();

					Log.d("Location", "stringtoloc get" + t);
					String inner[] = tem[i].split("_");
					gd.setLat(Double.parseDouble(inner[0]));
					gd.setLng(Double.parseDouble(inner[1]));
					Log.d("Location", "stringtoloc" + gd);
					temp.add(gd);
				}
			}
		} catch (Exception e) {
			Log.e("Exception Location sync stringtoloc", e.toString());
		}
		return temp;
	}

	public static String tymtostring(List<String> l) { // time object format to
														// DB format
		StringBuffer s = new StringBuffer();
		try {
			for (int i = 0; i < 5; i++) {
				if (i < l.size())
					s.append(l.get(i));
				else
					s.append("0");

				if (i != 4)
					s.append(",");
			}
			Log.d("Location", "tymtostring" + s.toString());
		} catch (Exception e) {
			Log.e("Exception Location sync tymtostring", e.toString());
		}
		return s.toString();
	}

	public static List<String> stringtotym(String t) { // Time DB format to list
														// object format
		List<String> temp = new ArrayList<String>();
		try {
			String[] tem = t.split(",", 5);
			for (int i = 0; i < 5; i++) {
				if (!tem[i].equals("0")) {
					temp.add(tem[i]);
					Log.d("Location", "stringtotym" + tem[i]);
				}
			}
		} catch (Exception e) {
			Log.e("Exception Location sync stringtotym", e.toString());
		}
		return temp;
	}

	// Refreshing unit

	public void refresh() {
		try {
			List<String> friendid = new ArrayList<String>();
			lddlist = new ArrayList<location_detail_dao>();
			Log.d("location", "Location refresh started");
			friendid = get_list_frdid();
			if (!friendid.isEmpty()) {
				String s = convert_where(friendid);
				lddlist = ldt.select_all(s);
				d = new DatabaseHandler(this.c);
				for (int i = 0; i < lddlist.size(); i++) {
					if (d.select(lddlist.get(i)) == null) // frd detail not in
															// db
					{
						d.insert(lddlist.get(i));
						Log.d("location refresh- inserted", lddlist.get(i).toString());
					}
					else
					{
						Log.d("location refresh- updated", lddlist.get(i).toString());
						d.update(lddlist.get(i));
					}
					
				}
			}
		} catch (Exception e) {
			Log.d("Already there()", e.toString());
		}

		finally {
			d.close();
		}
	}

	public List<String> get_list_frdid() {
		try {
			d = new DatabaseHandler(this.c);
			List<String> friendid = new ArrayList<String>();
			List<friend_detail_dao> friends = new ArrayList<friend_detail_dao>();
			friends = d.select_all_friend_detail();
			Log.d("location", "getting frd list");

			if (friends != null)
				for (int i = 0; i < friends.size(); i++) {

					if (friends.get(i).get_status() != null
							&& friends.get(i).get_status().equals("accepted"))

					{
						Log.d("location", friends.get(i).get_friend_id());

						user_detail_dao udd = new user_detail_dao();
						udd.set_user_id(friends.get(i).get_friend_id());
						// get display name from user detail table
						udd = d.select(udd);
						// phone contact delete but net contacts is still there
						if (udd != null) {
							friendid.add(friends.get(i).get_friend_id());
							Log.d("location", friends.get(i).get_friend_id()
									+ friends.get(i).get_status());

						}
					}

				}
			return friendid;
		} catch (Exception e) {
			Log.d("location sync get_list_frdid exception", e.toString());
			return null;
		} finally {
			d.close();
		}
	}

	public String convert_where(List<String> udd) {
		StringBuffer s = new StringBuffer();

		try {
			for (int i = 0; i < udd.size(); i++) {
				s.append("user_id='" + udd.get(i).toString() + "'");
				Log.d("Where", s.toString());
				if (i != udd.size() - 1)
					s.append(" OR ");
			}
		} catch (Exception e) {
			Log.e("where in location()", e.toString());
		}

		Log.d("Final Where", s.toString());
		return s.toString();
	}
}
