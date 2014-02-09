package app.socialgps.ui;

import java.util.ArrayList;
import java.util.Iterator;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.socialgps.middleware.UserInfo;

public class MapsFragment extends Fragment {
	
	// static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	// static final LatLng KIEL = new LatLng(53.551, 9.993);
	private GoogleMap googleMap;
	public static View v;

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
		if (googleMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			googleMap = ((SupportMapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			FragmentManager fragmentManager = getFragmentManager();
			SupportMapFragment mapFragment = (SupportMapFragment) fragmentManager
					.findFragmentById(R.id.map);
			googleMap = mapFragment.getMap();
			// Check if we were successful in obtaining the map.
			if (googleMap != null) {
				setUpMap();
			}
		}
	}

	private void setUpMap() {
		System.out.println("setup map executed");

		LatLng KODAMBAKKAM = new LatLng(13.054814, 80.226757);
		LatLng CHROMPET = new LatLng(12.945754, 80.131905);
		MarkerOptions kodambakkam = new MarkerOptions().position(
				KODAMBAKKAM).title("Balaji")
				.snippet("Kodambakkam");
		Marker mo = googleMap.addMarker(kodambakkam);
		Marker chrompet = googleMap.addMarker(new MarkerOptions()
				.position(CHROMPET)
				.title("Dinesh")
				.snippet("Chrompet")
				.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
		//icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET

		// Move the camera instantly to hamburg with a zoom of 15.
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(KODAMBAKKAM, 15));	

		// Zoom in, animating the camera.
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

		// ...
		//googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0))
			//	.title("Marker"));
	}
	private void setUpMarkers(ArrayList<UserInfo> userInfoList){
		//loop through friendslocation and assign markers and add to maps
		Iterator<UserInfo> itr = userInfoList.listIterator();
		while(itr.hasNext()){
			UserInfo uio = (UserInfo)itr.next();				
			MarkerOptions mo = new MarkerOptions().position(uio.laln)
					.title(uio.name)
					.snippet(uio.status)
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));		
			Marker m = googleMap.addMarker(mo);
			
		}
		
		
		
		
	}
	
}