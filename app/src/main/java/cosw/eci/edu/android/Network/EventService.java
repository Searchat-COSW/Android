package cosw.eci.edu.android.Network;

import java.util.List;

import cosw.eci.edu.android.data.entities.Event;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventService {

    @GET( "activity/" )
    Call<List<Event>> getAllEvents();



    @GET( "activity/location/{location}" )
    Call<List<Event>> getEventsByLocation(@Path("location") String location);

    @GET( "activity/owned/{username}" )
    Call<List<Event>> getEventsOwned(@Path("username") String username);

    @GET( "activity/joined/{username}" )
    Call<List<Event>> getEventsJoined(@Path("username") String username);
}
