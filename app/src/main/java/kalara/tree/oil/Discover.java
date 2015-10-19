package kalara.tree.oil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by avigma19 on 10/7/2015.
 */
public class Discover extends Fragment {
    GridView grid;
    ProgressDialog pDialog;
    ImageLoader imageloader;
    Context context;
    static ArrayList<Knowlegde_item> knowlegde_items=new ArrayList<Knowlegde_item>();
    ArrayList<HashMap<String, String>> Datalist1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.discover_productt, container, false);
        this.context=getActivity();
       Navigation_Acivity.title.setText("Discover Products");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        Datalist1=new ArrayList<HashMap<String,String>>();
        grid=(GridView)layout.findViewById(R.id.gridview);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String productname=Datalist1.get(position).get("product_name").toString();
                    String productcategory=Datalist1.get(position).get("product_categories").toString();
              //  String productid=Datalist1.get(position).get("").toString();
                    String id1=Datalist1.get(position).get("id").toString();
                    Bundle bundle=new Bundle();
                    bundle.putString("productname",productname);
                    bundle.putString("productcategory",productcategory);
                bundle.putInt("position", position);
                bundle.putString("id",id1);

                   // bundle.putString("image",productimages);
                    Fragment loginActivity1=new Product_detail();
                    loginActivity1.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container_body, loginActivity1).commit();
            }
        });

        imageloader=new ImageLoader(context);
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

                String url = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=getproduct";


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
                                   // String report = object1.getString("report");
                                    String barcode = object1.getString("barcode");
                                    //String product = object1.getString("product");
                                    String size = object1.getString("size");

                                    String status = object1.getString("status");
                                    String product_categories = object1.getString("category");
                                    String product_name = object1.getString("product_name");
                                    String product_video = object1.getString("product_video");
                                    String time = object1.getString("time");
                                    // String product_image = object1.getString("product_image");
                                    JSONArray product_image = object1.getJSONArray("product_image");
                                    /*knowlegde_items.get(i).setId(id);
                                    knowlegde_items.get(i).setProductcategory(product_categories);
                                    knowlegde_items.get(i).setBarcode(barcode);
                                    knowlegde_items.get(i).setSize(size);
                                    knowlegde_items.get(i).setProductname(product_name);
                                    knowlegde_items.get(i).setProductvideo(product_video);
                                    knowlegde_items.get(i).setStatus(status);
                                    knowlegde_items.get(i).setTime(time);*/
                                    for(int j=0;j<product_image.length();j++){
                                        String image = product_image.get(j).toString();

                                        knowlegde_items.add(new Knowlegde_item(i,image,id));
                                        System.out.println("imagesss are" + image+ " "+knowlegde_items.size());
                                    }


                                   HashMap<String,String> data=new HashMap<String, String>();

                                    int j;
                                  //  *//*for( j=0;j<product_image.length();j++) {
                                       // String image = product_image.get(j).toString();
                                      //  data.put("image", product_image.get(j).toString());


                                   // }*//*
                                    data.put("id", id);

                                    data.put("barcode", barcode);
                                    // data.put("product", product);
                                    data.put("size", size);

                                    data.put("status",status);
                                    data.put("product_categories",product_categories);
                                    data.put("product_name",product_name);
                                    data.put("product_video",product_video);
                                    Datalist1.add(data);
                                    System.out.println("vallllllll of thhhh" + id + " " + Datalist1);
                                    /*knowlegde_items.add(new Knowlegde_item(id, report, barcode, product, size, image, status, product_categories,
                                            product_name, product_video, time));*/

                                /*    Knowledgeadapter knowledgeadapter=new Knowledgeadapter(getActivity(),knowlegde_items);
                                    knowledgeadapter.notifyDataSetChanged();
                                    grid.setAdapter(knowledgeadapter);*/
                                    Knowledgeadapter adapter=new Knowledgeadapter(getActivity(),Datalist1);
                                    grid.setAdapter(adapter);

                                }



                            } else {
                                Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
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
        public class Knowledgeadapter extends BaseAdapter {

            Activity context;
            ArrayList<HashMap<String, String>> datArrayList;



            public Knowledgeadapter(Activity activity, ArrayList<HashMap<String, String>> datalist1)
            {
                // TODO Auto-generated constructor stub
                super();
                this.context=activity;
                this.datArrayList=datalist1;
            }


        @Override
        public int getCount()
        {
            // TODO Auto-generated method stub
            return datArrayList.size();
        }

        @Override
        public Object getItem(int position)
        {
            // TODO Auto-generated method stub
            return datArrayList.get(position);
        }

        @Override
        public long getItemId(int position)
        {
            // TODO Auto-generated method stub
            return datArrayList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // TODO Auto-generated method stub
            View row=convertView;
            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.discover_item, parent,false);
            }

            TextView product=(TextView)row.findViewById(R.id.productname);
            TextView category=(TextView)row.findViewById(R.id.categoryname);
            ImageView productimage=(ImageView)row.findViewById(R.id.productimage);
            HashMap<String, String> map = datArrayList.get(position);
String image=knowlegde_items.get(position).getProductimage();
            product.setText(map.get("product_name").toString());
            category.setText(map.get("product_categories").toString());
            System.out.println("image is"+image);
            //imageloader.DisplayImage(image, productimage);
            Picasso.with(getActivity())
                    .load(image)
                    .into(productimage);
            /*for(int i=0;i<datArrayList.size();i++){
               // productimage.setImageResource(datArrayList.get(i).getProductname());

                product.setText(datArrayList.get(i).getProduct());

            }*/

            return row;
        }

    }
}
