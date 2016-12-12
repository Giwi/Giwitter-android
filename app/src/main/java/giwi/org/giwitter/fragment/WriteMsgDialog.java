package giwi.org.giwitter.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestClientException;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import giwi.org.giwitter.R;
import giwi.org.giwitter.helpers.Constants;
import giwi.org.giwitter.helpers.MyRestClient;
import giwi.org.giwitter.helpers.Session;


/**
 * The type Write msg dialog.
 */
@EFragment
public class WriteMsgDialog extends DialogFragment {

    private EditText message;
    @RestService
    MyRestClient restClient;


    /**
     * On create dialog dialog.
     *
     * @param savedInstanceState the saved instance state
     * @return the dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        final View view = inflater.inflate(R.layout.dialog_msg, null);
        message = (EditText) view.findViewById(R.id.tchat_msg);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.validate, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        closeKeyboard();
                        if (!message.getText().toString().isEmpty()) {
                            //post message
                            sentMessage(message.getText().toString());
                            // new SendMessageAsyncTask(view.getContext()).execute(message.getText().toString());
                        } else {
                            message.setError(WriteMsgDialog.this.getActivity()
                                    .getString(R.string.error_missing_msg));
                        }
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        closeKeyboard();
                        WriteMsgDialog.this.dismiss();
                    }
                }

        );
        return builder.create();
    }

    /**
     * Sent message.
     *
     * @param s the s
     */
    @Background
    void sentMessage(String s) {
        try {
            JSONObject message = new JSONObject()
                    .put("user_id", Session.userId)
                    .put("content", s);
            restClient.sendMessage(message.toString());
        } catch (JSONException e) {
            Log.e(Constants.TAG, e.getMessage());
        } catch (RestClientException e) {
            dispalyError(e.getMessage());
        }
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(message.getWindowToken(), 0);
    }

    @UiThread
    void dispalyError(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }
}
