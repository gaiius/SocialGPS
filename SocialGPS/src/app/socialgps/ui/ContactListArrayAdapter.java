package app.socialgps.ui;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.socialgps.db.dao.user_detail_dao;


//sets the views in contact_fragment for each contact in a listview
public class ContactListArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final List<user_detail_dao> values;

  public ContactListArrayAdapter(Context context, List values) {
	  
    super(context, R.layout.contact_fragment, values);
    this.context = context;
    this.values = values;
    System.out.println(((user_detail_dao)values.get(0)).get_display_name());
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
	  try	{
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   // System.out.println(values.get(0).get_status());
    //System.out.println(values.size());
    
    
    View rowView = inflater.inflate(R.layout.contact_fragment, parent, false);
    
    TextView contactName = (TextView) rowView.findViewById(R.id.contact_name);
    
    TextView friendStatus = (TextView) rowView.findViewById(R.id.contact_friend_status);
    
    contactName.setText(values.get(position).get_display_name());
     //set contact name
    // Set "friends" textview for friends
   // String s = values[position];
    //set friend status
    //if (s.equalsIgnoreCase("Name 2")||s.equalsIgnoreCase("Name 4")||s.equalsIgnoreCase("Name 7")) { // enter friend condition here
   // 	friendStatus.setText("Friends");
    //} else{
   // 	friendStatus.setText("");
    //}
    return rowView;
	  } catch (Exception e) { Log.d("Exception 11", e.toString()); 	return null; }
    
  }
} 