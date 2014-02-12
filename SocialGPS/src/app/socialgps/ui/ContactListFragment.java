package app.socialgps.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import app.socialgps.middleware.UserInfo;

//connects with the adapter class and sets the view in main class
public class ContactListFragment extends ListFragment  
{  
	//In this class constructor we have to retrieve all matching contacts from server and assign it to a class object or list type
	
	String[] contactNames = new String[] { "Name 1", "Name 2", "Name 3",
	        "Name 4", "Name 5", "Name 6", "Name 7", "Name 8",
	        "Name 9", "Name 10" };
 
	
	//listener for contact click
 @Override  
 public void onListItemClick(ListView l, View v, int position, long id) {  
	 Intent i = new Intent(getActivity().getApplicationContext(),
				ContactActivity.class);
	 UserInfo uio = new UserInfo();
	 uio.name = "First Last";
	 uio.phone = "9999999999";
	 uio.status = "Hello this is my status";
	 i.putExtra("userinfoobj", uio);
		startActivity(i);	    
 }  
 
 @Override  
 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
   Bundle savedInstanceState) {  
  ContactListArrayAdapter adapter = new ContactListArrayAdapter(getActivity().getApplicationContext(), contactNames); //pass the class object/list type instead of contactNames
  setListAdapter(adapter);  
  return super.onCreateView(inflater, container, savedInstanceState);  
 }  
}  