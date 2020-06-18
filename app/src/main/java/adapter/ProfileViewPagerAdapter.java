package adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import fragment.ProfileFragment;

public class ProfileViewPagerAdapter extends FragmentPagerAdapter {
    int size = 0;

    public ProfileViewPagerAdapter(FragmentManager fm, int size) {
        super(fm);
        this.size = size;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return size;
    }

    public CharSequence getPageTittle(int position){
        switch (position){
            case 0:
                return "Posts";
            default:
                return null;
        }
    }
}