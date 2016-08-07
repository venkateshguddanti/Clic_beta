package com.clic.org.serve.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clic.imageservices.ui.GalleryActivityThumbs;
import com.clic.org.R;
import com.clic.imageservices.model.ImageCaptureType;
import com.clic.imageservices.utils.Constants;
import com.clic.imageservices.utils.ImageServices;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.Utils.JsonUtils;
import com.clic.org.serve.activity.ProductDetailsAndServicesActivity;
import com.clic.org.serve.activity.SignupGuideActvity;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.Address;
import com.clic.org.serve.data.ItemDocs;
import com.clic.org.serve.data.UserItem;
import com.clic.org.serve.data.UserItemsResponse;
import com.clic.org.serve.listener.ServiceListener;
import com.clic.org.serve.services.ServiceConstants;
import com.clic.org.serve.services.ServiceUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Venkatesh on 25-05-2016.
 */
public class AddInvoiceFragment extends Fragment implements DatePickerFragment.DateFromPickerListener,View.OnClickListener,AdapterView.OnItemSelectedListener{

    ImageView selectDocumentInvoice,selectDocumentWarranty,selectDocumentInsurance;
    Button btnSubmit;
    Uri imageUri = null;
    String mCurrentPath;
    InVoicePathListener mListener;

    private Calendar calendar;
    private Button dateView;
    public static final int DATEPICKER_FRAGMENT=1;
    public static final int READ_REQUEST_CODE=2;
    UserItemsResponse mUserItemsResponse = new UserItemsResponse();
    UserItem mUserItem = new UserItem();
    ItemDocs itemDoc = new ItemDocs();


    EditText warrantyYears;

    String DOCUMENT_TYPE;


    CheckBox itemCheck;
    Button itemAddress;
    Address mAddress = new Address();

    private Spinner documentTypes;
    private ArrayList<CharSequence> spinnerList = new ArrayList<>();
    private ArrayList<ItemDocs> documentList = new ArrayList<>();
    private boolean documentTypeSelected = false;
    private String type = "";
    private  ArrayAdapter<CharSequence> adapter;
    private TextView addPhotoText;
    @Override
    public void getDataFromPicker(String value) {

        dateView.setText(value);
        mUserItemsResponse.setYearop(value);
        mUserItemsResponse.setWarrentyMonths("5");
        warrantyYears.setText("5");

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.uploadImageInvoice:
                if(documentTypeSelected) {
                    uploadDocument();
                }else
                {
                    ClicUtils.displayToast(getActivity(),getString(R.string.err_select_documenttype));
                }
                break;
            case R.id.uploadImageWarranty:
                if(documentTypeSelected) {
                    uploadDocument();
                }else
                {
                    ClicUtils.displayToast(getActivity(),getString(R.string.err_select_documenttype));
                }
                break;
            case R.id.uploadImageInsurance:
                if(documentTypeSelected) {
                    uploadDocument();
                }else
                {
                    ClicUtils.displayToast(getActivity(),getString(R.string.err_select_documenttype));
                }
                break;
            case R.id.btn_submit:
                if(dateView.getText().toString().length()==4 )
                {
                    ClicUtils.displayToast(getActivity()," Date Required to Proceed!");
                    return;
                }
                else {
                    postUserItem(mUserItemsResponse);
                    ServiceUtils.postJsonObjectRequest(getActivity(), ServiceConstants.ADD_CUSTOMER_ITEM, mServiceListener, JsonUtils.getJsonString(mUserItem));
                }

                break;
            case R.id.btn_date:
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.setTargetFragment(AddInvoiceFragment.this, DATEPICKER_FRAGMENT);
                newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                break;

            case R.id.btn_ItemAddress:
                ClicUtils.createAddressDialog(getActivity(),R.layout.clic_address_layout,mAddress);
                break;
        }

    }

    private void postUserItem(UserItemsResponse mUserItemsResponse) {

        mUserItem.setCustomerID(mUserItemsResponse.getCustomerID());
        mUserItem.setBrandID(mUserItemsResponse.getBrandID());
        mUserItem.setCategoryID(mUserItemsResponse.getCategoryID());
        mUserItem.setSubcategoryID(mUserItemsResponse.getSubcategoryID());
        mUserItem.setItemID(mUserItemsResponse.getItemID());
        mUserItem.setYearop(mUserItemsResponse.getYearop());
        mUserItem.setWarrentyMonths(mUserItemsResponse.getWarrentyMonths());
        mUserItem.setDescription(mUserItemsResponse.getDescription());
        mUserItem.setModelNumber(mUserItemsResponse.getModelNumber());
        mUserItem.setProductID(mUserItemsResponse.getProductID());
        mUserItem.setSameAddress("yes");
        mUserItemsResponse.setItemDocs(documentList);
        mUserItem.setItemDocs(documentList);
        mUserItem.setAddress(mAddress);


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        documentTypeSelected = true;
        if(spinnerList.get(position).toString().equalsIgnoreCase(ClicConstants.DOC_INVOICE))
        {
            DOCUMENT_TYPE = ClicConstants.DOC_INVOICE;
            selectDocumentInvoice.setVisibility(View.VISIBLE);
            addPhotoText.setVisibility(View.VISIBLE);

        }
        else if(spinnerList.get(position).toString().equalsIgnoreCase(ClicConstants.DOC_WARRANTY))
        {
            DOCUMENT_TYPE = ClicConstants.DOC_WARRANTY;
            selectDocumentWarranty.setVisibility(View.VISIBLE);
            addPhotoText.setVisibility(View.VISIBLE);
        }
        else if(spinnerList.get(position).toString().equalsIgnoreCase(ClicConstants.DOC_INSURENCE))
        {
            DOCUMENT_TYPE = ClicConstants.DOC_INSURENCE;
            selectDocumentInsurance.setVisibility(View.VISIBLE);
            addPhotoText.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface InVoicePathListener
    {
        public void getInvoicePath(String value);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_invoice,container,false);

        initWidgets(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (InVoicePathListener)context;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initWidgets(View view) {

        mUserItemsResponse = getArguments().getParcelable(getString(R.string.user_item));
        documentList = mUserItemsResponse.getItemDocs();

        type = getArguments().getString(getString(R.string.activity_type));

        warrantyYears =(EditText)view.findViewById(R.id.edt_warranty);
        btnSubmit = (Button)view.findViewById(R.id.btn_submit);
        selectDocumentInvoice = (ImageView)view.findViewById(R.id.uploadImageInvoice);
        selectDocumentWarranty = (ImageView)view.findViewById(R.id.uploadImageWarranty);
        selectDocumentInsurance = (ImageView)view.findViewById(R.id.uploadImageInsurance);
        addPhotoText = (TextView)view.findViewById(R.id.txt_addphoto);

        itemCheck =(CheckBox)view.findViewById(R.id.addressCheck);
        itemAddress = (Button)view.findViewById(R.id.btn_ItemAddress);
        documentTypes = (Spinner)view.findViewById(R.id.spinnerDocuments);
        documentTypes.setOnItemSelectedListener(this);
        spinnerList.add("Select Document");
        spinnerList.add(ClicConstants.DOC_INVOICE);
        spinnerList.add(ClicConstants.DOC_WARRANTY);
        spinnerList.add(ClicConstants.DOC_INSURENCE);
        adapter = new ArrayAdapter<CharSequence>(getActivity(),android.R.layout.simple_spinner_dropdown_item
                ,spinnerList);
        documentTypes.setAdapter(adapter);


        if(documentList != null)
        {
            updateSpinnerAdapter(documentList);
        }
        else
        {
            documentList = new ArrayList<>();
        }

        itemAddress.setOnClickListener(this);
        selectDocumentInvoice.setOnClickListener(this);
        selectDocumentWarranty.setOnClickListener(this);
        selectDocumentInsurance.setOnClickListener(this);

        dateView = (Button)view.findViewById(R.id.btn_date);
        dateView.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);


        itemCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    mUserItem.setSameAddress("yes");
                }else
                {
                    mUserItem.setSameAddress("no");
                }
            }
        });

        warrantyYears.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_DONE)
                {
                    if(warrantyYears.getText().length() > 0)
                    {
                      mUserItemsResponse.setWarrentyMonths(warrantyYears.getText().toString());
                    }
                    else
                    {
                        ClicUtils.displayToast(getActivity(),"Please Enter Value..");
                    }
                }
                return false;
            }
        });

    }



    public void updateSpinnerAdapter(ArrayList<ItemDocs> itemDocs)
    {
        for(ItemDocs id: itemDocs)
        {
            if(id.getDocType().equalsIgnoreCase(ClicConstants.DOC_INVOICE_VALUE))
            {
                spinnerList.remove(1);
                adapter.notifyDataSetChanged();
            }
            else if(id.getDocType().equalsIgnoreCase(ClicConstants.DOC_WARRANTY_VALUE))
            {
                spinnerList.remove(2);
                adapter.notifyDataSetChanged();
            }
            else if(id.getDocType().equalsIgnoreCase(ClicConstants.DOC_INSURENCE_VALUE))
            {
                spinnerList.remove(3);
                adapter.notifyDataSetChanged();
            }
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
                    mListener.getInvoicePath(mCurrentPath);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, 100);

                } else if (items[which].toString().equalsIgnoreCase("Gallery")) {

                    Intent i = new Intent(getActivity(), GalleryActivityThumbs.class);
                    i.putExtra(Constants.Gallery.GALLARY_TYPE, Constants.Gallery.GALLARY_TYPE_IMAGE);
                    startActivityForResult(i, ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType());
                    //Will return image path to activity result by resultcode ImageCaptureType.CAPTURE_BY_GALLERY
                    // ImageServices.imageCapture(getActivity(), ImageCaptureType.CAPTURE_BY_GALLERY);

                } else {

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

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == ImageCaptureType.CAPTURE_BY_GALLERY.getImageCaptureType() && data != null)
        {

            if(ClicUtils.isFileSizeLimitExceed(data.getExtras().getString(Constants.Gallery.IMAGE_PATH)))
            {
                Toast.makeText(getActivity(), "File Size Shold be less than 1MB" , Toast.LENGTH_SHORT).show();

            }else {

                udateDocumentValue(mUserItemsResponse, data.getExtras().getString(Constants.Gallery.IMAGE_PATH));
                Toast.makeText(getActivity(), "Invoice Uploaded Successfully" + data.getExtras().getString(Constants.Gallery.IMAGE_PATH), Toast.LENGTH_SHORT).show();
            }



        }
        else  if (data == null && resultCode == Activity.RESULT_OK) {

            if(ClicUtils.isFileSizeLimitExceed(mCurrentPath))
            {
                Toast.makeText(getActivity(), "File Size Shold be less than 1MB" , Toast.LENGTH_SHORT).show();

            }else{
                udateDocumentValue(mUserItemsResponse, mCurrentPath);
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

    private void udateDocumentValue(UserItemsResponse mUserItemsResponse, String path) {


        if(DOCUMENT_TYPE.equalsIgnoreCase(ClicConstants.DOC_INVOICE))
        {
            itemDoc.setImageData(ClicUtils.convertBitmapToString(path));
            itemDoc.setDocType(ClicConstants.DOC_INVOICE_VALUE);
            documentList.add(itemDoc);
            itemDoc = new ItemDocs();
            Glide.with(getActivity())
                    .load(path)
                    .fitCenter()
                    .into(selectDocumentInvoice);
            selectDocumentInvoice.setClickable(false);
            removeSpinnerItem(spinnerList);
            adapter.notifyDataSetChanged();
            documentTypes.setSelection(0);


        }
        else if(DOCUMENT_TYPE.equalsIgnoreCase(ClicConstants.DOC_WARRANTY))
        {
            itemDoc.setDocType(ClicConstants.DOC_WARRANTY_VALUE);
            itemDoc.setImageData(ClicUtils.convertBitmapToString(path));
            documentList.add(itemDoc);
            itemDoc = new ItemDocs();
            Glide.with(getActivity())
                    .load(path)
                    .fitCenter()
                    .into(selectDocumentWarranty);
            selectDocumentWarranty.setClickable(false);
            removeSpinnerItem(spinnerList);
            adapter.notifyDataSetChanged();
            documentTypes.setSelection(0);


        }
        else if(DOCUMENT_TYPE.equalsIgnoreCase(ClicConstants.DOC_INSURENCE))
        {
            itemDoc.setDocType(ClicConstants.DOC_INSURENCE_VALUE);
            itemDoc.setImageData(ClicUtils.convertBitmapToString(path));
            documentList.add(itemDoc);
            itemDoc = new ItemDocs();
            Glide.with(getActivity())
                    .load(path)
                    .fitCenter()
                    .into(selectDocumentInsurance);
            selectDocumentInsurance.setClickable(false);
            removeSpinnerItem(spinnerList);
            adapter.notifyDataSetChanged();
            documentTypes.setSelection(0);
        }

    }

    private void removeSpinnerItem(ArrayList<CharSequence> spinnerList) {

        for(int i=1;i<spinnerList.size();i++)
        {

            if(spinnerList.get(i).toString().equalsIgnoreCase(DOCUMENT_TYPE))
            {
                spinnerList.remove(i);
            }

        }
    }


    ServiceListener mServiceListener = new ServiceListener() {
        @Override
        public void onServiceResponse(String response) {

            if(response.contains("errorCode"))
            {
                ClicUtils.displayToast(getActivity(),"Error Response From server");
                return;
            }

            if(type !=null && type.equalsIgnoreCase(getString(R.string.activity_upload_docs)))
            {
                getActivity().finish();
                //ClicUtils.createSuccessDialog(getActivity(),R.layout.clic_status,"");

                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent intent = new Intent(getActivity(), ProductDetailsAndServicesActivity.class);
                intent.putExtra(getString(R.string.user_item), mUserItemsResponse);
                intent.putExtra(getString(R.string.activity_type), "Document Added Successfully !");
                startActivity(intent);
            }
            else
            {
                getActivity().finish();
                //ClicUtils.createSuccessDialog(getActivity(),R.layout.clic_status,"");

                getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Intent intent = new Intent(getActivity(), ProductDetailsAndServicesActivity.class);
                intent.putExtra(getString(R.string.user_item), mUserItemsResponse);
                intent.putExtra(getString(R.string.activity_type), "Product Added Successfully!");
                startActivity(intent);
                ClicUtils.createPreferences(getActivity(), "true", R.string.is_product_exit);
            }
        }

        @Override
        public void onServiceError(String response) {

            ClicUtils.displayToast(getActivity(),"Connection Error....!");

        }
    };
}
