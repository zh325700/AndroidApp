package be.kuleuven.softdev.hehuang.pokepelican;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.facebook.Profile;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import be.kuleuven.softdev.hehuang.pokepelican.mCloud.MyConfiguration;
import be.kuleuven.softdev.hehuang.pokepelican.objects.NewsFeeds;

/**
 * The type Detail.
 */
public class Detail extends SwipeBackActivity {

    /**
     * The Price.
     */
    String price;
    /**
     * The Location.
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
     * The Url to reach the user_like table in databse.
     */
    String url_like_final;
    /**
     * The url after change all space to %20.
     */
    String newurl;
    /**
     * The Itemid.
     */
    int itemid;
    /**
     * show the item detail you clicked.
     * @param savedInstanceState the savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        Intent intent = getIntent();
        final NewsFeeds item = (NewsFeeds) intent.getSerializableExtra("theDetail");
        price = item.getPrice();
        location = item.getLocation();
        itemid = item.getItemid();
        name = item.getFeedName();
        discribtion = item.getContent();
        ownerId = item.getUserid();    // this userid is the id of the ower of the item
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            buyerid = profile.getId();
        } else {
            Toast.makeText(getApplicationContext(), "Please sign in first", Toast.LENGTH_LONG).show();
        }


        TextView price_view = (TextView) findViewById(R.id.price_detail);
        TextView location_view = (TextView) findViewById(R.id.location_detail);
        TextView name_view = (TextView) findViewById(R.id.name_detail);
        TextView discribtion_view = (TextView) findViewById(R.id.discribtion_detail);
        ImageView img = (ImageView) findViewById(R.id.pic_detail);
        Cloudinary cloud = new Cloudinary(MyConfiguration.getMyConfige());
        String url = cloud.url().generate("itempic/" + itemid + ".jpg");

        Button bt_addToCart = (Button) findViewById(R.id.bt_Cart);
        bt_addToCart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (buyerid.equals(ownerId) == false) {
                    String url_like = "http://studev.groept.be/api/a16_sd306/";
                    StringBuilder sb = new StringBuilder();
                    sb.append(url_like + "AddLike/");
                    sb.append(buyerid + "/");
                    sb.append(name + "/");
                    sb.append(itemid);
                    url_like_final = sb.toString();
                    String oldurl = url_like_final;
                    newurl = oldurl.replaceAll(" ", "%20");
                    newurl = newurl.replaceAll("\n", "%20");
                    new AlertDialog.Builder(Detail.this)
                            .setTitle("Add to cart")
                            .setMessage("Are you sure you want to add this to cart")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    new AddToCart().execute(newurl);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(R.drawable.pelican)
                            .show();

                } else {
                    Toast.makeText(getApplicationContext(), "This is your own item", Toast.LENGTH_LONG).show();
                }
            }
        });

        new DownloadImageTask(img)
                .execute(url);
        price_view.setText(price);
        location_view.setText(location);
        discribtion_view.setText(discribtion);
        name_view.setText(name);

    }

    /**
     * download image by url and set it in the immageview.
     */
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

    /**
     * Add item, buyer and owner information to database.
     */
    private class AddToCart extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String Jason_string;
                while ((Jason_string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Jason_string);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "Item is added to cart", Toast.LENGTH_LONG).show();
        }


    }
}
