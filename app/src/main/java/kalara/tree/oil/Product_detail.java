package kalara.tree.oil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
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


public class Product_detail extends Fragment implements View.OnClickListener {
TextView text1,text2,text3,text4,text5;
    public CallbackManager callbackManager;
    public AccessTokenTracker accessTokenTracker;
    public ProfileTracker profileTracker;
    LoginButton loginButton;
    ArrayList<HashMap<String, String>> Datalist2=new ArrayList<HashMap<String, String>>();

    private Button btn_reportdelete,rateus;
Button signup,signin;
    ImageView customfb;
    ImageView image1,image2,image3,image4,image5;
    private TextView txt_date, txt_size, txt_barcode, txt_comments, txt_product;
    private ImageView img_back, img_next;
    TextView nameproduct;
    ImageSwitcher sw;
    TextView descrition;
   Dialog dialog;
    File imgFile;
    int position;
    String id;
    String starvalue="0";
    private YouTubePlayer YPlayer;
    ListView list1;
    ImageView imageView;
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<HashMap<String,String>> Rating = new ArrayList<HashMap<String,String>>();
    TextView aveaareta;
    ArrayList<HashMap<String,String>> Datalist1 = new ArrayList<HashMap<String,String>>();
    ArrayList<HashMap<String,String>> Datalist3 = new ArrayList<HashMap<String,String>>();
    String product_name,productcategory,productimage,productid,product_url;
    int pos;
    Gallery gallery;
    ProgressDialog pDialog;
    ImageView myView;
    String comment;
    View layout;
    TextView customers;
    SurfaceView videoSurface;
    public static final String API_KEY = "AIzaSyCIWPU6kRlikh12pnqtLRbY8W9TcW76zEA";
    ArrayList<Knowlegde_item> knowlegde_items=new ArrayList<Knowlegde_item>();
    //http://youtu.be/<VIDEO_ID>
    public  String VIDEO_ID ;
int value;
    Bundle bundle;
    LinearLayout frame1;
    GoogleCloudMessaging gcm;
    public static String PROJECT_NUMBER = "321291005565";

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

    public Product_detail() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout


        // setContentView(R.layout.location);
        FacebookSdk.sdkInitialize(getActivity());
        layout = inflater.inflate(R.layout.product_detail, container, false);
        loginButton = (LoginButton) layout.findViewById(R.id.login_button);
        ImageView fbloginButton = (ImageView) layout.findViewById(R.id.custom_login_button);
        text1=(TextView)layout.findViewById(R.id.textView1);
        text2=(TextView)layout.findViewById(R.id.textViewt2);
        text3=(TextView)layout.findViewById(R.id.textViewt3);
        text4=(TextView)layout.findViewById(R.id.textViewt4);
        text5=(TextView)layout.findViewById(R.id.textViewt5);
        descrition=(TextView)layout.findViewById(R.id.productdesc);
        fbloginButton.setOnClickListener(this);
       // textView = (TextView) view.findViewById(R.id.textView);

        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
        product_name=getArguments().getString("productname");
        productcategory=getArguments().getString("productcategory");
        product_url=getArguments().getString("product_url");
        value=getArguments().getInt("value");
        bundle=new Bundle();
        bundle.putInt("value", value);
        System.out.println("===product_url============="+product_url);
        frame1=(LinearLayout)layout.findViewById(R.id.frame1);

        if(value==0){
            frame1.setVisibility(View.GONE);
        }
        else{
            frame1.setVisibility(View.VISIBLE);
        }
        productid=getArguments().getString("id");

       signin=(Button)layout.findViewById(R.id.signin);
        signin.setOnClickListener(this);

        signup=(Button)layout.findViewById(R.id.signup);
        signup.setOnClickListener(this);
        position=getArguments().getInt("position");
        System.out.println("values are" + product_name + " " + productcategory + " " + id + " " + position);
        aveaareta=(TextView)layout.findViewById(R.id.aveaareta);
       getproducts();
        System.out.println("values are    nn" + Discover.knowlegde_items.size());
        sw = (ImageSwitcher) layout.findViewById(R.id.imageSwitcher);
        nameproduct=(TextView)layout.findViewById(R.id.productname);
        nameproduct.setText(product_name);
        myView = new ImageView(getActivity());
        myView.setScaleType(ImageView.ScaleType.FIT_XY);
        myView.setBackgroundResource(R.drawable.thumbnail);
        myView.setLayoutParams(new ImageSwitcher.LayoutParams(300, 300));
        image1=(ImageView)layout.findViewById(R.id.image1);
        image2=(ImageView)layout.findViewById(R.id.image2);
        image3=(ImageView)layout.findViewById(R.id.image3);
        image4=(ImageView)layout.findViewById(R.id.image4);
        image5=(ImageView)layout.findViewById(R.id.image5);

        rateus=(Button)layout.findViewById(R.id.rateus);

        list1=(ListView)layout.findViewById(R.id.list);
     //   setListViewHeightBasedOnChildren(list1);
customers=(TextView)layout.findViewById(R.id.customer);

        rateus.setOnClickListener(this);
      /*  for(int i=0;i<knowlegde_items.size();i++){
            if(knowlegde_items.get(i).getId().equals(productid)){
           String image=knowlegde_items.get(i).getProductimage();

               list.add(image);
                System.out.println("values are    nn1" + list);
            }

        }*/

        img_back=(ImageView)layout.findViewById(R.id.know_lib_back_arrow);
        img_next=(ImageView)layout.findViewById(R.id.know_lib_next_arrow);
        // Note that Gallery view is deprecated in Android 4.1---
        gallery= (Gallery) layout.findViewById(R.id.myGallery);




      //  YouTubePlayerView youTubeView = (YouTubePlayerView)layout. findViewById(R.id.youtube_player);
       // youTubeView.initialize(API_KEY, this);


        /** Initializing YouTube player view **/
       // YouTubePlayerView youTubePlayerView = (YouTubePlayerView)layout.findViewById(R.id.youtube_player);
      //  youTubePlayerView.initialize(API_KEY, this);

       /* mVideoView=(VideoView)layout.findViewById(R.id.video);



        pDialog = new ProgressDialog(getActivity());

        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        try {
            // Start the MediaController
            MediaController mediacontroller = new MediaController(getActivity());
            mediacontroller.setAnchorView(mVideoView);

            Uri videoUri = Uri.parse(product_url);
            mVideoView.setMediaController(mediacontroller);
            mVideoView.setVideoURI(videoUri);

        } catch (Exception e) {

            e.printStackTrace();
        }

        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                pDialog.dismiss();
                mVideoView.start();
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            public void onCompletion(MediaPlayer mp) {
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }

            }
        });*/





        return layout;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        callbackManager.onActivityResult(requestCode, resultCode, data);
        /*int i=0;
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




   /* @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        *//** add listeners to YouTubePlayer instance **//*
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);

        *//** Start buffering **//*
        if (!wasRestored) {
            youTubePlayer.cueVideo("nCD2hj6zJEc");
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(getActivity(), "Failured to Initialize!", Toast.LENGTH_LONG).show();
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {


        public void onAdStarted() {
        }


        public void onError(YouTubePlayer.ErrorReason arg0) {
        }


        public void onLoaded(String arg0) {
        }


        public void onLoading() {
        }


        public void onVideoEnded() {
        }


        public void onVideoStarted() {
        }
    };
*/



    private void SendParameter() {


        // TODO Auto-generated method stub
        // Make RESTful webservice call using
        SharedPreferences preferences = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);

        String userid1 = preferences.getString("id", null);

        RequestParams params = new RequestParams();
        params.put("userid", userid1);
        params.put("productid", productid);
        params.put("rating", starvalue);



        AsyncHttpClient client = new AsyncHttpClient();

        System.out.println();

        client.post("http://www.support-4-pc.com/clients/kalara/subscriber.php?action=rate", params,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http
                    public void onStart() {
                        super.onStart();
                        pDialog = new ProgressDialog(getActivity());
                        pDialog.setMessage("Please wait...");
                        pDialog.setCancelable(false);

                     //   pDialog.show();
                    }

                    // response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog

                        System.out.println("=========response=========="
                                + response);
                        pDialog.hide();
                        if (pDialog != null) {
                          //  pDialog.dismiss();
                        }

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
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
    private void SendCamments() {


        // TODO Auto-generated method stub
        // Make RESTful webservice call using
        SharedPreferences preferences = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);

        String userid1 = preferences.getString("id", null);

        RequestParams params = new RequestParams();
        params.put("userid", userid1);
        params.put("productid", productid);
        params.put("comment", comment);



        AsyncHttpClient client = new AsyncHttpClient();

        System.out.println();

        client.post("http://www.support-4-pc.com/clients/kalara/subscriber.php?action=comment",params,
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
                          //  pDialog.dismiss();
                        }

                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            String result=json.getString("result");
                            if(result.equals("true")){

                                //dialog.dismiss();
                            }
                            else{
                                Toast.makeText(getActivity(),"You have already ranked this product.",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
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


    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        ArrayList<String> arrayList;
        public ImageAdapter(Context c,ArrayList<String> arrayList)
        {
            this.context = c;
            this.arrayList=arrayList;
            // sets a grey background; wraps around the images
          /*  TypedArray a =context.obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.drawable.thumbnail, 0);
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
            View row=convertView;
            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.productt_itm, parent,false);
            }
            ImageView  imageView=(ImageView)row.findViewById(R.id.image);
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

    public class CommentAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        ArrayList<HashMap<String,String>> arrayList;
        public CommentAdapter(Context c,ArrayList<HashMap<String,String>> arrayList)
        {
            this.context = c;
            this.arrayList=arrayList;
            // sets a grey background; wraps around the images

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
            View row=convertView;
            if(convertView==null){
                LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row=inflater.inflate(R.layout.rateitem, parent,false);
            }
            ImageView  imageView=(ImageView)row.findViewById(R.id.userimage);
            ImageView  star1=(ImageView)row.findViewById(R.id.star1);
            ImageView  star2=(ImageView)row.findViewById(R.id.star2);
            ImageView  star3=(ImageView)row.findViewById(R.id.star3);
            ImageView  star4=(ImageView)row.findViewById(R.id.star4);
            ImageView  star5=(ImageView)row.findViewById(R.id.star5);
            TextView name=(TextView)row.findViewById(R.id.name);
            TextView commenttextval=(TextView)row.findViewById(R.id.commentstext);
            TextView ratevalue=(TextView)row.findViewById(R.id.rate);


            HashMap<String, String> map =arrayList.get(position);
          //  String id=map.get("id").toString();

                String ratingusername=arrayList.get(position).get("username");
                String ratingimage=arrayList.get(position).get("image");
                String ratingval=arrayList.get(position).get("rating");
            for(int i=0;i<Datalist3.size();i++) {

                String username = Datalist3.get(i).get("username1");
                if (ratingusername.equals(username)) {
                    String commemnttext = Datalist3.get(i).get("comment");
                    System.out.println("commentssss" + commemnttext);
                    if (commemnttext.equals("0")) {
                        commenttextval.setVisibility(View.GONE);
                    } else {
                        commenttextval.setText(commemnttext);
                    }
                }
            }
                /*String COUNT=Datalist1.get(position).get("COUNT");
                String customers=Datalist1.get(position).get("count(userid)");*/


if(ratingusername.equals("0")&&ratingimage.equals("0")
       ){
    list1.setVisibility(View.GONE);
    name.setVisibility(View.GONE);
    System.out.println("rating vieww");
}
            else{
    name.setText(ratingusername);
            }
            if(ratingimage.equals("0")){
                imageView.setImageResource(R.drawable.icn);
            }
            else if(ratingimage.equals("")){
                imageView.setImageResource(R.drawable.icn);
            }
            else{
                Picasso.with(getActivity())
                        .load(ratingimage)
                        .into(imageView);
            }
            if(ratingval.equals("0")){
                ratevalue.setVisibility(View.GONE);
            }
            else{
                ratevalue.setText(ratingval + " " + "/" + " " + "5");
            }



if(ratevalue.equals("0")){
    star1.setVisibility(View.GONE);
    star2.setVisibility(View.GONE);
    star3.setVisibility(View.GONE);
    star4.setVisibility(View.GONE);
    star5.setVisibility(View.GONE);
}


                 else if(ratingval.equals("1")){
                        star1.setImageResource(R.drawable.fill);
                        star2.setImageResource(R.drawable.nill);
                        star3.setImageResource(R.drawable.nill);
                        star4.setImageResource(R.drawable.nill);
                        star5.setImageResource(R.drawable.nill);


                    }
else if(ratingval.equals("1.5")){
    star1.setImageResource(R.drawable.fill);
    star2.setImageResource(R.drawable.half);
    star3.setImageResource(R.drawable.nill);
    star4.setImageResource(R.drawable.nill);
    star5.setImageResource(R.drawable.nill);

}

                    else if(ratingval.equals("2")){
                        star1.setImageResource(R.drawable.fill);
                        star2.setImageResource(R.drawable.fill);
                        star3.setImageResource(R.drawable.nill);
                        star4.setImageResource(R.drawable.nill);
                        star5.setImageResource(R.drawable.nill);

                    }
else if(ratingval.equals("2.5")){
    star1.setImageResource(R.drawable.fill);
    star2.setImageResource(R.drawable.fill);
    star3.setImageResource(R.drawable.half);
    star4.setImageResource(R.drawable.nill);
    star5.setImageResource(R.drawable.nill);

}
                    else if(ratingval.equals("3")){
                        star1.setImageResource(R.drawable.fill);
                        star2.setImageResource(R.drawable.fill);
                        star3.setImageResource(R.drawable.fill);
                        star4.setImageResource(R.drawable.nill);
                        star5.setImageResource(R.drawable.nill);

                    }
else if(ratingval.equals("3.5")){
    star1.setImageResource(R.drawable.fill);
    star2.setImageResource(R.drawable.fill);
    star3.setImageResource(R.drawable.fill);
    star4.setImageResource(R.drawable.half);
    star5.setImageResource(R.drawable.nill);

}
                    else if(ratingval.equals("4")){
                        star1.setImageResource(R.drawable.fill);
                        star2.setImageResource(R.drawable.fill);
                        star3.setImageResource(R.drawable.fill);
                        star4.setImageResource(R.drawable.fill);
                        star5.setImageResource(R.drawable.nill);

                    }
else if(ratingval.equals("3.5")){
    star1.setImageResource(R.drawable.fill);
    star2.setImageResource(R.drawable.fill);
    star3.setImageResource(R.drawable.fill);
    star4.setImageResource(R.drawable.fill);
    star5.setImageResource(R.drawable.half);

}
                    else if(ratingval.equals("5")){
                        star1.setImageResource(R.drawable.fill);
                        star2.setImageResource(R.drawable.fill);
                        star3.setImageResource(R.drawable.fill);
                        star4.setImageResource(R.drawable.fill);
                        star5.setImageResource(R.drawable.fill);

                    }




            return row;
        }
    }
    private void ShowDialog() {
//
//		final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
         dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rank_dialog);

        dialog.setCancelable(true);
        RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.dialog_ratingbar);
        EditText comments=(EditText)dialog.findViewById(R.id.comments);
        ImageView close=(ImageView)dialog.findViewById(R.id.cllose);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        comment=comments.getText().toString().trim();
        Button rank_dialog_button=(Button)dialog.findViewById(R.id.rank_dialog_button);
        //        ratingBar.setRating(1.0f);

//        ratingBar.setRating(1.0f);
        //        ratingBar.setRating(1.0f);
        rank_dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendParameter();
                SendCamments();
                dialog.dismiss();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                // TODO Auto-generated method stub
                //Toast.makeText(getActivity(), "star:"+rating, Toast.LENGTH_LONG).show();
                starvalue=String.valueOf(rating);
                System.out.println("==starvalue=="+starvalue);

            }
        });



        dialog.show();
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
                                    String id = object1.getString("id");
                                 if(id.equals(productid)) {
                                     String descriptin = object1.getString("description");
                                     descrition.setText(descriptin);
                                     JSONArray product_image = object1.getJSONArray("product_image");
                                     for (int j = 0; j < product_image.length(); j++) {
                                         String image = product_image.get(j).toString();

                                         knowlegde_items.add(new Knowlegde_item(i, image, id));
                                         System.out.println("imagesss are" + " " + knowlegde_items.size());
                                         list.add(image);


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
                                                 if (pos < list.size() - 1) {
                                                     Picasso.with(getActivity())
                                                             .load(list.get(++pos))
                                                             .into(myView);
                                                 } else {
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

                                                 }
                                                 // gallery.setSelection(pos);

//                    gallery.setSelected(true);


                                             }
                                         });


                                         gallery.setFadingEdgeLength(0);
                                         gallery.setAdapter(new ImageAdapter(getActivity(), list));
                                         gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                 pos = position;
                                                 //  Toast.makeText(getActivity(), "pic" + (position + 1) + " selected",
                                                 //   Toast.LENGTH_SHORT).show();
                                                 // display the images selected
//                ImageView imageView = (ImageView) findViewById(R.id.know_lib_img);
                                                 // sw.setImageResource(imageIDs[position]);
                                                 Picasso.with(getActivity())

                                                         .load(list.get(pos))
                                                         .into(myView);

                                             }
                                         });


                                     }
                                     String product_video = object1.getString("product_video");
                                     VIDEO_ID = extractYoutubeId(product_video);

                                     System.out.println("=================VIDEO_ID====" + VIDEO_ID);
                                     YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                                     FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                                     transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

                                     youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {

                                         @Override
                                         public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                                            /* if (!b) {
                                                 // YPlayer = youTubePlayer;
                                                 //YPlayer.setFullscreen(false);
                                                 youTubePlayer.loadVideo(VIDEO_ID);
                                                 youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
                                                 youTubePlayer.play();
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
                                     JSONArray rating= object1.getJSONArray("rating");
                                     for(int k=0;k<rating.length();k++){
                                         JSONObject jsonObject=rating.getJSONObject(k);
                                         String rating1=jsonObject.getString("rating");
                                         String count=jsonObject.getString("COUNT");

                                         HashMap<String, String> rate = new HashMap<String, String>();
                                         rate.put("rating",rating1);
                                         rate.put("COUNT", count);
                                         Rating.add(rate);
                                         for(int p=0;p<Rating.size();p++){
                                         String rateval=Rating.get(p).get("rating");
                                             String countval=Rating.get(p).get("COUNT");
                                             if(rating1.equals("0")){

                                             }
                                             else if(rating1.equals("1")){
                                                 text5.setText(countval);

                                             }

                                             else if(rating1.equals("2")){
                                                 text4.setText(countval);

                                             }
                                             else if(rating1.equals("3")){
                                                 text3.setText(countval);

                                             }
                                             else if(rating1.equals("4")){
                                                 text2.setText(countval);

                                             }
                                             else if(rating1.equals("5")){
                                                 text1.setText(countval);

                                             }


                                         }





                                     }


                                     JSONArray ratings = object1.getJSONArray("ratings");

                                     for(int k=0;k<ratings.length();k++){

                                         JSONObject jsonObject=ratings.getJSONObject(k);
                                         String rating2=jsonObject.getString("rating");
                                         String username=jsonObject.getString("username");
                                         String image=jsonObject.getString("image");
                                        // String comment=jsonObject.getString("comment");
                                         HashMap<String, String> data1 = new HashMap<String, String>();
                                         data1.put("rating",rating2);
                                         data1.put("username",username);
                                         data1.put("image",image);

                                      //   data1.put("comment",comment);
                                         Datalist2.add(data1);
                                         CommentAdapter knowledgeadapter=new CommentAdapter(getActivity(),Datalist2);
                                         list1.setAdapter(knowledgeadapter);
                                         Helper.getListViewSize(list1);
                                     }
                                     JSONArray comments = object1.getJSONArray("comments");
                                     for(int p=0;p<comments.length();p++){
                                         JSONObject jsonObject1=comments.getJSONObject(p);
                                         String username1=jsonObject1.getString("username");
                                         int m=1;
                                         String commentvalue = null,image1value = null;
                                         HashMap<String, String> data2 = new HashMap<String, String>();
                                             commentvalue=jsonObject1.getString("comment");
                                             image1value=jsonObject1.getString("image");
                                             System.out.println("comment value for your" + comment);
                                         data2.put("comment", commentvalue);
                                             data2.put("username1", username1);
                                             data2.put("image1", image1value);
Datalist3.add(data2);

                                         }
                                            /* else{
                                                 data1.put("comment","");
                                                 data1.put("username1","");
                                                 data1.put("image1","");

                                             }


                                     }
                                     JSONArray AVG = object1.getJSONArray("AVG(rating)");
                                     for(int k=0;k<AVG.length();k++){
                                         JSONObject jsonObject=AVG.getJSONObject(k);
                                         double AVG1=jsonObject.getDouble("AVG(rating)");
                                         String count1=jsonObject.getString("count(userid)");
                                         if(String.valueOf(AVG1).equals("null")){
                                             aveaareta.setText(String.valueOf("0" + " " + "/ 5"));
                                         }
                                         else if(String.valueOf(AVG1).equals("0")){
                                             aveaareta.setText(String.valueOf("0" + " " + "/ 5"));
                                         }
                                         else{
                                             aveaareta.setText(String.valueOf(AVG1 + " " + "/ 5"));
                                         }
                                         if(count1.equals("null")){
                                             customers.setText("From"+" "+"0"+ " "+"customers");
                                         }
                                         else if(count1.equals("")){
                                             customers.setText("From"+" "+"0"+ " "+"customers");
                                         }
                                         else {
                                             customers.setText("From" + " " + count1 + " " + "customers");
                                         }
                                         double av=AVG1*100;



                                     }

/*
                                     String ratings1=object1.getString("ratings");
                                    *//* if(ratings1.equals("0")){
                                         HashMap<String, String> data = new HashMap<String, String>();

                                         data.put("ratingval", "");

                                         data.put("username", "");
                                         // data.put("product", product);
                                         data.put("image", "");

                                         data.put("comment", "");
                                         data.put("COUNT", "");


                                         Datalist1.add(data);
                                         CommentAdapter adapter = new CommentAdapter(getActivity(), Datalist1);
                                         list1.setAdapter(adapter);
                                     }

                                     else {*//*
                                         JSONArray jsonarray = new JSONArray(ratings1);
                                         JSONObject ob1;
                                         for (int k = 0; k < jsonarray.length(); k++) {
                                             ob1 = jsonarray.getJSONObject(k);
                                             String ratingval = ob1.getString("rating");
                                             String username = ob1.getString("username");
                                             String image = ob1.getString("image");

                                             String comment = ob1.getString("comment");

                                            *//* String COUNT = ob1.getString("COUNT");
                                             double AVG1=ob1.getDouble("AVG(rating)");
                                             String avgrate=new DecimalFormat("#.#").format(AVG1);*//*

                                           //  System.out.println("values of product detail" + AVG + " " + customers1);

                                           *//*  aveaareta.setText(new DecimalFormat("#.#").format(AVG) + " " + "/" + " " + "5");
                                             System.out.println("values for rating" + ratingval + " " + username);*//*
                                             HashMap<String, String> data = new HashMap<String, String>();

                                             data.put("ratingval", ratingval);

                                             data.put("username", username);
                                             // data.put("product", product);
                                             data.put("image", image);

                                             data.put("comment", comment);
                                            // data.put("COUNT", COUNT);


                                             Datalist1.add(data);
                                             CommentAdapter adapter = new CommentAdapter(getActivity(), Datalist1);
                                             list1.setAdapter(adapter);

                                     }*/


                                 }

                                    System.out.println("vallllllll of thhhh" + id + " " + Datalist1);
                                    /*knowlegde_items.add(new Knowlegde_item(id, report, barcode, product, size, image, status, product_categories,
                                            product_name, product_video, time));*/

                                /*    Knowledgeadapter knowledgeadapter=new Knowledgeadapter(getActivity(),knowlegde_items);
                                    knowledgeadapter.notifyDataSetChanged();
                                    grid.setAdapter(knowledgeadapter);*/


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


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.rateus){
            if(value==0) {
                ShowDialog();

            }
            else{
                     final Dialog dialog = new Dialog(getActivity());

                    //setting custom layout to dialog

                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alertmessage);

                    final Button ok = (Button) dialog.findViewById(R.id.ok);

                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {



                            Fragment loginActivity1=new Login_Activity();
                            loginActivity1.setArguments(bundle);
                            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().addToBackStack(null)
                                    .replace(R.id.container_body, loginActivity1).commit();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }


        }

        if(v.getId()==R.id.signin){
            Fragment loginActivity1=new Login_Activity();
            loginActivity1.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, loginActivity1).commit();
        }
        if(v.getId()==R.id.signup){
            Fragment Signup=new Signup();
            Signup.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, Signup).commit();
        }
        if(v.getId()==R.id.custom_login_button){
           loginButton.performClick();
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=register_fb";
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
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsListView.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
