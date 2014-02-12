package app.socialgps.middleware;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import app.socialgps.db.DatabaseHandler;
import app.socialgps.db.JSONParser;
import app.socialgps.db.dao.user_detail_dao;
import app.socialgps.db.dao.user_pass_dao;
import app.socialgps.db.dto.user_detail_dto;

public class contact_sync {
	ContentResolver cr;
	user_detail_dao udd;
	user_detail_dto udt;
	user_pass_dao upd;
	DatabaseHandler d;
	List<user_detail_dao> ans, reuse;

	public contact_sync(Context c) {
		try {
			d = new DatabaseHandler(c);
			udd = new user_detail_dao();
			upd = new user_pass_dao();
			upd = d.check_record();
			udt = new user_detail_dto(upd);
		} catch (Exception e) {
			Log.e("Exception main ", e.toString());
		} finally {
			d.close();
		}
	}

	public List<user_detail_dao> get_sync_contact(ContentResolver x) {
		try {
			ans = new ArrayList<user_detail_dao>();
			reuse = new ArrayList<user_detail_dao>();
			ans = readContacts(x);
			if (ans == null)
				return null;
			String s = convert_where(ans);
			reuse = udt.select_all(s);
			reuse = add_display_name(reuse, ans);
			return reuse;
		} catch (Exception e) {
			Log.e("Exception 2 ", e.toString());
			return null;
		}
	}

	public int sync_contact_db(ContentResolver x) {
		// DatabaseHandler d = null;
		try {
			int k = 0;
			System.out.print("Staated");

			ans = new ArrayList<user_detail_dao>();
			ans = get_sync_contact(x);
			if (ans != null)
				for (int i = 0; i < ans.size(); i++) {
					if (d.select(ans.get(0)) == null) {
						k++;
						d.insert(ans.get(0));
					}
				}
			return k;
		} catch (Exception e) {
			Log.e("Exception 3 ", e.toString());
			return 0;
		} finally {
			d.close(); //
		}
	}

	public List add_display_name(List<user_detail_dao> without,
			List<user_detail_dao> with) {
		try {
			for (int i = 0; i < without.size(); i++) {
				System.out.print("i");
				for (int j = 0; j < with.size(); j++) {
					System.out.print("k");
					if (without.get(i).get_user_id()
							.equals(with.get(j).get_user_id())) {
						without.get(i).set_display_name(
								with.get(j).get_display_name());
					}
				}
			}
			return without; // now display_name added
		} catch (Exception e) {
			Log.e("Exception 4 ", e.toString());
			return null;
		}

	}

	public List readContacts(ContentResolver x) {
		try {
			ContentResolver cr = x;// getContentResolver();

			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
					null, null, null);

			ans = new ArrayList<user_detail_dao>();
			if (cur.getCount() > 0) {
				while (cur.moveToNext()) {
					System.out.println("3");
					String id = cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur
							.getString(cur
									.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
					if (Integer
							.parseInt(cur.getString(cur
									.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

						Cursor pCur = cr
								.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
										null,
										ContactsContract.CommonDataKinds.Phone.CONTACT_ID
												+ " = ?", new String[] { id },
										null);
						while (pCur.moveToNext()) {
							String phone = pCur
									.getString(pCur
											.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

							phone = trim_ph(phone);
							udd = new user_detail_dao();
							udd.set_user_id(phone);
							udd.set_phone(Long.parseLong(phone));
							udd.set_display_name(name);
							if (phone.length() > 9) {
								// Log.d("phone", phone);
								ans.add(udd);
							}

						}
						pCur.close();
					}
				}
				Log.d("contacts retrived", "all contact numbers retrieved");
				return ans;
			}
			return null;
		} catch (Exception e) {
			Log.e("Exception 1 ", e.toString());
			return null;
		}
		// finally { cur.close(); }
	}

	public String trim_ph(String text) {
		int length = text.length();
		StringBuffer buffer = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			char ch = text.charAt(i);
			if (Character.isDigit(ch)) {
				buffer.append(ch);
			}
		}
		if (buffer.length() > 10) {
			//Log.d(buffer.toString(), buffer.substring(buffer.length() - 10));
			return buffer.substring(buffer.length() - 10);
		}

		return buffer.toString();
	}

	public String convert_where(List<user_detail_dao> udd) {
		System.out.println("way to ernter");

		StringBuffer s = new StringBuffer();
		for (int i = 0; i < udd.size(); i++) {
			s.append("user_id='" + udd.get(i).get_user_id() + "'");

			if (i != udd.size() - 1)
				s.append(" OR ");
		}
		Log.d("Where", s.toString());
		return s.toString();
	}

}
