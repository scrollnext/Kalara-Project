package kalara.tree.oil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

public class Splash_Screen extends AppCompatActivity
{
	int i=1;
	int  scanvalue=1;
	private static int SPLASH_TIME_OUT = 3000;
	public static final String API_KEY = "AIzaSyCIWPU6kRlikh12pnqtLRbY8W9TcW76zEA";
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash__screen);
		new Handler().postDelayed(new Runnable() {


			@Override
			public void run() {
				File f = getDatabasePath("/data/data/kalara.tree.oil/shared_prefs/Login.xml");

				if (f.exists()) {
					i=0;

					Intent login = new Intent(getApplicationContext(),
							Navigation_Acivity.class);
					login.putExtra("value", i);
					login.putExtra("scanvalue",scanvalue);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			/*login.putExtra("value", value);*/
					startActivity(login);
					// Closing dashboard screen
					finish();
				}
				else {
					i=1;
					Intent login = new Intent(getApplicationContext(),
							Navigation_Acivity.class);
					login.putExtra("value", i);
					login.putExtra("scanvalue", scanvalue);
					login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			/*login.putExtra("value", value);*/
					startActivity(login);
					// Closing dashboard screen
					finish();
				}
			}
		}, SPLASH_TIME_OUT);
	}

		
	}


	


