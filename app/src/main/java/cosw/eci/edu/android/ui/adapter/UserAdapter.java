package cosw.eci.edu.android.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import cosw.eci.edu.android.Network.RetrofitNetwork;
import cosw.eci.edu.android.R;
import cosw.eci.edu.android.data.entities.Event;
import cosw.eci.edu.android.data.entities.User;
import cosw.eci.edu.android.ui.activity.ShowEventActivity;

/**
 * Created by 2105409 on 5/17/18.
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Activity activity;
    private List<User> users;

    public UserAdapter(List<User> users, Activity activity){
        this.users = users;
        this.activity = activity;
    }


    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.user_row, viewGroup, false);

        return new UserAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.user = user;
        //Picasso.with(activity).load(event.getImage()).into(holder.image);
        Picasso.with(activity).load(RetrofitNetwork.BASE_URL + "user/" + user.getUsername() + "/image").into(holder.image, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                holder.image.setImageResource(R.drawable.no_user);
            }
        });
        //holder.image.setImageDrawable(activity.getResources().getDrawable(R.drawable.no_user));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        User user;

        public ViewHolder(View itemView) {
            super(itemView);

            image = (ImageView) itemView.findViewById(R.id.user_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    //login for the first time
                    //Intent intent = new Intent(activity, ShowUserActivity.class);
                    //intent.putExtra(ShowEventActivity.EVENT_OBJECT,user);
                    //Start the new activity using the intent.
                    //activity.startActivity(intent);
                }
            });
        }

    }
}
