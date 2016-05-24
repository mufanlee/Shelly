package com.mufan.shelly;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mufan.shelly.SwitchItemFragment.OnListFragmentInteractionListener;
import com.mufan.shelly.listItem.SwitchContent.SwitchItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link SwitchItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SwitchItemRecyclerViewAdapter extends RecyclerView.Adapter<SwitchItemRecyclerViewAdapter.ViewHolder> {

    private final List<SwitchItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    private static final String TAG = "SwitchItemRecyclerViewAdapter";
    public SwitchItemRecyclerViewAdapter(List<SwitchItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_switchitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).dpid);
        String content = mValues.get(position).mfr_desc + "\n"
                + mValues.get(position).hw_desc + "\n"
                + mValues.get(position).sw_desc;
                //+ mValues.get(position).serial_num + "\n"
                //+ mValues.get(position).dp_desc;
        holder.mContentView.setText(content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public SwitchItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.dpid_text);
            mContentView = (TextView) view.findViewById(R.id.swtich_detail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
