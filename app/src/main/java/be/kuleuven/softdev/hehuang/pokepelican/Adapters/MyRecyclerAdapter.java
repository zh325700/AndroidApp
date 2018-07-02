package be.kuleuven.softdev.hehuang.pokepelican.Adapters;

/**
 * Created by shuaigehan on 12/27/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import be.kuleuven.softdev.hehuang.pokepelican.NetworkController;
import be.kuleuven.softdev.hehuang.pokepelican.R;
import be.kuleuven.softdev.hehuang.pokepelican.objects.NewsFeeds;


/**
 * The type My recycler adapter.
 */
public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

    private List<NewsFeeds> feedsList;
    private Context context;
    private LayoutInflater inflater;


    /**
     * Instantiates a new My recycler adapter.
     *
     * @param context   the context
     * @param feedsList the feeds list
     */
    public MyRecyclerAdapter(Context context, List<NewsFeeds> feedsList) {

        this.context = context;
        this.feedsList = feedsList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *
     * @param parent the parent
     * @param viewType the viewType
     * @return rootview
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = inflater.inflate(R.layout.singleitem_recyclerview, parent, false);
        return new MyViewHolder(rootView);
    }

    /**
     *
     * @param holder myViewHolder
     * @param position the position of item
     */

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewsFeeds feeds = feedsList.get(position);
        //Pass the values of feeds object to Views
        holder.title.setText(feeds.getFeedName());
        holder.imageview.setImageUrl(feeds.getImgURL(), NetworkController.getInstance(context).getImageLoader());
        holder.price.setText(feeds.getPrice());
        holder.location.setText(feeds.getLocation());
        holder.username.setText(feeds.getUserName());
    }

    /**
     *
     * @return the size of feedList
     */
    @Override
    public int getItemCount() {
        return feedsList.size();
    }


    /**
     * The type My view holder.
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username, title, price, location;
        private NetworkImageView imageview;

        /**
         * Instantiates a new My view holder.
         *
         * @param itemView the item view
         */
        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title_view);
            // Volley's NetworkImageView which will load Image from URL
            imageview = (NetworkImageView) itemView.findViewById(R.id.thumbnail);
            price = (TextView) itemView.findViewById(R.id.price_view);
            location = (TextView) itemView.findViewById((R.id.Location));
            username = (TextView) itemView.findViewById(R.id.UserName);
        }
    }
}