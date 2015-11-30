package kalara.tree.oil;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;


public class Crop_image extends Fragment {
    private CropImageView mCropImageView;
    String path;
    ImageView mImageView;
    Button load,crop;
    String image;
   static File outputFile;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.crop_activity, container, false);
  Navigation_Acivity.title.setText("Crop");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        mCropImageView = (CropImageView)  layout.findViewById(R.id.CropImageView);
        mImageView=(ImageView)layout.findViewById(R.id.imageView1);
       // load=(Button)layout.findViewById(R.id.loadimage);
        image=getArguments().getString("URi");
        Uri uri=Uri.parse(image);
        mCropImageView.setImageUri(uri);
        /*load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(getPickImageChooserIntent(), 200);
                mCropImageView.setVisibility(View.VISIBLE);
                mImageView.setVisibility(View.GONE);
            }
        });*/
        crop=(Button)layout.findViewById(R.id.cropimage);
        crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap cropped =  mCropImageView.getCroppedImage(500, 500);
                if (cropped != null)
                    mImageView.setVisibility(View.VISIBLE);
                mCropImageView.setVisibility(View.GONE);
                mImageView.setImageBitmap(cropped);

                long timeinmilisecod = System.currentTimeMillis();
                OutputStream fOut = null;
                File file = new File("/sdcard/kalara/");
                file.mkdirs();
                outputFile = new File(file, "cropped" + timeinmilisecod
                        + ".png");

                path = outputFile.getAbsolutePath();

                Toast.makeText(getActivity(), path, Toast.LENGTH_LONG).show();
                Fragment loginActivity1 = new Signup();



                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().addToBackStack(null)
                        .replace(R.id.container_body, loginActivity1).commit();
                try {
                    fOut = new FileOutputStream(outputFile);
                    cropped.compress(Bitmap.CompressFormat.PNG, 100,
                            fOut);
                    try {
                        fOut.flush();
                        fOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        return layout;
    }






    public void onLoadImageClick(View view) {

    }
    public void onCropImageClick(View view) {
        Bitmap cropped =  mCropImageView.getCroppedImage(500, 500);
        if (cropped != null)
            mImageView.setVisibility(View.VISIBLE);
        mCropImageView.setVisibility(View.GONE);
        mImageView.setImageBitmap(cropped);

        long timeinmilisecod = System.currentTimeMillis();
        OutputStream fOut = null;
        File file = new File("/sdcard/kalara/");
        file.mkdirs();
 outputFile = new File(file, "cropped" + timeinmilisecod
                + ".png");

        path = outputFile.getAbsolutePath();

       // Toast.makeText(getActivity(), path, Toast.LENGTH_LONG).show();
        Fragment loginActivity1 = new Signup();



        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().addToBackStack(null)
                .replace(R.id.container_body, loginActivity1).commit();
        try {
            fOut = new FileOutputStream(outputFile);
            cropped.compress(Bitmap.CompressFormat.PNG, 100,
                    fOut);
            try {
                fOut.flush();
                fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int  requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri =  getPickImageResultUri(data);
            mCropImageView.setImageUri(imageUri);

           // Toast.makeText(getActivity(), "Hello", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Create a chooser intent to select the  source to get image from.<br/>
     * The source can be camera's  (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the  intent chooser.
     */
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

    /**
     * Get URI to image received from capture  by camera.
     */
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
