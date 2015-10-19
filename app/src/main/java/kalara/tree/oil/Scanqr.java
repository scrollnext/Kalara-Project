package kalara.tree.oil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/**
 * Created by avigma19 on 10/7/2015.
 */
public class Scanqr extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.scan_qr1, container, false);
        Navigation_Acivity.title.setText("Scan QR");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);
        return layout;
    }
}