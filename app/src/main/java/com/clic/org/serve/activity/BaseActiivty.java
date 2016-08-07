package com.clic.org.serve.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.Utils.JsonUtils;
import com.clic.org.serve.adapters.ItemGridAdapter;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.Profile;
import com.clic.org.serve.data.ServiceRequestAsRespose;
import com.clic.org.serve.data.SharedProfiles;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.listener.ServiceListener;
import com.clic.org.serve.services.ServiceConstants;
import com.clic.org.serve.services.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Venkatesh on 06-08-2016.
 */


public class BaseActiivty extends AppCompatActivity{

    private String SERVICE_TYPE;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_ID_MULTIPLE_PERMISSIONS);
        }

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    // permissions granted.
                } else {

                }
                break;
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
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                getSupportFragmentManager().popBackStack();

            } else
            {
                finish();
            }
        }
        else if(id  == R.id.action_home)
        {
            finish();
            startActivity(new Intent(this,ClicServeHome.class));
        }
        else if(id == R.id.action_clicshare)
        {
            SERVICE_TYPE = ClicConstants.CLIC_PROFILE_SHARE;
            ClicUtils.createOTPValidationDialog(this,
                    R.layout.otp_validation,null,
                    mServiceListener,
                    ClicConstants.DIALOG_TYPE_SHARE);

        }
        else if(id == R.id.action_clicRecommendClic)
        {
            ClicUtils.shareClic(this);

        }
        else if (id == R.id.action_clicSharedProfile)
        {
            if(ClicUtils.readPreference(BaseActiivty.this,R.string.clic_SharedClientID)!= null)
            {
                SERVICE_TYPE = ClicConstants.CLIC_PROFILES_SERVICE;
                ServiceUtils.makeJSONObjectReq(this,
                        ServiceConstants.CLIC_PROFILES_REQUEST + ClicUtils.readPreference(this, R.string.clic_SharedClientID),
                        mServiceListener,
                        null);
            }
            else
            {
                SERVICE_TYPE = ClicConstants.CLIC_PROFILES_SERVICE;
                ServiceUtils.makeJSONObjectReq(this,
                        ServiceConstants.CLIC_PROFILES_REQUEST + ClicUtils.readPreference(this, R.string.clic_ClientID),
                        mServiceListener,
                        null);
            }

        }
        else if(id == R.id.action_settings)
        {
            startActivity(new Intent(this,ClicSettingsActivity.class));
        }
        else if(id == R.id.action_clicLogout)
        {
            ClicUtils.clearAppPreferences(this);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void createListTypeDialog(final SharedProfiles sharedProfiles)
    {
        final ArrayList<Profile> profiles = sharedProfiles.getSahredProfilesList();
        if(profiles.size() > 0) {
            final String[] items= new String[profiles.size()+1];

            for(int i=0 ; i<= profiles.size();i++)
            {
                if(i==0)
                {
                    items[i] = "Self";
                    continue;
                }
                items[i] = profiles.get(i-1).getName();
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Profile");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection

                    if(item==0)
                    {
                        ClicUtils.createPreferences(BaseActiivty.this, sharedProfiles.getCustomerId(), R.string.clic_ClientID);
                        ClicUtils.createPreferences(BaseActiivty.this, sharedProfiles.getCustomerId(), R.string.clic_SharedClientID);

                    }else {
                        ClicUtils.createPreferences(BaseActiivty.this, profiles.get(item-1).getSubcustomerId(), R.string.clic_ClientID);
                    }
                    startHomeScreen();

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }else
        {
            ClicUtils.displayToast(BaseActiivty.this,"There is no Shared Profiles for you!");
        }
    }

    private void startHomeScreen() {

        Intent intent = new Intent(this,ClicServeHome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    ServiceListener mServiceListener =new ServiceListener() {
        @Override
        public void onServiceResponse(String response) {

            if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.CLIC_PROFILES_SERVICE))
            {
                SharedProfiles profiles = new Gson().fromJson(response,SharedProfiles.class);

                createListTypeDialog(profiles);
            }
            else if(SERVICE_TYPE.equalsIgnoreCase(ClicConstants.CLIC_PROFILE_SHARE)) {

                ClicUtils.createSuccessDialog(BaseActiivty.this, R.layout.clic_status, ClicConstants.CALLBACK_SERVICE);
            }

        }

        @Override
        public void onServiceError(String response) {

            ClicUtils.displayToast(BaseActiivty.this,"Connection Error....!");
        }
    };
}
