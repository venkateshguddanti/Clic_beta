package com.clic.org.serve.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.Utils.JsonUtils;
import com.clic.org.serve.adapters.ItemGridAdapter;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.ItemDocs;
import com.clic.org.serve.data.ServiceRequestAsRespose;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.listener.ServiceListener;
import com.clic.org.serve.services.ServiceConstants;
import com.clic.org.serve.services.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

public class ClicServeProducts extends AppCompatActivity {


    String type;
    ArrayList<UserItemsResponse> userItemsList = new ArrayList<>();
    ArrayList<ItemDocs> userItemDocsList = new ArrayList<>();
    ItemGridAdapter myAdapter;
    GridView myGrid;
    private String serviceType ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_menu);

        type = getIntent().getExtras().getString(getString(R.string.activity_type));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myGrid = (GridView)findViewById(R.id.gridView);

        ServiceUtils.makeJSONObjectReq(this, ServiceConstants.CUSTOMER_ITEMS_LIST + ClicUtils.readPreference(this, R.string.clic_ClientID), mServiceListener, null);

        myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == userItemsList.size()-1)
                {
                    Intent raiseReq = new Intent(ClicServeProducts.this, AddClicProductActivity.class);
                    raiseReq.putExtra(getString(R.string.activity_service_req), getString(R.string.activity_service_req_addProduct));
                    startActivity(raiseReq);
                }
                else if(type.equalsIgnoreCase(getString(R.string.activity_service_req))) {
                    Intent intent = new Intent(ClicServeProducts.this,ProductDetailsAndServicesActivity.class);
                    intent.putExtra(getString(R.string.user_item), userItemsList.get(position));
                    startActivity(intent);
                    finish();
                }
                else if (type.equalsIgnoreCase(getString(R.string.activity_callback)))
                {
                    serviceType = ClicConstants.CALLBACK_SERVICE;
                    ServiceUtils.makeJSONObjectReq(ClicServeProducts.this,
                            ServiceConstants.CLIC_CALLBACK_REQUEST + ClicUtils.readPreference(ClicServeProducts.this, R.string.clic_ClientID)
                            , mServiceListener, null);

                }

            }
        });
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
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    ServiceListener mServiceListener =new ServiceListener() {
        @Override
        public void onServiceResponse(String response) {

            if (serviceType.equalsIgnoreCase(ClicConstants.CALLBACK_SERVICE))
            {
                serviceType ="";
                ClicUtils.createSuccessDialog(ClicServeProducts.this,R.layout.clic_status,ClicConstants.CALLBACK_SERVICE);

            }else {
                JsonObject lobj = JsonUtils.getJsonObject(response);
                JsonArray lArray = lobj.getAsJsonArray("itemslist");

                for (int i = 0; i < lArray.size(); i++) {
                    userItemsList.add(new Gson().fromJson(lArray.get(i).toString(), UserItemsResponse.class));
                }


                myAdapter = new ItemGridAdapter(ClicServeProducts.this, userItemsList, userItemDocsList, ClicConstants.CLIC_PRODUCTS);
                myGrid = (GridView) findViewById(R.id.gridView);
                myGrid.setAdapter(myAdapter);
            }
        }

        @Override
        public void onServiceError(String response) {

            ClicUtils.displayToast(ClicServeProducts.this,"Connection Error....!");
        }
    };
}
