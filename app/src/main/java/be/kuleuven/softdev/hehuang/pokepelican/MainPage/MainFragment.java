package be.kuleuven.softdev.hehuang.pokepelican.MainPage;
/**
 * Created by shuaigehan on 12/27/2016.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cloudinary.Cloudinary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.softdev.hehuang.pokepelican.Adapters.MyRecyclerAdapter;
import be.kuleuven.softdev.hehuang.pokepelican.Detail;
import be.kuleuven.softdev.hehuang.pokepelican.NetworkController;
import be.kuleuven.softdev.hehuang.pokepelican.R;
import be.kuleuven.softdev.hehuang.pokepelican.RecyclerItemClickListener;
import be.kuleuven.softdev.hehuang.pokepelican.mCloud.MyConfiguration;
import be.kuleuven.softdev.hehuang.pokepelican.objects.NewsFeeds;

/**
 * The type Main fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    /**
     * The botton for fresh.
     */
    FloatingActionButton brefresh;
    /**
     * The Queue to hold request.
     */
    RequestQueue queue;
    /**
     * The Url to reach the database.
     */
    String url = "http://studev.groept.be/api/a16_sd306/search";
    /**
     * The Recycler view to show all the single items.
     */
    RecyclerView recyclerView;
    /**
     * The Feeds list to hold all the feeds.
     */
    List<NewsFeeds> feedsList = new ArrayList<NewsFeeds>();
    /**
     * The Adapter to adapte single view to recyclerview.
     */
    MyRecyclerAdapter adapter;

    /**
     * show all the items in the database
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.refreshmian:
                feedsList.clear();
                //Getting Instance of Volley Request Queue
                queue = NetworkController.getInstance(getActivity()).getRequestQueue();
                //Volley's inbuilt class to make Json array request
                JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

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
                //feedsList.clear();
                adapter.notifyDataSetChanged(); // this is important to refresh

                adapter = new MyRecyclerAdapter(getActivity(), feedsList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));      // also this part
                recyclerView.setAdapter(adapter);
                break;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainLayout = inflater.inflate(R.layout.main_layout,
                container, false);
        recyclerView = (RecyclerView) mainLayout.findViewById(R.id.recyclerview);
        //Initialize RecyclerView
        adapter = new MyRecyclerAdapter(getActivity(), feedsList);          // this part changed
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));      // also this part

        recyclerView.setAdapter(adapter);

        brefresh = (FloatingActionButton) mainLayout.findViewById(R.id.refreshmian);
        brefresh.setOnClickListener(this);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        NewsFeeds currentitem = feedsList.get(position);

                        Intent intent = new Intent(getActivity(), Detail.class);
                        intent.putExtra("theDetail", currentitem);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                })
        );
        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getActivity()).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

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
                        int itemid = obj.getInt("ItemID");
                        name = name.replaceAll("%20", " ");
                        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
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
        //feedsList.clear();
        adapter.notifyDataSetChanged(); // this is important to refresh

        return mainLayout;
    }

}