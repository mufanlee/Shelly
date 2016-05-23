package com.mufan.shelly;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mufan.utils.NetworkUtil;

import java.util.regex.Pattern;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;

/**
 * A start screen that offers start via ip/port.
 */
public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    /**
     * Id to identity ACCESS_NETWORK_STATE permission request.
     */
    private static final int REQUEST_ACCESS_NETWORK = 0;

    private boolean netStatus = false;

    /**
     * Keep track of the start task to ensure we can cancel it if requested.
     */
    private UserStartTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mIPView;
    private EditText mPortView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mIPView = (AutoCompleteTextView) findViewById(R.id.ip);
        isNetworkAvailable();

        mPortView = (EditText) findViewById(R.id.port);
        mPortView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.start_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void isNetworkAvailable() {
        if (!(netStatus = NetworkUtil.isNetworkAvailable(this))) {
            Toast.makeText(this, "Please Open the network", Toast.LENGTH_SHORT).show();
        }
        if (!mayRequestNetwork()) {
            return;
        }
    }

    private boolean mayRequestNetwork() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(ACCESS_NETWORK_STATE)) {
            Snackbar.make(mIPView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{ACCESS_NETWORK_STATE}, REQUEST_ACCESS_NETWORK);
                        }
                    });
        } else {
            requestPermissions(new String[]{ACCESS_NETWORK_STATE}, REQUEST_ACCESS_NETWORK);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_ACCESS_NETWORK) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isNetworkAvailable();
            }
        }
    }

    /**
     * Attempts to start.
     * If there are form errors (invalid ip, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        if (netStatus == false) {
            Toast.makeText(this, "Please Open the network.", Toast.LENGTH_SHORT).show();
            return;
        }
        // Reset errors.
        mIPView.setError(null);
        mPortView.setError(null);

        // Store values at the time of the login attempt.
        String ip = mIPView.getText().toString();
        String port = mPortView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid port.
        if (!TextUtils.isEmpty(port) && !isPortValid(port)) {
            mPortView.setError(getString(R.string.error_invalid_port));
            focusView = mPortView;
            cancel = true;
        } else if (TextUtils.isEmpty(port)){
            port = "8080";
        }

        // Check for a valid ip address.
        if (TextUtils.isEmpty(ip)) {
            mIPView.setError(getString(R.string.error_field_required));
            focusView = mIPView;
            cancel = true;
        } else if (!isIPValid(ip)) {
            mIPView.setError(getString(R.string.error_invalid_ip));
            focusView = mIPView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt start and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserStartTask(ip, port);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isIPValid(String ip) {
        Pattern pattern = Pattern.compile( "^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$" );
        return pattern.matcher( ip ).matches();
    }

    private boolean isPortValid(String port) {
        return Integer.parseInt(port) > 0;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous start task used to authenticate
     * the user.
     */
    public class UserStartTask extends AsyncTask<Void, Void, Boolean> {

        private final String mIP;
        private final String mPort;

        UserStartTask(String ip, String port) {
            mIP = ip;
            mPort = port;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            //try {
                // 访问IP和端口，看是否可以连接
                if (!NetworkUtil.isConnect(mIP, Integer.parseInt(mPort))) {
                    return false;
                }
                //Thread.sleep(1000);
            //} catch (InterruptedException e) {
                //return false;
            //}
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ip", mIP);
                bundle.putString("port", mPort);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {
                mPortView.setError(getString(R.string.error_incorrect_port));
                mPortView.requestFocus();
                Toast.makeText(LoginActivity.this, "IP or port is error, cannot to connect.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

