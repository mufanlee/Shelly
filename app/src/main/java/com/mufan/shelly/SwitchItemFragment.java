package com.mufan.shelly;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mufan.jsons.JsonToSwitches;
import com.mufan.models.Switch;
import com.mufan.shelly.listItem.SwitchContent;

import java.io.IOException;
import java.util.List;

/**
 * A fragment representing a list of Swtich Items.
 */
public class SwitchItemFragment extends Fragment{

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;


    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static final String TAG = "SwitchItemFragment";
    public SwitchItemFragment() {
    }

    public static SwitchItemFragment newInstance(int columnCount) {
        SwitchItemFragment fragment = new SwitchItemFragment();
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

        try {
            JsonToSwitches.getSwitches();
        } catch (IOException e) {
            Log.e(TAG, "onCreate: failed to get switch information" + e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_switchitem_list, container, false);

        List<Switch> switches = floodlightProvider.getSwitches(true);
        if (switches != null) {
            for (int i = 0; i < switches.size(); i++) {
                SwitchContent.ITEMS.clear();
                SwitchContent.ITEM_MAP.clear();
                SwitchContent.SwitchItem switchItem = new SwitchContent.SwitchItem(
                        switches.get(i).getDpid(),switches.get(i).getMfr_desc(),switches.get(i).getHw_desc()
                        ,switches.get(i).getSw_desc(),switches.get(i).getSerial_num(),switches.get(i).getDp_desc());
                SwitchContent.addItem(switchItem);
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
            recyclerView.setAdapter(new SwitchItemRecyclerViewAdapter(SwitchContent.ITEMS, mListener));
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SwitchContent.SwitchItem item);
    }
}
