package cosw.eci.edu.android.Network;

import cosw.eci.edu.android.data.entities.User;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface UserService {

    @POST( "user/login" )
    Call<Token> login(@Body LoginWrapper user );

    @POST("user/item")
    Call<Boolean> createUser(@Body User user);
}
