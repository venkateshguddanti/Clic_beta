package com.clic.org.serve.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clic.imageservices.model.ImageCaptureType;
import com.clic.imageservices.ui.GalleryActivityThumbs;
import com.clic.imageservices.utils.Constants;
import com.clic.imageservices.utils.ImageServices;
import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.Utils.JsonUtils;
import com.clic.org.serve.data.Address;
import com.clic.org.serve.data.RequestType;
import com.clic.org.serve.data.RequestTypeResponse;
import com.clic.org.serve.data.ServiceRequest;
import com.clic.org.serve.data.ServiceType;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.listener.ServiceListener;
import com.clic.org.serve.services.ServiceConstants;
import com.clic.org.serve.services.ServiceUtils;

import java.util.Calendar;

/**
 * Created by Venkatesh on 11-06-2016.
 */
public class ProductServiceScheduler extends Fragment implements View.OnClickListener,DatePickerFragment.DateFromPickerListener
        {

    ImageView chooseDocument,chooseLocaton;
    ImageView locationStatus;
    Button btnSubmit,dateView,timeView,address;
    public static final int DATEPICKER_FRAGMENT=1;
    public static final int READ_REQUEST_CODE=2;

    private RelativeLayout layoutUpload;
    private Address mAddress = new Address();
    Uri imageUri = null;
    String mCurrentPath;
    ServiceRequest mServiceRequest = new ServiceRequest();
    ServiceType mServiceType = new ServiceType();
    RequestType mRequestType = new RequestType();
            EditText edt_description;
            RequestTypeResponse requestTypeResponse = new RequestTypeResponse();
   // GoogleApiClient mGoogleApiClient;

            UserItemsResponse mUserItemsResponse;

            String type;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_product_scheduler,container,false);
        initWidgets(view);
        return view;

    }

    private void initWidgets(View view) {

       /* if(mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }*/

        type  = getArguments().getString(getString(R.string.activity_type));
        mServiceType = getArguments().getParcelable(getString(R.string.parcel_service_type));

        layoutUpload = (RelativeLayout)view.findViewById(R.id.layout_imgUpload);
        edt_description = (EditText)view.findViewById(R.id.edt_des);
        address = (Button)view.findViewById(R.id.btn_ItemAddress);


        if(type.equalsIgnoreCase(getString(R.string.schedule_reqReq))) {
            requestTypeResponse = getArguments().getParcelable(getString(R.string.parcel_repiar_type));
            mRequestType = getArguments().getParcelable(getString(R.string.parcel_repiar_type_req));
            mServiceRequest.setRepaiTypeId(mRequestType.getRequestTypeID());

        }
        if(mServiceType.getServiceType().equalsIgnoreCase("demo"))
        {
            layoutUpload.setVisibility(View.GONE);
            edt_description.setVisibility(View.GONE);
        }else
        {
            layoutUpload.setVisibility(View.VISIBLE);
            edt_description.setVisibility(View.VISIBLE);

        }
        mUserItemsResponse = getArguments().getParcelable(getString(R.string.user_item));



        chooseDocument = (ImageView)view.findViewById(R.id.chooseDocument);
       /* chooseLocaton = (ImageView)view.findViewById(R.id.gpsLocation);
        locationStatus = (ImageView)view.findViewById(R.id.gpsLocationStatus);*/
        btnSubmit = (Button)view.findViewById(R.id.btn_submit);

        chooseDocument.setOnClickListener(this);
       // chooseLocaton.setOnClickListener(this);
        //locationStatus.setOnClickListener(this);
        dateView = (Button)view.findViewById(R.id.btn_date);
        timeView = (Button)view.findViewById(R.id.btn_time);
        dateView.setOnClickListener(this);
        timeView.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        address.setOnClickListener(this);



        edt_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    if(edt_description.getText().toString().length() > 0)
                    {
                        mServiceRequest.setDescription(edt_description.getText().toString());
                    }else
                    {
                        ClicUtils.displayToast(getActivity(),"Plese Enter value....");
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.chooseDocument:
                uploadDocument();
                break;
            case R.id.btn_submit:
                if(dateView.getText().toString().length()==4 )
                {
                    ClicUtils.displayToast(getActivity()," Date & Time Required to Proceed!");
                    return;
                }

                mServiceRequest.setTypeOfRequest(mServiceType.getServiceTypeID());
                mServiceRequest.setCustomerID(mUserItemsResponse.getCustomerID());
                mServiceRequest.setCustomerItemID(mUserItemsResponse.getItemID());
                ServiceUtils.postJsonObjectRequest(getActivity(),
                        ServiceConstants.SERVICE_SCHEDULE,mServiceListener, JsonUtils.getJsonString(mServiceRequest));
                break;
            case R.id.btn_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(ProductServiceScheduler.this, DATEPICKER_FRAGMENT);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;
            case R.id.btn_time:
                final Calendar c = Calendar.getInstance();
                int  mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                timeView.setText(hourOfDay + ":" + minute);

                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
                break;
            case R.id.btn_ItemAddress:
                ClicUtils.createAddressDialog(getActivity(),R.layout.clic_address_layout,mAddress);
                break;
        }
    }

    public void uploadDocument()
    {

        final CharSequence[] items = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Make Your Selection");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].toString().equalsIgnoreCase("Camera")) {

                    imageUri = ImageServices.getOutputMediaFileUri();
                    mCurrentPath = ImageServices.getOutputMediaFile().getPath();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 100);

                } else if (items[which].toString().equalsIgnoreCase("Gallery")) {

                    Intent i = new Intent(getActivity(), GalleryActivityThumbs.class);
                    i.putExtra(Constants.Gallery.GALLARY_TYPE, Constants.Gallery.GALLARY_TYPE_IMAGE);
                    startActivityForResult(i, ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType());
                    //Will return image path to activity result by resultcode ImageCaptureType.CAPTURE_BY_GALLERY
                    // ImageServices.imageCapture(getActivity(), ImageCaptureType.CAPTURE_BY_GALLERY);

                } else
                {

                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    // Filter to show only images, using the image MIME data type.
                    // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                    // To search for all documents available via installed storage providers,
                    // it would be "*/*".
                    intent.setType("*/*");

                    startActivityForResult(intent, READ_REQUEST_CODE);

                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {

                if(requestCode == ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType() && data != null)
                {

                    if(ClicUtils.isFileSizeLimitExceed(data.getExtras().getString(Constants.Gallery.IMAGE_PATH)))
                    {
                        Toast.makeText(getActivity(), "File Size Shold be less than 1MB" , Toast.LENGTH_SHORT).show();

                    }else {
                        udateDocumentValue(mServiceRequest,data.getExtras().getString(Constants.Gallery.IMAGE_PATH));
                        Toast.makeText(getActivity(), "Invoice Uploaded Successfully" + data.getExtras().getString(Constants.Gallery.IMAGE_PATH), Toast.LENGTH_SHORT).show();
                    }



                }
                else  if (data == null && resultCode == Activity.RESULT_OK) {

                    if(ClicUtils.isFileSizeLimitExceed(mCurrentPath))
                    {
                        Toast.makeText(getActivity(), "File Size Shold be less than 1MB" , Toast.LENGTH_SHORT).show();

                    }else{
                        udateDocumentValue(mServiceRequest,mCurrentPath);

                        Toast.makeText(getActivity(), "Invoice Uploaded Successfully" + mCurrentPath, Toast.LENGTH_SHORT).show();
                    }
                }
                else if (data != null && resultCode == Activity.RESULT_OK) {
                    // The document selected by the user won't be returned in the intent.
                    // Instead, a URI to that document will be contained in the return intent
                    // provided to this method as a parameter.
                    // Pull that URI using resultData.getData().
           /* Uri uri = null;
            if (data != null) {
                File myFile = new File(ClicUtils.getFilePathFromUri(getActivity(),data.getData()));

                if(ClicUtils.isFileSizeLimitExceed(myFile.getAbsolutePath()))
                {
                    Toast.makeText(getActivity(), "File Size Shold be less than 1MB" , Toast.LENGTH_SHORT).show();

                }else {
                    udateDocumentValue(mUserItemsResponse,myFile.getAbsolutePath());
                    Toast.makeText(getActivity(), "Invoice Uploaded Successfully" , Toast.LENGTH_SHORT).show();
                }

            }*/
                }
            }

    private void udateDocumentValue(ServiceRequest mServiceRequest, String string)
    {
        mServiceRequest.setImageString(ClicUtils.convertBitmapToString(string));
        Glide.with(getActivity())
                .load(string)
                .fitCenter()
                .into(chooseDocument);

    }
    @Override
    public void getDataFromPicker(String value) {

        dateView.setText(value);
        mServiceRequest.setScheduledDate(value);
        final Calendar c = Calendar.getInstance();
        int  mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        timeView.setText(hourOfDay + ":" + minute);


                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

            ServiceListener mServiceListener = new ServiceListener() {
                @Override
                public void onServiceResponse(String response) {

                    if(response.contains("errorCode"))
                    {
                        ClicUtils.displayToast(getActivity(),"Error Response From server");
                        return;
                    }
                    //ClicUtils.createSuccessDialog(getActivity(), R.layout.clic_status,"");
                    getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    ProductLearnMoreFragment productLearnMoreFragment = new ProductLearnMoreFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(getString(R.string.user_item), mUserItemsResponse);
                    bundle.putString(getString(R.string.activity_type),"Request Submitted Successfully!");
                    productLearnMoreFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, productLearnMoreFragment).addToBackStack(null).commit();

                }

                @Override
                public void onServiceError(String response) {

                    ClicUtils.displayToast(getActivity(),"Connection Error....!");
                }
            };

   /* @Override
    public void onConnected(@Nullable Bundle bundle) {

        Location mLastLocation = null;
        try {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (mLastLocation != null) {
            mServiceRequest.setLang(String.valueOf(mLastLocation.getLongitude()));
            mServiceRequest.setLat(String.valueOf(mLastLocation.getLatitude()));

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }*/
}
