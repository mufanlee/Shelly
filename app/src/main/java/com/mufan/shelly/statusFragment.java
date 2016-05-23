package com.mufan.shelly;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Fragment for showing the status of floodlight Controller.
 */
public class StatusFragment extends Fragment {

    private static final String ARG_HOSTNAME = "hostname";
    private static final String ARG_STATUS = "status";
    private static final String ARG_ROLE = "role";
    private static final String ARG_UPTIME = "uptime";

    private String hostname;
    private String status;
    private String role;
    private String uptime;

    private OnFragmentInteractionListener mListener;

    public StatusFragment() {
    }


    public static StatusFragment newInstance(String hostname, String status, String role, String uptime) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HOSTNAME, hostname);
        args.putString(ARG_STATUS, status);
        args.putString(ARG_ROLE, role);
        args.putString(ARG_UPTIME, uptime);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hostname = getArguments().getString(ARG_HOSTNAME);
            status = getArguments().getString(ARG_STATUS);
            role = getArguments().getString(ARG_ROLE);
            uptime = getArguments().getString(ARG_UPTIME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_status, container, false);

        TextView hostname = (TextView) view.findViewById(R.id.hostname);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView role = (TextView) view.findViewById(R.id.role);
        TextView uptime = (TextView) view.findViewById(R.id.uptime);
        if (hostname != null) {
            hostname.setText(this.hostname);
        }
        if (status != null) {
            status.setText(this.status);
        }
        if (role != null) {
            role.setText(this.role);
        }
        if (uptime != null) {
            uptime.setText(this.uptime);
        }
        return view;
    }

//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
