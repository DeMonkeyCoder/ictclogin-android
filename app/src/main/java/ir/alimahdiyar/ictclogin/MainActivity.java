package ir.alimahdiyar.ictclogin;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password, username2, password2;
    private SharedPreferences sharedPref;

    private UserLoginTask mAuthTask = null;
    private UserLogoutTask mLogoutTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        username2 = findViewById(R.id.username2);
        password2 = findViewById(R.id.password2);

        username.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_NEXT || id == EditorInfo.IME_NULL) {
                    password.requestFocus();
                    return true;
                }
                return false;
            }
        });

        username2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_NEXT || id == EditorInfo.IME_NULL) {
                    password2.requestFocus();
                    return true;
                }
                return false;
            }
        });

        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attempLogin();
                    return true;
                }
                return false;
            }
        });

        password2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attempLogin();
                    return true;
                }
                return false;
            }
        });

        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.logout).setOnClickListener(this);

        sharedPref = getSharedPreferences("ir.alimahdiyar.ictclogin", Context.MODE_PRIVATE);
        username.setText(sharedPref.getString("saved_username", ""));
        password.setText(sharedPref.getString("saved_password", ""));
        username2.setText(sharedPref.getString("saved_username2", ""));
        password2.setText(sharedPref.getString("saved_password2", ""));
    }

    private void attempLogin() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("saved_username2", username2.getText().toString());
        editor.putString("saved_password2", password2.getText().toString());
        editor.putString("saved_username", username.getText().toString());
        editor.putString("saved_password", password.getText().toString());
        editor.commit();
        showProgress(true);
        mAuthTask = new UserLoginTask(username.getText().toString(), password.getText().toString(), username2.getText().toString(), password2.getText().toString());
        mAuthTask.execute((Void) null);
    }

    private void attempLogout() {
        showProgress(true);
        mLogoutTask = new UserLogoutTask();
        mLogoutTask.execute((Void) null);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.submit){
            //String res = Ictclogin_functions.doLogin(username.getText().toString(), password.getText().toString(), username2.getText().toString(), password2.getText().toString());
            //Toast.makeText(this, res, Toast.LENGTH_LONG).show();
            attempLogin();
        } else if (v.getId() == R.id.logout){
            //String res = Ictclogin_functions.doLogout();
            //Toast.makeText(this, res, Toast.LENGTH_LONG).show();
            attempLogout();
        }
    }

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

    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mUsername, mPassword, mUsername2, mPassword2;

        UserLoginTask(String username, String password, String username2, String password2) {
            mUsername = username;
            mPassword = password;
            mUsername2 = username2;
            mPassword2 = password2;
        }

        @Override
        protected String doInBackground(Void... params) {
            return Ictclogin_functions.doLogin(mUsername, mPassword, mUsername2, mPassword2);
        }

        @Override
        protected void onPostExecute(final String result) {
            mAuthTask = null;
            showProgress(false);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public class UserLogoutTask extends AsyncTask<Void, Void, String> {

        UserLogoutTask() {
        }

        @Override
        protected String doInBackground(Void... params) {
            return Ictclogin_functions.doLogout();
        }

        @Override
        protected void onPostExecute(final String result) {
            mLogoutTask = null;
            showProgress(false);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onCancelled() {
            mLogoutTask = null;
            showProgress(false);
        }
    }

}
