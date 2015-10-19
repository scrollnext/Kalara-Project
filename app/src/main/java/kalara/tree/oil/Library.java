package kalara.tree.oil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by avigma19 on 10/7/2015.
 */
public class Library extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.library, container, false);
        Navigation_Acivity.title.setText("My Library");
        Navigation_Acivity.sidepannel.setVisibility(View.VISIBLE);
        return layout;
    }

}