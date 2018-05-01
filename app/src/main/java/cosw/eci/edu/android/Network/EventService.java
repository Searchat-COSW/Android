package cosw.eci.edu.android.Network;

import java.util.List;

import cosw.eci.edu.android.data.entities.Event;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EventService {

    @POST( "activity/" )
    Call<List<Event>> getAllEvents();



    @POST( "activity/location/{location}" )
    Call<List<Event>> getEventsByLocation(@Path("location") String location);
}
