package be.kuleuven.softdev.hehuang.pokepelican;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cloudinary.Cloudinary;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.softdev.hehuang.pokepelican.Adapters.MyRecyclerAdapter;
import be.kuleuven.softdev.hehuang.pokepelican.mCloud.MyConfiguration;
import be.kuleuven.softdev.hehuang.pokepelican.objects.NewsFeeds;

/**
 * The type Search page.
 */
public class SearchPage extends SwipeBackActivity {
    /**
     * The Queue.
     */
    RequestQueue queue;
    /**
     * The Url.
     */
    String url = "http://api.a16_sd306.studev.groept.be/SearchItem/";
    /**
     * The Recycler view.
     */
    RecyclerView recyclerView;
    /**
     * The Feeds list.
     */
    List<NewsFeeds> feedsList = new ArrayList<NewsFeeds>();
    /**
     * The Adapter.
     */
    MyRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beauty);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        Intent intent = getIntent();
        String message = intent.getStringExtra(Search.EXTRA_MESSAGE);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerviewbeauty);
        adapter = new MyRecyclerAdapter(this, feedsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(adapter);
        //Getting Instance of Volley Request Queue
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(SearchPage.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        NewsFeeds currentitem = feedsList.get(position);

                        Intent intent = new Intent(SearchPage.this, Detail.class);
                        intent.putExtra("theDetail", currentitem);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                })
        );
        queue = NetworkController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url + message, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() == 0) {
                    Toast.makeText(getApplicationContext(), "There is nothing to show", Toast.LENGTH_LONG).show();
                }
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        String itemname = obj.getString("Itemname");
                        itemname = itemname.replaceAll("%20", " ");
                        String describtion = obj.getString("Describtion");
                        describtion = describtion.replaceAll("%20", " ");
                        String price = obj.getString("Price");
                        price = price.replaceAll("%20", " ");
                        String name = obj.getString("UserName");
                        name = name.replaceAll("%20", " ");
                        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
                        int itemid = obj.getInt("ItemID");
                        String picUrl = cloud.url().generate("itempic/" + itemid + ".jpg");
                        NewsFeeds feeds = new NewsFeeds(itemname, describtion, picUrl, price, obj.getString("Location"), obj.getString("ItemUserid"), name,itemid);

                        // adding movie to movies array
                        feedsList.add(feeds);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    } finally {
                        //Notify adapter about data changes
                        adapter.notifyItemChanged(i);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        //Adding JsonArrayRequest to Request Queue
        queue.add(newsReq);

    }
}
