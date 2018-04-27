package cosw.eci.edu.android.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.User;

public class EditProfileInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_information);

        Intent intent = getIntent();
        //Obtain the object
        User user=(User) intent.getBundleExtra(BaseActivity.PASS_USER).getSerializable(BaseActivity.PASS_USER_OBJECT);
        System.out.println("--------------------------------");
        System.out.println(user);
        System.out.println("--------------------------------");
    }
}
