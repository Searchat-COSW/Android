package cosw.eci.edu.android.ui.activity;

import android.content.Context;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.data.entities.User;
import cosw.eci.edu.android.ui.adapter.EventAdapter;
import cosw.eci.edu.android.ui.adapter.UserAdapter;
import de.hdodenhof.circleimageview.CircleImageView;


public class ShowEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EVENT_OBJECT = "event_object";

    private GoogleMap googleMap;

    private Event event;
    private Context context;
    private ImageView imageView;
    private TextView nameView;
    private TextView dateView;
    private TextView priceView;
    private TextView locationView;
    private TextView administratorView;
    private TextView descriptionView;
    private RetrofitNetwork retrofitNetwork;
    private RecyclerView listParticipants;
    private ArrayList<View> participants;
    ArrayList<User> mientras;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        event =(Event) getIntent().getSerializableExtra(EVENT_OBJECT);
        //Configure google maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        context=this;
        //initialize variables
        imageView = (ImageView) findViewById(R.id.image) ;
        nameView = (TextView) findViewById(R.id.event_name);
        dateView = (TextView) findViewById(R.id.event_date);
        priceView = (TextView) findViewById(R.id.event_price);
        locationView = (TextView) findViewById(R.id.event_location);
        //administratorView = (TextView) findViewById(R.id.event_administrator);
        descriptionView = (TextView) findViewById(R.id.event_description);
        listParticipants = (RecyclerView) findViewById(R.id.listParticipants);

        //set variables
        Picasso.with(this).load(RetrofitNetwork.BASE_URL+"activity/"+event.getId()+"/image").into(imageView);
        nameView.setText(event.getName().toUpperCase());
        dateView.setText(event.getDate());
        priceView.setText(event.getPrice().toString());
        locationView.setText(event.getLocation().toUpperCase());
        //administratorView.setText(event.getAdministrator().getFirstname().toUpperCase()+" "+event.getAdministrator().getLastname().toUpperCase());
        descriptionView.setText(event.getDescription());

        configureAdminRecyclerView();
        configureRecyclerView();

    }

    private void configureAdminRecyclerView() {
        List<User> adminList = new ArrayList<>();
        adminList.add(event.getAdministrator());
        UserAdapter userAdaptar = new UserAdapter(adminList,this);
        RecyclerView recyclerView = (RecyclerView) findViewById( R.id.list_admin );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(userAdaptar);
    }

    private void configureRecyclerView(){
        UserAdapter userAdaptar = new UserAdapter(event.getParticipants(),this);
        recyclerView = (RecyclerView) findViewById( R.id.listParticipants );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(userAdaptar);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        addMarkerAndZoom(event.getName(),15);

    }

    private void addMarkerAndZoom(String title, int zoom) {
        LatLng myLocation = new LatLng(event.getLatitude(), event.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(myLocation).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
    }
}
