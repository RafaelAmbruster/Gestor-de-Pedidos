package com.app.ambruster.gestiondepedidos.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import java.util.List;


public class RequestPairAdapter extends ArrayAdapter<UtilTextValuePair> {

    private LayoutInflater mInflater;
    private List<adRequest> req;

    public RequestPairAdapter(Context context, int textViewResourceId,
                              List<adRequest> requests) {
        super(context, textViewResourceId);
        mInflater = LayoutInflater.from(context);
        req = requests;

        try {
            for (adRequest req : requests) {
                super.add(new UtilTextValuePair(req.getClient().getCliname(), req
                        .getId()));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        final ListContent holder;
        View v = convertView;
        if (v == null) {
            v = mInflater.inflate(R.layout.spinner_item, null);
            holder = new ListContent();
            holder.value_spinner = (TextView) v
                    .findViewById(R.id.spinner_item);
            v.setTag(holder);
        } else {
            holder = (ListContent) v.getTag();
        }

        holder.value_spinner.setText(req.get(position).getId() + " | " + req.get(position).getClient().getCliname());
        return v;
    }


    public static class ListContent {
        public TextView value_spinner;
    }
}
