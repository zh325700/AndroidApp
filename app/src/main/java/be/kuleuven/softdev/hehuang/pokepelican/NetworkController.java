package be.kuleuven.softdev.hehuang.pokepelican;

/**
 * Created by shuaigehan on 12/27/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * The type Network controller.
 */
public class NetworkController {


    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;
    private static NetworkController mInstance;

    private NetworkController(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        // This will Load Images from Network in Separate Thread
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    //Create ImageCache of max size 10MB
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(10);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }


    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static synchronized NetworkController getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NetworkController(context);
        }
        return mInstance;
    }

    /**
     * Gets request queue.
     *
     * @return the request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }


    /**
     * Add to request queue.
     *
     * @param <T> the type parameter
     * @param req the req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    /**
     * Gets image loader.
     *
     * @return the image loader
     */
    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}