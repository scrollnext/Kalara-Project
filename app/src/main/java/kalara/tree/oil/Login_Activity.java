package kalara.tree.oil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.gcm.GoogleCloudMessaging;

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

public class Login_Activity extends Fragment implements View.OnClickListener {
    public CallbackManager callbackManager;
    public TextView textView;
    File imgFile;
SharedPreferences sharedPreferences;
    public AccessTokenTracker accessTokenTracker;
    public ProfileTracker profileTracker;
   LoginButton loginButton;
    SharedPreferences.Editor editor;
    static int i=1;
    TextView create;

    EditText username,password;
    GoogleCloudMessaging gcm;
    public static String PROJECT_NUMBER = "321291005565";

    public static String regid;
    TextView Forgotpassword;
    CheckBox remember;
    String user1,pass1;
    Button signin;
    ProgressDialog pDialog;
    public FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();

          /*  Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);*/
            Profile profile = Profile.getCurrentProfile();

            if (profile != null) {
              String  facebook_id=profile.getId();
                String   f_name=profile.getFirstName();
                String    m_name=profile.getMiddleName();
                String   l_name=profile.getLastName();
                String    full_name=profile.getName();
                String   profile_image=profile.getProfilePictureUri(400, 400).toString();
               // Toast.makeText(getActivity(),"Wait..."+f_name+" "+profile_image,Toast.LENGTH_SHORT).show();
                System.out.println("wait..." + " " + profile_image + " " + facebook_id);

                connectWithHttpPost(full_name,"" , facebook_id, "", "");

            }
        //  Toast.makeText(getActivity(),"Wait...",Toast.LENGTH_SHORT).show();
            GraphRequest request = GraphRequest.newMeRequest(accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                String   email_id = object.getString("email");
                                String   gender = object.getString("gender");
                             //   Toast.makeText(getActivity(),"Wait..."+email_id,Toast.LENGTH_SHORT).show();
                                //Start new activity or use this info in your project.
                                /*Intent i = new Intent(FacebookLogin.this, LoginSuccess.class);
                                i.putExtra("type", "facebook");
                                i.putExtra("facebook_id", facebook_id);
                                i.putExtra("f_name", f_name);
                                i.putExtra("m_name", m_name);
                                i.putExtra("l_name", l_name);
                                i.putExtra("full_name", full_name);
                                i.putExtra("profile_image", profile_image);
                                i.putExtra("email_id", email_id);
                                i.putExtra("gender", gender);

                                progress.dismiss();
                                startActivity(i);
                                finish();*/
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                //  e.printStackTrace();
                            }

                        }

                    });

            request.executeAsync();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    public Login_Activity() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker= new AccessTokenTracker() {
            @Override
            public void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
               // displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.activity_login_, container, false);
        getGcmRegisterId();

        create=(TextView)layout.findViewById(R.id.create);
        create.setOnClickListener(this);
        username=(EditText)layout.findViewById(R.id.username);
        password=(EditText)layout.findViewById(R.id.password);
        signin=(Button)layout.findViewById(R.id.signin);
        signin.setOnClickListener(this);
        String c="Forgot Password";
        String crreate="Create new account";
        Forgotpassword=(TextView)layout.findViewById(R.id.forgot);
        Forgotpassword.setOnClickListener(this);
        SpannableString spanString = new SpannableString(c);
        spanString.setSpan(new UnderlineSpan(), 0, spanString.length(), 0);

Forgotpassword.setText(spanString);
        SpannableString spanString1 = new SpannableString(crreate);
        spanString1.setSpan(new UnderlineSpan(), 0, spanString1.length(), 0);

        create.setText(spanString1);
        Navigation_Acivity.title.setText("Signin");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        remember=(CheckBox)layout.findViewById(R.id.remember);
        user1=username.getText().toString().trim();
        pass1=password.getText().toString().trim();
        File f = getActivity().getDatabasePath("/data/data/kalara.tree.oil/shared_prefs/remember.xml");

        if (f.exists()) {
            SharedPreferences preferences=getActivity().getSharedPreferences("remember", Context.MODE_PRIVATE);
            user1=preferences.getString("username",null);
            pass1=preferences.getString("email",null);
           username.setText(user1);
            password.setText(pass1);
            remember.setChecked(true);
        } else {
            username.setText("");
            password.setText("");
            remember.setChecked(false);

        }
        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(remember.isChecked()){

                    SharedPreferences preferences=getActivity().getSharedPreferences("remember",0);
                    SharedPreferences.Editor editor;
                    editor=preferences.edit();
                    editor.putString("username", user1);
                    editor.putString("email", pass1);
                    editor.commit();
                }
                else{
                    File f = getActivity().getDatabasePath("/data/data/kalara.tree.oil/shared_prefs/remember.xml");

                    if (f.exists()) {
                        f.delete();
                    }

                }
            }
        });
        return layout;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         loginButton = (LoginButton) view.findViewById(R.id.login_button);
        ImageView fbloginButton = (ImageView) view.findViewById(R.id.custom_login_button);
        fbloginButton.setOnClickListener(this);
        //textView = (TextView) view.findViewById(R.id.textView);

        loginButton.setReadPermissions("user_friends");
        loginButton.setReadPermissions("email");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);

      /* Intent intent=new Intent(getActivity(), Navigation_Acivity.class);
        intent.putExtra("value", i);
        intent.putExtra("scanvalue",j);
        startActivity(intent);
        SharedPreferences preferences=getActivity().getSharedPreferences("Login",0);
        SharedPreferences.Editor editor;
        editor=preferences.edit();

        editor.commit();*/
       /* Fragment loginActivity=new Demo();
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, loginActivity).commit();*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void displayMessage(Profile profile){
        if(profile != null){
            //textView.setText(profile.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
       // displayMessage(profile);
    }

    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.custom_login_button:
               loginButton.performClick();

               break;
           case R.id.create:
               Fragment loginActivity1=new Signup();
               android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
               fragmentManager.beginTransaction()
                       .replace(R.id.container_body, loginActivity1).commit();
               break;
           case R.id.signin:
           user1=username.getText().toString().trim();
         pass1=password.getText().toString().trim();
               if(username.getText().toString().trim().length()==0){
                   Toast.makeText(getActivity(),"Please enter Username",Toast.LENGTH_SHORT).show();
               }
               else if(username.getText().toString().trim().length()==0){
               Toast.makeText(getActivity(),"Please enter Password",Toast.LENGTH_SHORT).show();
           }
               else {
                   signin(user1, pass1);
               }
               break;

           case R.id.forgot:
               preview();
               break;
       }
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
                            JSONArray  array=null;
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
                                    File f = getActivity().getDatabasePath("/data/data/kalara.tree.oil/shared_prefs/remember.xml");

                                    if (f.exists()) {
                                        SharedPreferences preferences1 = getActivity().getSharedPreferences("remember", 0);
                                        SharedPreferences.Editor editor1;
                                        editor1 = preferences1.edit();
                                        editor1.putString("username", email);
                                        editor1.putString("email", pass);
                                        editor1.commit();
                                    }
                                    else{

                                    }
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
        httpGetAsyncTask.execute(email,pass);

    }
    protected void Forgotpass(final String email) {
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=forgotpass";
                    HttpPost post = new HttpPost(postURL);
                    System.out.println("========in============");


                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("email", new org.apache.http.entity.mime.content.StringBody(email));



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
                            JSONArray  array=null;
                            JSONObject object1;
                            if(object.equals("true")){



                                Toast.makeText(getActivity(), "Email id sent to your registered email address", Toast.LENGTH_SHORT).show();


                            }
                            else{
                                Toast.makeText(getActivity(), "Invalid emailid...", Toast.LENGTH_SHORT).show();
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
        httpGetAsyncTask.execute(email);

    }
    public void preview(){
        final Dialog dialog = new Dialog(getActivity());

        //setting custom layout to dialog

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.forgotpasswod);
      final EditText email=(EditText)dialog.findViewById(R.id.email);

        final Button close = (Button) dialog.findViewById(R.id.cancel);
        final Button send = (Button) dialog.findViewById(R.id.send);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //imgFile.delete();
                dialog.dismiss();
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

String email1=email.getText().toString().trim();
                //imgFile.delete();
                Forgotpass(email1);
                dialog.dismiss();
            }
        });

        dialog.show();
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=register_fb";
                    HttpPost post = new HttpPost(postURL);
                    System.out.println("========in============"+username1+" "+mobile+" "+email);


                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("username", new org.apache.http.entity.mime.content.StringBody(username1));
                    postEntity.addPart("fb_id",new org.apache.http.entity.mime.content.StringBody(email));
                    postEntity.addPart("mobile_no",new org.apache.http.entity.mime.content.StringBody(mobile));
                    postEntity.addPart("email",new org.apache.http.entity.mime.content.StringBody(pass));
                    postEntity.addPart("dob", new org.apache.http.entity.mime.content.StringBody(dobirth));
                    postEntity.addPart("gcm", new org.apache.http.entity.mime.content.StringBody(regid));

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
                            JSONArray array;
                            if(object.equals("true")){
                                //Toast.makeText(getActivity(), "Submitted Successfully..", Toast.LENGTH_SHORT).show();
                                String  respose=json.getString("response");
                                array=new JSONArray(respose);
                                for(int i=0;i<array.length();i++) {
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    String fb_id=jsonObject.getString("fb_id");
                                    String username=jsonObject.getString("username");
                                    String id=jsonObject.getString("id");

                                    i = 0;
                                    int j = 1;
                                    Intent intent = new Intent(getActivity(), Navigation_Acivity.class);
                                    intent.putExtra("value", i);
                                    intent.putExtra("scanvalue", j);
                                    startActivity(intent);
                                    SharedPreferences preferences = getActivity().getSharedPreferences("Login", 0);
                                    SharedPreferences.Editor editor;
                                    editor = preferences.edit();
                                    editor.putString("id", id);
                                    editor.putString("email", "");
                                    editor.putString("username", username);
                                    editor.putString("dob", "");
                                    editor.putString("mobile_no", "");
                                    editor.putString("image", "");
                                    editor.putString("facebook_id",fb_id);
                                    editor.commit();
                                }
                            }
                            else{

                              // Toast.makeText(getActivity(), "This User Already Registered..", Toast.LENGTH_SHORT).show();

                                array=new JSONArray(object);
                                for(int i=0;i<array.length();i++) {
                                    JSONObject jsonObject=array.getJSONObject(i);
                                    String fb_id=jsonObject.getString("fb_id");
                                    String username=jsonObject.getString("username");
                                    String id=jsonObject.getString("id");

                                    i = 0;
                                    int j = 1;
                                    Intent intent = new Intent(getActivity(), Navigation_Acivity.class);
                                    intent.putExtra("value", i);
                                    intent.putExtra("scanvalue", j);
                                    startActivity(intent);
                                    SharedPreferences preferences = getActivity().getSharedPreferences("Login", 0);
                                    SharedPreferences.Editor editor;
                                    editor = preferences.edit();
                                    editor.putString("id", id);
                                    editor.putString("email", "");
                                    editor.putString("username", username);
                                    editor.putString("dob", "");
                                    editor.putString("mobile_no", "");
                                    editor.putString("image", "");
                                    editor.putString("facebook_id",fb_id);
                                    editor.commit();
                                }
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
}

