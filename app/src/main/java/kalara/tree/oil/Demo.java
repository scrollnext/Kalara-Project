package kalara.tree.oil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Demo  extends Fragment implements View.OnClickListener{
    private YouTubePlayer YPlayer;
Bundle bundle;
    Button join;
    public CallbackManager callbackManager;
    public AccessTokenTracker accessTokenTracker;
    public ProfileTracker profileTracker;
    LoginButton loginButton;
    ProgressDialog pDialog;
    static ArrayList<Knowlegde_item> knowlegde_items=new ArrayList<Knowlegde_item>();
    ArrayList<HashMap<String, String>> Datalist1=new ArrayList<HashMap<String, String>>();
    TextView price2,offer,head,desc,desoffer;
    ImageView offerimage,myView,img_back,img_next;
    static int value;
    RelativeLayout sw;
    public  String VIDEO_ID ;
    File imgFile;
    int position;
    ArrayList<String> list = new ArrayList<String>();
    LinearLayout frame1;
    Button signup,signin;
    Button customfb;
    GoogleCloudMessaging gcm;
    TextView header;
TextView productdescription;
    ViewPager viewPager;
    public static String PROJECT_NUMBER = "321291005565";
    //ImageView mainimage;
    ImageView backimage,nextimage;
    ArrayList<String> arrayList =new ArrayList<String>();
    public static String regid;

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
                                String email_id = object.getString("email");
                                String gender = object.getString("gender");
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

    public Demo() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity());

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
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
      //  FacebookSdk.sdkInitialize(getActivity());
        View layout = inflater.inflate(R.layout.home_activity, container, false);
        Navigation_Acivity.title.setText("Home");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        getGcmRegisterId();
        value=getArguments().getInt("value");
        loginButton = (LoginButton) layout.findViewById(R.id.login_button);
       // ImageView fbloginButton = (ImageView) layout.findViewById(R.id.custom_login_button);
       // fbloginButton.setOnClickListener(this);
        //textView = (TextView) view.findViewById(R.id.textView);

        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
        frame1=(LinearLayout)layout.findViewById(R.id.frame1);
        join=(Button)layout.findViewById(R.id.join);
        join.setOnClickListener(this);
        if(value==0){
frame1.setVisibility(View.GONE);
            join.setVisibility(View.GONE);
        }
        else{
            frame1.setVisibility(View.VISIBLE);
            join.setVisibility(View.VISIBLE);
        }
        bundle=new Bundle();
        bundle.putInt("value", value);
        signin=(Button)layout.findViewById(R.id.signin);
        signin.setOnClickListener(this);
viewPager=(ViewPager)layout.findViewById(R.id.pager);
        signup=(Button)layout.findViewById(R.id.signup);
        signup.setOnClickListener(this);
        customfb=(Button)layout.findViewById(R.id.custom_login_button);
        customfb.setOnClickListener(this);

        price2=(TextView)layout.findViewById(R.id.price);
        offer=(TextView)layout.findViewById(R.id.offer);
        desc=(TextView)layout.findViewById(R.id.desc);
productdescription=(TextView)layout.findViewById(R.id.productdesc);
        offerimage=(ImageView)layout.findViewById(R.id.productimage);
        header=(TextView)layout.findViewById(R.id.header);
        //mainimage=(ImageView)layout.findViewById(R.id.image1);
        desoffer=(TextView)layout.findViewById(R.id.desoffer);
      backimage=(ImageView)layout.findViewById(R.id.arrowleft);
       nextimage=(ImageView)layout.findViewById(R.id.arrowright);
        myView = new ImageView(getActivity());
        myView.setScaleType(ImageView.ScaleType.FIT_XY);
        myView.setLayoutParams(new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        /*img_back=(ImageView)layout.findViewById(R.id.know_lib_back_arrow);
        img_next=(ImageView)layout.findViewById(R.id.know_lib_next_arrow);*/


        getproducts();


       /* img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "  Next Image" + pos, Toast.LENGTH_LONG).show();
                // sw.setImageResource(imageIDs[++pos]);
                Picasso.with(getActivity())
                        .load(list.get(++pos))
                        .into(myView);

                //gallery.setSelection(pos);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "previous Image" + pos, Toast.LENGTH_LONG).show();
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
        });*/
        return layout;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
      /* int i=0;
        int j=1;
        Intent intent=new Intent(getActivity(), Navigation_Acivity.class);
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

                String url = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=getoffer";


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
                                    String description = object1.getString("Description");
                                    String Offer_Title = object1.getString("Offer Title");
                                    String MRP = object1.getString("MRP");
                                    //String product = object1.getString("product");
                                    String offer_image = object1.getString("offer_image");
                                    String offer_description=object1.getString("Offer_Description");
                                    String Image_Title = object1.getString("Image_Title");
                                    String video = object1.getString("video");
                                    String offerprice=object1.getString("Offer_Price");
                                    JSONArray jsonArray1=object1.getJSONArray("images");
                                    for(int j=0;j<jsonArray1.length();j++){
                                        String image=jsonArray1.get(j).toString();
                                        arrayList.add(image);

                                        /*System.out.println("this sisis"+image +" "+arrayList);
                                        Picasso.with(getActivity())
                                                .load(arrayList.get(0))
                                                .into(mainimage);*/
                                    }
                                    CustomPagerAdapter customPagerAdapter=new CustomPagerAdapter(getActivity(),arrayList);
                                    viewPager.setAdapter(customPagerAdapter);
                                   /* backimage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            for(int k=arrayList.size();k>0;k--){
                                                Picasso.with(getActivity())
                                                        .load(arrayList.get(k))
                                                        .into(mainimage);
                                            }
                                        }
                                    });
                                    nextimage.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            for(int k=0;k<arrayList.size()-1;k++){
                                                Picasso.with(getActivity())
                                                        .load(arrayList.get(k))
                                                        .into(mainimage);
                                            }
                                        }
                                    });*/

                                    desoffer.setText(description);
                                   // desc.setText(description);

                                    HashMap<String,String> data=new HashMap<String, String>();

                                    int j;

                                    data.put("description", description);

                                    data.put("Offer", Offer_Title);
                                    // data.put("product", product);
                                    data.put("price", MRP);
data.put("offerprice",offerprice);

data.put("offer_description",offer_description);
                                    data.put("offer_image",offer_image);
                                    data.put("product_header",Image_Title);
                                    data.put("video",video);
                                   // data.put("product_video",product_video);
                                    Datalist1.add(data);

                                    /*knowlegde_items.add(new Knowlegde_item(id, report, barcode, product, size, image, status, product_categories,
                                            product_name, product_video, time));*/

                                /*    Knowledgeadapter knowledgeadapter=new Knowledgeadapter(getActivity(),knowlegde_items);
                                    knowledgeadapter.notifyDataSetChanged();
                                    grid.setAdapter(knowledgeadapter);*/
for(int k=0;k<Datalist1.size();k++){

String description1=Datalist1.get(0).get("description");
String Offer1=Datalist1.get(0).get("Offer");
    String price1=Datalist1.get(0).get("price");
    String offer_image1=Datalist1.get(0).get("offer_image");
    String product_header1=Datalist1.get(0).get("product_header");
    final String video1=Datalist1.get(0).get("video");

offer.setText(Datalist1.get(0).get("offerprice"));
    productdescription.setText(description1);
    header.setText(Offer1);
    price2.setText(price1);
desc.setText(Datalist1.get(0).get("offer_description"));
    Picasso.with(getActivity())
            .load(offer_image1)
            .into(offerimage);
    VIDEO_ID= extractYoutubeId(video1);
    System.out.println("=================VIDEO_ID====" + VIDEO_ID);
    YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
    transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

    youTubePlayerFragment.initialize(Splash_Screen.API_KEY, new YouTubePlayer.OnInitializedListener() {

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
            /*if (!b) {
                // YPlayer = youTubePlayer;
                //YPlayer.setFullscreen(false);
                youTubePlayer.loadVideo(VIDEO_ID);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
             //   youTubePlayer.play();
                youTubePlayer
            }
            else{
                youTubePlayer.loadVideo(VIDEO_ID);
                youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                youTubePlayer.pause();
            }*/

            if (VIDEO_ID != null) {
                if (!b) {
                   youTubePlayer.cueVideo(VIDEO_ID);;

                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                } else {
                   youTubePlayer.loadVideo(VIDEO_ID);
                    youTubePlayer.play();
                    youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                }
            }





        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
            // TODO Auto-generated method stub

        }
    });
    for(int ii=0;ii<knowlegde_items.size();ii++) {

            String image = knowlegde_items.get(i).getProductimage();
            System.out.println("values are    nn1" + image);
            list.add(image);
        }
    /*sw.setFactory(new ViewSwitcher.ViewFactory() {
        @Override
        public View makeView() {
            sw.removeView(myView);
            Picasso.with(getActivity())

                    .load(list.get(0))
                    .into(myView);
            return myView;
        }
    });*/
    for(int m=0;m<list.size();m++){
        String encodedString=list.get(0).toString();
        System.out.println("image of demooo" + encodedString);
        /*try{
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Drawable d = new BitmapDrawable(bitmap);
sw.setBackgroundDrawable(d);lklkl
        }catch(Exception e){
            e.getMessage();

        }*/

        /*Bitmap myImage =


        RelativeLayout rLayout=(RelativeLayout)findViewById(R.id.relativeLayout);

        //BitmapDrawable(obj) convert Bitmap object into drawable object.
        Drawable dr = new BitmapDrawable(myImage);
        rLayout.setBackgroundDrawable(dr);*/

       // Drawable d = new BitmapDrawable(bitmap);
//sw.setBackgroundDrawable(d);
    }
   /* String encodedString=list.get(0).toString();
    Picasso.with(getActivity())
            .load(encodedString)
            .into((Target) sw);*/
    /*Picasso.with(getActivity()).load(encodedString).into(new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            sw.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(final Drawable errorDrawable) {
            Log.d("TAG", "FAILED");
        }

        @Override
        public void onPrepareLoad(final Drawable placeHolderDrawable) {
            Log.d("TAG", "Prepare Load");
        }
    });*/


}





                                }



                            } else {
                              //  Toast.makeText(getActivity(), "Failed to login..", Toast.LENGTH_SHORT).show();
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
    private String extractYoutubeId(String url) {

        String video_id = "";
        if (url != null && url.trim().length() > 0 && url.startsWith("http")) {

            String expression = "^.*((youtu.be" + "\\/)"
                    + "|(v\\/)|(\\/u\\/w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#\\&\\?]*).*";
            CharSequence input = url;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                String groupIndex1 = matcher.group(7);
                if (groupIndex1 != null && groupIndex1.length() == 11)
                    video_id = groupIndex1;
            }
        }

        return video_id;
    }
    @Override
    public void onClick(View v) {


        if(v.getId()==R.id.signin){
            Fragment loginActivity1=new Login_Activity();
            loginActivity1.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, loginActivity1).commit();
        }
        if(v.getId()==R.id.signup){
            Fragment loginActivity1=new Signup();
            loginActivity1.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, loginActivity1).commit();
        }
        if(v.getId()==R.id.custom_login_button){
loginButton.performClick();
        }
        if(v.getId()==R.id.join){
            Fragment loginActivity1=new Signup();
            loginActivity1.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, loginActivity1).commit();
        }

    }
    public void displayMessage(Profile profile){
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
       //
       // displayMessage(profile);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putString(BundleKeys.MESSAGE, message);
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

                                //  Toast.makeText(getActivity(), "This User Already Registered..", Toast.LENGTH_SHORT).show();
                               // String  respose=json.getString("response");
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
    public class CustomPagerAdapter  extends PagerAdapter {
        ArrayList<String> arrayList;
        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context, ArrayList<String> arrayList) {
            this. mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.arrayList=arrayList;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            final ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            // imageView.setImageResource(mResources[position]);
            for(int i=0;i<=arrayList.size()-1;i++){
                String image11=arrayList.get(i).toString();
                System.out.println("this is what " + image11);
                Picasso.with(mContext).load(arrayList.get(position)).into(imageView);
            }
            //
            backimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*for(int k=arrayList.size();k>0;k--){
                        Picasso.with(getActivity())
                                .load(arrayList.get(k))
                                .into(imageView);
                    }*/
                    viewPager.setCurrentItem(getItem(-1), true);
                }
            });
            nextimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*for(int k=0;k<arrayList.size()-1;k++){
                        Picasso.with(getActivity())
                                .load(arrayList.get(k))
                                .into(imageView);
                    }*/
                    viewPager.setCurrentItem(getItem(+1), true);
                }
            });
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
        private int getItem(int i) {
            return viewPager.getCurrentItem() + i;
        }
    }
}
