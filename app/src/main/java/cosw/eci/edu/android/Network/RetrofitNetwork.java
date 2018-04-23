package cosw.eci.edu.android.Network;


import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNetwork implements Network {

    private static final String BASE_URL = "http://192.168.0.10:8080/";

    private NetworkService networkService;

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
        networkService = retrofit.create( NetworkService.class );

    }

    @Override
    public void login( final LoginWrapper loginWrapper, final RequestCallback<Token> requestCallback )
    {
        backgroundExecutor.execute( new Runnable()
        {
            @Override
            public void run()
            {
                Call<Token> call = networkService.login( loginWrapper );

                try
                {
                    Response<Token> execute = call.execute();
                    requestCallback.onSuccess( execute.body() );
                }
                catch ( IOException e )
                {
                    System.out.println(e);
                    requestCallback.onFailed( new NetworkException( null, e ) );
                }
            }
        } );

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
        networkService = retrofit.create( NetworkService.class );
    }
}
