package com.rolvatech.cgc.adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.rolvatech.cgc.dataobjects.Child;
import com.rolvatech.cgc.dataobjects.UserDTO;
import com.rolvatech.cgc.fragments.ChildTab1;
import com.rolvatech.cgc.fragments.ChildTab2;
import com.rolvatech.cgc.fragments.ChildTab3;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;
    UserDTO child;
    private String[] tabTitles = new String[]{"About Child", "Task List", "Reports"};

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount = tabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        Bundle bundle= new Bundle();
        switch (position) {
            case 0:
                Fragment tab1= new ChildTab1();
                return tab1;
            case 1:
                Fragment tab2= new ChildTab2();
                return tab2;
            case 2:
                Fragment tab3= new ChildTab3();
                return tab3;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabTitles.length;
    }

    public UserDTO getChild() {
        return child;
    }

    public void setChild(UserDTO child) {
        this.child = child;
    }
}
