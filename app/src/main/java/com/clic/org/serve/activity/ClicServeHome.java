package com.clic.org.serve.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.helpshift.support.Support;

import java.util.ArrayList;
import java.util.List;

public class ClicServeHome extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener{


    String type;
    ArrayList<UserItemsResponse> userItemsList = new ArrayList<>();
    ArrayList<ItemDocs> userItemDocsList = new ArrayList<>();
    ArrayList<ServiceRequestAsRespose> serviceReqList = new ArrayList<>();
    ItemGridAdapter myAdapter;
    RecyclerView myGrid;
    RecyclerView myListServiceRequests;
    MyRecyclerAdapter myRecyclerAdapter;
    FloatingActionsMenu floatingMenu;
    private String serviceType ="";
    RelativeLayout mCoordinatorLayout;
    View myRecyclerVIew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_home);

        type = getIntent().getExtras().getString(getString(R.string.activity_type));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Clic");

        myGrid = (RecyclerView)findViewById(R.id.gridView);
        mCoordinatorLayout = (RelativeLayout)findViewById(R.id.mainLayout);




        myListServiceRequests = (RecyclerView) findViewById(R.id.listSerivcesRequests);


        floatingMenu = (FloatingActionsMenu)findViewById(R.id.multiple_actions_menu);


        floatingMenu.setOnFloatingActionsMenuUpdateListener(this);

        mCoordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (floatingMenu.isExpanded()) {
                    floatingMenu.toggle();
                }
                return true;
            }
        });








      /*  myGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == userItemsList.size()-1)
                {
                    Intent raiseReq = new Intent(ClicServeHome.this, AddClicProductActivity.class);
                    raiseReq.putExtra(getString(R.string.activity_service_req), getString(R.string.activity_service_req_addProduct));
                    startActivity(raiseReq);
                }
                else if(type.equalsIgnoreCase(getString(R.string.activity_know_more))) {
                    Intent intent = new Intent(ClicServeHome.this,ProductDetailsAndServicesActivity.class);
                    intent.putExtra(getString(R.string.user_item), userItemsList.get(position));
                    startActivity(intent);
                }
                else if(type.equalsIgnoreCase(getString(R.string.activity_upload_docs)))
                {
                    Intent intent = new Intent(ClicServeHome.this,UploadDocumentsActivity.class);
                    intent.putExtra(getString(R.string.user_item), userItemsList.get(position));
                    startActivity(intent);
                }

            }
        });*/
    }



    @Override
    protected void onResume() {
        super.onResume();
        userItemsList.clear();
        serviceReqList.clear();
        ServiceUtils.makeJSONObjectReq(this, ServiceConstants.CUSTOMER_ITEMS_LIST + ClicUtils.readPreference(this, R.string.clic_ClientID), mServiceListener, null);
        if (floatingMenu.isExpanded())
        {
            myRecyclerVIew.setBackgroundColor(Color.TRANSPARENT);
        }
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

            if(serviceType.equalsIgnoreCase(ClicConstants.CALLBACK_SERVICE))
            {
                serviceType ="";
                ClicUtils.createSuccessDialog(ClicServeHome.this,R.layout.clic_status,ClicConstants.CALLBACK_SERVICE);

            }else {
                JsonObject lobj = JsonUtils.getJsonObject(response);
                JsonArray lArray = lobj.getAsJsonArray("itemslist");

                for (int i = 0; i < lArray.size(); i++) {
                    userItemsList.add(new Gson().fromJson(lArray.get(i).toString(), UserItemsResponse.class));
                }

                lArray = lobj.getAsJsonArray("servicerequestslist");
                for (int i = 0; i < lArray.size(); i++) {
                    serviceReqList.add(new Gson().fromJson(lArray.get(i).toString(), ServiceRequestAsRespose.class));
                }
                myAdapter = new ItemGridAdapter(ClicServeHome.this, userItemsList, userItemDocsList, ClicConstants.CLIC_PRODUCTS);
                myRecyclerAdapter = new MyRecyclerAdapter(serviceReqList,new ArrayList<UserItemsResponse>(),ClicConstants.LISTTYPE_SERVICEREQ_HOME);
                myListServiceRequests.setAdapter(myRecyclerAdapter);
                myRecyclerAdapter = new MyRecyclerAdapter(null,userItemsList,"");
                myGrid.setAdapter(myRecyclerAdapter);
            }

        }

        @Override
        public void onServiceError(String response) {

            ClicUtils.displayToast(ClicServeHome.this,"Connection Error....!");
        }
    };

    @Override
    public void onMenuExpanded() {

       // dimBackground();
        floatingMenu.toggle();
        startActivity(new Intent(ClicServeHome.this, BlurActivity.class));



    }

    @Override
    public void onMenuCollapsed() {

        //mPopupWindow.dismiss();

    }

    public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ItemsViewHolder>
    {

        List<ServiceRequestAsRespose> items;
        List<UserItemsResponse> muserList;
        String type;

        MyRecyclerAdapter(List<ServiceRequestAsRespose> items,List<UserItemsResponse> muserList,String type)

        {
            this.items = items;
            this.type = type;
            this.muserList = muserList;
            if(muserList.size() == 0)
            {
                muserList.add(new UserItemsResponse());
            }
            if(type.equalsIgnoreCase(ClicConstants.LISTTYPE_SERVICEREQ_HOME)) {
                myListServiceRequests.setLayoutManager(new LinearLayoutManager(ClicServeHome.this, LinearLayoutManager.HORIZONTAL, false));
            }else
            {
                myGrid.setLayoutManager(new LinearLayoutManager(ClicServeHome.this, LinearLayoutManager.VERTICAL, false));

            }

        }




        public  class ItemsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        {
            CardView cv;
            TextView name;
            TextView description;
            TextView price;
            ImageView itemPhoto;

            public ItemsViewHolder(View itemView) {
                super(itemView);
                cv = (CardView)itemView.findViewById(R.id.cv);
                ClicUtils.setItemHeightWidthFull(ClicServeHome.this, cv);
                myRecyclerVIew = cv;
                name = (TextView)itemView.findViewById(R.id.item_name);
                price = (TextView)itemView.findViewById(R.id.item_price);
                price.setVisibility(View.INVISIBLE);
                description = (TextView)itemView.findViewById(R.id.item_description);
                itemPhoto = (ImageView)itemView.findViewById(R.id.item_photo);
            }

            @Override
            public void onClick(View v) {

                Log.d("debug","position of view"+getLayoutPosition());
            }
        }
        @Override
        public ItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.clic_service_request_item, parent, false);
            ItemsViewHolder vh = new ItemsViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ItemsViewHolder holder, int position) {

            if(type.equalsIgnoreCase(ClicConstants.LISTTYPE_SERVICEREQ_HOME)) {
                holder.name.setText(items.get(position).getCustomerItemName());
                holder.description.setText(items.get(position).getStatus());
            }
            else
            {
                if(position == muserList.size()-1)
                {
                    holder.name.setText("Add Product");
                    holder.description.setText("");

                }else
                {
                    holder.name.setText(muserList.get(position).getCategoryName());
                    // holder.description.setText(items.get(position).getStatus());
                }
            }
            final int index = position;
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(type.equalsIgnoreCase(ClicConstants.LISTTYPE_SERVICEREQ_HOME))
                    {

                    }
                    else
                    {
                        if(index == userItemsList.size()-1)
                        {
                            Intent raiseReq = new Intent(ClicServeHome.this, AddClicProductActivity.class);
                            raiseReq.putExtra(getString(R.string.activity_service_req), getString(R.string.activity_service_req_addProduct));
                            startActivity(raiseReq);
                        }
                        else {
                            Intent intent = new Intent(ClicServeHome.this,ProductDetailsAndServicesActivity.class);
                            intent.putExtra(getString(R.string.user_item), userItemsList.get(index));
                            startActivity(intent);
                        }

                                           }
                }
            });


        }


        @Override
        public int getItemCount() {
            if(type.equalsIgnoreCase(ClicConstants.LISTTYPE_SERVICEREQ_HOME)) {
                return items.size();
            }else {
                return muserList.size();
            }
        }
    }


}
