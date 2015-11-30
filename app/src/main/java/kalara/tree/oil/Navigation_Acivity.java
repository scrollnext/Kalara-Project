package kalara.tree.oil;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Navigation_Acivity extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener,View.OnClickListener {
Toolbar toolbar;
    private FragmentDrawer drawerFragment;
    ImageView profilebar,notification;
    FrameLayout frameLayout;
 SharedPreferences preferences;
    static String contents;
   static TextView title;
    static LinearLayout sidepannel;
    static int value,scanvalue;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation__acivity);
        printHashKey();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setIcon(R.drawable.icn);
      //  getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        toolbar.setNavigationIcon(R.drawable.menu);

        title=(TextView)findViewById(R.id.title);
        sidepannel=(LinearLayout)findViewById(R.id.sidepannel);
        profilebar=(ImageView)findViewById(R.id.profilebr);
        profilebar.setOnClickListener(this);
        notification=(ImageView)findViewById(R.id.notificationbr);
        notification.setOnClickListener(this);

        getSupportActionBar().setTitle(null);
        value=getIntent().getExtras().getInt("value");
        scanvalue=getIntent().getExtras().getInt("scanvalue");
frameLayout=(FrameLayout)findViewById(R.id.container_body);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
System.out.println("values are      new one " + value+" "+scanvalue);
      bundle=new Bundle();
        bundle.putInt("value", value);
if(scanvalue==1) {
    if(value==0){
        Fragment loginActivity1 = new Demo();
        loginActivity1.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, loginActivity1).commit();
    }
    else if(value==1) {
        Fragment loginActivity1 = new Demo();
        loginActivity1.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, loginActivity1).commit();
    }
    else if(value==9){
        Fragment loginActivity1 = new Notification();
        loginActivity1.setArguments(bundle);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container_body, loginActivity1).commit();
    }

}
        else if(scanvalue==2){

    String manufacturer=getIntent().getExtras().getString("manufacturer");
   String id=getIntent().getExtras().getString("id");
String brand=getIntent().getExtras().getString("brand");
    String   barcode=getIntent().getExtras().getString("barcode");
     String    comments=getIntent().getExtras().getString("comments");
    String size=getIntent().getExtras().getString("size");
    String product=getIntent().getExtras().getString("product");
    String image=getIntent().getExtras().getString("image");
String productid=getIntent().getExtras().getString("productid");
    bundle.putString("manufacturer",manufacturer);
    bundle.putString("id",id);
    bundle.putString("productid",productid);
    bundle.putString("barcode",barcode);
    bundle.putInt("scanvalue", scanvalue);
    bundle.putString("comments", comments);
    bundle.putString("size",size);
    bundle.putString("product",product);
    bundle.putString("brand",brand);
    bundle.putString("image",image);
    Fragment loginActivity1 = new Camera_Activity();
    loginActivity1.setArguments(bundle);
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.container_body, loginActivity1).commit();
        }
        else if(scanvalue==3){

    Fragment loginActivity1 = new Page_not_found();
    loginActivity1.setArguments(bundle);
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.container_body, loginActivity1).commit();
        }
else if(scanvalue==7){

    String result=getIntent().getExtras().getString("result");

    bundle.putString("result",result);
bundle.putInt("scanvalue",scanvalue);
    Fragment loginActivity1 = new Camera_Activity();
    loginActivity1.setArguments(bundle);
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.container_body, loginActivity1).commit();
}
else if(scanvalue==4){
    String id=getIntent().getExtras().getString("id");
    String   category=getIntent().getExtras().getString("productcategory");
    String    product_url=getIntent().getExtras().getString("product_url");
   // String AVG=getIntent().getExtras().getString("AVG");
    String product_name=getIntent().getExtras().getString("product_name");
    String position=getIntent().getExtras().getString("position");
    bundle.putInt("value", value);
    bundle.putString("id", id);

    bundle.putString("productcategory", category);
    bundle.putString("product_url", product_url);
   // bundle.putString("AVG", AVG);
    bundle.putString("product_name", product_name);
    bundle.putString("position", "");
    Fragment loginActivity1 = new Product_detail();
    loginActivity1.setArguments(bundle);
    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction()
            .replace(R.id.container_body, loginActivity1).commit();
}
       /* else if(value==0){
            Fragment loginActivity=new Demo();
            loginActivity.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, loginActivity).commit();
        }*/



    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        switch (position){
            case 0:
                Fragment loginActivity=new Scan_qr_main();
                loginActivity.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_body, loginActivity).commit();
                break;
            case 1:
                Fragment Demo=new Demo();
                Demo.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1.beginTransaction()
                        .replace(R.id.container_body, Demo).commit();
                break;
            case 2:
                Fragment discover=new Discover();
                discover.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction()
                        .replace(R.id.container_body, discover).commit();
                break;
            case 3:
                Fragment notification=new Notification();
                bundle.putInt("value", value);
                notification.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager3 = getSupportFragmentManager();
                fragmentManager3.beginTransaction()
                        .replace(R.id.container_body, notification).commit();
                break;
            case 4:
                if(value==1){
                    Fragment login = new Login_Activity();
                    login.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager4 = getSupportFragmentManager();
                    fragmentManager4.beginTransaction()
                            .replace(R.id.container_body, login).commit();
                }
                else {
                    Fragment login = new Scanqr();
                    login.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager4 = getSupportFragmentManager();
                    fragmentManager4.beginTransaction()
                            .replace(R.id.container_body, login).commit();
                }
                break;
            case 5:
                if(value==1) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                   // Uri screenshotUri = Uri.parse(path);

                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Kalara tree app oil");
                    startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                }
                else{
                    if(value==0){
                        Fragment share = new Library();
                        share.setArguments(bundle);
                        android.support.v4.app.FragmentManager fragmentManager5 = getSupportFragmentManager();
                        fragmentManager5.beginTransaction()
                                .replace(R.id.container_body, share).commit();
                    }
                }
                break;
            case 6:
                if(value==1) {
                  /* Intent intent=new Intent(getApplicationContext(),Locateus.class);
                    startActivity(intent);*/
                    Fragment share1 = new Locateus();
                    share1.setArguments(bundle);
                    android.support.v4.app.FragmentManager fragmentManager5 = getSupportFragmentManager();
                    fragmentManager5.beginTransaction()
                            .replace(R.id.container_body, share1).commit();
                }
                else {
                    if (value == 0) {
                        Fragment share1 = new Knowledge();
                        share1.setArguments(bundle);
                        android.support.v4.app.FragmentManager fragmentManager5 = getSupportFragmentManager();
                        fragmentManager5.beginTransaction()
                                .replace(R.id.container_body, share1).commit();
                    }
                }

                break;
            case 7:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                // Uri screenshotUri = Uri.parse(path);

                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Kalara tree app oil");
                startActivity(Intent.createChooser(sharingIntent, "Share image using"));
                break;
            case 8: Fragment share1 = new Locateus();
                share1.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager5 = getSupportFragmentManager();
                fragmentManager5.beginTransaction()
                        .replace(R.id.container_body, share1).commit();

                break;

        }
    }

    public void printHashKey(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "net.simplifiedcoding.androidlogin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.profilebr){
            Fragment loginActivity1=new Account();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, loginActivity1).commit();
        }
        if(v.getId()==R.id.notificationbr){
            Fragment loginActivity1=new Notification();
            loginActivity1.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, loginActivity1).commit();
        }
    }
}
