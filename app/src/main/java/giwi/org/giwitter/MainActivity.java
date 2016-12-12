package giwi.org.giwitter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestClientException;


import giwi.org.giwitter.helpers.MyPrefs_;
import giwi.org.giwitter.helpers.MyRestClient;
import giwi.org.giwitter.helpers.NetworkHelper;
import giwi.org.giwitter.helpers.Session;

/**
 * The type Main activity.
 */
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewById
    Button signin_btn;
    @ViewById
    ProgressBar signin_pg;
    @ViewById
    Button signin_register;
    @ViewById
    EditText signin_username;
    @ViewById
    EditText signin_pwd;
    @RestService
    MyRestClient restClient;
    @Pref
    MyPrefs_ myPrefs;
    @StringRes
    String error_login;

    /**
     * Init.
     */
    @AfterViews
    void init() {
        signin_username.setText(myPrefs.username().getOr(""));
    }

    /**
     * Signin btn.
     */
    @Click
    void signin_btn() {
        if (!NetworkHelper.isInternetAvailable(this)) {
            return;
        }
        loading(true);
        doLogin(signin_username.getText().toString(), signin_pwd
                .getText().toString());
    }

    @UiThread
    void dispalyError(String message) {
        loading(false);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Signin register.
     */
    @Click
    void signin_register() {
        SignupActivity_.intent(this).start();
    }

    /**
     * Do login.
     *
     * @param login  the login
     * @param passwd the passwd
     */
    @Background
    void doLogin(String login, String passwd) {
        try {
            JSONObject body = new JSONObject()
                    .put("username", login)
                    .put("password", passwd);
            String resp = restClient.login(body.toString());
            System.out.println(resp);
            onPostExecute(resp);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RestClientException e) {
            dispalyError(e.getMessage());
        }
    }

    /**
     * Loading.
     *
     * @param loading the loading
     */
    void loading(boolean loading) {
        if (loading) {
            signin_pg.setVisibility(View.VISIBLE);
            signin_btn.setVisibility(View.INVISIBLE);
            signin_username.setEnabled(false);
            signin_pwd.setEnabled(false);
        } else {
            signin_pg.setVisibility(View.INVISIBLE);
            signin_btn.setVisibility(View.VISIBLE);
            signin_username.setEnabled(true);
            signin_pwd.setEnabled(true);
        }
    }

    /**
     * On post execute.
     *
     * @param secureToken the secure token
     */
    @UiThread
    void onPostExecute(final String secureToken) {
        loading(false);
        if (secureToken != null) {

            try {
                //save login
                myPrefs.username().put(signin_username.getText().toString());
                JSONObject resp = new JSONObject(secureToken);
                Session.token = resp.getString("secureToken");
                Session.userId = resp.getString("user_id");
                DrawerActivity_.intent(this).start();
           /*
                Intent i = new Intent(context, DrawerActivity.class);
                JSONObject resp = new JSONObject(secureToken);
                Log.i(Constants.TAG, secureToken);*/
            /*
                i.putExtra(Constants.INTENT_TOKEN, resp.getString("secureToken"));
                i.putExtra(Constants.INTENT_USER_ID, resp.getString("user_id"));
                startActivity(i);*/
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, error_login, Toast.LENGTH_LONG).show();
        }
    }

}
