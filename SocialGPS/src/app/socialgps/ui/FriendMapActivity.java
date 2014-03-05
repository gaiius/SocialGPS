package app.socialgps.ui;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import app.socialgps.db.dao.gps_details;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.location_sync;

public class FriendMapActivity extends Activity {
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	  static final LatLng KIEL = new LatLng(53.551, 9.993);
	  private GoogleMap map;
	  List<gps_details> templist;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_map);
		
		Intent i = getIntent();
		System.out.println("intent created");
		String uio = (String) i.getSerializableExtra("user_detail");
		templist = location_sync.stringtoloc(uio);
		
		System.out.println("obtained serialized object");

		Log.d("FriendShowMap", "content view set");
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
	
		for(int j=0; j<templist.size(); j++)
		{
			LatLng area = new LatLng(templist.get(j).getLat(), templist.get(j).getLng());
			setMarker(area, j);
		}
		   
		    // Move the camera instantly to hamburg with a zoom of 15.
		    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(templist.get(templist.size()-1).getLat(), templist.get(templist.size()-1).getLng()), 15));

		    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_map, menu);

		return true;
	}

	public void setMarker(LatLng l, int i)
	{
		Log.d("FriendShowMap",l.toString());
		
	    Marker hamburg = map.addMarker(new MarkerOptions().position(l)
		        .title("Location"+i));
	}
}
