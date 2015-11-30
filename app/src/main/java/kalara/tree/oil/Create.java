package kalara.tree.oil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Create extends Fragment implements View.OnClickListener{
    EditText manufacture,brand,size,product,comment;
    Button create;
    int scanvalue;
    String productname,sizename,brandname,manufacturername,comments,productimage,id,location,result,barcode,images,productid;
    ProgressDialog pDialog;
    ImageView producticon;
    String userid1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.createreport, container, false);
      Navigation_Acivity.title.setText("Create Report");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        SharedPreferences preferences = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);





         userid1 = preferences.getString("id", null);
        id=getArguments().getString("id");

       productname=getArguments().getString("product_name");
        sizename=getArguments().getString("size");
        result=getArguments().getString("result");
        barcode=getArguments().getString("barcode");
        brandname=getArguments().getString("brand");
        productid=getArguments().getString("productid");
       manufacturername=getArguments().getString("manufacturername");
        comments=getArguments().getString("comments");
       location=getArguments().getString("location");
images=getArguments().getString("productimage");
        scanvalue=getArguments().getInt("scanvalue");
        System.out.println("hellll" + location+" "+manufacturername);
        manufacture=(EditText)layout.findViewById(R.id.manufacturer);
        manufacture.setText(manufacturername);
        producticon=(ImageView)layout.findViewById(R.id.icon11);
        brand=(EditText)layout.findViewById(R.id.brand);
brand.setText(brandname);
        size=(EditText)layout.findViewById(R.id.size);
        size.setText(sizename);
        product=(EditText)layout.findViewById(R.id.product);
        product.setText(productname);
        comment=(EditText)layout.findViewById(R.id.comments);
comment.setText(comments);
        create=(Button)layout.findViewById(R.id.create);
        create.setOnClickListener(this);
        if(Camera_Activity.imageGallery.size()-1<=0){
String image1=ScannerFragment.arrayList.get(0);
            Picasso.with(getActivity()).load(image1).into(producticon);
              /*File imgFile = new File(ScannerFragment.arrayList.get(0));
                MediaScannerConnection.scanFile(getActivity(), new String[]{imgFile.toString()}, null, null);
                //	Toast.makeText(getApplicationContext(), "hello sir"+imgFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                System.out.println("imgaaa path in adapter11" + imgFile);
			    //Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
             // image.setImageBitmap(myBitmap);
                //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                // Toast.makeText(getApplicationContext(), "h "+myBitmap, Toast.LENGTH_SHORT).show();
                //	image.setImageURI(Uri.fromFile(imgFile));
               // Picasso.with(getActivity()).load(imgFile).into(producticon);
               // producticon.setImageURI(Uri.fromFile(imgFile));
                //File imgFile = new  File(“filepath”);
                if (imgFile.exists()) {
                    // ImageView myImage = new ImageView(this);

                   // producticon.setImageURI(Uri.fromFile(imgFile));
                   Picasso.with(getActivity()).load(imgFile.getAbsolutePath()).into(producticon);


            }*/
        }
        else{
            for(int i=0;i<Camera_Activity.imageGallery.size();i++){
                String image1= Camera_Activity.imageGallery.get(i);
                File imgFile = new  File(Camera_Activity.imageGallery.get(0));
                MediaScannerConnection.scanFile(getActivity(), new String[]{imgFile.toString()}, null, null);
                //	Toast.makeText(getApplicationContext(), "hello sir"+imgFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                System.out.println("imgaaa path in adapter12"+imgFile);
			    /*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
image.setImageBitmap(myBitmap);*/
                //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
                // Toast.makeText(getApplicationContext(), "h "+myBitmap, Toast.LENGTH_SHORT).show();
                //	image.setImageURI(Uri.fromFile(imgFile));


                //File imgFile = new  File(“filepath”);
                if(imgFile.exists())
                {
                    // ImageView myImage = new ImageView(this);

                    producticon.setImageURI(Uri.fromFile(imgFile));

                }

            }
        }

        return layout;
    }

    @Override
    public void onClick(View v) {
if(v.getId()==R.id.create){
    productname=product.getText().toString().trim();
    sizename=size.getText().toString().trim();
    brandname=brand.getText().toString().trim();
    manufacturername=manufacture.getText().toString().trim();
    comments=comment.getText().toString().trim();
if(scanvalue==7) {
    Toast.makeText(getActivity(),"saver",Toast.LENGTH_SHORT).show();
    connectWithHttpPost(manufacturername, brandname, productname, sizename, comments);
}
    else if(scanvalue==2){
    Toast.makeText(getActivity(),"builder",Toast.LENGTH_SHORT).show();
    SeconedMethod(manufacturername, brandname, productname, sizename, comments);
    }
}
    }

    protected void connectWithHttpPost(final String manufacturername,final String brandname, final String productname,final String sizename,final String comments) {
        // TODO Auto-generated method stub
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
            //private final String LOGTAG = "Login";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog


                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
          //      pDialog.show();
            }
            @Override
            protected String doInBackground(String... params) {



                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the
                // values through the URL

                //String url = "http://www.support-4-pc.com/clients/jie/subscriber.php?action=register&email="
                //		+ paramEmail+ "&&pass=" + paramPassword+"&&username="+paramUsername+"&&dob="+paramDob+"&&profilepic="+imageqq;


                //System.out.println("=======url======" + url);

                //HttpGet httpGet = new HttpGet(url);



                //System.out.println("=======url=1=====" + httpGet);

                try {


                    String postURL = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=submit_report";
                    HttpPost post = new HttpPost(postURL);
                    //     System.out.println("========in============"+username1+" "+mobile+" "+email);
//

                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("manufacturer", new org.apache.http.entity.mime.content.StringBody(manufacturername));
                    postEntity.addPart("barcode", new org.apache.http.entity.mime.content.StringBody(result));
                    postEntity.addPart("size", new org.apache.http.entity.mime.content.StringBody(sizename));
                    postEntity.addPart("brand", new org.apache.http.entity.mime.content.StringBody(brandname));
                    postEntity.addPart("comments", new org.apache.http.entity.mime.content.StringBody(comments));
                    postEntity.addPart("product", new org.apache.http.entity.mime.content.StringBody(productname));
                    postEntity.addPart("userid", new org.apache.http.entity.mime.content.StringBody(userid1));
                    postEntity.addPart("productid", new org.apache.http.entity.mime.content.StringBody(""));
                    postEntity.addPart("location", new org.apache.http.entity.mime.content.StringBody(location));
                    if (Camera_Activity.imageGallery.size()-1 <= 0) {
                        for (int i = 0; i < Camera_Activity.imageGallery.size(); i++) {

                            String imagepathhh = Camera_Activity.imageGallery.get(i);
                            System.out.println("bodyyyy edit1" + imagepathhh);
                            final File imgFile = new File(imagepathhh);
                            if (imgFile != null) {
                                FileBody body = new FileBody(imgFile);
                                System.out.println("bodyyyy" + body);
                                postEntity.addPart("report_image[]", body);
                            }
                            System.out.println("========in============" + postEntity);

                        }
                    } else {
                        for (int i = 0; i < Camera_Activity.imageGallery.size(); i++) {

                            String imagepathhh = Camera_Activity.imageGallery.get(i);
                            System.out.println("bodyyyy edit2" + imagepathhh);
                            final File imgFile = new File(imagepathhh);
                            if (imgFile != null) {
                                FileBody body = new FileBody(imgFile);
                                System.out.println("bodyyyy" + body);
                                postEntity.addPart("report_image[]", body);
                            }
                            System.out.println("========in============" + postEntity);

                        }
                    }
                   /* if(imgFile!=null){
                        FileBody body=new FileBody(imgFile);
                        System.out.println("bodyyyy"+body);
                        postEntity.addPart("image", body);
                    }*/
                    post.setEntity(postEntity);

                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponntse
                    HttpResponse httpResponse = httpClient.execute(post);

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
                  //  pDialog.dismiss();
                System.out.println("====================" + result);

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
                       try
                        {
                            String   object = json.getString("result");
                            System.out.println("object"+object);
                            if(object.equals("true")){
                                Toast.makeText(getActivity(), "Report created Successfully..", Toast.LENGTH_SHORT).show();

                                Fragment fragment=new Library();

                                //fragment.setArguments(bundle);
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container_body, fragment).commit();
                            }
                            else{
                                Toast.makeText(getActivity(), "Failed to create a report..", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }





                }

            }

        }
        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first
        // generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute(productname,sizename);

    }
    protected void SeconedMethod(final String manufacturername,final String brandname, final String productname,final String sizename,final String comments) {
        // TODO Auto-generated method stub
        class HttpGetAsyncTask extends AsyncTask<String, Void, String> {
            //private final String LOGTAG = "Login";

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog


                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                //      pDialog.show();
            }
            @Override
            protected String doInBackground(String... params) {



                HttpClient httpClient = new DefaultHttpClient();

                // Sending a GET request to the web page that we want
                // Because of we are sending a GET request, we have to pass the
                // values through the URL

                //String url = "http://www.support-4-pc.com/clients/jie/subscriber.php?action=register&email="
                //		+ paramEmail+ "&&pass=" + paramPassword+"&&username="+paramUsername+"&&dob="+paramDob+"&&profilepic="+imageqq;


                //System.out.println("=======url======" + url);

                //HttpGet httpGet = new HttpGet(url);



                //System.out.println("=======url=1=====" + httpGet);

                try {


                    String postURL = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=editreport";
                    HttpPost post = new HttpPost(postURL);
                    //     System.out.println("========in============"+username1+" "+mobile+" "+email);
//

                    MultipartEntity postEntity = new MultipartEntity();
                    postEntity.addPart("id", new org.apache.http.entity.mime.content.StringBody(id));
                    postEntity.addPart("manufacturer", new org.apache.http.entity.mime.content.StringBody(manufacturername));
                    postEntity.addPart("barcode", new org.apache.http.entity.mime.content.StringBody(barcode));
                    postEntity.addPart("size", new org.apache.http.entity.mime.content.StringBody(sizename));
                    postEntity.addPart("brand", new org.apache.http.entity.mime.content.StringBody(brandname));
                    postEntity.addPart("comments", new org.apache.http.entity.mime.content.StringBody(comments));
                    postEntity.addPart("product", new org.apache.http.entity.mime.content.StringBody(productname));
                    postEntity.addPart("userid", new org.apache.http.entity.mime.content.StringBody(userid1));
                    postEntity.addPart("productid", new org.apache.http.entity.mime.content.StringBody(productid));
                    postEntity.addPart("location", new org.apache.http.entity.mime.content.StringBody(location));
                   // postEntity.addPart("report_image[]",new org.apache.http.entity.mime.content.StringBody("") );

                    if (Camera_Activity.imageGallery.size()-1 <= 0) {
                      //  postEntity.addPart("report_image[]",new org.apache.http.entity.mime.content.StringBody("") );
                       for (int i = 0; i < ScannerFragment.arrayList.size(); i++) {

                            String imagepathhh = ScannerFragment.arrayList.get(i);
                            System.out.println("bodyyyy level" + imagepathhh);
                            final File imgFile1 = new File("imagepathhh");
                           /* FileBody body = new FileBody(imgFile);
                            postEntity.addPart("report_image[]",body );*/
                           System.out.println("bodyyyy 3" + imgFile1);
                            if (imgFile1 != null) {
                                FileBody body1= new FileBody(imgFile1);
                                System.out.println("bodyyyy 3" + body1);

                            }
                            System.out.println("========in============" + postEntity);

                        }
                    } else {
                        for (int i = 0; i < Camera_Activity.imageGallery.size(); i++) {

                            String imagepathhh = Camera_Activity.imageGallery.get(i);
                            System.out.println("bodyyyy 4" + imagepathhh);
                            String[] bits = imagepathhh.split("/");
                            String lastOne = bits[bits.length-1];
                            System.out.println("bodyyyy 4" + lastOne);


                            System.out.println("bodyyyy 4" + lastOne);
                            final File imgFile = new File(imagepathhh);
                            if (imgFile != null) {
                                FileBody body = new FileBody(imgFile);
                                System.out.println("bodyyyy" + body);
                                postEntity.addPart("report_image[]", body);
                            }
                            System.out.println("========in============" + postEntity);

                        }
                    }
                   /* if(imgFile!=null){
                        FileBody body=new FileBody(imgFile);
                        System.out.println("bodyyyy"+body);
                        postEntity.addPart("image", body);
                    }*/
                    post.setEntity(postEntity);

                    // execute(); executes a request using the default context.
                    // Then we assign the execution result to HttpResponntse
                    HttpResponse httpResponse = httpClient.execute(post);

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
                    //  pDialog.dismiss();
                    System.out.println("====================" + result);

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
                        try
                        {
                            String   object = json.getString("result");
                            System.out.println("object"+object);
                            if(object.equals("true")){
                                Toast.makeText(getActivity(), "Report created Successfully..", Toast.LENGTH_SHORT).show();

                                Fragment fragment=new Library();

                                //fragment.setArguments(bundle);
                                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.container_body, fragment).commit();
                            }
                            else{
                                Toast.makeText(getActivity(), "Failed to create a report..", Toast.LENGTH_SHORT).show();
                            }

                        }
                        catch (JSONException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }





                }

            }

        }
        // Initialize the AsyncTask class
        HttpGetAsyncTask httpGetAsyncTask = new HttpGetAsyncTask();
        // Parameter we pass in the execute() method is relate to the first
        // generic type of the AsyncTask
        // We are passing the connectWithHttpGet() method arguments to that
        httpGetAsyncTask.execute(productname,sizename);

    }


}
