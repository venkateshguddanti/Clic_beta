package com.clic.org.serve.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.clic.org.R;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.fragments.AddInvoiceFragment;
import com.clic.org.serve.fragments.MyListFragment;
import com.clic.org.serve.fragments.ProductDetailsFragment;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDetailsAndServicesActivity extends AppCompatActivity
        implements
        MyListFragment.AddClicProductListener,AddInvoiceFragment.InVoicePathListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener{

    final int LOCATION_PERMISSION = 0;
    UserItemsResponse mUserItemsResponse;
    ImageView productImage;
    FloatingActionsMenu floatingMenu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_service_details_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserItemsResponse = getIntent().getExtras().getParcelable(getString(R.string.user_item));

        productImage = (ImageView) findViewById(R.id.backdrop);
        productImage.setBackgroundResource(R.drawable.img__tv);

        floatingMenu = (FloatingActionsMenu)findViewById(R.id.multiple_actions_menu);

        floatingMenu.setOnFloatingActionsMenuUpdateListener(this);




        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.BLUETOOTH,
                    Manifest.permission.BLUETOOTH_ADMIN,
                    Manifest.permission.INTERNET,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.ACCESS_NETWORK_STATE,
            }, LOCATION_PERMISSION);
        }

        Bundle bundle = new Bundle();
        ProductDetailsFragment productDetailsFragment = new ProductDetailsFragment();
        bundle.putParcelable(getString(R.string.user_item), mUserItemsResponse);
        if(getIntent().getExtras().getString(getString(R.string.activity_type))!= null)
        {
           bundle.putString(getString(R.string.activity_type),getIntent().getExtras().getString(getString(R.string.activity_type)));
        }
        productDetailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, productDetailsFragment).addToBackStack(null).commit();

    }

    public void onProductClic(View view)
    {


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_details_and_services, menu);


        /*searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings)
        {
            return true;
        }
        else if(id == android.R.id.home)
        {
            Log.d("debug", "count" + getSupportFragmentManager().getBackStackEntryCount());

            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();

            } else
            {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
           finish();

        } else
        {
            super.onBackPressed();
        }

    }

    @Override
    public void getInvoicePath(String value) {

    }

    @Override
    public void onArticalSelectedProgress(int value) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case LOCATION_PERMISSION:




                break;
        }
    }

    @Override
    public void onMenuExpanded() {
        floatingMenu.toggle();
        startActivity(new Intent(ProductDetailsAndServicesActivity.this, BlurActivity.class));

    }

    @Override
    public void onMenuCollapsed() {

    }
}
