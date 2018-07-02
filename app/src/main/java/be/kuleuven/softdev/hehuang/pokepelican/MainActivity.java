package be.kuleuven.softdev.hehuang.pokepelican;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONObject;

import be.kuleuven.softdev.hehuang.pokepelican.Cart.CartFragment;
import be.kuleuven.softdev.hehuang.pokepelican.Category.Beauty;
import be.kuleuven.softdev.hehuang.pokepelican.Category.Bikes;
import be.kuleuven.softdev.hehuang.pokepelican.Category.Books;
import be.kuleuven.softdev.hehuang.pokepelican.Category.CategoryFragment;
import be.kuleuven.softdev.hehuang.pokepelican.Category.Electronics;
import be.kuleuven.softdev.hehuang.pokepelican.Category.ForHome;
import be.kuleuven.softdev.hehuang.pokepelican.Category.Toys;
import be.kuleuven.softdev.hehuang.pokepelican.MainPage.MainFragment;
import be.kuleuven.softdev.hehuang.pokepelican.Profile.AboutUs;
import be.kuleuven.softdev.hehuang.pokepelican.Profile.DetectLocation;
import be.kuleuven.softdev.hehuang.pokepelican.Profile.SettingFragment;

/**
 * MainActivity, Control the fragment
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {
    private ProfilePictureView profilePictureView;
    /**
     * The Callback manager.
     */
    CallbackManager callbackManager;
    /**
     * The Profile tracker.
     */
    ProfileTracker profileTracker;
    /**
     * The Access token.
     */
    AccessToken accessToken;
    /**
     * The Profile view.
     */
    ProfilePictureView profileView;
    /**
     * The Json url.
     */
    String json_url;
    /**
     * The Jason string.
     */
    String Jason_string;
    /**
     * The Username.
     */
    String username;
    /**
     * The Userid.
     */
    String userid;
    /**
     * The Userlink.
     */
    String userlink;
    /**
     * The Queue.
     */
    RequestQueue queue;
    /**
     * Show the fragment of MainFragment
     */
    private MainFragment mainFragment;

    /**
     * Show the fragment of CategoryFtagment
     */
    private CategoryFragment categoryFragment;

    /**
     * Show the fragment of CartFragment
     */
    private CartFragment cartFragment;

    /**
     * Show the fragment of SettingFragment
     */
    private SettingFragment settingFragment;

    /**
     * MainLayout
     */
    private View mainLayout;

    /**
     * CategoryLayout
     */
    private View categroyLayout;

    /**
     * CartLayout
     */
    private View cartLayout;

    /**
     * ProfileLayout
     */
    private View settingLayout;

    /**
     * Show the Main image on tab
     */
    private ImageView mainImage;

    /**
     * Show the Category image on tab
     */
    private ImageView categoryImage;

    /**
     * Show the Cart image on tab
     */
    private ImageView cartImage;

    /**
     * Show the Setting image on tab
     */
    private ImageView settingImage;

    /**
     * Show the Main text on tab
     */
    private TextView mainText;

    /**
     * Show the MCategory text on tab
     */
    private TextView categoryText;

    /**
     * Show the Cart text on tab
     */
    private TextView cartText;

    /**
     * Show the Profile text on tab
     */
    private TextView settingText;

    /**
     * Manange Fragment
     */
    private FragmentManager fragmentManager;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        AppEventsLogger.activateApp(this.getApplication());

        //callback Manager

        callbackManager = CallbackManager.Factory.create();

        //find login button

        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        setContentView(R.layout.activity_main);
        // initialize
        initViews();
        fragmentManager = getFragmentManager();
        //select 0 tab at first
        setTabSelection(0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
            }
        };

        //find loginButton

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            //login success

            @Override
            public void onSuccess(LoginResult loginResult) {

                //accessToken save

                accessToken = loginResult.getAccessToken();

                //send request and call graph api

                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {


                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                                username = object.optString("name");
                                userlink = object.optString("link");
                                userid = object.optString("id");
                                ProfilePictureView profilePictureView;
                                profilePictureView = (ProfilePictureView) findViewById(R.id.image);
                                profilePictureView.setProfileId(userid);
                                json_url = "http://studev.groept.be/api/a16_sd306/";
                                StringBuilder sb = new StringBuilder();
                                sb.append(json_url + "Register/");
                                sb.append(userid + "/");
                                sb.append(username + "/");
                                json_url = sb.toString();
                                String oldurl = json_url;
                                String newurl = oldurl.replaceAll(" ", "%20");
                                //Getting Instance of Volley Request Queue
                                queue = NetworkController.getInstance(MainActivity.this).getRequestQueue();
                                //Volley's inbuilt class to make Json array request
                                JsonArrayRequest newsReq = new JsonArrayRequest(newurl, new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {

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
                        });

                //send request

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link");
                request.setParameters(parameters);
                request.executeAsync();


            }


            //cancel login

            @Override
            public void onCancel() {
                // App code
            }

            //login failed

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
    }

    private void updateUI() {

    }

    /**
     * find every layout
     */
    private void initViews() {
        mainLayout = findViewById(R.id.main_layout);
        categroyLayout = findViewById(R.id.category_layout);
        cartLayout = findViewById(R.id.cart_layout);
        settingLayout = findViewById(R.id.setting_layout);
        mainImage = (ImageView) findViewById(R.id.main_image);
        categoryImage = (ImageView) findViewById(R.id.category_image);
        cartImage = (ImageView) findViewById(R.id.cart_image);
        settingImage = (ImageView) findViewById(R.id.setting_image);
        mainText = (TextView) findViewById(R.id.main_text);
        categoryText = (TextView) findViewById(R.id.category_text);
        cartText = (TextView) findViewById(R.id.cart_text);
        settingText = (TextView) findViewById(R.id.setting_text);
        mainLayout.setOnClickListener(this);
        categroyLayout.setOnClickListener(this);
        cartLayout.setOnClickListener(this);
        settingLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_layout:
                setTabSelection(0);
                break;
            case R.id.category_layout:
                setTabSelection(1);
                break;
            case R.id.cart_layout:
                setTabSelection(2);
                break;
            case R.id.setting_layout:
                setTabSelection(3);
                break;
            default:
                break;
        }
    }

    /**
     * select tab
     *
     * @param index every layout in tabs
     */
    private void setTabSelection(int index) {
        clearSelection();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideFragments(transaction);
        switch (index) {
            case 0:
                mainImage.setImageResource(R.drawable.ic_language_white_24dp);
                mainText.setTextColor(Color.WHITE);
                if (mainFragment == null) {
                    mainFragment = new MainFragment();
                    transaction.add(R.id.content, mainFragment);
                } else {
                    transaction.show(mainFragment);
                }
                break;
            case 1:
                categoryImage.setImageResource(R.drawable.ic_assessment_white_24dp);
                categoryText.setTextColor(Color.WHITE);
                if (categoryFragment == null) {
                    categoryFragment = new CategoryFragment();
                    transaction.add(R.id.content, categoryFragment);
                } else {
                    transaction.show(categoryFragment);
                }
                break;
            case 2:
                cartImage.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
                cartText.setTextColor(Color.WHITE);
                if (cartFragment == null) {
                    cartFragment = new CartFragment();
                    transaction.add(R.id.content, cartFragment);
                } else {
                    transaction.show(cartFragment);
                }
                break;
            case 3:
            default:
                settingImage.setImageResource(R.drawable.ic_assignment_ind_white_24dp);
                settingText.setTextColor(Color.WHITE);
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.content, settingFragment);
                } else {
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }


    private void clearSelection() {
        mainImage.setImageResource(R.drawable.ic_language_white_24dp);
        mainText.setTextColor(Color.parseColor("#82858b"));
        categoryImage.setImageResource(R.drawable.ic_assessment_white_24dp);
        categoryText.setTextColor(Color.parseColor("#82858b"));
        cartImage.setImageResource(R.drawable.ic_shopping_cart_white_24dp);
        cartText.setTextColor(Color.parseColor("#82858b"));
        settingImage.setImageResource(R.drawable.ic_assignment_ind_white_24dp);
        settingText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * set all fragment hiden
     *
     * @param transaction manange the fragment
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (mainFragment != null) {
            transaction.hide(mainFragment);
        }
        if (categoryFragment != null) {
            transaction.hide(categoryFragment);
        }
        if (cartFragment != null) {
            transaction.hide(cartFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Search.
     *
     * @param item the item
     */
    public void Search(MenuItem item) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }

    /**
     * Change location.
     *
     * @param item the item
     */
    public void ChangeLocation(MenuItem item) {
        Intent intent = new Intent(this, DetectLocation.class);
        startActivity(intent);
    }

    /**
     * Upload.
     *
     * @param view the view
     */
    public void upload(View view) {
        Intent intent = new Intent(this, UpLoad.class);
        startActivity(intent);
    }


    /**
     * Aboutus.
     *
     * @param view the view
     */
    public void aboutus(View view) {
        Intent intent = new Intent(this, AboutUs.class);
        startActivity(intent);
    }

    /**
     * Electronics botton.
     *
     * @param view the view
     */
    public void electronicsBotton(View view) {
        Intent intent = new Intent(this, Electronics.class);
        startActivity(intent);
    }

    /**
     * Beauty botton.
     *
     * @param view the view
     */
    public void beautyBotton(View view) {
        Intent intent = new Intent(this, Beauty.class);
        startActivity(intent);
    }

    /**
     * Forhome botton.
     *
     * @param view the view
     */
    public void forhomeBotton(View view) {
        Intent intent = new Intent(this, ForHome.class);
        startActivity(intent);
    }

    /**
     * Books botton.
     *
     * @param view the view
     */
    public void booksBotton(View view) {
        Intent intent = new Intent(this, Books.class);
        startActivity(intent);
    }

    /**
     * Toys botton.
     *
     * @param view the view
     */
    public void toysBotton(View view) {
        Intent intent = new Intent(this, Toys.class);
        startActivity(intent);
    }

    /**
     * Bikes botton.
     *
     * @param view the view
     */
    public void bikesBotton(View view) {
        Intent intent = new Intent(this, Bikes.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
    }

}