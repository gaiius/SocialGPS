package app.socialgps.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class FriendListCustomAdaptor extends BaseAdapter implements OnClickListener,OnCheckedChangeListener {

	/**
	 * @param args
	 */
	Context context;
    String[] values;    
   // private static LayoutInflater inflater = null;

	public FriendListCustomAdaptor(Context context, String[] values) {
		//super();
		this.context = context;
		this.values = values;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub					
		int pos = (Integer)v.getTag();		 
		Toast.makeText(v.getContext(), "Friend position is " + pos,
	               Toast.LENGTH_SHORT).show();

		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return values.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return values[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {		
		 if (convertView == null) {
		        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        convertView = inflater.inflate(R.layout.friends_fragment, null);
		    }
		 TextView friendName = (TextView) convertView.findViewById(R.id.friend_name);
		 Switch friendViewToggle = (Switch) convertView.findViewById(R.id.friend_view_toggle);
		 friendName.setText(values[position]);
		 friendName.setTag(new Integer(position));
		 friendViewToggle.setTag(new Integer(position));
		 friendName.setOnClickListener(this);	
		 friendViewToggle.setOnCheckedChangeListener(this);
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		int pos = (Integer)buttonView.getTag();
		Toast.makeText(buttonView.getContext(), "Monitored switch at "+ pos + " is " + (isChecked ? "on" : "off"),
	               Toast.LENGTH_SHORT).show();
		
	}

}
