package kalara.tree.oil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Library_detail_page extends Fragment implements View.OnClickListener {


    TextView reporter, size, barcode, product, comment;
    Button delete;
    ProgressDialog pDialog;
    Gallery gallery;
    ImageSwitcher sw;
    String product_id;
    private ImageView img_back, img_next;
    int pos;
    List<Knowlegde_item> knowlegde_items = new ArrayList<Knowlegde_item>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<HashMap<String, String>> Datalist1;
    ImageView myView;
    TextView date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.library_detail_page, container, false);


        Navigation_Acivity.title.setText("My Library");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        Datalist1 = new ArrayList<HashMap<String, String>>();
        product_id = getArguments().getString("id");
        //  position=getArguments().getInt("position");
        sw = (ImageSwitcher) layout.findViewById(R.id.imageSwitcher);
        myView = new ImageView(getActivity());
        myView.setScaleType(ImageView.ScaleType.FIT_XY);
        myView.setLayoutParams(new ImageSwitcher.LayoutParams(300, 300));
        myView.setBackgroundResource(R.drawable.thumbnail);
        img_back = (ImageView) layout.findViewById(R.id.know_lib_back_arrow);
        img_next = (ImageView) layout.findViewById(R.id.know_lib_next_arrow);
        // Note that Gallery view is deprecated in Android 4.1---
        gallery = (Gallery) layout.findViewById(R.id.myGallery);
        product = (TextView) layout.findViewById(R.id.product);
        reporter = (TextView) layout.findViewById(R.id.reporter);
        size = (TextView) layout.findViewById(R.id.size);
        barcode = (TextView) layout.findViewById(R.id.barcode);
        comment = (TextView) layout.findViewById(R.id.comments);
        date = (TextView) layout.findViewById(R.id.date);
        delete = (Button) layout.findViewById(R.id.delete);
        delete.setOnClickListener(this);
        getproducts();
        return layout;
    }

    protected void getproducts() {
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

                String url = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=getreport";


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
                                    String id = object1.getString("id");
                                    if (id.equals(product_id)) {
                                        // String report = object1.getString("report");
                                        String userid = object1.getString("userid");
                                        String product1 = object1.getString("product");
                                        String manufacturer = object1.getString("manufacturer");

                                        String barcode1 = object1.getString("barcode");
                                        String size1 = object1.getString("size");
                                        String comments = object1.getString("comments");
                                        String product_video = object1.getString("brand");
                                        String time = object1.getString("time");
                                        String[] separated = time.split(" ");
                                        String part1= separated[0];
                                        SimpleDateFormat readFormat = new SimpleDateFormat("yyyy-mm-dd");
                                        SimpleDateFormat writeFormat = new SimpleDateFormat("MMM dd,yyyy");

                                        java.util.Date date1;

                                        try {
                                            date1 = readFormat.parse(part1);
                                            String finaldate=writeFormat.format(date1);
                                            date.setText(finaldate);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        product.setText(product1);
                                        barcode.setText(barcode1);
                                        size.setText(size1);
                                        comment.setText(comments);

                                       // JSONArray product_image = object1.getJSONArray("image");

                                        HashMap<String, String> data = new HashMap<String, String>();
                                        data.put("id", id);

                                        data.put("barcode", barcode1);
                                        // data.put("product", product);
                                        data.put("size", size1);

                                        data.put("userid", userid);
                                        data.put("product", product1);
                                        data.put("manufacturer", manufacturer);
                                        data.put("comments", comments);

                                        JSONArray product_image1 = object1.getJSONArray("report_image");
                                        for (int j = 0; j < product_image1.length(); j++) {

                                            String product_image2 = product_image1.get(j).toString();

                                            list.add(product_image2);

                                            reporter.setText(product1);


                                            if (product_image2.equals("")) {
                                                sw.setFactory(new ViewSwitcher.ViewFactory() {
                                                    @Override
                                                    public View makeView() {
                                                        sw.removeView(myView);
                                                       /* Picasso.with(getActivity())

                                                                .load(list.get(0))
                                                                .into(myView);*/
                                                        myView.setImageResource(R.drawable.icn);
                                                        return myView;
                                                    }
                                                });

                                            } else {
                                                sw.setFactory(new ViewSwitcher.ViewFactory() {
                                                    @Override
                                                    public View makeView() {
                                                        sw.removeView(myView);
                                                        Picasso.with(getActivity())

                                                                .load(list.get(0))
                                                                .into(myView);
                                                        return myView;
                                                    }
                                                });

                                                // sw.setImageResource(imageIDs[0]);
                                                img_next.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                       // Toast.makeText(getActivity(), "  Next Image" + pos, Toast.LENGTH_LONG).show();
                                                        // sw.setImageResource(imageIDs[++pos]);

                                                        if(pos<list.size()-1) {
                                                            Picasso.with(getActivity())
                                                                    .load(list.get(++pos))
                                                                    .into(myView);
                                                        }
                                                        else{
                                                            //  Toast.makeText(getActivity(), "  Next Image" + pos, Toast.LENGTH_LONG).show();
                                                        }
                                                        //gallery.setSelection(pos);
                                                    }
                                                });

                                                img_back.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                      //  Toast.makeText(getActivity(), "previous Image" + pos, Toast.LENGTH_LONG).show();
                                                        if (pos == 0) {
                                                            // sw.setImageResource(imageIDs[0]);
                                                            Picasso.with(getActivity())
                                                                    .load(list.get(0))
                                                                    .into(myView);

                                                            // imageView.setImageResource(imageIDs[pos]);
//            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
//                    imageView.setBackgroundResource(itemBackground);

                                                        } else {
                                                            //sw.setImageResource(imageIDs[--pos]);
                                                            Picasso.with(getActivity())
                                                                    .load(list.get(--pos))
                                                                    .into(myView);
                                                            // gallery.setSelection(pos);

//                    gallery.setSelected(true);

                                                        }


                                                    }
                                                });


                                                gallery.setFadingEdgeLength(0);
                                                gallery.setAdapter(new ImageAdapter(getActivity(), list));
                                                gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                        pos = position;
                                                        Toast.makeText(getActivity(), "pic" + (position + 1) + " selected",
                                                                Toast.LENGTH_SHORT).show();
                                                        // display the images selected
//                ImageView imageView = (ImageView) findViewById(R.id.know_lib_img);
                                                        // sw.setImageResource(imageIDs[position]);
                                                        Picasso.with(getActivity())

                                                                .load(list.get(pos))
                                                                .into(myView);

                                                    }
                                                });

                                            }
                                        }


                                        Datalist1.add(data);
                                        System.out.println("vallllllll of thhhh" + id + " " + Datalist1);

                                    }

                                }


                            } else {
                                // Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
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

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        ArrayList<String> arrayList;

        public ImageAdapter(Context c, ArrayList<String> arrayList) {
            this.context = c;
            this.arrayList = arrayList;
            // sets a grey background; wraps around the images
           /* TypedArray a = context.obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();*/
        }

        // returns the number of images
        public int getCount() {
            return arrayList.size();
        }

        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }

        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }

        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.productt_itm, parent, false);
            }
            ImageView imageView = (ImageView) row.findViewById(R.id.image);

         /*   imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(30, 30);
            imageView.setLayoutParams(layoutParams);*/
            Picasso.with(getActivity())
                    .load(arrayList.get(position))
                    .into(imageView);
            //   imageView.setImageResource(imageIDs[position]);
//            imageView.setLayoutParams(new Gallery.LayoutParams(100, 100));
            imageView.setBackgroundResource(R.drawable.thumbnail);
            return row;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete) {
            SendParameter();
        }
    }

    private void SendParameter() {


        // TODO Auto-generated method stub
        // Make RESTful webservice call using
        SharedPreferences preferences = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);

        String userid1 = preferences.getString("id", null);

        RequestParams params = new RequestParams();
        params.put("id", product_id);


        AsyncHttpClient client = new AsyncHttpClient();

        System.out.println();

        client.post("http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=delreport", params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    public void onStart() {
                        super.onStart();
                        pDialog = new ProgressDialog(getActivity());
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);

                        pDialog.show();
                    }

                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog

                        System.out.println("=========response=========="
                                + response);
                        pDialog.hide();
                        if (pDialog != null) {
                            pDialog.dismiss();
                        }

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            if (json != null) {
                                String result = json.getString("result");
                                if (result.equals(true)) {
                                    Fragment fragment = new Library();

                                    //fragment.setArguments(bundle);
                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.container_body, fragment).commit();
                                } else {
                                    Toast.makeText(getActivity(), "Failed to delete a Report", Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                        // When the response returned by REST has Http
                    // response code other than '200' such as '404',
                    // '500' or '403' etc
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        // Hide Progress Dialog
                        pDialog.hide();
                        if (pDialog != null) {
                            pDialog.dismiss();
                        }
                        // When Http response code is '404'
                        if (statusCode == 404) {
                            Toast.makeText(getActivity(),
                                    "Connection error",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code is '500'
                        else if (statusCode == 500) {
                            Toast.makeText(getActivity(),
                                    "Internal server error",
                                    Toast.LENGTH_LONG).show();
                        }
                        // When Http response code other than 404, 500
                        else {
                            Toast.makeText(getActivity(),
                                    "Connection error ", Toast.LENGTH_LONG)
                                    .show();

                        }
                    }
                });


    }
}