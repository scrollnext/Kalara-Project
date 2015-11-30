package kalara.tree.oil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerFragment extends Fragment implements MessageDialogFragment.MessageDialogListener,
        ZXingScannerView.ResultHandler, FormatSelectorDialogFragment.FormatSelectorDialogListener,
        CameraSelectorDialogFragment.CameraSelectorDialogListener {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private static final String CAMERA_ID = "CAMERA_ID";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;
    private int mCameraId = -1;
ProgressDialog pDialog;
   static ArrayList<String> arrayList=new ArrayList<String>();
    ArrayList<Knowlegde_item> knowlegde_items=new ArrayList<Knowlegde_item>();
    ArrayList<HashMap<String, String>> Datalist1=new ArrayList<HashMap<String,String>>();
    Bundle bundle ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        mScannerView = new ZXingScannerView(getActivity());
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
            mCameraId = state.getInt(CAMERA_ID, -1);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
            mCameraId = -1;
        }
        setupFormats();
        return mScannerView;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setHasOptionsMenu(false);
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


        if(mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.menu_camera_selector, 0, R.string.select_camera);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if(mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if(mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            case R.id.menu_formats:
                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(this, mSelectedIndices);
                fragment.show(getActivity().getSupportFragmentManager(), "format_selector");
                return true;
            case R.id.menu_camera_selector:
                mScannerView.stopCamera();
                DialogFragment cFragment = CameraSelectorDialogFragment.newInstance(this, mCameraId);
                cFragment.show(getActivity().getSupportFragmentManager(), "camera_selector");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
        outState.putInt(CAMERA_ID, mCameraId);
    }

    @Override
    public void handleResult(Result rawResult) {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
     // showMessageDialog("Contents = " + rawResult.getText() + ", Format = " + rawResult.getBarcodeFormat().toString());
        String result=rawResult.getText();

        String[] separated = result.split(",");
       String part1= separated[0]; // this will contain "Fruit"
       // showMessageDialog("Contents = " + part1 + ", Format = " + rawResult.getBarcodeFormat().toString());
       // separated[1]; // this will contain " they taste good"
   //showMessageDialog(""+part1);
        if(Scanqr.value==0) {
            getproducts(part1);



        }
        else if(Scanqr.value==1){
            Getproductdetail(part1);
        }

    }

    public void showMessageDialog(String message) {
        DialogFragment fragment = MessageDialogFragment.newInstance("Scan Results", message, this);
        fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }


    public void onCameraSelected(int cameraId) {
        mCameraId = cameraId;
        mScannerView.startCamera(mCameraId);
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
    }
    public void getproducts(final String content) {
        // TODO Auto-generated method stub
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog

                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
               // pDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings
                // as the argument
                // We need to specifically get the givenUsername and
                // givenPassword
					/* code=editText.getText().toString();*/


                // Create an intermediate to connect with the Internet

                // Set up secret key spec for 128-bit AES encryption and decryption


                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the
                // values through the URL

                String url = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=getreport";


                System.out.println("=======url======" + url);
                HttpGet httpGet = new HttpGet(url);

                System.out.println("=======url=1=====" + httpGet);

                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponseo
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    System.out.println("==========httpResponse====="
                            + httpResponse.toString());
                    // System.out.println("httpResponse					// getEntity() ; " +
                    // "obtains the message entity of this response
                    // getContent() ; creates a new InputStream object of the
                    // entity.
                    // Now we need a readable source to read the byte stream
                    // that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity()
                            .getContent();

                    // We have a byte stream. Next step is to convert it to a
                    // Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);

                    // Then we have to wraps the existing reader
                    // (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);

                    // InputStreamReader contains a buffer of bytes read from
                    // the source stream and converts these into characters as
                    // needed.
                    // The buffer size is 8K
                    // Therefore we need a mechanism to append the separately
                    // coming chunks in to one String element
                    // We have to use a class that can handle modifiable
                    // sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    // There may be so many buffered chunks. We have to go
                    // through each and every chunk of characters
                    // and assign a each chunk to bufferedStrChunk String
                    // variable
                    // and append that value one by one to the stringBuilder
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    // Now we have the whole response as a String value.
                    // We return that value then the onPostExecute() can handle
                    // the content
                    System.out.println("Returning of doInBackground :"
                            + stringBuilder.toString());

                    // If the Username and Password match, it will return
                    // "working" as response
                    // If the Username or Password wrong, it will return
                    // "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exceptionrates caz of httpResponse :"
                            + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out
                            .println("Secondption generates caz of httpResponse :"
                                    + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            // Argument comes for this method according to the return type of
            // the doInBackground() and
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (pDialog.isShowing())
                   // pDialog.dismiss();
                System.out.println("====================" + result);

                if (result != null) {

                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (json != null) {
                        // use myJson as needed, for example
                        try {
                            String object = json.getString("result");
                            System.out.println("object" + object);
                            JSONArray array = null;

                            if (object.equals("true")) {

                                String respose = json.getString("response");
                                array = new JSONArray(respose);
                                JSONObject object1 = null;
                                String barcode="";
                                for (int i = 0; i < array.length(); i++) {
                                     object1 = array.getJSONObject(i);
                                    barcode = object1.getString("barcode");
                                    System.out.println("vlaue of barcode"+content);


                                     if(barcode.equals(content)) {
                                        System.out.println(" barcode valuueee  ");
                                        String id1 = object1.getString("id");
                                        String manufacturer = object1.getString("manufacturer");
                                        String barcode1 = object1.getString("barcode");
                                        String product = object1.getString("product");
                                        String size = object1.getString("size");
                                        String productid=object1.getString("productid");

                                        String comments = object1.getString("comments");
                                        String brand = object1.getString("brand");

                                        String time = object1.getString("time");
                                        System.out.println("nitesh"+manufacturer+" "+barcode1);
                                        int  scanvalue1=2;
                                        JSONArray product_image = object1.getJSONArray("report_image");

                                        for(int j=0;j<product_image.length();j++) {
                                           String image = product_image.get(j).toString();
                                            System.out.println("nitesh imagess"+image);
                                            arrayList.add(image);
                                        }
                                        Intent intent=new Intent(getActivity(),Navigation_Acivity.class);
                                        intent.putExtra("value",Scanqr.value);
                                        intent.putExtra("id",id1);
                                        intent.putExtra("scanvalue",scanvalue1);
                                        intent.putExtra("product",product);
                                        intent.putExtra("barcode",barcode);
                                        intent.putExtra("size",size);
                                        intent.putExtra("comments",comments);
                                        intent.putExtra("productid",productid);
                                        intent.putExtra("manufacturer",manufacturer);
                                        //intent.putExtra("image",image);
                                        intent.putExtra("brand", brand);
                                        System.out.println("nitesh" + "Hello1" + " " + barcode1);

                                        startActivity(intent);
                                        System.out.println("nitesh" + "Hello" + " " + barcode1);


                                          break;

                                       /* Fragment create=new Create();*/

                                       /* bundle =new Bundle();dcd
                                        bundle.putInt("value",Scanqr.value);
                                        bundle.putString("id",id);
                                        bundle.putString("category",category);
                                        bundle.putString("qrcode",qrcode);
                                        bundle.putString("size",size);
                                        bundle.putString("product_name",product_name);
                                        bundle.putString("image",image);
                                        create.setArguments(bundle);
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.container_body, create).commit();*/

                                    }


                                    if(!barcode.equals(content)) {

                                        int  scanvalue2=7;
                                        System.out.println("vlaue of barcode friend"+content);
                                        System.out.println("nitesh" + "Noooo" + " " );
                                        Intent intent1=new Intent(getActivity(),Navigation_Acivity.class);
                                        intent1.putExtra("value",Scanqr.value);
                                        intent1.putExtra("result",content);
                                        intent1.putExtra("scanvalue",scanvalue2);

                                        startActivity(intent1);
                                    }
                                }





                            } else{
                                //Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
                                int  scanvalue=3;

                                Intent intent=new Intent(getActivity(),Navigation_Acivity.class);
                                intent.putExtra("value",Scanqr.value);

                                intent.putExtra("scanvalue",scanvalue);

                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                }
                // Initialize the AsyncTask class

            }
        }
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first
        // generic type oodf the AsyncTask
        // We are passing the connectWithHttpGet() meth arguments to that
        httpGetAsyncTask.execute();
    }
    public void Getproductdetail(final String content) {
        // TODO Auto-generated method stub
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog

                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();

            }

            @Override
            protected String doInBackground(String... params) {

                // As you can see, doInBackground has taken an Array of Strings
                // as the argument
                // We need to specifically get the givenUsername and
                // givenPassword
					/* code=editText.getText().toString();*/


                // Create an intermediate to connect with the Internet

                // Set up secret key spec for 128-bit AES encryption and decryption


                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the
                // values through the URL

                String url = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=getproduct";


                System.out.println("=======url======" + url);
                HttpGet httpGet = new HttpGet(url);

                System.out.println("=======url=1=====" + httpGet);

                try {
                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponseo
                    HttpResponse httpResponse = httpClient.execute(httpGet);

                    System.out.println("==========httpResponse====="
                            + httpResponse.toString());
                    // System.out.println("httpResponse					// getEntity() ; " +
                    // "obtains the message entity of this response
                    // getContent() ; creates a new InputStream object of the
                    // entity.
                    // Now we need a readable source to read the byte stream
                    // that comes as the httpResponse
                    InputStream inputStream = httpResponse.getEntity()
                            .getContent();

                    // We have a byte stream. Next step is to convert it to a
                    // Character stream
                    InputStreamReader inputStreamReader = new InputStreamReader(
                            inputStream);

                    // Then we have to wraps the existing reader
                    // (InputStreamReader) and buffer the input
                    BufferedReader bufferedReader = new BufferedReader(
                            inputStreamReader);

                    // InputStreamReader contains a buffer of bytes read from
                    // the source stream and converts these into characters as
                    // needed.
                    // The buffer size is 8K
                    // Therefore we need a mechanism to append the separately
                    // coming chunks in to one String element
                    // We have to use a class that can handle modifiable
                    // sequence of characters for use in creating String
                    StringBuilder stringBuilder = new StringBuilder();

                    String bufferedStrChunk = null;

                    // There may be so many buffered chunks. We have to go
                    // through each and every chunk of characters
                    // and assign a each chunk to bufferedStrChunk String
                    // variable
                    // and append that value one by one to the stringBuilder
                    while ((bufferedStrChunk = bufferedReader.readLine()) != null) {
                        stringBuilder.append(bufferedStrChunk);
                    }

                    // Now we have the whole response as a String value.
                    // We return that value then the onPostExecute() can handle
                    // the content
                    System.out.println("Returning of doInBackground :"
                            + stringBuilder.toString());

                    // If the Username and Password match, it will return
                    // "working" as response
                    // If the Username or Password wrong, it will return
                    // "invalid" as response
                    return stringBuilder.toString();

                } catch (ClientProtocolException cpe) {
                    System.out.println("Exceptionrates caz of httpResponse :"
                            + cpe);
                    cpe.printStackTrace();
                } catch (IOException ioe) {
                    System.out
                            .println("Secondption generates caz of httpResponse :"
                                    + ioe);
                    ioe.printStackTrace();
                }

                return null;
            }

            // Argument comes for this method according to the return type of
            // the doInBackground() and
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (pDialog.isShowing())
                    pDialog.dismiss();
                System.out.println("====================" + result);

                if (result != null) {

                    JSONObject json = null;
                    try {
                        json = new JSONObject(result);
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (json != null) {
                        // use myJson as needed, for example
                        try {
                            String object = json.getString("result");
                            System.out.println("object" + object);
                            JSONArray array = null;
                            JSONObject object1;
                            if (object.equals("true")) {

                                String respose = json.getString("response");
                                array = new JSONArray(respose);
                                for (int i = 0; i < array.length(); i++) {
                                    object1 = array.getJSONObject(i);
                                    String product_name = object1.getString("product_name");
                                    System.out.println("postvalue"+product_name);
                                    if(product_name.equals(content)) {
                                        System.out.println("postvalue 11"+product_name);
                                        String id = object1.getString("id");
                                        String product_name1= object1.getString("product_name");
                                        // String report = object1.getString("report");
                                        String barcode = object1.getString("barcode");
                                        //String product = object1.getString("product");
                                        String size = object1.getString("size");

                                        String status = object1.getString("status");
                                        String product_categories = object1.getString("category");

                                        String product_video = object1.getString("product_video");
                                        String time = object1.getString("time");
                                        // String product_image = object1.getString("product_image");
                                        JSONArray product_image = object1.getJSONArray("product_image");
                                        String ratings = object1.getString("ratings");
                                        if (ratings.equals("0")) {

                                        } else {
                                            JSONArray jsonarray = new JSONArray(ratings);
                                            JSONObject ob1;
                                            for (int k = 0; k < jsonarray.length(); k++) {
                                                ob1 = jsonarray.getJSONObject(k);
                                                String ratingval = ob1.getString("rating");
                                                String username = ob1.getString("username");
                                                String image = ob1.getString("image");

                                               /* String comment = ob1.getString("comment");

                                                String COUNT = ob1.getString("COUNT");*/


                                            }
                                        }
                                       /* HashMap<String, String> data = new HashMap<String, String>();
                                        JSONArray avgrating = new JSONArray("AVG(rating)");
                                        String AVG=null;
                                        for (int j = 0; j < avgrating.length(); j++) {
                                            JSONObject object2 = avgrating.getJSONObject(j);
                                            AVG = object2.getString("AVG(rating)");
                                            data.put("AVG", AVG);
                                        }

*/
                                        for (int j = 0; j < product_image.length(); j++) {
                                            String image = product_image.get(j).toString();

                                            knowlegde_items.add(new Knowlegde_item(i, image, id));
                                            System.out.println("imagesss are" + " " + knowlegde_items.size());
                                        }


                                        int j;
                                        //  *//*for( j=0;j<product_image.length();j++) {
                                        // String image = product_image.get(j).toString();
                                        //  data.put("image", product_image.get(j).toString());


                                        // }*//*
                                     /*   data.put("id", id);

                                        data.put("barcode", barcode);
                                        // data.put("product", product);
                                        data.put("size", size);

                                        data.put("status", status);
                                        data.put("product_categories", product_categories);
                                        data.put("product_name", product_name1);
                                        data.put("product_video", product_video);

                                        Datalist1.add(data);*/
                                        System.out.println("vallllllll of thhhh" + id + " " + Datalist1);
                                        int  scanvalue=4;

                                        Intent intent=new Intent(getActivity(),Navigation_Acivity.class);
                                        intent.putExtra("value",Scanqr.value);
                                        intent.putExtra("id",id);
                                        intent.putExtra("scanvalue",scanvalue);
                                        intent.putExtra("productcategory",product_categories);
                                        intent.putExtra("product_url",product_video);
                                     //   intent.putExtra("AVG",AVG);
                                        intent.putExtra("product_name",product_name);
                                        intent.putExtra("position","");
                                        startActivity(intent);
                                        /*Bundle bundle=new Bundle();
                                        bundle.putString("productname",product_name);
                                        bundle.putString("productcategory",product_categories);
                                        bundle.putString("product_url",product_video);
                                        bundle.putString("AVG",AVG);
                                        bundle.putString("id", id);
                                        bundle.putString("position","");
                                        bundle.putInt("value", Scanqr.value);
                                        // bundle.putString("image",productimages);
                                        Fragment loginActivity1=new Product_detail();
                                        loginActivity1.setArguments(bundle);
                                        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.container_body, loginActivity1).commit();*/

                                    }
else{
                                        //Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
                                        int  scanvalue=3;

                                        Intent intent=new Intent(getActivity(),Navigation_Acivity.class);
                                        intent.putExtra("value",Scanqr.value);

                                        intent.putExtra("scanvalue",scanvalue);

                                        startActivity(intent);
                                    }
                                }



                            } else {
                                 //Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
                             int  scanvalue=3;

                                Intent intent=new Intent(getActivity(),Navigation_Acivity.class);
                                intent.putExtra("value",Scanqr.value);

                                intent.putExtra("scanvalue",scanvalue);

                                startActivity(intent);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }


                }
                // Initialize the AsyncTask class

            }
        }
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first
        // generic type oodf the AsyncTask
        // We are passing the connectWithHttpGet() meth arguments to that
        httpGetAsyncTask.execute();
    }

}
