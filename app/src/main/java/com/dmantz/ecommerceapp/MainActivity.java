package com.dmantz.ecommerceapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dmantz.ecommerceapp.Adapters.RecyclerViewAdapter;
import com.dmantz.ecommerceapp.Fragments.OneFragment;
import com.dmantz.ecommerceapp.model.Product;
import com.dmantz.ecommerceapp.model.ProductList;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    public static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private RecyclerViewAdapter adapter;
    private ArrayList<ProductList> arrayList;
    private ProductList mProductList;
    private ProductList mFilteredProductList;
    SearchView mSearchView;
    Context mContext;
    Button btnCategories;


    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.appbar);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.navigation_icon);
        toolbar.setNavigationIcon(drawable);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();

        btnCategories = findViewById(R.id.catergories_button);
        btnCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent categoriesList = new Intent(MainActivity.this, CategoriesListActivity.class);
                startActivity(categoriesList);

            }
        });


        //      viewPager = findViewById(R.id.viewpager);
        //      setupViewPager(viewPager);

        //     tabLayout = findViewById(R.id.tabs);
        //     tabLayout.setupWithViewPager(viewPager);

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.navigation_icon);
        mDrawerLayout = findViewById(R.id.navigation_drawer);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if(itemId == R.id.mens_cloth){
                   // Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this,ItemClient.class);
                    startActivity(intent);

                }

                return false;
            }
        });








        mRecyclerView = findViewById(R.id.recyclerviewone);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        List<String> mDataset = new ArrayList<>();
        // specify an adapter (see also next example)


        //   adapter = new RecyclerViewAdapter(MainActivity.this, mProductList);
        //   mRecyclerView.setAdapter(adapter);


        ECApplication lapp = (ECApplication) getApplication();

        try {
            mProductList = lapp.catalogClient.productDisplayList();

            mAdapter = new RecyclerViewAdapter(mProductList, getApplicationContext());
            //ECApplication)getApplication());
            mRecyclerView.setAdapter(mAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: sucesfully entered into method");
        getMenuInflater().inflate(R.menu.app_bar_menu, menu);
        // MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.carticon:
                cartonclick();
                return true;


        }

        return super.onOptionsItemSelected(item);


    }


    public void cartonclick() {
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);


    }

    @Override
    public void onBackPressed() {

        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "ONE");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        Log.d(TAG, "onQueryTextChange: ");
        newText = newText.toLowerCase();
        ArrayList<Product> newList = new ArrayList<>();

        for (Product product : mProductList.getProductList()) {
            String itemName = product.getItemName().toLowerCase();
            if (itemName.contains(newText))
                newList.add(product);
        }

        mFilteredProductList = new ProductList();
        mFilteredProductList.setProductList(newList);

        adapter = new RecyclerViewAdapter(mFilteredProductList, getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        // mRecyclerView.updateList(newList);
        return true;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}




