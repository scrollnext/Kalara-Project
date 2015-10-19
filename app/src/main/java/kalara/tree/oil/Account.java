package kalara.tree.oil;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/**
 * Created by avigma19 on 10/5/2015.
 */
public class Account extends Fragment implements View.OnClickListener{
TextView editprofile;
    TextView emailid,username,dob;
    String  name,email,dateofbirth,id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.profile, container, false);
       Navigation_Acivity.title.setText("My Account");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        editprofile=(TextView)layout.findViewById(R.id.editprofile);
        SharedPreferences preferences=getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        name=preferences.getString("username",null);
        email=preferences.getString("email",null);
        dateofbirth=preferences.getString("dob",null);
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
            Fragment loginActivity=new Account();
            android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().addToBackStack(null)
                    .replace(R.id.container_body, loginActivity).commit();
        }
    }
}