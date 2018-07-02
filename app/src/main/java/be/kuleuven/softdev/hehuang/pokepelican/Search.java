package be.kuleuven.softdev.hehuang.pokepelican;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.liuguangqiang.swipeback.SwipeBackActivity;
import com.liuguangqiang.swipeback.SwipeBackLayout;

/**
 * The type Search.
 */
public class Search extends SwipeBackActivity {
    public final static String EXTRA_MESSAGE = "be.kuleuven.softdev.hehuang.pokepelican.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setDragEdge(SwipeBackLayout.DragEdge.LEFT);
        Intent intent = getIntent();
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_search);
    }

    /**
     * Search by item name.
     *
     * @param view the view
     */
    public void Search(View view) {
        Intent intent = new Intent(this, SearchPage.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}