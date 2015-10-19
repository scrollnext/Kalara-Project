package kalara.tree.oil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Login_Activity extends Fragment implements View.OnClickListener {
    private CallbackManager callbackManager;
    private TextView textView;
SharedPreferences sharedPreferences;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    LoginButton loginButton;
    SharedPreferences.Editor editor;
    static int i=1;
    TextView create;
    EditText username,password;
    TextView Forgotpassword;
    String user,pass;
    Button signin;
    ProgressDialog pDialog;
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            displayMessage(profile);
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
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
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
        create=(TextView)layout.findViewById(R.id.create);
        create.setOnClickListener(this);
        username=(EditText)layout.findViewById(R.id.username);
        password=(EditText)layout.findViewById(R.id.password);
        signin=(Button)layout.findViewById(R.id.signin);
        signin.setOnClickListener(this);
        Forgotpassword=(TextView)layout.findViewById(R.id.forgot);
        Forgotpassword.setOnClickListener(this);
        Navigation_Acivity.title.setText("Signin");
       Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
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
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
         i=0;

        Intent intent=new Intent(getActivity(),Navigation_Acivity.class);
        intent.putExtra("value", i);
        startActivity(intent);

       /* Fragment loginActivity=new Demo();
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, loginActivity).commit();*/
    }

    private void displayMessage(Profile profile){
       /* if(profile != null){
            textView.setText(profile.getName());
        }*/
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
        displayMessage(profile);
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
           user=username.getText().toString().trim();
         pass=password.getText().toString().trim();
            signin(user,pass);

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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=login";
                    HttpPost post = new HttpPost(postURL);
                    System.out.println("========in============");


                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("email", new org.apache.http.entity.mime.content.StringBody(email));
                    postEntity.addPart("pass",new org.apache.http.entity.mime.content.StringBody(pass));


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
                                    SharedPreferences preferences=getActivity().getSharedPreferences("Login",0);
                                    SharedPreferences.Editor editor;
                                    editor=preferences.edit();
                                    editor.putString("id", id);
                                    editor.putString("email", email);
                                    editor.putString("username", username);
                                    editor.putString("dob", dob);
                                    editor.putString("mobile_no", mobileno);
                                    //editor.putInt("value", i);
                                    editor.commit();
                                    i=0;
                                    Intent  intent=new Intent(getActivity(),Navigation_Acivity.class);
                                    intent.putExtra("value",i);
                                    startActivity(intent);
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=forgotpass";
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
        httpGetAsyncTask.execute(email,pass);

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

}

