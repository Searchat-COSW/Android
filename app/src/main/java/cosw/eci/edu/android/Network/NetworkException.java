package cosw.eci.edu.android.Network;

public class NetworkException extends Exception{


        public NetworkException(String  s, Throwable t) {
            super(s,t);
        }

        public NetworkException(String message){
            super(message);
        }
}
