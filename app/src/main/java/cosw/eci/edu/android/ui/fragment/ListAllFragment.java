package cosw.eci.edu.android.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cosw.eci.edu.android.Network.Network;
import cosw.eci.edu.android.Network.NetworkException;
import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.data.entities.User;
import cosw.eci.edu.android.ui.adapter.EventAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListAllFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListAllFragment extends Fragment {

    private final static int REQUEST_CODE_FOR_LOCATION = 18;
    private final static int GPS_INTENT = 60;
    //view params
    private View rootView;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private RetrofitNetwork retrofitNetwork;

    //for location
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private ListAllFragment fragment;
    //app params
    private Context context;
    private List<Event> events;


    private OnFragmentInteractionListener mListener;

    public ListAllFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListAllFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListAllFragment newInstance(String param1, String param2) {
        ListAllFragment fragment = new ListAllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
        events = new ArrayList<>();
        retrofitNetwork = new RetrofitNetwork();

        //obtain the location
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        checkLocationPermissons();
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location != null) {
                    fragment.location = location;
                    //Clean the string
                    String cityLocation = getCityNameByLocation(location);
                    cityLocation = getCleanString(cityLocation);
                    locationManager.removeUpdates(locationListener);
                    retrofitNetwork.getEventsByLocation(cityLocation,new Network.RequestCallback<List<Event>>() {
                        @Override
                        public void onSuccess(List<Event> response) {
                            events = response;
                            if(events == null) events = new ArrayList<>();
                            events.add(new Event(1,"Monserrate",null,null,
                                    null,null,new Date(20000),null,new Long(0),null,"https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Monserrate_Sanctuary.JPG/1200px-Monserrate_Sanctuary.JPG"));
                            events.add(new Event(2,"Candelaria",null,null,
                                    null,null,new Date(200000),null,new Long(10000000),null,"https://media.wsimag.com/attachments/c4701fa97eaae33ac66d3712332a65486569ac44/store/fill/1090/613/aeed32aade2ea76cb5c6d22be74e255b21c9fa092353fc9f077676a60bc3/Bogota-Colombia-Barrio-de-La-Candelaria.jpg"));
                            events.add(new Event(3,"Mi casa",null,null,
                                    null,null,new Date(50),null,new Long(0),null,"https://i.pinimg.com/originals/ac/32/6a/ac326afab46ea50c9abbd8650e1cca3a.jpg"));
                            events.add(new Event(4,"Transmilenio trip",null,null,
                                    null,null,new Date(30000),null,new Long(40000),null,"https://static.iris.net.co/semana/upload/images/2014/9/24/403969_145933_1.jpg"));
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    configureRecyclerView();
                                }
                            });
                        }

                        @Override
                        public void onFailed(NetworkException e) {
                            System.out.println(e.getMessage()+ "---------------");
                            for(StackTraceElement el : e.getStackTrace()){
                                System.out.println(el.toString());
                            }
                            //getActivity().finish();
                        }
                    });
                }


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {


            }

            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @NonNull
    private String getCleanString(String cityLocation) {
        cityLocation = Normalizer.normalize(cityLocation, Normalizer.Form.NFD);
        cityLocation = cityLocation.replaceAll("[^\\p{ASCII}]", "");
        cityLocation = cityLocation.toLowerCase();
        System.out.println(cityLocation);
        System.out.println("--------------------------------------");
        return cityLocation;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_FOR_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkLocationPermissons();
                }
                return;
        }
    }


    private void checkLocationPermissons(){
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            }, REQUEST_CODE_FOR_LOCATION);
        }
        if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, GPS_INTENT);
        }


    }

    @Override
     public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode){
            case GPS_INTENT:
                checkLocationPermissons();
                return;
        }
    }


    private String getCityNameByLocation(Location location) {
        String fnialAddress ="";
        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault()); //it is Geocoder
        try {
            List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            for(Address a:address){
                System.out.println(a);
            }

            fnialAddress = address.get(0).getSubAdminArea(); //This is the complete address.
        } catch (IOException e) {}
        catch (NullPointerException e) {}
        return fnialAddress;
    }


    private Location getBestLastKnownLocation() {
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list_all, container,    false);
        configureRecyclerView();
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void configureRecyclerView(){
        eventAdapter = new EventAdapter(events,getActivity());
        recyclerView = (RecyclerView) rootView.findViewById( R.id.recycler_view_all );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(eventAdapter);
    }
}
