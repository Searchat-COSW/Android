package cosw.eci.edu.android.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Lenguage;
import cosw.eci.edu.android.data.entities.User;

public class showUser extends AppCompatActivity {

    private User user;
    private ImageView imageView;
    private TextView userNameView;
    private TextView aboutYouView;
    private TextView nationalityView;
    private TextView languaguesView;
    private TextView fullUserNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);

        //user = (User) getIntent().getSerializableExtra()

        //initializate variable
        imageView = (ImageView) findViewById(R.id.user_picture);
        userNameView = (TextView) findViewById(R.id.user_name);
        aboutYouView = (TextView) findViewById(R.id.user_about_you);
        nationalityView = (TextView) findViewById(R.id.user_nationality);
        languaguesView = (TextView) findViewById(R.id.user_languagues);
        fullUserNameView = (TextView) findViewById(R.id.user_full_name);

        //set variables
        Picasso.with(this).load(RetrofitNetwork.BASE_URL+"user/"+user.getUsername()+"/image").into(imageView);
        userNameView.setText(user.getUsername());
        fullUserNameView.setText(user.getFirstname()+" "+user.getLastname());
        aboutYouView.setText(user.getProfileInformation().getAboutYou());

        String value = user.getProfileInformation().getNationality()==null || user.getProfileInformation().getNationality().equals("") ? "Dont add nationality yet" : user.getProfileInformation().getNationality();
        nationalityView.setText(value);

        List<Lenguage> lenguagueList = user.getProfileInformation().getLanguages();
        String allLenguages = "";
        for(int i = 0; i < lenguagueList.size(); i++){
            allLenguages += lenguagueList.get(i).getLenguage()+", ";
        }
        languaguesView.setText(allLenguages);

    }
}
