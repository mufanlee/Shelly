package com.mufan.shelly;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mufan.models.Device;
import com.mufan.shelly.listItem.HostContent;
import com.mufan.shelly.listItem.HostContent.HostItem;

import java.util.List;

/**
 * A fragment representing a list of host Items.
 */
public class HostItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static final String TAG = "HostItemFragment";
    public HostItemFragment() {
    }

    public static HostItemFragment newInstance(int columnCount) {
        HostItemFragment fragment = new HostItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hostitem_list, container, false);

        List<Device> devices = floodlightProvider.getDevices(false);
        if (devices == null) {
            devices = floodlightProvider.getDevices(true);
        }
        if (devices != null) {
            for (int i = 0; i < devices.size(); i++) {
                if (!HostContent.ITEM_MAP.containsKey(devices.get(i).getMac_addr())) {
                    HostItem hostItem = new HostItem(devices.get(i).getMac_addr(),
                            devices.get(i).getIpv4_addr(), devices.get(i).getVlan(),
                            devices.get(i).getAttachmentPoint().getSwitchDPID(),
                            devices.get(i).getAttachmentPoint().getPort(), devices.get(i).getLastSeen());
                    HostContent.addItem(hostItem);
                }
            }
        }
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new HostItemRecyclerViewAdapter(HostContent.ITEMS, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(HostItem item);
    }
}
