package com.clic.org.serve.Utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clic.org.R;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.Address;
import com.clic.org.serve.data.Customer;
import com.clic.org.serve.data.OTP;
import com.clic.org.serve.data.OtpValidation;
import com.clic.org.serve.data.ProfileData;
import com.clic.org.serve.data.ServiceRequest;
import com.clic.org.serve.data.ServiceRequestAsRespose;
import com.clic.org.serve.listener.ServiceListener;
import com.clic.org.serve.services.ServiceConstants;
import com.clic.org.serve.services.ServiceUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Venkatesh on 21-05-2016.
 */
public  class ClicUtils {



    public static void shareClic(Activity activity)
    {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        activity.startActivity(Intent.createChooser(sendIntent, "send to"));
    }
    public static void createSuccessDialog(final Activity activity,int layout,final String type)
    {
        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        TextView OK = (TextView)dialog.findViewById(R.id.txt_ok);
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equalsIgnoreCase(ClicConstants.CALLBACK_SERVICE)) {
                    // activity.finish();
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                    //activity.finish();
                }
            }
        });
        // TouchImageView imageView = (TouchImageView)dialog.findViewById(R.id.pinchImage);

        dialog.show();
    }
    public static void createDialogWithTwoButtons(final Activity activity,int layout,final String type,final ImageView btn)
    {
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Holo_Light_Dialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*LayoutInflater inflater = (LayoutInflater)activity.getApplicationContext().getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout,null);
        setItemHeightWidthFull(activity,view);*/
        dialog.setContentView(layout);

        TextView OK = (TextView)dialog.findViewById(R.id.txt_ok);
        TextView des1 = (TextView)dialog.findViewById(R.id.txt_des1);
        TextView des2 = (TextView)dialog.findViewById(R.id.txt_des2);
        TextView des3 = (TextView)dialog.findViewById(R.id.txt_des3);
        TextView cancel = (TextView)dialog.findViewById(R.id.txt_cancel);
        EditText comments = (EditText)dialog.findViewById(R.id.edt_commetns);

        if(type.equalsIgnoreCase(ClicConstants.DIALOG_TYPE_LEARNMORE_LIKE))
        {
            des1.setText("Thanks For Rating Us");
            des2.setVisibility(View.GONE);
            comments.setVisibility(View.GONE);
            des3.setText("");
            OK.setText("OK");
            cancel.setText("Cancel");
        }
        else
        {
            des1.setText("Thanks For Rating Us");
            des2.setVisibility(View.GONE);
            des3.setText("");
            comments.setVisibility(View.GONE);
            OK.setText("OK");
            cancel.setText("Cancel");
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if (type.equalsIgnoreCase(ClicConstants.DIALOG_TYPE_LEARNMORE_LIKE)) {
                    btn.setBackgroundResource(R.drawable.icn__like);
                } else {
                    btn.setBackgroundResource(R.drawable.icn__dislike);
                }

            }
        });
        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type.equalsIgnoreCase(ClicConstants.CALLBACK_SERVICE)) {
                    dialog.dismiss();

                } else {
                    dialog.dismiss();
                }
            }
        });
        // TouchImageView imageView = (TouchImageView)dialog.findViewById(R.id.pinchImage);

        dialog.show();
    }
    public static void createServiceRequestDetailsDialog(final Activity activity,int layout,ServiceRequestAsRespose mDetails)
    {
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Holo_Light_NoActionBar);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        ImageView icon = (ImageView)dialog.findViewById(R.id.icon);
        TextView back = (TextView)dialog.findViewById(R.id.backButton);
        TextView name = (TextView)dialog.findViewById(R.id.modelName);
        TextView status = (TextView)dialog.findViewById(R.id.reqStatus);
        TextView comments = (TextView)dialog.findViewById(R.id.reqComments);

        name.setText(mDetails.getCustomerItemName());
        status.setText(mDetails.getStatus());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        dialog.show();

    }
    public static void createImagePinchDialog(final Activity activity,int layout,final String path)
    {
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Holo_Light_NoActionBar);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

       // TouchImageView imageView = (TouchImageView)dialog.findViewById(R.id.pinchImage);
        ImageView imageView = (ImageView)dialog.findViewById(R.id.pinchImage);

        Glide.with(activity)
                .load(path)
                .fitCenter()
                .into(imageView);
        dialog.show();
    }


    public static void createAddressDialog(final Activity activity,int layout, final Address address)
    {
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Holo_Light_NoActionBar);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        final EditText edtHouseNo = (EditText)dialog.findViewById(R.id.edithouseNo);
        final EditText edtStreet = (EditText)dialog.findViewById(R.id.editStreetName);
        final EditText edtCity = (EditText)dialog.findViewById(R.id.editCity);
        final EditText edtState = (EditText)dialog.findViewById(R.id.editState);
        final EditText edtCountry = (EditText)dialog.findViewById(R.id.editCountry);
        final EditText edtPincode = (EditText)dialog.findViewById(R.id.editPincode);
        Button buttonSubmit = (Button)dialog.findViewById(R.id.btn_submit);
        final TextView back = (TextView)dialog.findViewById(R.id.backButton);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edtPincode.getText().toString().length() == 0) {
                    ClicUtils.displayToast(activity, "Please Enter pincode to proceed");
                } else {
                    dialog.dismiss();
                    address.setCity(edtCity.getText().toString());
                    address.setHouseNumber(edtHouseNo.getText().toString());
                    address.setCountry(edtCountry.getText().toString());
                    address.setState(edtState.getText().toString());
                    address.setPinCode(Integer.parseInt(edtPincode.getText().toString()));
                    address.setStreetName(edtStreet.getText().toString());
                }
            }
        });

        dialog.show();
    }
    public static Dialog createMenuDialog(Activity activity,int layout)
    {
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Light_NoTitleBar_Fullscreen);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layout);

        GridView myGridView = (GridView)dialog.findViewById(R.id.gridView);

       // ItemGridAdapter myAdapter = new ItemGridAdapter(activity,web,imageId,ClicConstants.CLIC_MENU);
       // myGridView.setAdapter(myAdapter);


        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dialog.dismiss();

            }
        });
        dialog.getWindow().setLayout(getDeviceWidth(activity, ClicConstants.DEVICE_WIDTH)
                , getDeviceWidth(activity, ClicConstants.DEVICE_HEIGHT) - 250);

        dialog.show();

        return dialog;

    }
    public static void updateCustomerDetails(final Activity activity,final ServiceListener mListener,final Customer mCustomer)
    {
        final Dialog register_layout = new Dialog(activity,android.R.style.Theme_Holo_Light_NoActionBar);
        register_layout.setContentView(R.layout.layout_register);
        final EditText inputName, inputEmail, inputPassword,inputMobile;
        Button btnRegister;
        inputName = (EditText) register_layout.findViewById(R.id.input_name);
        inputEmail = (EditText) register_layout.findViewById(R.id.input_email);
        inputPassword = (EditText) register_layout.findViewById(R.id.input_password);
        inputMobile = (EditText) register_layout.findViewById(R.id.input_mobile);
        btnRegister = (Button) register_layout.findViewById(R.id.btn_register);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(inputName.getText().toString().length() == 0
                        || inputEmail.getText().toString().length() == 0
                        || inputPassword.getText().toString().length()==0
                        || inputMobile.getText().toString().length()==0)
                {
                    displayToast(activity,"All fileds required to enter");
                }
                else
                {
                    mCustomer.setCustomerName(inputName.getText().toString());
                    mCustomer.setPassword(inputPassword.getText().toString());
                    mCustomer.setEmailID(inputEmail.getText().toString());
                    mCustomer.setPhoneNumber(inputMobile.getText().toString());

                    register_layout.dismiss();
                }
            }
        });

        register_layout.show();
    }
    public static void guestDialogLogin(final Activity activity,int layout,final ServiceListener mListener, final Customer mCustomer)
    {
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Holo_Light_NoActionBar);
        final boolean flag = false;
        dialog.setContentView(layout);


        final LinearLayout layoutOTP =(LinearLayout)dialog.findViewById(R.id.layoutOTP);
        final EditText edtMobile = (EditText)dialog.findViewById(R.id.edtMobileNumber);
        final EditText edtName = (EditText)dialog.findViewById(R.id.edtName);
        final TextView header = (TextView)dialog.findViewById(R.id.txt_heading);
        final TextView back = (TextView)dialog.findViewById(R.id.backButton);
        final TextView title = (TextView)dialog.findViewById(R.id.txt_title);
        final TextView info = (TextView)dialog.findViewById(R.id.txt_info);


            header.setText("Clicserve");
            title.setText("Enter Mobile Number");

            layoutOTP.setVisibility(View.GONE);
            edtMobile.setVisibility(View.VISIBLE);
            edtName.setVisibility(View.GONE);
            info.setVisibility(View.GONE);


        Button btnSubmit = (Button)dialog.findViewById(R.id.btnValidate);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtMobile.getText().toString().length() != 10)
                {
                    displayToast(activity, "Mobile Number Should be 10 digits");

                }else
                {
                    mCustomer.setPhoneNumber(edtMobile.getText().toString());
                    mCustomer.setPassword(edtMobile.getText().toString());
                    dialog.dismiss();
                    ServiceUtils.postJsonObjectRequest(activity,
                            ServiceConstants.CREATE_CUSTOMER, mListener, JsonUtils.getJsonString(mCustomer));
                }
            }
        });

        dialog.show();
    }
    public static String createOTPValidationDialog(final Activity activity,int layout,final OtpValidation params,final ServiceListener mListener,final String type)
    {
        final String result = "";
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Holo_Light_NoActionBar);
        final boolean flag = false;
        dialog.setContentView(layout);


        final LinearLayout layoutOTP =(LinearLayout)dialog.findViewById(R.id.layoutOTP);
        final EditText edtOTP = (EditText)dialog.findViewById(R.id.editTextOtp);
        final EditText edtOTP1 = (EditText)dialog.findViewById(R.id.editTextOtp1);
        final EditText edtOTP2 = (EditText)dialog.findViewById(R.id.editTextOtp2);
        final EditText edtOTP3 = (EditText)dialog.findViewById(R.id.editTextOtp3);
        final EditText edtMobile = (EditText)dialog.findViewById(R.id.edtMobileNumber);
        final EditText edtName = (EditText)dialog.findViewById(R.id.edtName);
        final TextView header = (TextView)dialog.findViewById(R.id.txt_heading);
        final TextView back = (TextView)dialog.findViewById(R.id.backButton);
        final TextView title = (TextView)dialog.findViewById(R.id.txt_title);
        final TextView info = (TextView)dialog.findViewById(R.id.txt_info);

        if(type.equalsIgnoreCase(ClicConstants.DIALOG_TYPE_OTP))
        {
            header.setText("Confirm Registration");
            title.setText("Enter OTP");

        }
        else if(type.equalsIgnoreCase(ClicConstants.DIALOG_TYPE_SHARE))
        {
            header.setText("Share Clic");
            title.setText("Enter Mobile Number");
            layoutOTP.setVisibility(View.GONE);
            edtMobile.setVisibility(View.VISIBLE);
            edtName.setVisibility(View.VISIBLE);
            info.setVisibility(View.VISIBLE);
        }

        Button btnSubmit = (Button)dialog.findViewById(R.id.btnValidate);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (type.equalsIgnoreCase(ClicConstants.DIALOG_TYPE_SHARE)) {

                    if (edtMobile.getText().toString().length() < 10) {
                        displayToast(activity, "Mobile number must be 10 digits");
                        return;
                    } else if (edtName.getText().toString().length() == 0) {
                        displayToast(activity, "Please Enter Name..");
                        return;
                    } else {
                        ProfileData profileData = new ProfileData();
                        profileData.setName(edtName.getText().toString());
                        profileData.setCustomerId(readPreference(activity, R.string.clic_ClientID));
                        profileData.setPhonenumber(edtMobile.getText().toString());
                        ServiceUtils.postJsonObjectRequest(activity,
                                ServiceConstants.CLIC_PROFILE_SHARE_REQUEST,
                                mListener,
                                new Gson().toJson(profileData));
                        dialog.dismiss();
                    }

                } else if (type.equalsIgnoreCase(ClicConstants.DIALOG_TYPE_OTP)) {

                    String values = edtOTP.getText().toString() + edtOTP1.getText().toString() + edtOTP2.getText().toString()
                            + edtOTP3.getText().toString();

                    if (!values.equalsIgnoreCase(params.getCustomerOTP())) {

                        displayToast(activity, "OTP is Not matched");

                    } else {
                        dialog.dismiss();
                        ServiceUtils.postJsonObjectRequest(activity, ServiceConstants.OTP_VALIDATION, mListener, JsonUtils.getJsonString(params));

                    }


                }



            }
        });

        dialog.show();


        return edtOTP.getText().toString();

    }

   public static ServiceListener mListener = new ServiceListener() {
        @Override
        public void onServiceResponse(String response) {

        }

        @Override
        public void onServiceError(String response) {

        }
    };
    public static int getDeviceWidth(Context context,String type)
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        if(type.equalsIgnoreCase(ClicConstants.DEVICE_HEIGHT)) {
            return height;
        }
        else
        {
            return width;
        }
    }

    public static void setItemHeightWidth(Context context,View view)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getDeviceWidth(context,ClicConstants.DEVICE_WIDTH)/2,
                getDeviceWidth(context,ClicConstants.DEVICE_WIDTH)/2);
        view.setLayoutParams(layoutParams);
    }
    public static void setImageHeightWidth(ImageView view)
    {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10,0,0,0);
        view.setLayoutParams(layoutParams);
    }

    public static void setItemHeightWidthFull(Context context,View view)
    {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(getDeviceWidth(context,ClicConstants.DEVICE_WIDTH),
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(layoutParams);
    }
    public static void createPreferences(Context context,String prefValue,int prefKey)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(prefKey), prefValue);
        editor.commit();
    }

    public static void clearAppPreferences(Context context)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().commit();
    }
    public  static String readPreference(Context context,int prefKey)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(context.getString(prefKey), null);
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
    public static void displayToast(final Activity activity, final String message)
    {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,message,Toast.LENGTH_LONG).show();
            }
        });
    }

    public static String getJsonStringFromObject(Object jsonObject)
    {
        Gson gson = new Gson();
       // Staff obj = new Staff();
        // 2. Java object to JSON, and assign to a String
        String jsonInString = gson.toJson(jsonObject);
        return jsonInString;
    }

    public static JsonObject getJsonObject( String obj )
    {
        JsonParser lParser = new JsonParser();
        return lParser.parse( obj ).getAsJsonObject();
    }

    public static String convertBitmapToString(String path)
    {

        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        Log.d("debug","decoded image"+Base64.encodeToString(b, Base64.DEFAULT));
        return Base64.encodeToString(b, Base64.DEFAULT);
    }



    public static boolean isFileSizeLimitExceed(String path)
    {
        File file = new File(path);

// Get length of file in bytes
        long fileSizeInBytes = file.length();
// Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
// Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        long fileSizeInMB = fileSizeInKB / 1024;
        if(fileSizeInMB >= 2 )
        {
            return true;
        }else {
            return false;
        }
    }

   /* public static String getFilePathFromUri(Activity activity,Uri data) {

        // Will return "image:x*"
        String wholeID = DocumentsContract.getDocumentId(data);

// Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

// where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = activity.getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return filePath;
    }*/

    public static void createProgressBar(ProgressDialog mProgress)
    {
        mProgress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mProgress.setIndeterminate(true);
        mProgress.show();
        mProgress.setCancelable(false);
        mProgress.setContentView(R.layout.progress);
    }

    public static String getDeviceId(Context context)
    {
        String ts = Context.TELEPHONY_SERVICE;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(ts);
        return telephonyManager.getDeviceId();
    }

    public String getDeviceName() {

        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public String getAndroidVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }



}
