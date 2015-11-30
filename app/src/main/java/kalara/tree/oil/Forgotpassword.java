package kalara.tree.oil;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class Forgotpassword extends Fragment {
    ProgressDialog pDialog;

    ListView listView;
    List<Knowlegde_item> knowlegde_items = new ArrayList<Knowlegde_item>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.knowledge, container, false);
        listView = (ListView) layout.findViewById(R.id.listview);


        Navigation_Acivity.title.setText("Knowledge Library");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);

        return layout;
    }
}