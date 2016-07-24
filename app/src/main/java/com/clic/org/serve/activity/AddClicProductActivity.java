package com.clic.org.serve.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.data.ItemDocs;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.fragments.AddInvoiceFragment;
import com.clic.org.serve.fragments.MyListFragment;
import com.clic.org.serve.listener.MyEventListener;
import com.clic.org.serve.listener.ServiceListener;

public class AddClicProductActivity extends AppCompatActivity implements MyListFragment.AddClicProductListener,
        AddInvoiceFragment.InVoicePathListener{

    String inVoice;
    ProgressBar mProgress;
    UserItemsResponse mUserItemsResponse = new UserItemsResponse();
    ItemDocs mItemDocs = new ItemDocs();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clic_product);
        mProgress = (ProgressBar)findViewById(R.id.progress);
        mProgress.setProgress(0);
        mProgress.setMax(100);
        mProgress.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Add Product");


        MyListFragment fragment = new MyListFragment();
        Bundle b = new Bundle();
        b.putString(getString(R.string.list_type),getString(R.string.list_Brand));
        mUserItemsResponse.setCustomerID(ClicUtils.readPreference(this,R.string.clic_ClientID));
        b.putParcelable(getString(R.string.user_item), mUserItemsResponse);

        fragment.setArguments(b);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onEvent(MyEventListener myEventListener)
    {
        mItemDocs = myEventListener.getItemDocs();
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
        if(id == android.R.id.home)
        {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();

            } else
            {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void getInvoicePath(String value) {

        inVoice = value;

    }

    ServiceListener mServiceListener = new ServiceListener() {
        @Override
        public void onServiceResponse(String response) {

        }

        @Override
        public void onServiceError(String response) {

        }
    };

    @Override
    public void onArticalSelectedProgress(int value) {

        mProgress.setVisibility(View.VISIBLE);
        mProgress.setProgress(value);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
