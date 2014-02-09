package app.socialgps.db;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import app.socialgps.db.dao.*;

public class getJson { //JSON to DAO Conversation 
	AsyncTask<String, String, JSONObject> json_1;
	JSONObject products;
	int result=0;
	
	public getJson(String uid, String query) throws JSONException {  // __Cons for getting JSON object, uid for user authentication
		json_1 = new return_Json().execute(uid, query);
		try {
			products = json_1.get();
			result=products.getInt("res_code");
			Log.d("Returns", products.toString());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public int get_res_code() { //get res_code
		try {
			Log.d("got","code");
			return result;
		} catch (Exception e) {
			Log.e("Error in getting JSON value", e.toString());
			return 105;
		}
	}

	public user_pass_dao get_user_pass_result() { // make json to user_pass DAO
		try {
			Log.d("res","code");
			if(get_res_code()==3)
			{
			user_pass_dao upd = new user_pass_dao();
			upd.set_user_id(products.getJSONArray("product").getJSONObject(0)
					.getString("user_id"));
			upd.set_passwd(products.getJSONArray("product").getJSONObject(0)
					.getString("passwd"));
			Log.d("user_pass","selelct");
			
			return upd;
			}
			else
			{
			Log.d("res","code");
			return null; 
			}
		} catch (JSONException e) {
			Log.e("Error in getting JSON value", e.toString());
			return null;
		} catch (Exception e) {
			Log.e("Genral Error" , e.toString());
			return null;
		}

	}
	

	

	public user_detail_dao get_user_detail_result() { // JSON to user_detail dao
		try {
			if(get_res_code()==3)
			{
			user_detail_dao upd = new user_detail_dao();
			upd.set_user_id(products.getJSONArray("product").getJSONObject(0)
					.getString("user_id"));
			upd.set_user_name(products.getJSONArray("product").getJSONObject(0)
					.getString("user_name"));
			upd.set_phone(products.getJSONArray("product").getJSONObject(0)
					.getLong("phone"));
			upd.set_status(products.getJSONArray("product").getJSONObject(0)
					.getString("status"));
			upd.set_email_id(products.getJSONArray("product").getJSONObject(0)
					.getString("email_id"));
			return upd;
			}
			else
				return null;
		} catch (JSONException e) {
			Log.e("Error in getting JSON value", e.toString());
			return null;
		}
	}

	public location_detail_dao get_location_detail_result() { //json to location detail dao 
		try {
			if(get_res_code()==3)
			{
			location_detail_dao upd = new location_detail_dao();
			upd.set_user_id(products.getJSONArray("product").getJSONObject(0)
					.getString("user_id"));
			upd.set_location(products.getJSONArray("product").getJSONObject(0)
					.getString("location"));
			upd.set_tyme(products.getJSONArray("product").getJSONObject(0).getString("tyme"));
			return upd;
			}
			else
				return null;
		} catch (JSONException e) {
			Log.e("Error in getting JSON value", e.toString());
			return null;
		}
	}



	public List<friend_detail_dao> get_friend_detail_result() //json to get collection of friend details dao
	{
		try
		{
			if(get_res_code()==3)
			{
			
	List<friend_detail_dao> lfd = new ArrayList<friend_detail_dao>();
	friend_detail_dao fdd;
	
	for (int i=0;i<products.getJSONArray("product").length()-1;i++)
	{
		fdd= new friend_detail_dao();
		fdd.set_user_id(products.getJSONArray("product").getJSONObject(i)
				.getString("user_id"));
		fdd.set_friend_id(products.getJSONArray("product").getJSONObject(i)
				.getString("friend_id"));
		fdd.set_status(products.getJSONArray("product").getJSONObject(i).getString("status"));
		lfd.add(fdd);
	}
	return lfd;
			}
			else
				return null;
		}
	catch (JSONException e) {
		Log.e("Error in getting JSON value", e.toString());
		return null;
	} catch (Exception e) {
		System.out.println("Genral Error" + e.toString());
		return null;
	} 
}	
	

class return_Json extends AsyncTask<String, String, JSONObject> {//background task getting json from online
	//@override
	protected JSONObject doInBackground(String... args) { //main activity for getting results
		try {
			JSONObject json;
			String url_all_products ="http://locobava.site90.net/main.php";
			JSONParser jsonParser = new JSONParser();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", args[0]));
			params.add(new BasicNameValuePair("query", args[1]));
			json = jsonParser.makeHttpRequest(url_all_products, "GET", params);
			return json;
		} catch (Exception e) {
			Log.e("Err", e.toString());
			return null;
		}

	}
}
}
