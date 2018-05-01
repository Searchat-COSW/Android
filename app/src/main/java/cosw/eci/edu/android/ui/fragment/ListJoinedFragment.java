package cosw.eci.edu.android.ui.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import cosw.eci.edu.android.ui.adapter.EventAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListJoinedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListJoinedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListJoinedFragment extends Fragment {

    private final static int REQUEST_CODE_FOR_LOCATION = 19;
    private final static int GPS_INTENT = 61;

    //view params
    private View rootView;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    //app params
    private Context context;
    private List<Event> events;

    //for location
    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private ListJoinedFragment fragment;
    private RetrofitNetwork retrofitNetwork;


    private ListJoinedFragment.OnFragmentInteractionListener mListener;

    public ListJoinedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListJoinedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListJoinedFragment newInstance(String param1, String param2) {
        ListJoinedFragment fragment = new ListJoinedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofitNetwork = new RetrofitNetwork();
        // Define a listener that responds to location updates
        fragment = this;
        //obtain the location
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (location != null) {
                    fragment.location = location;
                    //Clean the string
                    String cityLocation = getCityNameByLocation(location);
                    cityLocation = getCleanString(cityLocation);
                    locationManager.removeUpdates(locationListener);
                    //get username
                    String defaultValue = getResources().getString(R.string.default_access);
                    SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.shared_preferences),Context.MODE_PRIVATE);
                    String username= sharedPref.getString(getString(R.string.saved_username),defaultValue);
                    //consult  by username
                    retrofitNetwork.getEventsJoined(username,new Network.RequestCallback<List<Event>>() {
                        @Override
                        public void onSuccess(List<Event> response) {
                            events = response;
                            if(events == null) events = new ArrayList<>();
                            events.add(new Event(1,"Monserrate",null,null,
                                    null,null,new Date(20000),null,new Long(0),null,"https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Monserrate_Sanctuary.JPG/1200px-Monserrate_Sanctuary.JPG"));
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_list_joined, container,    false);
        if (events!=null) configureRecyclerView();

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
        if (context instanceof ListJoinedFragment.OnFragmentInteractionListener) {
            mListener = (ListJoinedFragment.OnFragmentInteractionListener) context;
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
        recyclerView = (RecyclerView) rootView.findViewById( R.id.recycler_view_joined );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(eventAdapter);
    }


    private String getCityNameByLocation(Location location) {
        String fnialAddress ="";
        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault()); //it is Geocoder
        try {
            List<Address> address = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            fnialAddress = address.get(0).getSubAdminArea(); //This is the complete address.
        } catch (IOException e) {}
        catch (NullPointerException e) {}
        return fnialAddress;
    }

    @NonNull
    private String getCleanString(String cityLocation) {
        cityLocation = Normalizer.normalize(cityLocation, Normalizer.Form.NFD);
        cityLocation = cityLocation.replaceAll("[^\\p{ASCII}]", "");
        cityLocation = cityLocation.toLowerCase();
        return cityLocation;
    }
}
