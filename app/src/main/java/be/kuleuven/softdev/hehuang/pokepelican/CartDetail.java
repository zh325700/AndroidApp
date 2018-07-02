package be.kuleuven.softdev.hehuang.pokepelican;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.facebook.Profile;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import be.kuleuven.softdev.hehuang.pokepelican.mCloud.MyConfiguration;
import be.kuleuven.softdev.hehuang.pokepelican.objects.NewsFeeds;

/**
 * The type Cart detail.
 */
public class CartDetail extends SwipeBackActivity {
    private View mMessengerButton;
    /**
     * The Price of item you choose.
     */
    String price;
    /**
     * The Location where your item is in.
     */
    String location;
    /**
     * The Name of item.
     */
    String name;
    /**
     * The Discribtion.
     */
    String discribtion;
    /**
     * The Owner id.
     */
    String ownerId;
    /**
     * The Buyerid.
     */
    String buyerid;
    /**
     * The name of the owner.
     */
    String username;
    /**
     * The Itemid.
     */
    int itemid;

    /**
     * show the item you clicked in cart.
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_detail);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        Intent intent = getIntent();
        final NewsFeeds item = (NewsFeeds) intent.getSerializableExtra("theDetail");
        price = item.getPrice();
        location = item.getLocation();
        name = item.getFeedName();
        itemid = item.getItemid();
        discribtion = item.getContent();
        ownerId = item.getUserid();    // this userid is the id of the ower of the item
        username = item.getUserName();
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            buyerid = profile.getId();
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in first", Toast.LENGTH_LONG).show();
        }
        TextView contact_view = (TextView) findViewById(R.id.contact_detail);
        TextView price_view = (TextView) findViewById(R.id.price_detail);
        TextView location_view = (TextView) findViewById(R.id.location_detail);
        TextView name_view = (TextView) findViewById(R.id.name_detail);
        TextView discribtion_view = (TextView) findViewById(R.id.discribtion_detail);
        ImageView img = (ImageView) findViewById(R.id.pic_detail);
        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
        String url = cloud.url().generate("itempic/" + itemid + ".jpg");
        Button bt_addToCart = (Button) findViewById(R.id.bt_Cart);


        new DownloadImageTask(img)
                .execute(url);
        price_view.setText(price);
        location_view.setText(location);
        discribtion_view.setText(discribtion);
        name_view.setText(name);
        contact_view.setGravity(Gravity.CENTER);
        contact_view.setText("Contact        " + username);
        mMessengerButton = findViewById(R.id.messenger_send_button);
        mMessengerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMessengerButtonClicked();
            }
        });
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        /**
         * The Bm image.
         */
        ImageView bmImage;

        /**
         * Instantiates a new Download image task.
         *
         * @param bmImage the bm image
         */
        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {

                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void onMessengerButtonClicked() {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/"));
        startActivity(intent);
    }
}
