package be.kuleuven.softdev.hehuang.pokepelican.Profile;
/**
 * Created by shuaigehan on 12/27/2016.
 */

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.softdev.hehuang.pokepelican.HttpDataHandler;
import be.kuleuven.softdev.hehuang.pokepelican.Location.Antwerpen;
import be.kuleuven.softdev.hehuang.pokepelican.Location.Brussel;
import be.kuleuven.softdev.hehuang.pokepelican.Location.Leuven;
import be.kuleuven.softdev.hehuang.pokepelican.R;

/**
 * The type Detect location.
 */
public class DetectLocation extends SwipeBackActivity implements LocationListener {
    /**
     * The Botton show.
     */
    Button btnShow;
    /**
     * The Text view.
     */
    TextView textView;

    /**
     * The Detected adress.
     */
    String detectedAdress;
    /**
     * The Location manager.
     */
    LocationManager locationManager;
    /**
     * The Provider.
     */
    String provider;
    /**
     * The My permission request code.
     */
    final int MY_PERMISSION_REQUEST_CODE = 2345;
    /**
     * The Lat.
     */
    double lat, /**
     * The Lng.
     */
    lng;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    getLocation();
                break;

        }
    }

    /**
     * detect location and jump to the corresponding location and show the items there
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_location);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        Intent intent = getIntent();
        btnShow = (Button) findViewById(R.id.btnGetAddress);
        textView = (TextView) findViewById(R.id.txtAddress);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, MY_PERMISSION_REQUEST_CODE);

        } else {
            getLocation();
        }

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                lat = myLocation.getLatitude();
                lng = myLocation.getLongitude();

                new GetAddress().execute(String.format("%.4f,%.4f", lat, lng));
            }
        });
    }

    /**
     * detect the location and junmp to certain zone
     */
    protected void change() {
        if (detectedAdress.contains("Leuven")) {
            Intent intent = new Intent(this, Leuven.class);
            startActivity(intent);
        } else if (detectedAdress.contains("Brussels")) {
            Intent intent = new Intent(this, Brussel.class);
            startActivity(intent);
        } else if (detectedAdress.contains("Antwerp")) {
            Intent intent = new Intent(this, Antwerpen.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Service in this location is upcoming", Toast.LENGTH_LONG).show();
        }
    }


    private void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        final Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        if (location == null)
            Log.e("ERROR", "Location is null");
    }

    /**
     * get Latitude and Longitude
     * @param location the ocation
     */
    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lng = location.getLongitude();

        new GetAddress().execute(String.format("%.4f,%.4f", lat, lng));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class GetAddress extends AsyncTask<String, Void, String> {

        /**
         * The Dialog.
         */
        ProgressDialog dialog = new ProgressDialog(DetectLocation.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait...");
            dialog.setCanceledOnTouchOutside(false);
            if (!isFinishing()) {
                dialog.show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                double lat = Double.parseDouble(strings[0].split(",")[0]);
                double lng = Double.parseDouble(strings[0].split(",")[1]);
                String response;
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%.4f,%.4f&sensor=false", lat, lng);
                response = http.GetHTTPData(url);
                return response;
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);

                String address = ((JSONArray) jsonObject.get("results")).getJSONObject(0).get("formatted_address").toString();
                detectedAdress = address;
                textView.setText(address);
                change();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }


    /**
     * jump to leuven zone.
     *
     * @param view the view
     */
    public void leuven(View view) {
        Intent intent = new Intent(DetectLocation.this, Leuven.class);
        startActivity(intent);
    }

    /**
     * jump to Brussels zone.
     *
     * @param view the view
     */
    public void brussels(View view) {
        Intent intent = new Intent(this, Brussel.class);
        startActivity(intent);
    }

    /**
     * jump to Antwerp zone.
     *
     * @param view the view
     */
    public void antwerp(View view) {
        Intent intent = new Intent(this, Antwerpen.class);
        startActivity(intent);
    }
}