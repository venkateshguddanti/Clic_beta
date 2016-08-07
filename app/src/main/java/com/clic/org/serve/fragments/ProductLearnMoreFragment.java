package com.clic.org.serve.fragments;

import android.app.Activity;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.clic.org.R;
import com.clic.org.serve.Utils.ClicUtils;
import com.clic.org.serve.activity.ClicServeHome;
import com.clic.org.serve.constants.ClicConstants;
import com.clic.org.serve.data.ServiceRequest;
import com.clic.org.serve.data.UserItemsResponse;


public class ProductLearnMoreFragment extends Fragment implements View.OnClickListener{

    private TextView txt_title;

    private ImageView likeGuide,dislikeGuide;


    private UserItemsResponse mUserItem;
    private ServiceRequest mServiceReq;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_learn_more, container, false);
        initWidgets(view);
        return view;
    }

    private void initWidgets(View view) {

        /*VideoView videoView = (VideoView)view.findViewById(R.id.videoView);
        videoView.setVideoURI(Uri.parse("http://0.s3.envato.com/h264-video-previews/80fad324-9db4-11e3-bf3d-0050569255a8/490527.mp4"));
        videoView.start();*/

        txt_title = (TextView)view.findViewById(R.id.txt_title);
        likeGuide = (ImageView) view.findViewById(R.id.img_like);
        dislikeGuide = (ImageView) view.findViewById(R.id.img_dislike);
        likeGuide.setBackgroundResource(R.drawable.icn__like);
        dislikeGuide.setBackgroundResource(R.drawable.icn__dislike);

        likeGuide.setOnClickListener(this);
        dislikeGuide.setOnClickListener(this);

        if(getArguments().getString(getString(R.string.activity_type))!=null)
        {
            txt_title.setText(getArguments().getString(getString(R.string.activity_type)));
        }
        else if(getArguments().getParcelable(getString(R.string.user_item)) != null)
        {
            mUserItem = getArguments().getParcelable(getString(R.string.user_item));
            mServiceReq = getArguments().getParcelable(getString(R.string.activity_service_req));
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id)
        {
            case R.id.img_like:
                likeGuide.setBackgroundResource(R.drawable.icn__like_active);
                dislikeGuide.setBackgroundResource(R.drawable.icn__dislike);
                ClicUtils.createDialogWithTwoButtons(getActivity(),R.layout.dialog_with_two_buttons, ClicConstants.DIALOG_TYPE_LEARNMORE_LIKE,likeGuide);
                break;
            case R.id.img_dislike:
                dislikeGuide.setBackgroundResource(R.drawable.icn__dislike_active);
                likeGuide.setBackgroundResource(R.drawable.icn__like);
                ClicUtils.createDialogWithTwoButtons(getActivity(),R.layout.dialog_with_two_buttons, ClicConstants.DIALOG_TYPE_LEARNMORE_DISLIKE,dislikeGuide);
                break;

        }
    }
}
