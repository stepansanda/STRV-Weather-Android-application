package cz.stepansanda.weather.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import cz.stepansanda.weather.android.R;

/**
 * Created by a111 on 30.05.15.
 */
public class EnableGpsDialog extends DialogFragment {

    public interface OnGpsDialogResultListener {
        void onGpsDialogResult(int resultCode);
    }

    private OnGpsDialogResultListener mListener;


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnGpsDialogResultListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement OnGpsDialogResultListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_gps_off_title)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage(R.string.dialog_gps_dialog_text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onGpsDialogResult(Activity.RESULT_OK);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onGpsDialogResult(Activity.RESULT_CANCELED);
                        dismiss();
                    }
                })
                .setCancelable(false);
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
