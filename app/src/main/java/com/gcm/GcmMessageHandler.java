package com.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import kalara.tree.oil.Navigation_Acivity;
import kalara.tree.oil.R;

public class GcmMessageHandler extends IntentService {
String title;
    String image;
     String mes;
     String  username ;
     String propic;
     String email ;
     String id;
     Bitmap bitmapurl;
     public static final int notifyID = 9001;
     NotificationCompat.Builder builder;
     private Handler handler;
     String str1;
     String message; 
     String msgType;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
         str1=intent.getExtras().toString();
        System.out.println("===extras============"+str1);
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        
        
       

     mes = extras.getString("message");
        title=extras.getString("title");
        image=extras.getString("image");

        System.out.println("======msg gcm====="+mes);
     /* JSONObject myJson;
		try {
			myJson = new JSONObject(mes);
			
			  // use myJson as needed, for example 
		      message = myJson.optString("message");
		      msgType = myJson.optString("msg_type");
		      System.out.println("===msgType============"+msgType);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
   
        

       

       
       if (!extras.isEmpty()) {
           if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
                   .equals(messageType)) {
               sendNotification("Send error: " + extras.toString());
           } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
                   .equals(messageType)) {
               sendNotification("Deleted messages on server: "
                       + extras.toString());
           } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
                   .equals(messageType)) {
              // sendNotification(""+extras.get("message"));
        	   
        	   sendNotification1(mes,username,propic,email,id,1,2);
           }
           
       }
       showToast();
      // Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("message"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);
        

    }

    private Bitmap getBitmapFromURL(String sUrl) {
    	try {
            URL url = new URL(sUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
	}

	private Bitmap decodeFile(File f) {
		 try {
	            //decode image size
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

	            //Find the correct scale value. It should be the power of 2.
	            final int REQUIRED_SIZE=70;
	            int width_tmp=o.outWidth, height_tmp=o.outHeight;
	            int scale=1;
	            while(true){
	                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
	                    break;
	                width_tmp/=2;
	                height_tmp/=2;
	                scale*=2;
	            }

	            //decode with inSampleSize
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	            o2.inSampleSize=scale;
	            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	        } catch (FileNotFoundException e) {}
	        return null;
	}

	
	
	private void sendNotification1(String msg, String username2, String propic2, String email2, String id2, int i,int k) {
		// TODO Auto-generated method stub
		long when = System.currentTimeMillis();
		
		Drawable d = new BitmapDrawable(getResources(), bitmapurl);
		int n=9;
    	 Intent resultIntent = new Intent(this, Navigation_Acivity.class);
    	resultIntent.putExtra("value",n);
         resultIntent.putExtra("msg", message);
         resultIntent.putExtra("notificationId", String.valueOf(i));
         resultIntent.putExtra("propic", propic2);
         resultIntent.putExtra("email", email2);
         resultIntent.putExtra("id", id2);
         resultIntent.putExtra("username", username2);
         resultIntent.putExtra("message", message);
         resultIntent.putExtra("msgType", msgType);
         PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                 resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
         
         resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
         NotificationCompat.Builder mNotifyBuilder;
         NotificationManager mNotificationManager;

         mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

         mNotifyBuilder = new NotificationCompat.Builder(this)
                 .setContentTitle(username)
                 .setContentText(message)
                 .setSmallIcon(R.drawable.ic_launcher);
                
                
         // Set pending intent
         mNotifyBuilder.setContentIntent(resultPendingIntent);

         // Set Vibrate, Sound and Light            
         
         SharedPreferences sharedPrefs = getSharedPreferences("kalara.tree.oil", MODE_PRIVATE);
         
         boolean vibrate= sharedPrefs.getBoolean("vibrate", true);
         int defaults = 0;
         if(vibrate){
        	  defaults = defaults | Notification.DEFAULT_LIGHTS;
              defaults = defaults | Notification.DEFAULT_VIBRATE;
            
         }
         
         else{
        	
              defaults = defaults | Notification.DEFAULT_LIGHTS;
              defaults = defaults | Notification.DEFAULT_VIBRATE;
              defaults = defaults | Notification.DEFAULT_SOUND; 
        	 
         }
         
         
       

         mNotifyBuilder.setDefaults(defaults);
         // Set the content for Notification 
         mNotifyBuilder.setContentText(message);
         // Set autocancel
         mNotifyBuilder.setAutoCancel(true);
         // Post a notification
         mNotificationManager.notify((int)when, mNotifyBuilder.build());
	}
	
	private void sendNotification(String msg) {
		// TODO Auto-generated method stub
		long when = System.currentTimeMillis();
		
		Drawable d = new BitmapDrawable(getResources(), bitmapurl);
		
    	 Intent resultIntent = new Intent(this, Navigation_Acivity.class);
    	
         resultIntent.putExtra("msg", mes);
         resultIntent.putExtra("notificationId", 1);
         resultIntent.putExtra("propic", propic);
         resultIntent.putExtra("email", email);
         resultIntent.putExtra("id", id);
         resultIntent.putExtra("message", message);
         resultIntent.putExtra("msgType", msgType);
         PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,
                 resultIntent, 0);
         
         resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
         NotificationCompat.Builder mNotifyBuilder;
         NotificationManager mNotificationManager;

         mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

         mNotifyBuilder = new NotificationCompat.Builder(this)
                 .setContentTitle(username)
                 .setContentText(message)
                 .setSmallIcon(R.drawable.ic_launcher);
                
                
         // Set pending intent
         mNotifyBuilder.setContentIntent(resultPendingIntent);

         // Set Vibrate, Sound and Light            
         int defaults = 0;
         defaults = defaults | Notification.DEFAULT_LIGHTS;
         defaults = defaults | Notification.DEFAULT_VIBRATE;
         defaults = defaults | Notification.DEFAULT_SOUND;

         mNotifyBuilder.setDefaults(defaults);
         // Set the content for Notification 
         mNotifyBuilder.setContentText(message);
         // Set autocancel
         mNotifyBuilder.setAutoCancel(true);
         // Post a notification
         mNotificationManager.notify((int)when, mNotifyBuilder.build());
	}

	public void showToast(){
        handler.post(new Runnable() {
            public void run() {
              //  Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
                
                
                System.out.println("==============mes============="+mes);
                
               /* new AlertDialog.Builder(getApplicationContext())
                .setTitle("Delete entry")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // continue with delete
                    }
                 })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) { 
                        // do nothing
                    }
                 })
                .setIcon(android.R.drawable.ic_dialog_alert)
                 .show();*/
                
                
            }
         });

    }
}