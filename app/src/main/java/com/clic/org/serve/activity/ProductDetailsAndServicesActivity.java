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
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.Utils.JsonUtils;
import com.clic.org.serve.adapters.ItemGridAdapter;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.ServiceRequestAsRespose;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.fragments.AddInvoiceFragment;
import com.clic.org.serve.fragments.MyListFragment;
import com.clic.org.serve.fragments.ProductDetailsFragment;
import com.clic.org.serve.listener.ServiceListener;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProductDetailsAndServicesActivity extends BaseActiivty
        implements
        MyListFragment.AddClicProductListener,AddInvoiceFragment.InVoicePathListener,
        FloatingActionsMenu.OnFloatingActionsMenuUpdateListener{

    final int LOCATION_PERMISSION = 0;
    UserItemsResponse mUserItemsResponse;
    ImageView productImage;
    FloatingActionsMenu floatingMenu;

    String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_service_details_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUserItemsResponse = getIntent().getExtras().getParcelable(getString(R.string.user_item));
        type = getIntent().getExtras().getString(getString(R.string.activity_type));

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
           bundle.putString(getString(R.string.activity_type),type);
        }
        productDetailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, productDetailsFragment).addToBackStack(null).commit();

    }

    public void onProductClic(View view)
    {


    }




    ServiceListener mServiceListener =new ServiceListener() {
        @Override
        public void onServiceResponse(String response) {



        }

        @Override
        public void onServiceError(String response) {

            ClicUtils.displayToast(ProductDetailsAndServicesActivity.this,"Connection Error....!");
        }
    };

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
