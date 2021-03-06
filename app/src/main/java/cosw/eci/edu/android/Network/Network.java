package cosw.eci.edu.android.Network;

import java.util.List;

import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.data.entities.User;
import okhttp3.MultipartBody;

public interface Network {

    //for user service
    void login( LoginWrapper loginWrapper, RequestCallback<Token> requestCallback );

    void createUser(User user, RequestCallback<Boolean> requestCallback);

    void getUser(String username, RequestCallback<User> requestCallback);

    void updateUser(User user, RequestCallback<Boolean> requestCallback);

    void updateImageUser(String username, MultipartBody.Part file, RequestCallback<Boolean> requestCallback);

    //for event service

    void getAllEvents(RequestCallback<List<Event>> requestCallback);

    void getEventsByLocation(String location,RequestCallback<List<Event>> requestCallback);

    void getEventsOwned(String username,RequestCallback<List<Event>> requestCallback);

    void getEventsJoined(String username,RequestCallback<List<Event>> requestCallback);

    void createEvent(Event event,RequestCallback<Event> requestCallback);

    void updateImagEvent(int activityId, MultipartBody.Part file, RequestCallback<Boolean> requestCallback);

    void joinEvent(int activityId, String username, RequestCallback<Boolean> requestCallback);

    interface RequestCallback<T>
    {

        void onSuccess( T response );

        void onFailed( NetworkException e );

    }

}
