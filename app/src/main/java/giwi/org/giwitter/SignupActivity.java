package giwi.org.giwitter;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import giwi.org.giwitter.helpers.MyRestClient;
import giwi.org.giwitter.helpers.NetworkHelper;


/**
 * The type Signup activity.
 */
@EActivity(R.layout.activity_signup)
public class SignupActivity extends Activity {
    @ViewById
    EditText signup_username;
    @ViewById
    EditText signup_pwd;
    @ViewById
    EditText signup_name;
    @ViewById
    EditText signup_firstname;
    @ViewById
    ProgressBar signup_pg;
    @ViewById
    Button signup_btn;
    @RestService
    MyRestClient restClient;

    @Click
    void signup_btn() {
        if (!NetworkHelper.isInternetAvailable(this)) {
            //error
            return;
        }
        loading(true);
        doRegister();
    }

    @Background
    void doRegister() {
        try {

            JSONObject register = new JSONObject()
                    .put("username", signup_username.getText().toString())
                    .put("password", signup_pwd.getText().toString())
                    .put("name", signup_name.getText().toString())
                    .put("firstname", signup_firstname.getText().toString());

            restClient.register(register.toString());
            redirect();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void redirect() {
        loading(false);
        this.finish();
    }

    private void loading(boolean loading) {
        if (loading) {
            signup_pg.setVisibility(View.VISIBLE);
            signup_btn.setVisibility(View.INVISIBLE);
        } else {
            signup_pg.setVisibility(View.INVISIBLE);
            signup_btn.setVisibility(View.VISIBLE);
        }
    }
}