package cz.stepansanda.weather.android.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import cz.stepansanda.weather.android.R;

/**
 * Created by a111 on 02.06.15.
 */
public class RemoveLocationDialog extends DialogFragment {

    public interface OnRemoveLocationDialogResult {
        void onRemoveLocationDialogResult(int resultCode, int positoin);
    }

    private static final String POSITION_ARG = "positoin_arg";

    private OnRemoveLocationDialogResult mListener;
    private int mPosition;


    public static RemoveLocationDialog newInstance(int position) {
        RemoveLocationDialog dialog = new RemoveLocationDialog();
        Bundle args = new Bundle();
        args.putInt(POSITION_ARG, position);
        dialog.setArguments(args);
        return dialog;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION_ARG, 0);
        }
    }


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (OnRemoveLocationDialogResult) getTargetFragment();
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
        builder.setTitle(R.string.dialog_remove_location_title)
                .setMessage(R.string.dialog_remove_location_text)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onRemoveLocationDialogResult(Activity.RESULT_OK, mPosition);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onRemoveLocationDialogResult(Activity.RESULT_CANCELED, mPosition);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}