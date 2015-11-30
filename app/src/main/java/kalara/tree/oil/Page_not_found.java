package kalara.tree.oil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class Page_not_found extends Fragment {
    RelativeLayout issue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.pagenotfound, container, false);


        Navigation_Acivity.title.setText("Page Not Found");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        issue=(RelativeLayout)layout.findViewById(R.id.returnimage);
        issue.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("value",Scanqr.value);
                Fragment loginActivity1=new Demo();
                loginActivity1.setArguments(bundle);
                android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_body, loginActivity1).commit();
            }
        });

        return layout;
    }
}
