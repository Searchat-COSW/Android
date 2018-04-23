package cosw.eci.edu.android.Network;

public interface Network {
    void login( LoginWrapper loginWrapper, RequestCallback<Token> requestCallback );


    interface RequestCallback<T>
    {

        void onSuccess( T response );

        void onFailed( NetworkException e );

    }

}
