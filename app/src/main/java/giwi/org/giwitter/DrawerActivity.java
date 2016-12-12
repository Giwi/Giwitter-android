package giwi.org.giwitter;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import giwi.org.giwitter.fragment.MessagesFragment;
import giwi.org.giwitter.fragment.MessagesFragment_;
import giwi.org.giwitter.fragment.UsersFragment;
import giwi.org.giwitter.fragment.WriteMsgDialog;
import giwi.org.giwitter.helpers.Session;

/**
 * The type Drawer activity.
 */
@EActivity(R.layout.activity_drawer)
public class DrawerActivity extends AppCompatActivity {

    @ViewById
    DrawerLayout drawer_layout;
    @ViewById
    Toolbar toolbar;
    @ViewById
    ViewPager viewpager;
    @ViewById
    TabLayout tabs;
    @ViewById
    NavigationView nav_view;
    @ViewById
    FloatingActionButton fab;

    @Click
    void fab() {
        WriteMsgDialog.getInstance(Session.token, Session.userId).show(DrawerActivity.this.getFragmentManager(), "write");
    }

    @AfterViews
    void init() {
        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.mipmap.ic_launcher);
            ab.setDisplayHomeAsUpEnabled(true);
        }
        setSupportActionBar(toolbar);
        if (viewpager != null) {
            Adapter adapter = new Adapter(getSupportFragmentManager());
            adapter.addFragment(MessagesFragment_.builder().build(), "Messages");
            adapter.addFragment(new UsersFragment(), "Users");
            viewpager.setAdapter(adapter);
        }
        tabs.setupWithViewPager(viewpager);
        if (nav_view != null) {
            setupNavigationView(nav_view);
        }
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer_layout.setDrawerListener(toggle);
        toggle.syncState();
    }


    private void setupNavigationView(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.tchat_disconnect) {
                            Session.token = null;
                            DrawerActivity.this.finish();
                        } else {
                            menuItem.setChecked(true);
                            drawer_layout.closeDrawers();
                        }
                        return true;
                    }
                });
    }


    /**
     * The type Adapter.
     */
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        /**
         * Instantiates a new Adapter.
         *
         * @param fm the fm
         */
        Adapter(FragmentManager fm) {
            super(fm);
        }

        /**
         * Add fragment.
         *
         * @param fragment the fragment
         * @param title    the title
         */
        void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        /**
         * Gets item.
         *
         * @param position the position
         * @return the item
         */
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        /**
         * Gets count.
         *
         * @return the count
         */
        @Override
        public int getCount() {
            return mFragments.size();
        }

        /**
         * Gets page title.
         *
         * @param position the position
         * @return the page title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
