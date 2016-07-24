package com.clic.org.serve.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.clic.org.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductLearnMoreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductLearnMoreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductLearnMoreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView txt_title;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductLearnMoreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductLearnMoreFragment newInstance(String param1, String param2) {
        ProductLearnMoreFragment fragment = new ProductLearnMoreFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProductLearnMoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

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

        if(getArguments().getString(getString(R.string.activity_type))!=null)
        {
            txt_title.setText(getArguments().getString(getString(R.string.activity_type)));
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


}
