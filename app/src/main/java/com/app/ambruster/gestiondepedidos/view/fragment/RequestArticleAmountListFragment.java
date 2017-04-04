package com.app.ambruster.gestiondepedidos.view.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.CreateRequestServiceTask;
import com.app.ambruster.gestiondepedidos.util.CustomDateFormat;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectHeaderAdapter;
import com.app.ambruster.gestiondepedidos.view.view.CellAmountArticle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;

public class RequestArticleAmountListFragment extends CoreFragment
        implements AdapterView.OnItemClickListener,
        CreateRequestServiceTask.CreateRequestServiceTaskCallBack {

    public static final String USER = "user";
    public static final String REQUEST = "request";
    public static final String VISIBILITY = "visibility";

    public adUser us;
    public adRequest rq;
    public adArticleAmount am;
    public int vs;

    public ObjectHeaderAdapter<Object> adapter;
    public AbsListView list;
    public ArrayList<Object> listobjects = new ArrayList<>();
    public View remove, save, send, add;
    public TextView user, date;
    public LinearLayout widget;

    public int status = 0;

    public static RequestArticleAmountListFragment newInstance(adUser user, adRequest request) {
        RequestArticleAmountListFragment fragment = new RequestArticleAmountListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(REQUEST, gSon.toJson(request));
        fragment.setArguments(args);
        return fragment;
    }

    public static RequestArticleAmountListFragment newInstance(adUser user, adRequest request, int showwidget) {
        RequestArticleAmountListFragment fragment = new RequestArticleAmountListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(REQUEST, gSon.toJson(request));
        args.putString(VISIBILITY, gSon.toJson(showwidget));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list,
                container, false);
        setFragmentView(view);
        setHasOptionsMenu(true);

        vs = 1;

        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(USER), new TypeToken<adUser>() {
                }.getType());
            }

            if (bundle.containsKey(REQUEST)) {
                Gson gSon = new Gson();
                rq = gSon.fromJson(bundle.getString(REQUEST), new TypeToken<adRequest>() {
                }.getType());
            }

            if (bundle.containsKey(VISIBILITY)) {
                Gson gSon = new Gson();
                vs = gSon.fromJson(bundle.getString(VISIBILITY), new TypeToken<Integer>() {
                }.getType());
            }
        }

        status = 0;
        Load();
        Fill();
        Add();

        return view;
    }

    private void SendRequest() {
        if (rq.getAmountlist().size() > 0) {
            status = 2;
            Run(true, getActivity().getResources().getString(R.string.send_loading));
            new CreateRequestServiceTask(this).CallService(us, rq, 2);
        } else {
            Toast.makeText(getActivity(), R.string.almost_one, Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveRequest() {
        if (rq.getAmountlist().size() > 0) {
            status = 1;
            Run(true, getActivity().getResources().getString(R.string.save_loading));
            new CreateRequestServiceTask(this).CallService(us, rq, 1);
        } else {
            Toast.makeText(getActivity(), R.string.almost_one, Toast.LENGTH_SHORT).show();
        }
    }

    private void DeleteArticle() {
        if (rq.getAmountlist().size() > 0) {
            if (am != null) {
                rq.getAmountlist().remove(am);
                fillArticleList();
            } else {
                Toast.makeText(getActivity(), R.string.choose_art, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void AddArticle() {
        if (getFragmentManager().beginTransaction() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, FamilyListFragment.newInstance(us, rq));
            ft.commit();
        }
    }

    @Override
    protected void Load() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.situation));
        View v = View.inflate(getActivity(), R.layout.list_request_article_amount, null);
        list = (AbsListView) v.findViewById(R.id.list);
        user = (TextView) v.findViewById(R.id.Name);
        date = (TextView) v.findViewById(R.id.Date);
        widget = (LinearLayout) v.findViewById(R.id.widget_options);

        if (vs == 1)
            widget.setVisibility(View.VISIBLE);
        else
            widget.setVisibility(View.GONE);

        send = v.findViewById(R.id.send);
        send.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SendRequest();
                    }
                });
        remove = v.findViewById(R.id.remove);
        remove.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DeleteArticle();
                    }
                });
        save = v.findViewById(R.id.save);
        save.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SaveRequest();
                    }
                });
        add = v.findViewById(R.id.add);
        add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AddArticle();
                    }
                });

        ((ViewGroup) fragmentView).addView(v);
    }

    private void fillArticleList() {
        ArrayList<Object> objects = new ArrayList<>();

        if (rq.getAmountlist() != null) {
            if (rq.getAmountlist().isEmpty()) {

            } else {

                for (adArticleAmount amount : rq.getAmountlist()) {
                    objects.add(amount);
                }

                new View(getActivity()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Empty(false, "");
                    }
                },200);
            }
        }

        RequestArticleAmountListFragment.this.listobjects.clear();
        RequestArticleAmountListFragment.this.listobjects.addAll(objects);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void Fill() {
        adapter = new ObjectHeaderAdapter<>(getActivity(), listobjects, R.layout.cell_amount, CellAmountArticle.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (rq != null)
            user.setText(rq.getClient().getCliname());

        if (rq.getAmountlist() != null) {
            if (rq.getAmountlist().size() > 0) {
                date.setText(CustomDateFormat.getCurrentTimeYMD(rq.getRequestdate()));
            } else {
                date.setText(CustomDateFormat.getCurrentTimeWOhour(new Date()));
            }
        } else
            date.setText(CustomDateFormat.getCurrentTimeWOhour(new Date()));

        fillArticleList();
    }

    @Override
    protected void Add() {
        list.setOnItemClickListener(this);
    }

    @Override
    public void onError(String message) {
        Run(false, "");
        switch (status) {
            case 1:
                Toast.makeText(getActivity(), R.string.dont_save_request, Toast.LENGTH_LONG).show();
                break;

            case 2:
                Toast.makeText(getActivity(), R.string.dont_send_request, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onCreateRequestServiceTaskCallBack(Boolean response, int id) {
        Run(false, "");
        if (response) {
            switch (status) {

                case 1:
                    if (getFragmentManager().beginTransaction() != null) {
                        rq = null;
                        Toast.makeText(getActivity(), R.string.save_secesfully, Toast.LENGTH_SHORT).show();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_container, RequestListFragment.newInstance(us));
                        ft.commit();

                    }
                    break;

                case 2:
                    if (getFragmentManager().beginTransaction() != null) {
                        rq = null;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        Toast.makeText(getActivity(), R.string.send_succesfully, Toast.LENGTH_SHORT).show();
                        ft.replace(R.id.frame_container, RequestListFragment.newInstance(us));
                        ft.commit();
                    }
                    break;
            }
        } else
            Toast.makeText(getActivity(), R.string.dont_send_request, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.back_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.back:
                GPApplication.getInstance().cancelPendingRequests("cre_request");
                goBack();
                return true;

            default:
                GPApplication.getInstance().cancelPendingRequests("cre_request");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

    }

    public void goBack() {
        if (getFragmentManager().beginTransaction() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
            rq = null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, RequestListFragment.newInstance(us));
            ft.commit();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        am = (adArticleAmount) adapter.getItem(i);
    }


}