package app.socialgps.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.friend_detail_dao;
import app.socialgps.db.dao.gps_details;
import app.socialgps.db.dao.location_detail_dao;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.UserInfo;
import app.socialgps.middleware.location_sync;

public class MapsFragment extends Fragment {

	// static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	// static final LatLng KIEL = new LatLng(53.551, 9.993);
	private GoogleMap googleMap;
	public static View v;
	user_detail_dao udd = new user_detail_dao();
	DatabaseHandler d;
	List<gps_details> templist;
	List<String> timelst;
	location_detail_dao l;
	LatLng ll;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (v != null) {
			ViewGroup parent = (ViewGroup) v.getParent();
			if (parent != null)
				parent.removeView(v);
		}
		try {
			v = inflater.inflate(R.layout.map_fragment, container, false);
			setUpMapIfNeeded();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		googleMap = ((SupportMapFragment) getFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		setUpMapIfNeeded();
	}

	private void setUpMapIfNeeded() {
		// Do a null check to confirm that we have not already instantiated the
		// map.
		List<UserInfo> mapdetails = new ArrayList<UserInfo>();
		if (googleMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			googleMap = ((SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
//			FragmentManager fragmentManager = getFragmentManager();
//			SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
//					.findFragmentById(R.id.map);
//			googleMap = mapFragment.getMap();
			
		}
			// Check if we were successful in obtaining the map.
			if (googleMap != null) {
				mapdetails = createDetails();
				if (mapdetails != null && mapdetails.size() > 0)
					setUpMarkers(mapdetails);
			}
		
	}

	private void setUpMap() {
		System.out.println("setup map executed");

		LatLng KODAMBAKKAM = new LatLng(13.054814, 80.226757);
		LatLng CHROMPET = new LatLng(12.945754, 80.131905);
		MarkerOptions kodambakkam = new MarkerOptions().position(KODAMBAKKAM)
				.title("Balaji").snippet("Kodambakkam");
		Marker mo = googleMap.addMarker(kodambakkam);
		Marker chrompet = googleMap.addMarker(new MarkerOptions()
				.position(CHROMPET)
				.title("Dinesh")
				.snippet("Chrompet")
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		// icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET

		// Move the camera instantly to hamburg with a zoom of 15.
		googleMap
				.moveCamera(CameraUpdateFactory.newLatLngZoom(KODAMBAKKAM, 15));

		// Zoom in, animating the camera.
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		// ...
		// googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0))
		// .title("Marker"));
	}

	private void setUpMarkers(List<UserInfo> userInfoList) {
		// loop through friendslocation and assign markers and add to maps
		Iterator<UserInfo> itr = userInfoList.listIterator();
		Log.d("Map size", String.valueOf(userInfoList.size()));
		while (itr.hasNext()) {
			UserInfo uio = (UserInfo) itr.next();
			if(ll==null && (ll.latitude==0.0 || ll.longitude==0.0) )
			{
				System.out.println("gps is turn on dialogue");
				new GPSTracker(getActivity().getApplicationContext()).showSettingsAlert();
				continue;
			}
			System.out.println("gps is turn on menu finished"+ll);
			
			MarkerOptions mo = new MarkerOptions()
					.position(uio.getLaln())
					.title(uio.getName())
					.snippet(uio.getStatus())
					.icon(BitmapDescriptorFactory
							.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
			Marker m = googleMap.addMarker(mo);
			if (ll != null)
				googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
			googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

		}
	}

	private List<UserInfo> createDetails() {
		try {
			System.out.println("Creating mapdetails");

			List<UserInfo> mapdetails = new ArrayList<UserInfo>();
			d = new DatabaseHandler(getActivity().getApplicationContext());
			List<location_detail_dao> loclist = new ArrayList<location_detail_dao>();
			loclist = d.select_all_location_detail();
			if (loclist != null) {
				for (int i = 0; i < loclist.size(); i++) {
					l = loclist.get(i);
					System.out.println("map 1");
	 				friend_detail_dao frd= new friend_detail_dao();
					frd.set_friend_id(l.get_user_id());
					System.out.println(l.get_user_id()+" "+frd);
					System.out.println("map 2 ");
 					frd= d.selectbyfrdid(frd);
 					System.out.println(frd +" "+ l);
					System.out.println("map 3 ");
					if (frd==null || ((l.get_status() == null || l.get_status().equals("off")) && (frd.get_status()!=null) && frd.get_status().equals("accepted") ))			{
 						System.out.println("map 4 ");
						UserInfo ui = new UserInfo();
						templist = new ArrayList<gps_details>();
						timelst = new ArrayList<String>();
						templist = location_sync.stringtoloc(l.get_location());
						timelst = location_sync.stringtotym(l.get_tyme());
						LatLng area = new LatLng(templist.get(
								templist.size() - 1).getLat(), templist.get(
								templist.size() - 1).getLng());
						ui.setLaln(area);
						user_detail_dao udd = new user_detail_dao();
						udd.set_user_id(loclist.get(i).get_user_id());
						udd = d.select(udd);
						if (udd != null) {
							ui.setName(udd.get_display_name());
							ui.setStatus(udd.get_status());
							mapdetails.add(ui);
							if (d.check_record().get_user_id()
									.equals(udd.get_user_id())) // current user
								ll = area;
						//	Log.d("MAP details", ui.toString());
						}
					} else
						return null;
				}
				return mapdetails;
			}
		} catch (Exception e) {
			Log.e("Map fragment createdetails()", e.toString());
			return null;
		} finally {
			d.close();
		}
		return null;
	}
}