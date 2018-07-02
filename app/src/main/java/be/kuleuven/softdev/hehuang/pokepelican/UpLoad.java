package be.kuleuven.softdev.hehuang.pokepelican;
/**
 * Created by shuaigehan on 12/27/2016.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.facebook.Profile;
import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import be.kuleuven.softdev.hehuang.pokepelican.mCloud.MyConfiguration;

/**
 * The type Upload.
 */
public class UpLoad extends SwipeBackActivity implements View.OnClickListener {

    /**
     * The Spinnercata.
     */
    Spinner spinnercata, /**
     * The Spinnerlocation array;
     */
    spinnerlocation;
    /**
     * The Spadaper.
     */
    ArrayAdapter<CharSequence> spadaper, /**
     * The Spadpterlocation.
     */
    spadpterlocation;  // for spinner
    /**
     * The Image to uplaod.
     */
    ImageView imageToUplaod;
    /**
     * The B upload image.
     */
    Button bUploadImage, /**
     * The B select image.
     */
    bSelectImage;
    /**
     * The Upload image name.
     */
    EditText uploadImageName, /**
     * The Item price.
     */
    itemPrice, /**
     * The Item discribtion.
     */
    itemDiscribtion;
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final String TAG = "UpLoad";

    /**
     * The Queue.
     */
    RequestQueue queue;
    /**
     * The Selecturl.
     */
    String selecturl;
    /**
     * The Itname.
     */
    String itname;
    /**
     * The Itdiscribtion.
     */
    String itdiscribtion;
    /**
     * The Itlocation.
     */
    String itlocation;
    /**
     * The Itcatagory.
     */
    String itcatagory;
    /**
     * The Itprice.
     */
    String itprice;
    /**
     * The Itemuserid.
     */
    String itemuserid;
    /**
     * The Username.
     */
    String username;
    /**
     * The Json url.
     */
    String json_url;
    /**
     * The Json string.
     */
    String json_string;
    /**
     * The Jason string.
     */
    String Jason_string;
    /**
     * The Itemid.
     */
    int itemid;

    /**
     * read all te information user insert and upload the information into database.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_load);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        spinnercata = (Spinner) findViewById(R.id.spItemCatagory);
        spinnerlocation = (Spinner) findViewById(R.id.spItemLocation);
        spadaper = ArrayAdapter.createFromResource(this, R.array.Catagories, android.R.layout.simple_spinner_item);
        spadpterlocation = ArrayAdapter.createFromResource(this, R.array.Locations, android.R.layout.simple_spinner_item);
        spadaper.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spadpterlocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercata.setAdapter(spadaper);
        spinnerlocation.setAdapter(spadpterlocation);
        spinnercata.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itcatagory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerlocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                itlocation = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_up_load);

        imageToUplaod = (ImageView) findViewById(R.id.imageToUplaod);

        bSelectImage = (Button) findViewById(R.id.selectImageToUpload);
        bUploadImage = (Button) findViewById(R.id.bUploadImage);

        uploadImageName = (EditText) findViewById(R.id.etUplaodNames);
        itemPrice = (EditText) findViewById(R.id.etItemPrice);
        itemDiscribtion = (EditText) findViewById(R.id.etItemDiscribtion);

        bSelectImage.setOnClickListener(this);
        bUploadImage.setOnClickListener(this);
        selecturl = "http://api.a16_sd306.studev.groept.be/SelectNew";
        queue = NetworkController.getInstance(this).getRequestQueue();
        //Volley's inbuilt class to make Json array request
        JsonArrayRequest newsReq = new JsonArrayRequest(selecturl, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        itemid = obj.getInt("ItemID");
                        itemid = itemid +1;
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    } finally {
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

    /**
     * create the action when you click the botton.
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.selectImageToUpload:
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                break;
            case R.id.bUploadImage:

                Profile profile = Profile.getCurrentProfile();
                if (profile != null) {
                    itemuserid = profile.getId();
                    username = profile.getName();
                } else {
                    Toast.makeText(getApplicationContext(), "Please log in", Toast.LENGTH_LONG).show();
                }
                itname = uploadImageName.getText().toString();
                itprice = itemPrice.getText().toString();
                itdiscribtion = itemDiscribtion.getText().toString();
                if (imageToUplaod.getDrawable() == null) {
                    Toast.makeText(getApplicationContext(), "Please upload pictures of products", Toast.LENGTH_LONG).show();
                    break;
                } else if (itname.isEmpty() || itprice.isEmpty() || itdiscribtion.isEmpty() || itlocation.isEmpty() || itcatagory.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the details about the item", Toast.LENGTH_LONG).show();
                    break;
                } else if (itemuserid == null) {
                    Toast.makeText(getApplicationContext(), "Please log in", Toast.LENGTH_LONG).show();
                    break;
                }
                Bitmap image = ((BitmapDrawable) imageToUplaod.getDrawable()).getBitmap();

                json_url = "http://api.a16_sd306.studev.groept.be/";
                StringBuilder sb = new StringBuilder();
                sb.append(json_url + "AddItem/");
                sb.append(itname + "/");
                sb.append(itprice + "/");
                sb.append(itcatagory + "/");
                sb.append(itdiscribtion + "/");
                sb.append(itlocation + "/");
                sb.append(itemuserid + "/");
                sb.append(username);
                json_url = sb.toString();

                String oldurl = json_url;
                String newurl = oldurl.replaceAll(" ", "%20");
                newurl = newurl.replaceAll("\n", "%20");

                new UploadImage(image, itname).execute(newurl);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

        }
    }

    /**
     * Used to set the imageview with image chosen from gallery.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            imageToUplaod.setImageURI(null);
            imageToUplaod.setImageURI(selectedImage);
            bSelectImage.setText("Change Image");
        }
    }

    /**
     * AsyncTask to upload picture to cloudinary and data of item into database.
     */
    private class UploadImage extends AsyncTask<String, Void, String> {
        /**
         * The Name.
         */
        String name;
        /**
         * The Image.
         */
        Bitmap image;

        private UploadImage(Bitmap image, String name) {
            this.name = name;
            this.image = image;
            Toast.makeText(getApplicationContext(), "Item is uploading, please wait", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... urls) {
            Cloudinary cloudinary = new Cloudinary(MyConfiguration.getMyConfige());
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 30, byteArrayOutputStream);
            byte[] imageInByte = byteArrayOutputStream.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                while ((Jason_string = bufferedReader.readLine()) != null) {
                    stringBuilder.append(Jason_string);
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                cloudinary.uploader().upload(bis,
                        ObjectUtils.asMap("folder", "itempic/", "public_id", Integer.toString(itemid)));

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
            json_string = result;
            Jason_string = null;
            Toast.makeText(getApplicationContext(), "Item is uploaded", Toast.LENGTH_LONG).show();
        }


    }
}



