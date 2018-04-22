package cosw.eci.edu.android.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.ui.adapter.EventAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOwnedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOwnedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListOwnedFragment extends Fragment {


    //view params
    private View rootView;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    //app params
    private Context context;
    private List<Event> events;


    private ListOwnedFragment.OnFragmentInteractionListener mListener;

    public ListOwnedFragment() {
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        events = new ArrayList<>();
        events.add(new Event(1,"Monserrate",null,null,
                null,null,new Date(20000),null,new Long(0),null,"https://upload.wikimedia.org/wikipedia/commons/thumb/7/7d/Monserrate_Sanctuary.JPG/1200px-Monserrate_Sanctuary.JPG"));
        events.add(new Event(2,"Candelaria",null,null,
                null,null,new Date(200000),null,new Long(10000000),null,"https://media.wsimag.com/attachments/c4701fa97eaae33ac66d3712332a65486569ac44/store/fill/1090/613/aeed32aade2ea76cb5c6d22be74e255b21c9fa092353fc9f077676a60bc3/Bogota-Colombia-Barrio-de-La-Candelaria.jpg"));
        events.add(new Event(3,"Mi casa",null,null,
                null,null,new Date(50),null,new Long(0),null,"https://i.pinimg.com/originals/ac/32/6a/ac326afab46ea50c9abbd8650e1cca3a.jpg"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_list_owned, container,    false);

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
        if (context instanceof ListAllFragment.OnFragmentInteractionListener) {
            mListener = (ListOwnedFragment.OnFragmentInteractionListener) context;
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
        recyclerView = (RecyclerView) rootView.findViewById( R.id.recycler_view_list );
        recyclerView.setHasFixedSize( true );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager( getContext() );
        recyclerView.setLayoutManager( layoutManager );
        recyclerView.setAdapter(eventAdapter);
    }
}
