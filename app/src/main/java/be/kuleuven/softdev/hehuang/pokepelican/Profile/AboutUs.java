package be.kuleuven.softdev.hehuang.pokepelican.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

import be.kuleuven.softdev.hehuang.pokepelican.R;

/**
 * shows information about the developers
 * The type About us.
 */
public class AboutUs extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        Intent intent = getIntent();
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_about_us);


    }


}
