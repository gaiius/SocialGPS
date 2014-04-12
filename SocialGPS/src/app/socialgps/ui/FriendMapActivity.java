package app.socialgps.ui;

import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import app.socialgps.db.dao.gps_details;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.middleware.location_sync;

public class FriendMapActivity extends Activity {
	  private GoogleMap map;
	  List<gps_details> templist;
	  List<String> times;
		GPSTracker gps;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friend_map);
		
		Intent i = getIntent();
		System.out.println("intent created");
		String uio = (String) i.getSerializableExtra("user_detail");
		String type= i.getStringExtra("type");
		templist = location_sync.stringtoloc(uio);
		gps = new GPSTracker(getApplicationContext());
		
		System.out.println("obtained serialized object");

		Log.d("FriendShowMap", "content view set");
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
		        .getMap();
		try
		{
	if(type.equals("marker"))
	{
		String time = (String) i.getSerializableExtra("user_timing");
		times = location_sync.stringtotym(time);
		
		for(int j=0; j<templist.size(); j++)
		{
			LatLng area = new LatLng(templist.get(j).getLat(), templist.get(j).getLng());
			setMarker(area, j);
			if(j<templist.size()-1)
				map.addPolyline(new PolylineOptions()
			       .add(new LatLng(templist.get(j).getLat(), templist.get(j).getLng()), new LatLng(templist.get(j+1).getLat(), templist.get(j+1).getLng()))
			       .width(2)
			       .color(Color.BLUE));
		}
		    // Move the camera instantly to hamburg with a zoom of 15.
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(templist.get(templist.size()-1).getLat(), templist.get(templist.size()-1).getLng()), 15));
		map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
	}
	else if(type.equals("route"))
	{
		System.out.println("My : "+ new LatLng(gps.getLatitude(), gps.getLongitude()).toString());
		System.out.println("Frd :"+(new LatLng(templist.get(templist.size()-1).getLat(), templist.get(templist.size()-1).getLng())));
		
		Polyline line = map.addPolyline(new PolylineOptions()
	       .add(new LatLng(templist.get(templist.size()-1).getLat(), templist.get(templist.size()-1).getLng()), new LatLng(gps.getLatitude(), gps.getLongitude()))
	       .width(5)
	       .color(Color.BLUE));
		System.out.println("Frd :"+(new LatLng(templist.get(templist.size()-1).getLat(), templist.get(templist.size()-1).getLng())));
		 map.addMarker(new MarkerOptions().position( new LatLng(gps.getLatitude(), gps.getLongitude()))
			        .title("Your Location"));
		 map.addMarker(new MarkerOptions().position((new LatLng(templist.get(templist.size()-1).getLat(), templist.get(templist.size()-1).getLng())))
			        .title("Friend Location"));
		  
	}
		}
		catch(Exception e)
		{
			Log.e("Exception in map", e.toString());
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.friend_map, menu);
		return false;
	}

	public void setMarker(LatLng l, int i)
	{
		Log.d("FriendShowMap",l.toString());
		map.addMarker(new MarkerOptions().position(l)
		        .title(times.get(i)));
	    
	}
	
}

