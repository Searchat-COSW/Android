package cosw.eci.edu.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import cosw.eci.edu.android.Network.Network;
import cosw.eci.edu.android.Network.NetworkException;
import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.User;
import cosw.eci.edu.android.ui.adapter.FixedTabsPagerAdapter;
import cosw.eci.edu.android.ui.fragment.ListAllFragment;
import cosw.eci.edu.android.ui.fragment.ListJoinedFragment;
import cosw.eci.edu.android.ui.fragment.ListOwnedFragment;

public class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
                    ListAllFragment.OnFragmentInteractionListener,
                    ListOwnedFragment.OnFragmentInteractionListener,
                    ListJoinedFragment.OnFragmentInteractionListener {


    NavigationView navigationView;
    private String defaultValue;

    //acces token key
    private String accessToken;
    private String username;

    //local memory
    SharedPreferences sharedPref;

    //Network
    private RetrofitNetwork retrofitNetwork;

    private Activity activity;
    //User data
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        activity = this;
        //ask if he has already logged in
        sharedPref = getSharedPreferences(getString(R.string.shared_preferences),Context.MODE_PRIVATE);
        defaultValue = getResources().getString(R.string.default_access);
        accessToken = sharedPref.getString(getString(R.string.saved_access_token), defaultValue);
        if(accessToken.equals( getResources().getString(R.string.default_access))){
            //login for the first time
            Intent intent = new Intent(this, LoginActivity.class);
            //Start the new activity using the intent.
            startActivity(intent);
            //delete the current activity from the stack
            finish();
        }


        //user is already loggin
        retrofitNetwork = new RetrofitNetwork();




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //ViewPager section
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new FixedTabsPagerAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);


        //settingup the nav_header

        setUpUsername();
    }

    private void setUpUsername(){
        username = sharedPref.getString(getString(R.string.saved_username),defaultValue);
        if(!username.equals(defaultValue)) {
            View headerView = navigationView.getHeaderView(0);
            TextView navUsernameView = (TextView) headerView.findViewById(R.id.nav_header_username);
            navUsernameView.setText(username);

            retrofitNetwork.getUser(username, new Network.RequestCallback<User>() {
                @Override
                public void onSuccess(User response) {
                    user = response;
                    if (user != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                View headerView = navigationView.getHeaderView(0);
                                TextView navUserRealNameView = (TextView) headerView.findViewById(R.id.nav_header_real_name);
                                navUserRealNameView.setText(user.getFirstname() + " " + user.getLastname());
                                TextView navUserEmailView = (TextView) headerView.findViewById(R.id.nav_header_email);
                                navUserEmailView.setText(user.getEmail());
                                final ImageView image = (ImageView) headerView.findViewById(R.id.nav_header_image);
                                Picasso.with(activity).load(RetrofitNetwork.BASE_URL+"user/"+username+"/image").into(image, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        image.setImageResource(R.drawable.no_user);
                                    }
                                });
                            }
                        });
                    }
                }

                @Override
                public void onFailed(NetworkException e) {
                    //
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_editProfile) {
            // Handle the camera action
        } else if (id == R.id.nav_signOut) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
