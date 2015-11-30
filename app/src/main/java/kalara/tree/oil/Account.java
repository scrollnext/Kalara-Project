package kalara.tree.oil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;


public class Account extends Fragment implements View.OnClickListener{
TextView editprofile;
    TextView emailid,username,dob;
    String  name,email,dateofbirth,id,image;
    ImageView image1;
    Button logout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.profile, container, false);
       Navigation_Acivity.title.setText("My Account");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        editprofile=(TextView)layout.findViewById(R.id.editprofile);
        editprofile.setOnClickListener(this);
        image1=(ImageView)layout.findViewById(R.id.image1);
        logout=(Button)layout.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        SharedPreferences preferences=getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        name=preferences.getString("username",null);
        email=preferences.getString("email",null);
        dateofbirth=preferences.getString("dob",null);

        image=preferences.getString("image",null);
        System.out.println("image value"+image);
        if(!image.equals("")) {
            if(image.equals("null")){

            }
            else {
                Picasso.with(getActivity())
                        .load(image)
                        .transform(new RoundedTransformation(100, 0))
                        .resize(80, 80)
                        .into(image1);
            }
        }


        else{

        }
        System.out.println("image value"+image);
        id=preferences.getString("id",null);
        emailid =(TextView)layout.findViewById(R.id.email);
        username=(TextView)layout.findViewById(R.id.username);
        dob=(TextView)layout.findViewById(R.id.dob1);

        emailid.setText(email);
        username.setText(name);
        dob.setText(dateofbirth);



        return layout;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.editprofile){
            Bundle bundle=new Bundle();
            bundle.putString("image",image);
            bundle.putString("username",name);
            bundle.putString("id",id);
            bundle.putString("email",email);
            bundle.putString("dob",dateofbirth);
            Fragment loginActivity=new EditAccount();
            loginActivity.setArguments(bundle);
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, loginActivity).commit();
        }
        if(v.getId()==R.id.logout){
            File f = getActivity().getDatabasePath("/data/data/kalara.tree.oil/shared_prefs/Login.xml");

            if (f.exists()) {
                f.delete();
               int i=1;
                int scanvalue=1;
                Intent login = new Intent(getActivity(),
                        Navigation_Acivity.class);
                login.putExtra("value", i);
                login.putExtra("scanvalue",scanvalue);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			/*login.putExtra("value", value);*/
                startActivity(login);
                // Closing dashboard screen

            }
        }
    }
}