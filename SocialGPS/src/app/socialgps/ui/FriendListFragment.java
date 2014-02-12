package app.socialgps.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import app.socialgps.middleware.UserInfo;

//connects with the adapter class and sets the view in main class
public class FriendListFragment extends ListFragment  
{  
	//In this class constructor we have to retrieve all matching contacts from server and assign it to a class object or list type
	
	String[] friendNames = new String[] { "Friend 1", "Friend 2", "Friend 3",
	        "Friend 4", "Friend Friend 5", "Friend 6", "Friend 7", "Friend 8",
	        "Friend 9", "Friend 10" };
 
	
	//listener for contact click
 @Override  
 public void onListItemClick(ListView l, View v, int position, long id) {  
	 Toast.makeText(getActivity().getApplicationContext(),
				"FriendListItem onClickListener position " + position,
				Toast.LENGTH_LONG).show();
//	 Intent i = new Intent(getActivity().getApplicationContext(),
//				ContactActivity.class);
//	 UserInfo uio = new UserInfo();
//	 uio.name = "First Last";
//	 uio.phone = "9999999999";
//	 uio.status = "Hello this is my status";
//	 i.putExtra("userinfoobj", uio);
//		startActivity(i);	    
 }  
 
 @Override  
 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
   Bundle savedInstanceState) {  
	 FriendListCustomAdaptor adapter = new FriendListCustomAdaptor(getActivity().getApplicationContext(), friendNames); //pass the class object/list type instead of contactNames
  setListAdapter(adapter);  
  return super.onCreateView(inflater, container, savedInstanceState);  
 }  
}  