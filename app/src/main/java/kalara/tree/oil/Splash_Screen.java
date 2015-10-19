package kalara.tree.oil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
public class Splash_Screen extends AppCompatActivity
{
	int i=1;
	private static int SPLASH_TIME_OUT = 3000;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash__screen);
		new Handler().postDelayed(new Runnable() {


			@Override
			public void run() {
				Intent login = new Intent(getApplicationContext(),
						Navigation_Acivity.class);
                login.putExtra("value",i);
				login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			/*login.putExtra("value", value);*/
				startActivity(login);
				// Closing dashboard screen
				finish();
			}
		}, SPLASH_TIME_OUT);
	}

		
	}


	


