package cosw.eci.edu.android.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.User;

public class EditProfileInformationActivity extends AppCompatActivity {

    private Button nationality;
    private TextView name;
    private TextView lastName;
    private TextView nationalityValue;
    private TextView aboutYou;
    private ImageView image;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_information);
        context=this;
        Intent intent = getIntent();
        //Obtain the object
        User user=(User) intent.getBundleExtra(BaseActivity.PASS_USER).getSerializable(BaseActivity.PASS_USER_OBJECT);
        System.out.println("--------------------------------");
        System.out.println(user);
        System.out.println("--------------------------------");

        image = (ImageView) findViewById(R.id.image);

        Picasso.with(context).load(RetrofitNetwork.BASE_URL+"user/"+user.getUsername()+"/image").into(image, new Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError() {
                image.setImageResource(R.drawable.no_user);
            }
        });

        name = (TextView) findViewById(R.id.name);
        lastName = (TextView) findViewById(R.id.last_name);
        nationalityValue = (TextView) findViewById(R.id.nationality_value);
        aboutYou = (TextView) findViewById(R.id.about_you);
        name.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        aboutYou.setText(user.getProfileInformation().getAboutYou());

        String value = user.getProfileInformation().getNationality()==null? "Choose..." : user.getProfileInformation().getNationality();
        nationalityValue.setText(value);


        nationality  = (Button) findViewById(R.id.nationality);
        nationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence countries[] = new CharSequence[] {"Colombia","United States","France","Italy","Germany","Japan","Russia","United Kingdom","Argentina","Mexico","Brazil"};

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Pick a country");

                builder.setItems(countries, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nationalityValue.setText(countries[which]);
                    }
                });
                builder.show();
            }
        });


    }


}
