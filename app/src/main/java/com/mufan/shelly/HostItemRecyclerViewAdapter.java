package com.mufan.shelly;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mufan.shelly.HostItemFragment.OnListFragmentInteractionListener;
import com.mufan.shelly.listItem.HostContent.HostItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link HostItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class HostItemRecyclerViewAdapter extends RecyclerView.Adapter<HostItemRecyclerViewAdapter.ViewHolder> {

    private final List<HostItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public HostItemRecyclerViewAdapter(List<HostItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_hostitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).mac_addr);
        String content = mValues.get(position).ipv4_addr + "\n"
                + mValues.get(position).attach_sw + "\n"
                + mValues.get(position).attach_port;
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
        public HostItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.mac_text);
            mContentView = (TextView) view.findViewById(R.id.host_detail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
