package cosw.eci.edu.android.Network;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.Call;

public interface NetworkService {

    @POST( "user/login" )
    Call<Token> login(@Body LoginWrapper user );
}
