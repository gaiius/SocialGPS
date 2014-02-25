package app.socialgps.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import app.socialgps.db.dao.user_detail_dao;


	public class NotificationListAdaptor extends ArrayAdapter<user_detail_dao> {

		private List<user_detail_dao>  items;
		private int layoutResourceId;
		private Context context;
		TextView notificationName;
		Button acceptButton;
		Button denyButton;

		public NotificationListAdaptor(Context context, int layoutResourceId, List<user_detail_dao>  items) {
			super(context, layoutResourceId, items);
			System.out.println("noti list adaptor constructor entered");
			this.layoutResourceId = layoutResourceId;
			this.context = context;
			this.items = items;
			System.out.println("noti list adaptor constructor end");
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			//AtomPaymentHolder holder = null;
			System.out.println("getview entered");
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			System.out.println("inflating");
			row = inflater.inflate(layoutResourceId, parent, false);
			System.out.println("inflated");

			notificationName = (TextView)row.findViewById(R.id.notification_name);
			acceptButton = (Button)row.findViewById(R.id.notification_accept_button);
			denyButton = (Button)row.findViewById(R.id.notification_deny_button);
//			notificationName.setText("Hello hi");
			notificationName.setText(items.get(position).get_display_name());
			acceptButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
			});
//			row.setTag(holder);
//			setupItem(holder);
			return row;
		}

//		private void setupItem(AtomPaymentHolder holder) {
//			holder.name.setText(holder.atomPayment.getName());
//			holder.value.setText(String.valueOf(holder.atomPayment.getValue()));
//		}
//
//		public static class AtomPaymentHolder {
//			AtomPayment atomPayment;
//			TextView name;
//			TextView value;
//			ImageButton removePaymentButton;
//		}
	}
