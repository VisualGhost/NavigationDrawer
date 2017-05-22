package com.mynavigationdrawer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.mynavigationdrawer.fragments.DetailsFragment;
import com.mynavigationdrawer.fragments.ItemContract;
import com.mynavigationdrawer.fragments.ItemFragment;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private DrawerContent mDrawerContent;
    private DrawerItemsFactory mDrawerItemsFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment fragment = getFragmentManager().findFragmentByTag("ItemFrag");
        Log.e("Test", "Fragment: " + fragment);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.drawer);
        mDrawerContent = new DrawerContent(this, recyclerView, new DrawerContent.OnNavigationDrawerClickListener() {
            @Override
            public void onClick(int id) {
                mDrawerLayout.closeDrawers();
                addFragment(getString(id));
            }
        });
        mDrawerItemsFactory = new DrawerItemsFactory("Jonathan Lee", "heyfromjonathan@gmail.com", this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.app_name));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                toolbar,
                R.string.drawer_open,
                R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.addDrawerListener(mToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mToggle.syncState();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDrawerContent.setItems(mDrawerItemsFactory.getItems());
        Log.d("Test", "> " + mDrawerContent.getActivatedPosition());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDrawerContent != null) {
            mDrawerContent.onSaveInstantState(outState);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (mDrawerContent != null) {
            mDrawerContent.onRestoreInstantState(savedInstanceState);
        }
    }

    private void addFragment(String s) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ItemFragment itemFragment = new ItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemContract.TEXT_ITEM, s);
        itemFragment.setArguments(bundle);
        transaction.replace(R.id.item, itemFragment, "ItemFrag");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showDetails(String s) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ItemContract.DETAILS_ITEM, "Details: " + s);
        detailsFragment.setArguments(bundle);
        transaction.replace(R.id.details, detailsFragment, "DetailsFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
