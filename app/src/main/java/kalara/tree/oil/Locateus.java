package kalara.tree.oil;

import android.annotation.TargetApi;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Locateus extends Fragment  implements OnMapReadyCallback {

    GoogleMap googleMap;
    MapView mMapView;
    Location location;
    public ArrayList<HashMap<String, String>> locationList;
    LatLng latLng,latLng2;
int value;
    MarkerOptions markerOptions;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.location, container, false);
        mMapView = (MapView) v.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        Navigation_Acivity.title.setText("Locate Us");
       // Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        value=getArguments().getInt("value");
        if(value==1){
            Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        }
        else if(value==0){
            Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        }
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        locationList = new ArrayList<HashMap<String, String>>();

//        LocationManager locationManager=(LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        new Get_location().execute();

        return v;



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /*    @Override
        public void onLocationChanged(Location location) {

            // create marker
            MarkerOptions marker = new MarkerOptions().position(
                    new LatLng(location.getLatitude(), location.getLongitude())).title("Hello Maps");

            // Changing marker icon
            marker.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

            // adding marker
            googleMap.addMarker(marker);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude())).zoom(12).build();
            googleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));

            // Perform any camera updates here
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }*/
    private class Get_location extends AsyncTask<Void, Void, Void> {


        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Void doInBackground(Void... voids) {
            // TODO Auto-generated method stub
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            // HttpPost httppost = new HttpPost(UserFunctions.newsURL);
            HttpGet httpGet = new HttpGet("http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=getlocation"
                    );

            String responseBody = "";

            HttpResponse response = null;
            // httpGet.setEntity(new StringEntity(obj.toString(), "UTF-8"));

            // Execute HTTP Post Request
            try {
                response = httpclient.execute(httpGet);
                responseBody = EntityUtils.toString(response.getEntity());
                System.out.println("====$$$$===" + responseBody);
                JSONObject info=null;
//                JSONObject jsonObject = new JSONObject(responseBody);

try {
    info = new JSONObject(responseBody);
}
catch (JSONException e) {
    e.printStackTrace();
}

                String location=null;
                String response1=null;

if(info!=null){
    try{
                        String result = info.getString("result");
                        response1 = info.getString("response");
                        if(result.equals("true")) {
                            JSONArray array = new JSONArray(response1);
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject jsonObject = array.getJSONObject(j);
                                location = jsonObject.getString("location");
                                String address1 = jsonObject.getString("address");
                                System.out.println("id,name,image" + location + "==" + address1);

                                HashMap<String,String> contact=new HashMap<String,String>();
                                contact.put("location", location);
                                System.out.println("======loveuthis is a array=="
                                        + contact);
                                locationList.add(contact);
                            }



                        }

                        // Decode Bitmap




                        // new LoadImageFromURL().execute();



                    }
    catch(JSONException e){
        e.printStackTrace();
    }
                }

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);

            for(int i=0;i<locationList.size();i++){
                String event_location=locationList.get(i).get("location");

                System.out.println("==hero===="+event_location);
                new GeocoderTask().execute(event_location);
            }

        }
    }

    private class GeocoderTask  extends AsyncTask<String,Void,List<Address>> {
        @Override
        protected List<Address> doInBackground(String... locationName) {
            Geocoder geocoder = new Geocoder(getActivity());
            List<Address> addresses = null;

            try {
                // Getting a maximum of 3 Address that matches the input text


                addresses = geocoder.getFromLocationName(locationName[0],3);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            super.onPostExecute(addresses);
            if(addresses==null || addresses.size()==0){
//							Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }


            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
                System.out.println("===address===="+addresses.get(i));
                Address address = (Address) addresses.get(i);



                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());


                String addressText = String.format("%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                        address.getCountryName());

                //First marker
                markerOptions = new MarkerOptions();

                System.out.println("=====lotus===="+latLng);
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));

                googleMap.addMarker(markerOptions);

                // Locate the first location
                if(i==0)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4));


                addresses.clear();
            }
        }
    }


    }
