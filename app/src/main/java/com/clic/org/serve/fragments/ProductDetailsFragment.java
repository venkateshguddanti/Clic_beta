package com.clic.org.serve.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.clic.org.R;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.ServiceType;
import com.clic.org.serve.data.UserItemsResponse;
import com.helpshift.support.Support;

/**
 * Created by Venkatesh on 14-07-2016.
 */
public class ProductDetailsFragment extends Fragment implements View.OnClickListener{

    UserItemsResponse mUserItemsResponse;
    LinearLayout productInfo,productUploadDOcs,productLearnmore,productServiceReq,productDemoReq,chat;
    TextView txtHint;
    String type;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product_service_details_options, container, false);
        initWidgets(view);
        return view;

    }

    private void initWidgets(View view) {

        mUserItemsResponse = getArguments().getParcelable(getString(R.string.user_item));
        productInfo = (LinearLayout)view.findViewById(R.id.product_info);
        productServiceReq = (LinearLayout)view.findViewById(R.id.product_service);
        productDemoReq = (LinearLayout)view.findViewById(R.id.product_demoReq);
        productUploadDOcs = (LinearLayout)view.findViewById(R.id.product_sell);
        productLearnmore = (LinearLayout)view.findViewById(R.id.product_learn);
        chat = (LinearLayout)view.findViewById(R.id.product_chat);
        txtHint = (TextView)view.findViewById(R.id.txt_hint);


        productInfo.setOnClickListener(this);
        productServiceReq.setOnClickListener(this);
        productDemoReq.setOnClickListener(this);
        productUploadDOcs.setOnClickListener(this);
        productLearnmore.setOnClickListener(this);
        chat.setOnClickListener(this);

        if(getArguments().getString(getString(R.string.activity_type))!= null)
        {
            if(getArguments().getString(getString(R.string.activity_type)).equalsIgnoreCase(getString(R.string.activity_upload_docs)))
            {
                txtHint.setVisibility(View.VISIBLE);
                txtHint.setText(getArguments().getString(getString(R.string.activity_type)));

            }else {
                txtHint.setVisibility(View.VISIBLE);
                txtHint.setText(getArguments().getString(getString(R.string.activity_type)));
            }
        }


    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        Bundle bundle = new Bundle();
        switch (id) {

            case R.id.product_demoReq:

                ProductServiceScheduler productServiceScheduler = new ProductServiceScheduler();
                ServiceType mSerReq = new ServiceType();
                Bundle b = new Bundle();
                mSerReq.setServiceType("demo");
                mSerReq.setServiceTypeID(ClicConstants.SERVICE_TYPE_DEMO);

                    b.putString(getString(R.string.activity_type), getString(R.string.schedule_serReq));
                    b.putParcelable(getString(R.string.parcel_service_type), mSerReq);
                    b.putParcelable(getString(R.string.user_item), mUserItemsResponse);
                    productServiceScheduler.setArguments(b);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, productServiceScheduler).addToBackStack(null).commit();

                break;
            case R.id.product_info:
               // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ProductInfoFragment productInfoFragment = new ProductInfoFragment();
                bundle.putParcelable(getString(R.string.user_item), mUserItemsResponse);
                productInfoFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, productInfoFragment).addToBackStack(null).commit();
                break;
            case R.id.product_service:
               // getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                MyListFragment fragment = new MyListFragment();
                bundle.putString(getString(R.string.list_type),getString(R.string.schedule_service));
                bundle.putParcelable(getString(R.string.user_item), mUserItemsResponse);
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
               break;
            case R.id.product_sell:
                //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
               /* ProductSellFragment productSellFragment = new ProductSellFragment();
                bundle.putParcelable(getString(R.string.user_item), mUserItemsResponse);
                productSellFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, productSellFragment).addToBackStack(null).commit();
*/
                AddInvoiceFragment invoiceFragment = new AddInvoiceFragment();
                bundle.putString(getString(R.string.list_type), getString(R.string.add_invovice));
                bundle.putParcelable(getString(R.string.user_item), mUserItemsResponse);
                invoiceFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, invoiceFragment).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();

                break;
            case R.id.product_learn:
                //getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ProductLearnMoreFragment productLearnMoreFragment = new ProductLearnMoreFragment();
                bundle.putParcelable(getString(R.string.user_item), mUserItemsResponse);
                productLearnMoreFragment.setArguments(bundle);
               getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content, productLearnMoreFragment).addToBackStack(null).commit();
                break;
            case R.id.product_chat:
               // Support.showConversation(getActivity());

                break;
        }
        }
}
