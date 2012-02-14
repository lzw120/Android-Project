package com.neu.android.localtweets;

//import android.app.Activity;
//import android.os.Bundle;

//public class LocalTweetsActivity extends Activity {
//    /** Called when the activity is first created. */
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//    }
//}

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.parser.JSONArray;
import org.json.simple.parser.JSONObject;
import org.json.simple.parser.JSONParser;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.neu.android.tweets.*;
import android.view.View.OnClickListener;


public class LocalTweetsActivity extends Activity {
    /** Called when the activity is first created. */
	public ListView listView;
	public Button mybutton;
	public ArrayList<Tweet> tweets;
	public TweetItemAdapter myadapter;
//    private List<String> getData(){
//    List<String> data = new ArrayList<String>();
//        		        data.add("data1");
//        		        data.add("data2");
//        		        data.add("data3");
//        		        data.add("data4");       		         
//        		        return data;
//        		    }

//	private List<Map<String, Object>> getData() {
//		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//
//		Map<String, Object> map = new HashMap<String, Object>();
//        map.put("title", "G1");
//        map.put("info", "google 1");
//        map.put("img", R.drawable.i1);
//        list.add(map);
// 
//        map = new HashMap<String, Object>();
//        map.put("title", "G2");
//        map.put("info", "google 2");
//        map.put("img", R.drawable.i2);
//        list.add(map);
// 
//        map = new HashMap<String, Object>();
//        map.put("title", "G3");
//        map.put("info", "google 3");
//        map.put("img", R.drawable.i3);
//        list.add(map);
//         
//        return list;
//    }
	
	public Bitmap getBitmap(String bitmapUrl) {
		try {
			URL url = new URL(bitmapUrl);
			return BitmapFactory.decodeStream(url.openConnection().getInputStream()); 
		}
		catch(Exception ex) {return null;}
	}
	
	public class TweetItemAdapter extends ArrayAdapter<Tweet> {
		  public ArrayList<Tweet> tweets;

		  public TweetItemAdapter(Context context, int textViewResourceId, ArrayList<Tweet> tweets) {
		    super(context, textViewResourceId, tweets);
		    this.tweets = tweets;
		  }

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    View v = convertView;
		    if (v == null) {
		      LayoutInflater vi = 
		         (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      //.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		      v = vi.inflate(R.layout.vlist, null);
		    }

		    Tweet tweet = tweets.get(position);
		    if (tweet != null) {
		      TextView username = (TextView) v.findViewById(R.id.title);//.id.username);
		      TextView message = (TextView) v.findViewById(R.id.info);
		      ImageView image = (ImageView) v.findViewById(R.id.img);

		      if (username != null) {
		        username.setText(tweet.username);
		      }

		      if(message != null) {
		        message.setText(tweet.message);
		      }
		        
		      if(image != null) {
		        image.setImageBitmap(getBitmap(tweet.image_url));
		      }
		    }
		    return v;
		  }
		}
	
	public ArrayList<Tweet> getTweets(String searchTerm, int page) {
		  String searchUrl = 
		        "http://search.twitter.com/search.json?geocode=" 
		        + searchTerm + "&rpp=25&page=" + page;
		    
		  ArrayList<Tweet> tweets = 
		        new ArrayList<Tweet>();
		    
		  HttpClient client = new  DefaultHttpClient();
		  HttpGet get = new HttpGet(searchUrl);
		        
		  ResponseHandler<String> responseHandler = 
		        new BasicResponseHandler();

		  String responseBody = null;
		  try {
		    responseBody = client.execute(get, responseHandler);
		  } catch(Exception ex) {
		    ex.printStackTrace();
		  }

		  JSONObject jsonObject = null;
		  JSONParser parser=new JSONParser();
		    
		  try {
		    Object obj = parser.parse(responseBody);
		    jsonObject=(JSONObject)obj;
		  }catch(Exception ex){
		    Log.v("TEST","Exception: " + ex.getMessage());
		  }
		    
		  JSONArray arr = null;
		    
		  try {
		    Object j = jsonObject.get("results");
		    arr = (JSONArray)j;
		  } catch(Exception ex){
		    Log.v("TEST","Exception: " + ex.getMessage());
		  }


		  

		  for(Object t : arr) {
			  Tweet tweet = new Tweet(
					  ((JSONObject)t).get("from_user").toString(),
					  ((JSONObject)t).get("text").toString(),
					  ((JSONObject)t).get("profile_image_url").toString()
					  );
			  tweets.add(tweet);
		  }

		  return tweets;
		}

    private final LocationListener locationListener = new LocationListener() {  
    	public void onLocationChanged(Location location) { //  
    		// log it when the location changes  
    		if (location != null) {  
    			Log.i("SuperMap", "Location changed : Lat: "  
    					+ location.getLatitude() + " Lng: "  
    					+ location.getLongitude());  
    		} 
    		//TextView mytextview1 = (TextView)findViewById(R.id.textview1); 
    		//double latitude = location.getLatitude();     //
        	//double longitude = location.getLongitude(); //  
        	//double altitude =  location.getAltitude();     //
        	//mytextview1.setText("la:"+latitude+" lo:"+longitude+" al:"+altitude);
    		
    	}  
    	public void onProviderDisabled(String provider) {  
    		//  
    	}  

    	public void onProviderEnabled(String provider) {  
    		//     
    	}  

    	public void onStatusChanged(String provider, int status, Bundle extras) {  
    		//   
    	}  
    };  
	    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    	
      LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
    	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
    			 1000, 0, locationListener); 
    	Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
    	double latitude = location.getLatitude();     //
    	double longitude = location.getLongitude(); //  
    	double altitude =  location.getAltitude();     //
//    	TextView mytextview1 = (TextView)findViewById(R.id.textview1);   	
        ArrayList<Tweet> tweets = getTweets(latitude+","+longitude+",50mi", 1);
        listView = (ListView)findViewById(R.id.listView1);
        myadapter = new TweetItemAdapter(this, R.layout.vlist, tweets);
        listView.setAdapter(myadapter);
        //mybutton = (Button)findViewById(R.id.button1);
       // mybutton.setOnClickListener(new OnButtonClickListener ());
//        int i = 50000000;
//        while (i>=0)
//        {
//        	if (i == 0) 
//        	{i = 5;}
//        	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
//        	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
//        			 1000, 0, locationListener); 
//        	location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
//            latitude = location.getLatitude();     //
//    		longitude = location.getLongitude(); //  
//    		altitude =  location.getAltitude();     //
//    		tweets = getTweets(latitude+","+longitude+",50mi", 1);
//    		myadapter = new TweetItemAdapter(this, R.layout.vlist, tweets);
//    		listView.setAdapter(myadapter);
//        }
        
//        latitude = location.getLatitude();     //
//		longitude = location.getLongitude(); //  
//		altitude =  location.getAltitude();     //
//		tweets = getTweets(latitude+","+longitude+",50mi", 1);
//		myadapter.tweets=tweets;
//		myadapter.notifyDataSetChanged();
        //listView.setAdapter(new TweetItemAdapter(this, R.layout.vlist, tweets));
        
        //listView.setAdapter(adapter);
        //mytextview1.setText("la:"+latitude+" lo:"+longitude+" al:"+altitude);
        //listView.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1, getData()) );
       
    }
    class OnButtonClickListener implements OnClickListener
    {

		public void onClick(View v) {
			// TODO Auto-generated method stub
//			LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);  
//			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,  
//					1000, 0, locationListener); 
//			Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
//			double latitude = location.getLatitude();     //
//			double longitude = location.getLongitude(); //  
//			double altitude =  location.getAltitude();     //
//			tweets = getTweets(latitude+","+longitude+",50mi", 1);
//			//myadapter.setNotifyOnChange(true);
//			myadapter.tweets=tweets;
//			myadapter.notifyDataSetChanged();
			//listView.setAdapter(myadapter);


//			//TweetItemAdapter adapter = new TweetItemAdapter (this, R.layout.vlist, tweets);
//			myadapter = new TweetItemAdapter (this, R.layout.vlist, tweets);
//			listView.setAdapter (myadapter);
			
			
		}
    	
    }
    
    
}




