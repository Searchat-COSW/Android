package cosw.eci.edu.android.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;

public class ShowEventActivity extends AppCompatActivity {

    public static final String EVENT_OBJECT = "event_object";

    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_event);
        event =(Event) getIntent().getSerializableExtra(EVENT_OBJECT);

    }
}
