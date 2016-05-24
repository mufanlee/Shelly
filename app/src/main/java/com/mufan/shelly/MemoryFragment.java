package com.mufan.shelly;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;


/**
 * Fragment for showing the memory consumption of floodlight Controller.
 */
public class MemoryFragment extends Fragment {

    private static final String ARG_TOTAL = "total";
    private static final String ARG_USED = "used";

    // TODO: Rename and change types of parameters
    private long total;
    private long used;

    private OnFragmentInteractionListener mListener;

    public MemoryFragment() {
    }

    public static MemoryFragment newInstance(long total, long used) {
        MemoryFragment fragment = new MemoryFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TOTAL, total);
        args.putLong(ARG_USED, used);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            total = getArguments().getLong(ARG_TOTAL);
            used = getArguments().getLong(ARG_USED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_memory, container, false);
        PieChartView pieChartView = (PieChartView) view.findViewById(R.id.memorychart);
        pieChartView.setOnValueTouchListener(new ValueTouchListener());

        List<SliceValue> values = new ArrayList<>();
        SliceValue usedValue = new SliceValue((float)used, ChartUtils.pickColor());
        usedValue.setLabel("Used");
        values.add(usedValue);
        SliceValue freeValue = new SliceValue((float)total-used, ChartUtils.nextColor());
        freeValue.setLabel("Free");
        values.add(freeValue);

        PieChartData pieChartData = new PieChartData(values);
        pieChartData.setHasLabels(true);
        pieChartData.setHasLabelsOnlyForSelected(false);
        pieChartData.setHasLabelsOutside(false);
        pieChartData.setHasCenterCircle(true);
        pieChartData.setSlicesSpacing(20);
        pieChartData.setCenterText1("Memory");
        pieChartData.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.center_text_size)));
        pieChartData.setCenterText2("Consumption");
        pieChartData.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.center_text_size)));
        pieChartView.setPieChartData(pieChartData);
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

    private class ValueTouchListener implements PieChartOnValueSelectListener {
        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            Toast.makeText(getActivity(), String.valueOf(value.getLabelAsChars()) + ": " + value.getValue(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }
    }
}
