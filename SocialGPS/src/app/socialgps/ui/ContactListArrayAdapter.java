package app.socialgps.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


//sets the views in contact_fragment for each contact in a listview
public class ContactListArrayAdapter extends ArrayAdapter<String> {
  private final Context context;
  private final String[] values;

  public ContactListArrayAdapter(Context context, String[] values) {
    super(context, R.layout.contact_fragment, values);
    this.context = context;
    this.values = values;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.contact_fragment, parent, false);
    TextView contactName = (TextView) rowView.findViewById(R.id.contact_name);
    TextView friendStatus = (TextView) rowView.findViewById(R.id.contact_friend_status);
    contactName.setText(values[position]);//set contact name
    // Set "friends" textview for friends
    String s = values[position];
    //set friend status
    if (s.equalsIgnoreCase("Name 2")||s.equalsIgnoreCase("Name 4")||s.equalsIgnoreCase("Name 7")) { // enter friend condition here
    	friendStatus.setText("Friends");
    } else{
    	friendStatus.setText("");
    }
    return rowView;
  }
} 