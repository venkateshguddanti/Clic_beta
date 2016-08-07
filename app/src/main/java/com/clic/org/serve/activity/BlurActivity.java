package com.clic.org.serve.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.helpshift.support.Support;

public class BlurActivity extends AppCompatActivity implements FloatingActionsMenu.OnFloatingActionsMenuUpdateListener{

    com.getbase.floatingactionbutton.FloatingActionButton addProdcuts,callback,chat,serviceRequest;
    FloatingActionsMenu floatingMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blur);
        floatingMenu = (FloatingActionsMenu)findViewById(R.id.multiple_actions_menu);
        addProdcuts = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.button_addproduct);
        callback = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.button_callback);
        chat = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.button_chat);
        serviceRequest = (com.getbase.floatingactionbutton.FloatingActionButton)findViewById(R.id.button_serviceRequest);

        floatingMenu.setOnFloatingActionsMenuUpdateListener(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                floatingMenu.toggle();

            }
        },300);


        floatingMenu.setBackgroundColor(Color.TRANSPARENT);

        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_blur,null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });


        addProdcuts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                floatingMenu.toggle();
                Intent raiseReq = new Intent(BlurActivity.this, AddClicProductActivity.class);
                raiseReq.putExtra(getString(R.string.activity_service_req), getString(R.string.activity_service_req_addProduct));
                startActivity(raiseReq);

            }
        });
        callback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                floatingMenu.toggle();
               /* floatingMenu.toggle();
                Intent intent = new Intent(ClicServeHome.this,ClicSettingsActivity.class);
                intent.putExtra(getString(R.string.user_item), userItemsList.get(position));
                startActivity(intent);*/
               /* serviceType = ClicConstants.CALLBACK_SERVICE;
                ServiceUtils.makeJSONObjectReq(ClicServeHome.this,
                        ServiceConstants.CLIC_CALLBACK_REQUEST + ClicUtils.readPreference(ClicServeHome.this, R.string.clic_ClientID)
                        , mServiceListener, null);*/

                if (ClicUtils.readPreference(getApplicationContext(), R.string.clic_ClientID) == null) {
                    Intent raiseReq = new Intent(BlurActivity.this, AddClicProductActivity.class);
                    raiseReq.putExtra(getString(R.string.activity_service_req), getString(R.string.activity_service_req_addProduct));
                    startActivity(raiseReq);
                } else {
                    Intent knowMore = new Intent(BlurActivity.this, ClicServeProducts.class);
                    knowMore.putExtra(getString(R.string.activity_type), getString(R.string.activity_callback));
                    startActivity(knowMore);
                }
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingMenu.toggle();
                finish();
               // Support.showConversation(BlurActivity.this);


            }
        });


        serviceRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                floatingMenu.toggle();
                if (ClicUtils.readPreference(getApplicationContext(), R.string.clic_ClientID) == null) {
                    Intent raiseReq = new Intent(BlurActivity.this, AddClicProductActivity.class);
                    raiseReq.putExtra(getString(R.string.activity_service_req), getString(R.string.activity_service_req_addProduct));
                    startActivity(raiseReq);
                } else {
                    Intent knowMore = new Intent(BlurActivity.this, ClicServeProducts.class);
                    knowMore.putExtra(getString(R.string.activity_type), getString(R.string.activity_service_req));
                    startActivity(knowMore);
                }

            }
        });
    }

    @Override
    public void onMenuExpanded() {

    }

    @Override
    public void onMenuCollapsed() {

        finish();
    }
}
