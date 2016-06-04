package com.mufan.shelly;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mufan.jsons.StatisticsCollector;
import com.mufan.models.OFPort;
import com.mufan.models.Port;
import com.mufan.models.Switch;
import com.mufan.models.SwitchPortBandwidth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;


/**
 * A fragment for Statics.
 */
public class StatisticFragment extends Fragment {

    private static FloodlightProvider floodlightProvider = FloodlightProvider.getSingleton();
    private static final String TAG = "StatisticFragment";

    //line chart
    private LineChartView chartView;
    private LineChartData chartData;

    //Parameter for setting the chart
    private static final int numberOfPoints = 15;
    private static final boolean hasLines = true;
    private static final boolean hasPoints = true;
    private static final ValueShape shape = ValueShape.CIRCLE;
    private static final boolean isFilled = false;
    private static final boolean hasLabels = true;
    private static final boolean isCubic = false;
    private static final boolean hasLabelForSelected = false;

    Axis axisX = new Axis();
    Axis axisY = new Axis().setHasLines(true);
    private static int X = 0;
    private final double UNIT = 0.001;

    private List<Line> lines = new ArrayList<>();
    private List<PointValue> rxPoint = new ArrayList<>();
    private List<PointValue> txPoint = new ArrayList<>();

    Spinner swSpinner;
    Spinner portSpinner;
    ArrayAdapter<String> swAdapter;
    ArrayAdapter<String> portAdapter;
    List<String> sw_list = new ArrayList<>();
    List<String> port_list = new ArrayList<>();

    private String sw = "";
    private int port = -1;
    private Map<String, Switch> switchesMap = new HashMap<>();

    //enable Statistics of floodlight
    private boolean enable = false;

    private OnFragmentInteractionListener mListener;

    //Timer
    private static final int TIME = 5000;
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (enable && !sw.equals("") && port != -1) {
                try {
                    SwitchPortBandwidth bandwidth = StatisticsCollector.getSwitchPortBandwidth(sw, OFPort.of(port));
                    if (bandwidth != null) {
                        if(rxPoint.size() == numberOfPoints) {
                            rxPoint.remove(0);
                            txPoint.remove(0);
                        }
                        rxPoint.add(new PointValue((X++)*5, (float)(bandwidth.getBitsPerSecondRx()*UNIT)));
                        txPoint.add(new PointValue((X++)*5, (float) (bandwidth.getBitsPerSecondTx()*UNIT)));
                        Line rxLine = new Line(rxPoint);
                        Line txLine = new Line(txPoint);
                        rxLine.setColor(ChartUtils.COLOR_RED);
                        txLine.setColor(ChartUtils.COLOR_GREEN);
                        rxLine.setShape(shape);
                        txLine.setShape(shape);
                        rxLine.setCubic(isCubic);
                        txLine.setCubic(isCubic);
                        rxLine.setFilled(isFilled);
                        txLine.setFilled(isFilled);
                        rxLine.setHasLabels(hasLabels);
                        txLine.setHasLabels(hasLabels);
                        rxLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
                        txLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
                        rxLine.setHasLines(hasLines);
                        txLine.setHasLines(hasLines);
                        rxLine.setHasPoints(hasPoints);
                        txLine.setHasPoints(hasPoints);
                        lines.add(rxLine);
                        lines.add(txLine);
                        chartData = new LineChartData(lines);
                        axisX.setName("Time/s");
                        axisY.setName("Bandwidth/kbps");
                        chartData.setAxisXBottom(axisX);
                        chartData.setAxisYLeft(axisY);
                        chartView.setLineChartData(chartData);
                    }
                } catch (IOException e) {
                    Log.e(TAG, "onCreateView: Get Switch Bandwidth failed: " + e.getMessage());
                }
            }
        }
    };

    public StatisticFragment() {
    }

    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistic, container, false);

        if (!enable) {
            try {
                enable = StatisticsCollector.enableStatistics();
                if (!enable) {
                    Log.e(TAG, "onCreate: Enable Statistics failed.");
                }
            } catch (IOException e) {
                Log.e(TAG, "onCreate: " + e.getMessage());
            }
        }

        swSpinner = (Spinner) view.findViewById(R.id.sw_spinner);
        portSpinner = (Spinner) view.findViewById(R.id.port_spinner);

        List<Switch> switches = floodlightProvider.getSwitches(false);
        if (switches == null || switches.size() < 1) {
            switches = floodlightProvider.getSwitches(true);
        }
        for (int i = 0; i < switches.size(); i++) {
            sw_list.add(switches.get(i).getDpid());
            switchesMap.put(switches.get(i).getDpid(), switches.get(i));
        }
        swAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sw_list);
        swAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        swSpinner.setAdapter(swAdapter);
        swSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                sw = spinner.getSelectedItem().toString();
                Log.i(TAG, "onItemSelected: " + sw);

                if (!sw.equals("")) {
                    if (switchesMap.containsKey(sw)) {
                        List<Port> ports = switchesMap.get(sw).getPorts();
                        Log.i(TAG, "onItemSelected: " + ports.size());
                        port_list.clear();
                        if (ports.size() > 0) {
                            for (int i = 0; i < ports.size(); i++)
                                port_list.add(String.valueOf(ports.get(i).getPortNo()));
                        }
                    }
                }

                portAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, port_list);
                portAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                portSpinner.setAdapter(portAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        portSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Spinner spinner = (Spinner) parent;
                port = Integer.valueOf(spinner.getSelectedItem().toString());
                Log.i(TAG, "onItemSelected: " + port);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        timer.schedule(task, TIME, TIME);

        chartView = (LineChartView) view.findViewById(R.id.statistics_chart);
        chartView.setOnValueTouchListener(new ValueTouchListener());
        chartData = new LineChartData(lines);
        axisX.setName("Time/s");
        axisY.setName("Bandwidth/kbps");
        axisX.setLineColor(ChartUtils.COLOR_BLUE);
        axisX.setTextColor(ChartUtils.COLOR_BLUE);
        axisY.setLineColor(ChartUtils.COLOR_BLUE);
        axisY.setTextColor(ChartUtils.COLOR_BLUE);
        chartData.setAxisXBottom(axisX);
        chartData.setAxisYLeft(axisY);
        chartView.setLineChartData(chartData);
        return view;
    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    private class ValueTouchListener implements LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
            Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {

        }

    }

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
