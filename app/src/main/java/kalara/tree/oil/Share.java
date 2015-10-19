package kalara.tree.oil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by avigma19 on 10/7/2015.
 */
public class Share extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.share_it, container, false);
  Navigation_Acivity.title.setText("Share it");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        return layout;
    }
}