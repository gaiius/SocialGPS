package app.socialgps.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.middleware.UserInfo;

//connects with the adapter class and sets the view in main class
public class ContactListFragment extends ListFragment  
{  
	List<user_detail_dao> frd=  new ArrayList<user_detail_dao>();
	DatabaseHandler d;
	
	//listener for contact click
 @Override  
 public void onListItemClick(ListView l, View v, int position, long id) {  
	 Intent i = new Intent(getActivity().getApplicationContext(),
				ContactActivity.class);
	frd= get_list();
	i.putExtra("user_detail", frd.get(position));
		startActivity(i);	    
 }  
 
 @Override  
 public View onCreateView(LayoutInflater inflater, ViewGroup container,  
   Bundle savedInstanceState) {
	 try	{	
	 frd= get_list();	 
	 
  ContactListArrayAdapter adapter = new ContactListArrayAdapter(getActivity().getApplicationContext(), frd);  //in doubt
  setListAdapter(adapter);  
  	}
  catch (Exception e) { Log.d("Exception 2", e.toString()); 	 }
	 return super.onCreateView(inflater, container, savedInstanceState);
 }  
  
 
 public List<user_detail_dao> get_list()
 {	
	 try {
	 d= new DatabaseHandler(getActivity().getApplicationContext());
	 user_pass_dao udd= new user_pass_dao();;
	 udd=  d.check_record();
	 frd= d.select_all();
	// frd= remove_mine(udd.get_user_id(), frd);
	 System.out.println("Frien siz "+frd.size()+" "+frd.get(0).get_display_name());
	 return frd;
	 } catch (Exception e) { Log.d("Exception 1", e.toString()); 	return null; }
	 finally { d.close(); }
 }
 

public List remove_mine(String id, List<user_detail_dao> a)
{
	if(a!=null)
		for (int i=0; i<a.size(); i++)
		{
			if(a.get(i).get_user_id().equals(id))
			{
				a.remove(i);
			}
		}
	return a; 
}
}