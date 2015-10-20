package kalara.tree.oil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by avigma19 on 10/7/2015.
 */
public class Scanqr extends Fragment {

    private static final int ZBAR_SCANNER_REQUEST = 0;
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout

        View layout = inflater.inflate(R.layout.scan_qr1, container, false);
        Navigation_Acivity.title.setText("Scan QR");
        Navigation_Acivity.sidepannel.setVisibility(View.INVISIBLE);




        return layout;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == getActivity().RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                String format = data.getStringExtra("SCAN_RESULT_FORMAT");
                Log.i("xZing", "contents: " + contents + " format: " + format); // Handle successful scan
                Toast.makeText(getActivity(),contents,Toast.LENGTH_LONG).show();

            } else if (resultCode == getActivity().RESULT_CANCELED) { // Handle cancel
                Log.i("xZing", "Cancelled");
            }
        }
    }}