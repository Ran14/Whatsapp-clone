package com.example.ranji.whatsapp;

//import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;




//import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class TabAccessAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    public TabAccessAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch(position)
        {

            case 0:
                ChatsFragment tab1 = new ChatsFragment();
                return tab1;
            case 1:
                ContactsFragment tab2 = new ContactsFragment();
                return  tab2;
            case 2:
                GroupsFragment tab3 = new GroupsFragment();
                return  tab3;
            case 3:
                RequestsFragment tab4=new RequestsFragment();
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}
