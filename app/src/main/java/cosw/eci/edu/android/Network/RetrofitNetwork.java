package cosw.eci.edu.android.Network;


import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.data.entities.User;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNetwork implements Network {

    public static final String BASE_URL = "https://searchat.herokuapp.com/";

    private UserService userService;
    private EventService eventService;

    private ExecutorService backgroundExecutor = Executors.newFixedThreadPool( 1 );

    public RetrofitNetwork()
    {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();


        Retrofit retrofit =
                new Retrofit.Builder().baseUrl( BASE_URL ).client(okHttpClient).addConverterFactory( GsonConverterFactory.create() ).build();
        userService = retrofit.create( UserService.class );
        eventService = retrofit.create(EventService.class);

    }

    @Override
    public void login( final LoginWrapper loginWrapper, final RequestCallback<Token> requestCallback )
    {
        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call<Token> call = userService.login( loginWrapper );

                try
                {
                    Response<Token> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {

                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );

    }

    @Override
    public void createUser(final User user,final  RequestCallback<Boolean> requestCallback) {

        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> call = userService.createUser( user );

                try{
                    Response<Boolean> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }

            }
        });
    }

    @Override
    public void getUser(final String username,final RequestCallback<User> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<User> call = userService.getUser( username );

                try{
                    Response<User> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( e ) );
                }

            }
        });
    }

    @Override
    public void updateUser(final User user,final RequestCallback<Boolean> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> call = userService.updateUser(user.getUsername(), user );

                try{
                    Response<Boolean> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }

            }
        });
    }

    @Override
    public void updateImageUser(final String username, final MultipartBody.Part file, final  RequestCallback<Boolean> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> call = userService.updateImageUser(username,file);
                try{
                    Response<Boolean> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }

            }
        });
    }

    @Override
    public void getAllEvents(final RequestCallback<List<Event>> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<Event>> call = eventService.getAllEvents();
                try{
                    Response<List<Event>> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( e ) );
                }

            }
        });
    }

    @Override
    public void getEventsByLocation(final String location, final RequestCallback<List<Event>> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<Event>> call = eventService.getEventsByLocation(location);
                try{
                    Response<List<Event>> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( e ) );
                }

            }
        });
    }

    @Override
    public void getEventsOwned(final String username, final RequestCallback<List<Event>> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<Event>> call = eventService.getEventsOwned(username);
                try{
                    Response<List<Event>> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( e ) );
                }

            }
        });
    }

    @Override
    public void getEventsJoined(final String username, final RequestCallback<List<Event>> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<List<Event>> call = eventService.getEventsJoined(username);
                try{
                    Response<List<Event>> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( e ) );
                }

            }
        });
    }

    @Override
    public void createEvent(final Event event,final RequestCallback<Event> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<Event> call = eventService.createEvent(event);
                try{
                    Response<Event> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( e ) );
                }

            }
        });
    }

    @Override
    public void updateImagEvent(final int activityId, final MultipartBody.Part file,final  RequestCallback<Boolean> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> call = eventService.createImageEvent(activityId,file);
                try{
                    Response<Boolean> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }

            }
        });
    }

    @Override
    public void joinEvent(final int activityId, final String username, final RequestCallback<Boolean> requestCallback) {
        backgroundExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Call<Boolean> call = eventService.joinEvent(activityId,username);
                try{
                    Response<Boolean> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );

                }catch ( Exception e )
                {
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }

            }
        });
    }

    public void addSecureTokenInterceptor( final String token )
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor( new Interceptor()
        {
            @Override
            public okhttp3.Response intercept( Chain chain )
                    throws IOException
            {
                Request original = chain.request();

                Request request = original.newBuilder().header( "Accept", "application/json" ).header( "Authorization",
                        "Bearer "
                                + token ).method(
                        original.method(), original.body() ).build();
                return chain.proceed( request );
            }
        } );
        Retrofit retrofit =
                new Retrofit.Builder().baseUrl( BASE_URL ).addConverterFactory( GsonConverterFactory.create() ).client(
                        httpClient.build() ).build();
        userService = retrofit.create( UserService.class );
    }
}
