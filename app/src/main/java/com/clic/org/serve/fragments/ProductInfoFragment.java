package com.clic.org.serve.fragments;

import android.app.Activity;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.ItemDocs;
import com.clic.org.serve.data.UserItemsResponse;

import java.util.ArrayList;
import java.util.List;


public class ProductInfoFragment extends Fragment implements View.OnClickListener{

    UserItemsResponse mUserItemsResponse = new UserItemsResponse();

    ImageView warrantyStatus,insuranceStatus,invoiceStatus;
    TextView txtModelName,txtModelNumber,txtYearOfPurchase,txtwarrantyStatus,txtinsuranceStatus,txtInvoiceStatus;
    View layoutViewInvoice;
    List<ItemDocs> listItemDocs = new ArrayList<>();

    public ProductInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_info, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {

        mUserItemsResponse = getArguments().getParcelable(getString(R.string.user_item));

        listItemDocs = mUserItemsResponse.getItemDocs();

        txtModelName = (TextView)view.findViewById(R.id.modelName);
        txtModelNumber = (TextView)view.findViewById(R.id.modelNumber);
        txtYearOfPurchase = (TextView)view.findViewById(R.id.purchaseYear);
        warrantyStatus = (ImageView)view.findViewById(R.id.warrantyStatus);
        txtwarrantyStatus = (TextView)view.findViewById(R.id.txt_statusWarranty);
        txtwarrantyStatus.setOnClickListener(this);
        insuranceStatus = (ImageView)view.findViewById(R.id.insuranceStatus);
        txtinsuranceStatus = (TextView)view.findViewById(R.id.txt_statusInsurance);
        txtinsuranceStatus.setOnClickListener(this);
        invoiceStatus = (ImageView)view.findViewById(R.id.invoiceStatus);
        txtInvoiceStatus = (TextView)view.findViewById(R.id.txt_statusInvoce);
        txtInvoiceStatus.setOnClickListener(this);


        invoiceStatus.setBackgroundResource(R.drawable.no_update);
        warrantyStatus.setBackgroundResource(R.drawable.no_update);
        insuranceStatus.setBackgroundResource(R.drawable.no_update);


        layoutViewInvoice = (RelativeLayout)view.findViewById(R.id.viewInvoice);
        layoutViewInvoice.setOnClickListener(this);

        txtModelName.setText(mUserItemsResponse.getCategoryName());
        txtModelNumber.setText(mUserItemsResponse.getModelNumber());
        txtYearOfPurchase.setText(mUserItemsResponse.getYearop());

        if(mUserItemsResponse.getItemDocs().size() > 0) {
            setItemDocsStatus(mUserItemsResponse.getItemDocs());
        }


    }

    private void setItemDocsStatus(ArrayList<ItemDocs> itemDocs) {

        for(ItemDocs id : itemDocs)
        {
            if(id.getDocType().equalsIgnoreCase(ClicConstants.DOC_INVOICE_VALUE))
            {
                txtInvoiceStatus.setText("View");
                invoiceStatus.setBackgroundResource(R.drawable.success);
                txtInvoiceStatus.setOnClickListener(this);

            }
            else if(id.getDocType().equalsIgnoreCase(ClicConstants.DOC_WARRANTY_VALUE))
            {
                txtwarrantyStatus.setText("View");
                warrantyStatus.setBackgroundResource(R.drawable.success);
                txtwarrantyStatus.setOnClickListener(this);

            }
            else if(id.getDocType().equalsIgnoreCase(ClicConstants.DOC_INSURENCE_VALUE))
            {
                txtinsuranceStatus.setText("View");
                insuranceStatus.setBackgroundResource(R.drawable.success);
                txtinsuranceStatus.setOnClickListener(this);

            }

        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public ItemDocs getItemDocument(ArrayList<ItemDocs> listItmemDocs,String documentType)
    {
        for(ItemDocs document : listItmemDocs)
        {

            if(document.getDocType().equalsIgnoreCase(documentType))
            {
                return document;
            }
        }
        return null;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id)
        {
            case R.id.viewInvoice:

                break;

            case R.id.txt_statusInsurance:
                if(getItemDocument(mUserItemsResponse.getItemDocs(), ClicConstants.DOC_INSURENCE_VALUE) != null) {
                    ClicUtils.createImagePinchDialog(getActivity(), R.layout.pinch_zoom_image,
                            getItemDocument(mUserItemsResponse.getItemDocs(), ClicConstants.DOC_INSURENCE_VALUE).getFilePath() );
                }else
                {
                   startUploadFragment();
                }
                break;
            case R.id.txt_statusInvoce:
                if(getItemDocument(mUserItemsResponse.getItemDocs(), ClicConstants.DOC_INVOICE_VALUE) != null) {
                    ClicUtils.createImagePinchDialog(getActivity(), R.layout.pinch_zoom_image,
                            getItemDocument(mUserItemsResponse.getItemDocs(), ClicConstants.DOC_INVOICE_VALUE).getFilePath());
                }else
                {
                    startUploadFragment();
                }
                break;
            case R.id.txt_statusWarranty:
                if(getItemDocument(mUserItemsResponse.getItemDocs(), ClicConstants.DOC_WARRANTY_VALUE) != null) {
                    ClicUtils.createImagePinchDialog(getActivity(), R.layout.pinch_zoom_image,
                            getItemDocument(mUserItemsResponse.getItemDocs(), ClicConstants.DOC_WARRANTY_VALUE).getFilePath() );
                }else
                {
                    startUploadFragment();
                }
                break;
        }
    }

    private void startUploadFragment() {

         AddInvoiceFragment invoiceFragment = new AddInvoiceFragment();
            Bundle b = new Bundle();
            b.putString(getString(R.string.activity_type), getString(R.string.activity_upload_docs));
            b.putParcelable(getString(R.string.user_item), mUserItemsResponse);
            invoiceFragment.setArguments(b);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, invoiceFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();


    }
}
