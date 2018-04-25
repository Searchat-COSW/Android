package cosw.eci.edu.android.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import cosw.eci.edu.android.Network.Network;
import cosw.eci.edu.android.Network.NetworkException;
import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.User;

public class SignUpActivity extends AppCompatActivity {


    private RetrofitNetwork retrofitNetwork;
    private Context context;
    private Activity activity;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        retrofitNetwork = new RetrofitNetwork();
        context = this;
        activity = this;

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE); //View.Visible

        Button signUpButton = (Button) findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewPerson(view);
            }
        });
    }

    private void createNewPerson(View view) {
        AutoCompleteTextView name = (AutoCompleteTextView) findViewById(R.id.first_name);
        AutoCompleteTextView lastName = (AutoCompleteTextView) findViewById(R.id.last_name);
        AutoCompleteTextView email = (AutoCompleteTextView) findViewById(R.id.email);
        AutoCompleteTextView username = (AutoCompleteTextView) findViewById(R.id.username);
        TextView password = (TextView) findViewById(R.id.password);
        if (validateEmail(email.getText().toString())){
            final User user = new User(username.getText().toString(),email.getText().toString(),password.getText().toString(),name.getText().toString(),lastName.getText().toString());
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            changeProgressBar(true);

            retrofitNetwork.createUser(user, new Network.RequestCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    changeProgressBar(false);
                    if (response != null && response){

                        //login for the first time
                        Intent intent = new Intent(activity, LoginActivity.class);
                        //Start the new activity using the intent.
                        startActivity(intent);
                        //delete the current activity from the stack
                        activity.finish();
                    }
                    else{
                        showErrMessage(getString(R.string.signup_err_cant_add_user));
                    }
                }

                @Override
                public void onFailed(NetworkException e) {
                    changeProgressBar(false);
                    showErrMessage(getString(R.string.signup_err_username_already_created));
                }
            });

        }
        else{
            email.setError(getString(R.string.signup_err_email));
        }

    }

    private Boolean validateEmail(String s) {
        return s.contains("@") && s.contains(".");
    }

    private void changeProgressBar(final Boolean status){

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                    progressBar.setVisibility(status ? View.VISIBLE: View.GONE);
                    progressBar.animate().setDuration(shortAnimTime).alpha(
                            status ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            progressBar.setVisibility(status ? View.VISIBLE : View.GONE);
                        }
                    });

                }
            });

    }

    private void showErrMessage(final String message){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView errView = (TextView) findViewById(R.id.errorMessage);
                errView.setText(message);
            }
        });

    }
}
