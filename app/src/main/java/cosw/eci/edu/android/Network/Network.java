package cosw.eci.edu.android.Network;

import cosw.eci.edu.android.data.entities.User;

public interface Network {
    void login( LoginWrapper loginWrapper, RequestCallback<Token> requestCallback );

    void createUser(User user, RequestCallback<Boolean> requestCallback);

    void getUser(String username, RequestCallback<User> requestCallback);


    interface RequestCallback<T>
    {

        void onSuccess( T response );

        void onFailed( NetworkException e );

    }

}
