package kalara.tree.oil;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class EditAccount extends Fragment {
String image,username,id;
    EditText username1,password,cnfrmpass;
    ImageView imageview;
     String cnfpas,dob,email;
 String pass;
    Button update;
    ProgressDialog pDialog;
    public static int REQUEST_CAMERA = 1;
    File imgFile;
    public static int SELECT_FILE = 2;
    File file;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.account, container, false);
     Navigation_Acivity.title.setText("My Account");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        username1=(EditText)layout.findViewById(R.id.changeusername);
        password=(EditText)layout.findViewById(R.id.password);
        cnfrmpass=(EditText)layout.findViewById(R.id.confirmpassword);
imageview=(ImageView)layout.findViewById(R.id.image1);

        image=getArguments().getString("image");
        System.out.println("image value E"+image);
       /* if(image!="") {
            Uri uri = Uri.parse(image);
            file = new File(getPath(uri));
        }*/
        username=getArguments().getString("username");
        dob=getArguments().getString("dob");
        email=getArguments().getString("email");
        id=getArguments().getString("id");
        username1.setText(username);
if(!image.equals("")){
        Picasso.with(getActivity())
                .load(image)
                .transform(new RoundedTransformation(100, 0))
                .resize(80, 80)
                .into(imageview);}

pass=password.getText().toString().trim();
        cnfpas=cnfrmpass.getText().toString();
update=(Button)layout.findViewById(R.id.update);
        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass=password.getText().toString().trim();
                username=username1.getText().toString().trim();

                signin();
            }
        });
        return layout;
    }
    protected void signin() {
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/admin/sub.php?action=editpro";
                    HttpPost post = new HttpPost(postURL);
                    System.out.println("========in============");


                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("id", new org.apache.http.entity.mime.content.StringBody(id));
                    postEntity.addPart("username",new org.apache.http.entity.mime.content.StringBody(username));
                    postEntity.addPart("password", new org.apache.http.entity.mime.content.StringBody(pass));
                    //postEntity.addPart("username",new org.apache.http.entity.mime.content.StringBody(cnfpas));


                    System.out.println("========in============"+postEntity);
                    System.out.println("imahhdh"+imgFile);
                    if(imgFile!=null){
                        FileBody body=new FileBody(imgFile);
                        System.out.println("bodyyyy"+body);
                        postEntity.addPart("image", body);
                    }
                   /* else{
                        Uri uri=Uri.parse(image);
                        imgFile=new File(getPath(uri));
                        FileBody body=new FileBody(imgFile);
                        System.out.println("bodyyyy"+body);
                        postEntity.addPart("image", body);
                    }
*/

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
                                   // String id=object1.getString("id");
                                   // String email=object1.getString("email");
                                    String username=object1.getString("username");
                                   // String dob=object1.getString("dob");
                                   // String mobileno=object1.getString("mobile_no");
                                    String image=object1.getString("image");
                                    SharedPreferences preferences=getActivity().getSharedPreferences("Login",0);
                                    SharedPreferences.Editor editor;
                                    editor=preferences.edit();
                                    editor.putString("id", id);
                                    editor.putString("email", email);
                                    editor.putString("username", username);
                                   editor.putString("dob", dob);
                                   // editor.putString("mobile_no", mobileno);
                                    editor.putString("image", image);
                                    //editor.putInt("value", i);
                                    editor.commit();
                                    Fragment loginActivity=new Account();

                                    android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                                    fragmentManager.beginTransaction().addToBackStack(null)
                                            .replace(R.id.container_body, loginActivity).commit();
                                    Toast.makeText(getActivity(), "Profile updated sucessfully..", Toast.LENGTH_SHORT).show();
                                }




                            }
                            else{
                                Toast.makeText(getActivity(), "Failed to update..", Toast.LENGTH_SHORT).show();
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
        httpGetAsyncTask.execute();

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == getActivity().RESULT_OK) {
            Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                System.out.println("hello" + thumbnail);
               // Toast.makeText(getActivity(),"hello1"+thumbnail,Toast.LENGTH_SHORT).show();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                imgFile = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis() + ".jpg");
                FileOutputStream fo;
                try {
                    imgFile.createNewFile();
                    fo = new FileOutputStream(imgFile);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Picasso.with(getActivity())
                        .load(imgFile)
                        .transform(new RoundedTransformation(100, 0))
                        .resize(80, 80)
                        .into(imageview);
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                imgFile=new File(getPath(selectedImageUri));
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = getActivity().managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                String selectedImagePath = cursor.getString(column_index);
                System.out.println("hello"+selectedImagePath);
                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                Picasso.with(getActivity())
                        .load(imgFile)
                        .transform(new RoundedTransformation(100, 0))
                        .resize(80, 80)
                        .into(imageview);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                   getActivity(). startActivityForResult(
                           Intent.createChooser(intent, "Select File"),
                           SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
}
