package kalara.tree.oil;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by avigma19 on 10/14/2015.
 */
public class Product_detail extends Fragment implements View.OnClickListener {
    private Button btn_reportdelete,rateus;
 //  ArrayList<Knowlegde_item> knowlegde_items=new ArrayList<Knowlegde_item>();
    private TextView txt_date, txt_size, txt_barcode, txt_comments, txt_product;
    private ImageView img_back, img_next;
    TextView nameproduct;
    ImageSwitcher sw;
    int position;
    String id;
    String starvalue="0";
    private YouTubePlayer YPlayer;
    ImageView imageView;
    ArrayList<String> list = new ArrayList<String>();
    String product_name,productcategory,productimage,productid,product_url;
    int pos;
    Gallery gallery;
    ProgressDialog pDialog;
    ImageView myView;
    View layout;
    SurfaceView videoSurface;
    public static final String API_KEY = "AIzaSyCIWPU6kRlikh12pnqtLRbY8W9TcW76zEA";

    //http://youtu.be/<VIDEO_ID>
    public  String VIDEO_ID ;

    // VideoView mVideoView;
    //the images to display
    int[] imageIDs = {
           /* R.drawable.mathemetics,
            R.drawable.physics,
            R.drawable.chemistry,
            R.drawable.geography,
            R.drawable.economics,
            R.drawable.art,
            R.drawable.computing,
            R.drawable.history,
            R.drawable.biology,*/

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout


        // setContentView(R.layout.location);

        layout = inflater.inflate(R.layout.product_detail, container, false);
        product_name=getArguments().getString("productname");
        productcategory=getArguments().getString("productcategory");
        product_url=getArguments().getString("product_url");

        System.out.println("===product_url============="+product_url);

        productid=getArguments().getString("id");
        position=getArguments().getInt("position");
        System.out.println("values are" + product_name + " " + productcategory + " " + id + " " + position);
        System.out.println("values are    nn" + Discover.knowlegde_items.size());
        sw = (ImageSwitcher) layout.findViewById(R.id.imageSwitcher);
        nameproduct=(TextView)layout.findViewById(R.id.productname);
        nameproduct.setText(product_name);
        myView = new ImageView(getActivity());
        myView.setScaleType(ImageView.ScaleType.FIT_XY);
        myView.setLayoutParams(new ImageSwitcher.LayoutParams(300, 300));
        rateus=(Button)layout.findViewById(R.id.rateus);
        rateus.setOnClickListener(this);
        for(int i=0;i<Discover.knowlegde_items.size();i++){
            if(Discover.knowlegde_items.get(i).getId().equals(id)){
           String image=Discover.knowlegde_items.get(i).getProductimage();
                System.out.println("values are    nn1"+image);
               list.add(image);

            }

        }

        img_back=(ImageView)layout.findViewById(R.id.know_lib_back_arrow);
        img_next=(ImageView)layout.findViewById(R.id.know_lib_next_arrow);
        // Note that Gallery view is deprecated in Android 4.1---
        gallery= (Gallery) layout.findViewById(R.id.myGallery);

         VIDEO_ID= extractYoutubeId(product_url);

       System.out.println("=================VIDEO_ID===="+VIDEO_ID);
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.youtube_fragment, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                   // YPlayer = youTubePlayer;
                    //YPlayer.setFullscreen(false);
                    youTubePlayer.loadVideo("f-JL0Iucdq0");
                    youTubePlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                // TODO Auto-generated method stub

            }
        });


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



/*
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
        });



        gallery.setFadingEdgeLength(0);
        gallery.setAdapter(new ImageAdapter(getActivity(),list));
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
        });*/
        return layout;
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


    @Override
    public void onClick(View v) {
if(v.getId()==R.id.rateus){
    ShowDialog();
}
    }
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
            TypedArray a =context.obtainStyledAttributes(R.styleable.MyGallery);
            itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            a.recycle();
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
            imageView.setBackgroundResource(itemBackground);
            return row;
        }
    }
    private void ShowDialog() {
//
//		final AlertDialog.Builder popDialog = new AlertDialog.Builder(getActivity());
        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.rank_dialog);

        dialog.setCancelable(true);
        RatingBar ratingBar = (RatingBar)dialog.findViewById(R.id.dialog_ratingbar);
        Button rank_dialog_button=(Button)dialog.findViewById(R.id.rank_dialog_button);
        //        ratingBar.setRating(1.0f);

//        ratingBar.setRating(1.0f);
        //        ratingBar.setRating(1.0f);
        rank_dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendParameter();
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
}
