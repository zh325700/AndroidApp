package be.kuleuven.softdev.hehuang.pokepelican.Profile;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.kuleuven.softdev.hehuang.pokepelican.R;

/**
 * The type Setting fragment.
 */
public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View settingLayout = inflater.inflate(R.layout.setting_layout,
                container, false);
        return settingLayout;
    }

}
