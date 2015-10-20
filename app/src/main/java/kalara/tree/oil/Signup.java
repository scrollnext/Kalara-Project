package kalara.tree.oil;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by avigma19 on 10/5/2015.
 */
public class Signup  extends Fragment implements View.OnClickListener{
    EditText username,mobileno,emailid,password,confirm;
    TextView dob;
    ImageView imageView;
    Button submit;
    ProgressDialog pDialog;
    String user,mobile,email,pass,dob1;
    static Calendar calendar;
    public static int REQUEST_CAMERA = 1;
    File imgFile;
    public static int SELECT_FILE = 2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.signup, container, false);
        calendar=Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
       Navigation_Acivity.title.setText("Signup");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
       username=(EditText)layout.findViewById(R.id.username);
        mobileno=(EditText)layout.findViewById(R.id.mobileno);
        emailid=(EditText)layout.findViewById(R.id.emailid);
        password=(EditText)layout.findViewById(R.id.password);
        confirm=(EditText)layout.findViewById(R.id.confirmpassword);
        imageView=(ImageView)layout.findViewById(R.id.image1);
        imageView.setOnClickListener(this);
        dob=(TextView)layout.findViewById(R.id.dob);
        dob.setOnClickListener(this);
        user=username.getText().toString().trim();
        mobile=mobileno.getText().toString().trim();
        email=emailid.getText().toString().trim();
        pass=password.getText().toString().trim();
        dob1=dob.getText().toString().trim();
        submit=(Button)layout.findViewById(R.id.submit);
        submit.setOnClickListener(this);


        return layout;
    }
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };
    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dob.setText(sdf.format(calendar.getTime()));
    }
    @Override
    public void onClick(View v) {
if(v.getId()==R.id.submit){
    user=username.getText().toString().trim();
    mobile=mobileno.getText().toString().trim();
    email=emailid.getText().toString().trim();
    pass=password.getText().toString().trim();
    dob1=dob.getText().toString().trim();
connectWithHttpPost(user,mobile,email,pass,dob1);
}
        if(v.getId()==R.id.image1){
            selectImage();
        }
        if(v.getId()==R.id.dob){
            new DatePickerDialog(getActivity(), date, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
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


                    String postURL = "http://www.support-4-pc.com/clients/kalara/subscriber.php?action=register";
                    HttpPost post = new HttpPost(postURL);
                    System.out.println("========in============"+username1+" "+mobile+" "+email);


                    MultipartEntity postEntity = new MultipartEntity();

                    postEntity.addPart("username", new org.apache.http.entity.mime.content.StringBody(username1));
                    postEntity.addPart("email",new org.apache.http.entity.mime.content.StringBody(email));
                    postEntity.addPart("mobile",new org.apache.http.entity.mime.content.StringBody(mobile));
                    postEntity.addPart("pass",new org.apache.http.entity.mime.content.StringBody(pass));
                    postEntity.addPart("dob", new org.apache.http.entity.mime.content.StringBody(dobirth));


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
                            if(object.equals("true")){
                                Toast.makeText(getActivity(), "Submitted Successfully..", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "This User Already Registered..", Toast.LENGTH_SHORT).show();
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
        httpGetAsyncTask.execute(username1,mobile,email,pass,dobirth);

    }
    private void selectImage() {


        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(
                            Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");

                    Intent chooser = Intent
                            .createChooser(
                                    intent,
                                    "Choose a Picture");
                getActivity().startActivityForResult(
                        chooser,
                        REQUEST_CAMERA);
                  /*  Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(intent, REQUEST_CAMERA);*/
                } else if (items[item].equals("Choose from Library")) {
                    Intent cameraIntent = new Intent(
                            android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(
                            cameraIntent,
                            SELECT_FILE);
                   /* Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    getActivity().startActivityForResult(intent, SELECT_FILE);*/
                  /*  Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image*//*");
                    getActivity().startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);*/
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                /*Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
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
*/
                if (data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    photo = Bitmap.createScaledBitmap(photo, 80, 80, false);
                    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, "title", null);

                    // imageView.setImageBitmap(photo);
                    Picasso.with(getActivity())
                            .load(path)
                            .transform(new RoundedTransformation(100, 0))
                            .resize(80, 80)
                            .into(imageView);
                } else {
                }
            }
               /* Bitmap bitmap=(Bitmap)data.getExtras().get("data");
                Uri uri=data.getData();
                String image=uri.toString();

                imageView.setImageBitmap(bitmap);
                imgFile = new  File(image);
                FileOutputStream fo;
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
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


            }*/


            else if (requestCode == SELECT_FILE) {
                if (data != null) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    photo = Bitmap.createScaledBitmap(photo, 80, 80, false);
                    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, "title", null);

                    // imageView.setImageBitmap(photo);
                    Picasso.with(getActivity())
                            .load(path)
                            .transform(new RoundedTransformation(100, 0))
                            .resize(80, 80)
                            .into(imageView);
                } else {
                }
        /*        Uri selectedImageUri = data.getData();
            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
            String image=selectedImageUri.toString();
            System.out.println("selected uriiii "+image);
            Picasso.with(getActivity()
                    .load(image)
                    .transform(new RoundedTransformation(100, 0))
                    .resize(80, 80)
                    .into(imageView);
            imageView.setImageBitmap(bitmap);
            imgFile = new  File(image);
            FileOutputStream fo;
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            try {
                imgFile.createNewFile();
                fo = new FileOutputStream(imgFile);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}
