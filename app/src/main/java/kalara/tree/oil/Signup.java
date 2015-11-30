package kalara.tree.oil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class Signup  extends Fragment implements View.OnClickListener{
    EditText username,mobileno,emailid,password,confirm;
    TextView dob;
    ImageView imageView;
    Button submit;
    ProgressDialog pDialog;
    String user,mobile,email,pass,dob1;
    static Calendar calendar;
    public static int REQUEST_CAMERA = 1;
    public static String PROJECT_NUMBER = "321291005565";

    GoogleCloudMessaging gcm;
    public static String regid;
    File imgFile;
    public static int SELECT_FILE = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.signup, container, false);
        calendar=Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        getGcmRegisterId();

        Navigation_Acivity.title.setText("Signup");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
       username=(EditText)layout.findViewById(R.id.username);
        mobileno=(EditText)layout.findViewById(R.id.mobileno);
        emailid=(EditText)layout.findViewById(R.id.emailid);
        password=(EditText)layout.findViewById(R.id.password);
        confirm=(EditText)layout.findViewById(R.id.confirmpassword);
        imageView=(ImageView)layout.findViewById(R.id.image1);
        imageView.setOnClickListener(this);
        dob=(TextView)layout.findViewById(R.id.dob);
        dob.setOnClickListener(this);
        user=username.getText().toString().trim();
        mobile=mobileno.getText().toString().trim();
        email=emailid.getText().toString().trim();
        pass=password.getText().toString().trim();
        dob1=dob.getText().toString().trim();
        submit=(Button)layout.findViewById(R.id.submit);
        submit.setOnClickListener(this);
if(Crop_image.outputFile!=null) {
    Picasso.with(getActivity())
            .load(Crop_image.outputFile)
            .transform(new RoundedTransformation(100, 0))
            .resize(80, 80)
            .into(imageView);
}
        else{

        }
        return layout;
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(calendar.getTime()));
    }
    @Override
    public void onClick(View v) {
if(v.getId()==R.id.submit){
    user=username.getText().toString().trim();
    mobile=mobileno.getText().toString().trim();
    email=emailid.getText().toString().trim();
    pass=password.getText().toString().trim();
    dob1=dob.getText().toString().trim();
connectWithHttpPost(user,mobile,email,pass,dob1);
}
        if(v.getId()==R.id.image1){
           // selectImage();
            /*Fragment loginActivity1 = new Crop_image();



            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, loginActivity1).commit();*/
            startActivityForResult(getPickImageChooserIntent(), 200);
        }
        if(v.getId()==R.id.dob){
            new DatePickerDialog(getActivity(), date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    protected void connectWithHttpPost(final String username1,final String mobile,final String email,final String pass, final String dobirth) {
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
                pDialog.show();
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=register";
                    HttpPost post = new HttpPost(postURL);
                    System.out.println("========in============"+username1+" "+mobile+" "+email);


                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("username", new org.apache.http.entity.mime.content.StringBody(username1));
                    postEntity.addPart("email",new org.apache.http.entity.mime.content.StringBody(email));
                    postEntity.addPart("mobile",new org.apache.http.entity.mime.content.StringBody(mobile));
                    postEntity.addPart("pass",new org.apache.http.entity.mime.content.StringBody(pass));
                    postEntity.addPart("dob", new org.apache.http.entity.mime.content.StringBody(dobirth));


                    System.out.println("========in============"+postEntity);


                    if(imgFile!=null){
                        FileBody body=new FileBody(imgFile);
                        System.out.println("bodyyyy"+body);
                        postEntity.addPart("image", body);
                    }
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
                    pDialog.dismiss();
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
                                Toast.makeText(getActivity(), "Submitted Successfully..", Toast.LENGTH_SHORT).show();
                                signin(email,pass);
                            }
                            else{
                                Toast.makeText(getActivity(), "This User Already Registered..", Toast.LENGTH_SHORT).show();
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
        httpGetAsyncTask.execute(username1, mobile, email, pass, dobirth);

    }

    public void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                  startActivityForResult(
                          Intent.createChooser(intent, "Select File"),
                          SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



        @Override
        public void onActivityResult(int  requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                Uri imageUri =  getPickImageResultUri(data);
                Fragment loginActivity1 = new Crop_image();

Bundle bundle=new Bundle();
                bundle.putString("URi",imageUri.toString());
loginActivity1.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.container_body, loginActivity1).commit();
                //mCropImageView.setImageUri(imageUri);

                // Toast.makeText(getActivity(), "Hello", Toast.LENGTH_LONG).show();
            }
        }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
    private void getGcmRegisterId() {
        // TODO Auto-generated method stub
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging
                                .getInstance(getActivity());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = regid;
                    Log.i("GCM", msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                // etRegId.setText(msg + "\n");
                regid = msg;
                System.out.println("==========Gcm Registrationid=============="
                        + msg);
            }
        }.execute(null, null, null);
    }
    protected void signin(final String email,final String pass) {
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
                pDialog.show();
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=login";
                    HttpPost post = new HttpPost(postURL);
                    System.out.println("========in============");


                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("email", new org.apache.http.entity.mime.content.StringBody(email));
                    postEntity.addPart("pass",new org.apache.http.entity.mime.content.StringBody(pass));

                    postEntity.addPart("gcm",new org.apache.http.entity.mime.content.StringBody(regid));
                    System.out.println("========in============"+postEntity);



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
                    pDialog.dismiss();
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
                            JSONArray array=null;
                            JSONObject object1;
                            if(object.equals("true")){

                                String  respose=json.getString("response");
                                array=new JSONArray(respose);
                                for(int i=0;i<array.length();i++){
                                    object1=array.getJSONObject(i);
                                    String id=object1.getString("id");
                                    String email=object1.getString("email");
                                    String username=object1.getString("username");
                                    String dob=object1.getString("dob");
                                    String mobileno=object1.getString("mobile_no");
                                    String image=object1.getString("image");
                                    SharedPreferences preferences=getActivity().getSharedPreferences("Login",0);
                                    SharedPreferences.Editor editor;
                                    editor=preferences.edit();
                                    editor.putString("id", id);
                                    editor.putString("email", email);
                                    editor.putString("username", username);
                                    editor.putString("dob", dob);
                                    editor.putString("mobile_no", mobileno);
                                    editor.putString("image", image);
                                    //editor.putInt("value", i);
                                    editor.commit();
                                    i=0;
                                    int  j=1;

                                    Intent  intent=new Intent(getActivity(),Navigation_Acivity.class);
                                    intent.putExtra("value",i);
                                    intent.putExtra("scanvalue", j);
                                    startActivity(intent);
                                    getActivity().finish();

                                    Toast.makeText(getActivity(), "Login Successfully..", Toast.LENGTH_SHORT).show();
                                }




                            }
                            else{
                                Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
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
        httpGetAsyncTask.execute(email, pass);

    }
    public Intent getPickImageChooserIntent() {

// Determine Uri of camera image to  save.
        Uri outputFileUri =  getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager =  getActivity().getPackageManager();

// collect all camera intents
        Intent captureIntent = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam =  packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new  Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

// collect all gallery intents
        Intent galleryIntent = new  Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery =  packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new  Intent(galleryIntent);
            intent.setComponent(new  ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

// the main intent is the last in the  list (fucking android) so pickup the useless one
        Intent mainIntent =  allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if  (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity"))  {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

//Create a chooser from the main  intent
        Intent chooserIntent =  Intent.createChooser(mainIntent, "Select source");

//Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,  allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;

    }
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getActivity().getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new  File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from  {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera  and gallery image.
     *
     * @param data the returned data of the  activity result
     */
    public Uri getPickImageResultUri(Intent  data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null  && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ?  getCaptureImageOutputUri() : data.getData();
    }
}
