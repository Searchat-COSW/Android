package cosw.eci.edu.android.ui.activity;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;

public class ShowEventActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EVENT_OBJECT = "event_object";

    private GoogleMap googleMap;

    private Event event;
    private ImageView imageView;
    private TextView nameView;
    private TextView dateView;
    private TextView priceView;
    private TextView locationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        event =(Event) getIntent().getSerializableExtra(EVENT_OBJECT);
        //Configure google maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialize variables
        imageView = (ImageView) findViewById(R.id.image) ;
        nameView = (TextView) findViewById(R.id.event_name);
        dateView = (TextView) findViewById(R.id.event_date);
        priceView = (TextView) findViewById(R.id.event_price);
        locationView = (TextView) findViewById(R.id.event_location);

        //set variables
        Picasso.with(this).load(RetrofitNetwork.BASE_URL+"activity/"+event.getId()+"/image").into(imageView);
        nameView.setText(event.getName());
        dateView.setText(event.getDate());
        priceView.setText(event.getPrice().toString());
        locationView.setText(event.getLocation());

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        System.out.println("EN EL METODO");
        addMarkerAndZoom(event.getName(),15);

    }

    private void addMarkerAndZoom(String title, int zoom) {
        LatLng myLocation = new LatLng(event.getLatitude(), event.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(myLocation).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
    }
}
