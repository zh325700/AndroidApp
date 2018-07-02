package be.kuleuven.softdev.hehuang.pokepelican.Category;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.kuleuven.softdev.hehuang.pokepelican.R;


/**
 * The type Category fragment.
 */
public class CategoryFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View categoryLayout = inflater.inflate(R.layout.category_layout,
                container, false);

        return categoryLayout;
    }

}
