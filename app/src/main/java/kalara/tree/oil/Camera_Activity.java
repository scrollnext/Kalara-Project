package kalara.tree.oil;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Camera_Activity extends Fragment implements View.OnClickListener {
    Button camera,gallery,next;
    GridView gridView;
    public static int REQUEST_CAMERA = 1;
    static ArrayList<String> imageGallery;
    public static int SELECT_FILE = 2;
    GridAdapter adapter;
    int scanvalue;
    Boolean isInternetPresent = true;
    GPSTracker gps;
    String productname,sizename,brandname,manufacturername,comments,productimage,id,result,barcode,brand,productid;
    double longitude,latitude;
    String cityName,stateName,countryName,part1;
    String[] parts;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.notification_item, container, false);
      /*  SharedPreferences preferences = getActivity().getSharedPreferences("Login",
                Context.MODE_PRIVATE);
        userid1 = preferences.getString("id", null);*/
  ;
        productname=getArguments().getString("product");
        sizename=getArguments().getString("size");
        barcode=getArguments().getString("barcode");
        id=getArguments().getString("id");
        manufacturername=getArguments().getString("manufacturer");
        productimage=getArguments().getString("image");
        productid=getArguments().getString("productid");
        result=getArguments().getString("result");
comments=getArguments().getString("comments");
        brandname=getArguments().getString("brand");
        scanvalue=getArguments().getInt("scanvalue");
        System.out.println("scan vlaue"+scanvalue+" "+comments);
        System.out.println("scanvalue"+scanvalue);
        camera=(Button)layout.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        gallery=(Button)layout.findViewById(R.id.galler);
        gallery.setOnClickListener(this);

        next=(Button)layout.findViewById(R.id.next);
        next.setOnClickListener(this);
        imageGallery=new ArrayList<String>();


        gridView =(GridView)layout.findViewById(R.id.grid);
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
            gps = new GPSTracker(getActivity());

            // check if GPS enabled
            if(gps.canGetLocation()){

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();


                // \n is for new line
              //  Toast.makeText(getActivity(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                List<Address> addresses = null;

                try
                {
 	   						/*double lat= 25.800136;
 	   						double lang= -80.201361;*/
                    addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    cityName = addresses.get(0).getAddressLine(0);
                    stateName = addresses.get(0).getAddressLine(1);
                    countryName = addresses.get(0).getAddressLine(2);
                    System.out.println("countryyyy "+cityName);
                    System.out.println("countryyyy 1 "+stateName );
                    System.out.println("countryyyy 2 "+countryName);
            }
                catch (IOException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            }

                else{

                showSettingsAlert();
                //	finish();
            }
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
    /* showAlertDialog(SplashScreen.this, "No Internet Connection",
              "You don't have internet connection.", false);*/
            Toast.makeText(getActivity(), "No internet Connection ", Toast.LENGTH_LONG).show();

        }
        Navigation_Acivity.title.setText("Create Report");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);

        return layout;
    }
    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        if(v.getId()==R.id.camera){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, REQUEST_CAMERA);
        }
        if(v.getId()==R.id.galler){
            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
            startActivityForResult(galleryIntent, SELECT_FILE);
        }
        if(v.getId()==R.id.next){
            if(scanvalue==7) {
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("location", stateName);
                bundle.putString("result", result);
                bundle.putInt("scanvalue", scanvalue);
            /*bundle.putString("size",sizename);
            bundle.putString("product_name",productname);
*/
                Fragment loginActivity1 = new Create();
                loginActivity1.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_body, loginActivity1).commit();
            }
            else if(scanvalue==2){




                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("location", stateName);
                bundle.putString("result", result);
                bundle.putString("size",sizename);
                bundle.putString("product_name",productname);
                bundle.putString("barcode",barcode);
                bundle.putString("manufacturername",manufacturername);
                bundle.putString("productimage",productimage);
                bundle.putString("productid",productid);
                bundle.putString("comments",comments);
                bundle.putInt("scanvalue", scanvalue);
                bundle.putString("brand",brandname);
                Fragment loginActivity1 = new Create();
                loginActivity1.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_body, loginActivity1).commit();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                final File destination = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageGallery.add(destination.getAbsolutePath());
                adapter=new GridAdapter(getActivity(), imageGallery);
                gridView.setAdapter(adapter);

            }
            else if (requestCode == SELECT_FILE) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
              String  imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // Set the Image in ImageView after decoding the String
               /* imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));*/
                imageGallery.add(imgDecodableString);
                adapter=new GridAdapter(getActivity(), imageGallery);
                gridView.setAdapter(adapter);



            }




        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public class GridAdapter extends BaseAdapter {

        Context context;
        ArrayList<String> datArrayList;

        public GridAdapter(Context context2, ArrayList<String> datalist1)
        {
            // TODO Auto-generated constructor stub

            super();
            this.context=context2;
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
                row=inflater.inflate(R.layout.listitem, parent,false);
            }



            ImageView image=(ImageView)row.findViewById(R.id.listitem1);
            File imgFile = new  File(datArrayList.get(position));
            MediaScannerConnection.scanFile(context, new String[]{imgFile.toString()}, null, null);
            //	Toast.makeText(getApplicationContext(), "hello sir"+imgFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            System.out.println("imgaaa path in adapter"+imgFile);
			    /*Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
image.setImageBitmap(myBitmap);*/
            //ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
            // Toast.makeText(getApplicationContext(), "h "+myBitmap, Toast.LENGTH_SHORT).show();
            //	image.setImageURI(Uri.fromFile(imgFile));


            //File imgFile = new  File(“filepath”);
            if(imgFile.exists())
            {
                // ImageView myImage = new ImageView(this);

                image.setImageURI(Uri.fromFile(imgFile));

            }


            return row;
        }

    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()
        );
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Enable Location Provider! Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
		                  /* Intent intent=new Intent(getApplicationContext(), NavigationActivity.class);
		                   startActivity(intent);

		                   SharedPreferences preferences=getSharedPreferences("Location",0);
		   					Editor editor;
		   					editor=preferences.edit();
		   					editor.putString("city", "New Delhi");
		   					editor.commit();
		               finish();*/
                    }
                });
        alertDialog.show();
    }

}
