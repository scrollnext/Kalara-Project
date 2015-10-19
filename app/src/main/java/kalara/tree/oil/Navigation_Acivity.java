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
    ImageView profilebar;
    FrameLayout frameLayout;
 SharedPreferences preferences;
    static String contents;
   static TextView title;
    static LinearLayout sidepannel;
    static int value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation__acivity);
        printHashKey();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
getSupportActionBar().setDisplayHomeAsUpEnabled(true);;
       // getSupportActionBar().setIcon(R.drawable.icn);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        title=(TextView)findViewById(R.id.title);
        sidepannel=(LinearLayout)findViewById(R.id.sidepannel);
        profilebar=(ImageView)findViewById(R.id.profilebr);
        profilebar.setOnClickListener(this);

        getSupportActionBar().setTitle(null);
        value=getIntent().getExtras().getInt("value");
frameLayout=(FrameLayout)findViewById(R.id.container_body);
        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);
System.out.println("values are"+value);
        if(value==1){
            Fragment loginActivity1=new Login_Activity();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, loginActivity1).commit();
        }

        else if(value==0){
            Fragment loginActivity=new Demo();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container_body, loginActivity).commit();
        }



    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        switch (position){
            case 0:
                Fragment loginActivity=new Scanqr();
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_body, loginActivity).commit();
                break;
            case 1:
                Fragment Demo=new Demo();
                android.support.v4.app.FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1.beginTransaction()
                        .replace(R.id.container_body, Demo).commit();
                break;
            case 2:
                Fragment discover=new Discover();
                android.support.v4.app.FragmentManager fragmentManager2 = getSupportFragmentManager();
                fragmentManager2.beginTransaction()
                        .replace(R.id.container_body, discover).commit();
                break;
            case 3:
                Fragment notification=new Notification();
                android.support.v4.app.FragmentManager fragmentManager3 = getSupportFragmentManager();
                fragmentManager3.beginTransaction()
                        .replace(R.id.container_body, notification).commit();
                break;
            case 4:
                if(value==1){
                    Fragment login = new Login_Activity();
                    android.support.v4.app.FragmentManager fragmentManager4 = getSupportFragmentManager();
                    fragmentManager4.beginTransaction()
                            .replace(R.id.container_body, login).commit();
                }
                else {
                    Fragment login = new Create();
                    android.support.v4.app.FragmentManager fragmentManager4 = getSupportFragmentManager();
                    fragmentManager4.beginTransaction()
                            .replace(R.id.container_body, login).commit();
                }
                break;
            case 5:
                if(value==1) {
                    Fragment share = new Share();
                    android.support.v4.app.FragmentManager fragmentManager5 = getSupportFragmentManager();
                    fragmentManager5.beginTransaction()
                            .replace(R.id.container_body, share).commit();
                }
                else{
                    if(value==0){
                        Fragment share = new Library();
                        android.support.v4.app.FragmentManager fragmentManager5 = getSupportFragmentManager();
                        fragmentManager5.beginTransaction()
                                .replace(R.id.container_body, share).commit();
                    }
                }
                break;
            case 6:
                if(value==1) {
                    Fragment location= new Locateus();
                    android.support.v4.app.FragmentManager fragmentManager7 = getSupportFragmentManager();
                    fragmentManager7.beginTransaction()
                            .replace(R.id.container_body, location).commit();

                }
                else {
                    if (value == 0) {
                        Fragment share1 = new Knowledge();
                        android.support.v4.app.FragmentManager fragmentManager5 = getSupportFragmentManager();
                        fragmentManager5.beginTransaction()
                                .replace(R.id.container_body, share1).commit();
                    }
                }

                break;
            case 7:
                Fragment share2 = new Share();
                android.support.v4.app.FragmentManager fragmentManager6 = getSupportFragmentManager();
                fragmentManager6.beginTransaction()
                        .replace(R.id.container_body, share2).commit();
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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i("Barcode Result", contents);
                /*Intent i1 = new Intent(QRCodeSampleActivity.this, webclass.class);
                startActivity(i1);*/
                // Handle successful scan
            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
                Log.i("Barcode Result","Result canceled");
            }
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
    }
}
