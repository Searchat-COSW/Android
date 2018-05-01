package cosw.eci.edu.android.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.ui.activity.BaseActivity;
import cosw.eci.edu.android.ui.activity.ShowEventActivity;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Activity activity;
    private List<Event> events;

    public EventAdapter(List<Event> events, Activity activity){
        this.events = events;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.event_row, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Event event = events.get(position);
        holder.event = event;
        holder.name.setText(event.getName()); //title.setText(movie.getTitle());
        holder.date.setText(event.getDate().toString());
        if(event.getPrice().intValue()<=0){
            holder.price.setText("FREE");
            holder.price.setTextColor(ResourcesCompat.getColor(activity.getResources(), R.color.colorEventCost, null));
        }
        else{
            holder.price.setText("$"+event.getPrice().toString());
        }
        //Picasso.with(activity).load(event.getImage()).into(holder.image);
        Picasso.with(activity).load(RetrofitNetwork.BASE_URL+"activity/"+event.getId()+"/image").into(holder.image);



    }

    @Override
    public int getItemCount() {
        return events.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;
        TextView price;
        ImageView image;
        Event event;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.event_row_image);
            name = (TextView) itemView.findViewById(R.id.event_row_name);
            date = (TextView) itemView.findViewById(R.id.event_row_date);
            price = (TextView) itemView.findViewById(R.id.event_row_price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //login for the first time
                    Intent intent = new Intent(activity, ShowEventActivity.class);
                    intent.putExtra(ShowEventActivity.EVENT_OBJECT,event);
                    //Start the new activity using the intent.
                    activity.startActivity(intent);
                }
            });
        }

    }
}
