package kalara.tree.oil;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import kalara.tree.oil.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MessageDialogFragment extends DialogFragment {
    public interface MessageDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    private String mTitle;
    private String mMessage;
    private MessageDialogListener mListener;

    public void onCreate(Bundle state) {
        super.onCreate(state);
        setRetainInstance(true);
    }

    public static MessageDialogFragment newInstance(String title, String message, MessageDialogListener listener) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        fragment.mTitle = title;
        fragment.mMessage = message;
        fragment.mListener = listener;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(mMessage)
                .setTitle(mTitle);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(mListener != null) {
                    mListener.onDialogPositiveClick(MessageDialogFragment.this);
                    int live=1;

                    Bundle bundle=new Bundle();
                    bundle.putInt("live",live);
                    bundle.putString("scanresult", ScannerFragment.scanresult1);
                    /*Fragment fragment1=new HomeScreen();
                    fragment1.setArguments(bundle);
                    FragmentManager fragmentManager1 = getFragmentManager();
                    fragmentManager1.beginTransaction().addToBackStack(null)
                            .replace(R.id.content_frame, fragment1)
                            .commit();*/
                   // Toast.makeText(getActivity(),"hellllo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }
}
