package be.kuleuven.softdev.hehuang.pokepelican.Cart;
/**
 * Created by shuaigehan on 12/27/2016.
 */

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cloudinary.Cloudinary;
import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.softdev.hehuang.pokepelican.Adapters.MyRecyclerAdapter;
import be.kuleuven.softdev.hehuang.pokepelican.CartDetail;
import be.kuleuven.softdev.hehuang.pokepelican.NetworkController;
import be.kuleuven.softdev.hehuang.pokepelican.R;
import be.kuleuven.softdev.hehuang.pokepelican.RecyclerItemClickListener;
import be.kuleuven.softdev.hehuang.pokepelican.mCloud.MyConfiguration;
import be.kuleuven.softdev.hehuang.pokepelican.objects.NewsFeeds;


/**
 * The type Cart fragment.
 */
public class CartFragment extends Fragment implements View.OnClickListener {

    /**
     * The Buyerid.
     */
    String buyerid;

    /**
     * The Userid.
     */
    String userid;

    /**
     * The Item name.
     */
    String itemName;

    /**
     * The Search deleteurl. to search the item you want to delete
     */
    String searchDeleteurl;

    /**
     * The Url. to search item in cart by buyerid
     */
    String url;

    /**
     * The Item id.
     */
    int itemID;

    /**
     * The Botton to refresh the cart
     */
    FloatingActionButton brefreshcart;


    /**
     * The Queue. to hold request
     */
    RequestQueue queue;

    /**
     * The Queue 2. to hold request
     */
    RequestQueue queue2;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            buyerid = profile.getId();
            url = "http://studev.groept.be/api/a16_sd306/SearchCart/" + buyerid;
        } else {
            Toast.makeText(getActivity(), "Please Log in first", Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return mainlayout
     */

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainLayout = inflater.inflate(R.layout.cart_layout,
                container, false);

        //Initialize RecyclerView
        recyclerView = (RecyclerView) mainLayout.findViewById(R.id.recyclerviewcart);
        adapter = new MyRecyclerAdapter(getActivity(), feedsList);          // this part changed
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));      // also this part
        recyclerView.setAdapter(adapter);

        brefreshcart = (FloatingActionButton) mainLayout.findViewById(R.id.refreshcart);
        brefreshcart.setOnClickListener(this);

        //Getting Instance of Volley Request Queue
        queue = NetworkController.getInstance(getActivity()).getRequestQueue();    // and  this part
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //parsing json and create feedList
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
                        int itemid = obj.getInt("ItemID");
                        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
                        String picUrl = cloud.url().generate("itempic/" + itemid + ".jpg");
                        NewsFeeds feeds = new NewsFeeds(itemname, describtion, picUrl, price, obj.getString("Location"), obj.getString("ItemUserid"), name,itemid);
                        userid = obj.getString("UserId");

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

        recyclerView.addOnItemTouchListener(    //when click the item
                new RecyclerItemClickListener(getActivity(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        NewsFeeds currentitem = feedsList.get(position);
                        Intent intent = new Intent(getActivity(), CartDetail.class);
                        intent.putExtra("theDetail", currentitem);
                        startActivity(intent);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {   // long click the item
                        NewsFeeds currentitem = feedsList.get(position);       // get the name of the long clicked item
                        itemName = currentitem.getFeedName();
                        itemID = currentitem.getItemid();
                        searchDeleteurl = "http://api.a16_sd306.studev.groept.be/Delete/" +itemID;
                        new AlertDialog.Builder(getActivity())
                                .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        //do the delete
                                        Toast.makeText(getActivity(), "Item is deleted from the cart , please refresh", Toast.LENGTH_LONG).show();
                                        queue = NetworkController.getInstance(getActivity()).getRequestQueue();
                                        JsonArrayRequest newsReq = new JsonArrayRequest(searchDeleteurl, new Response.Listener<JSONArray>() {
                                            @Override
                                            public void onResponse(JSONArray response) {

                                                for (int i = 0; i < response.length(); i++) {
                                                    try {
                                                        queue2 = NetworkController.getInstance(getActivity()).getRequestQueue();
                                                    } catch (Exception e) {
                                                        System.out.println(e.getMessage());
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
                                })
                                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .show();
                    }
                })
        );

        return mainLayout;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.refreshcart:
                feedsList.clear();
                queue = NetworkController.getInstance(getActivity()).getRequestQueue();    // and  this part
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
                                int itemid = obj.getInt("ItemID");
                                Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
                                String picUrl = cloud.url().generate("itempic/" + itemid + ".jpg");
                                NewsFeeds feeds = new NewsFeeds(itemname, describtion, picUrl, price, obj.getString("Location"), obj.getString("ItemUserid"), name,itemid);
                                userid = obj.getString("UserId");
                                // adding movie to movies array
                                feedsList.add(feeds);

                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            } finally {
                                //Notify adapter about data changes
                                adapter.notifyItemChanged(i);
                                getActivity().runOnUiThread(new Runnable() {
                                    public void run() {
                                        adapter.notifyDataSetChanged();
                                    }
                                });
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

                adapter = new MyRecyclerAdapter(getActivity(), feedsList);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));      // also this part
                recyclerView.setAdapter(adapter);
                break;
        }
    }
}