package cosw.eci.edu.android.Network;

import cosw.eci.edu.android.data.entities.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Path;

public interface UserService {

    @POST( "user/login" )
    Call<Token> login(@Body LoginWrapper user );

    @POST("user/item")
    Call<Boolean> createUser(@Body User user);

    @GET("user/{username}")
    Call<User> getUser(@Path("username") String username);

    @POST("user/update/{username}")
    Call<Boolean> updateUser(@Path("username") String username, @Body User user);

}
