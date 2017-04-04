package com.app.ambruster.gestiondepedidos.view.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.dao.MessageDAO;
import com.app.ambruster.gestiondepedidos.data.dao.task.MessageDAOTask;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.data.model.adMessage;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.ArticleServiceTask;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectAdapter;
import com.app.ambruster.gestiondepedidos.view.view.CellMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MessageListFragment extends CoreFragment implements AdapterView.OnItemClickListener,
        MessageDAOTask.MessageDAOTaskCallBack {

    public static final String USER = "user";
    public static final String REQUEST = "request";

    public static adUser us;
    public static adRequest rq;

    public List<adMessage> messagelist = new ArrayList<>();
    public AbsListView list;
    public ObjectAdapter<adMessage> adapter;
    private boolean error;



    public static MessageListFragment newInstance(adUser user, adRequest req) {
        MessageListFragment fragment = new MessageListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(REQUEST, gSon.toJson(req));
        fragment.setArguments(args);
        return fragment;
    }



    public static MessageListFragment newInstance(adUser user) {
        MessageListFragment fragment = new MessageListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
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

        error = false;
        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(USER), new TypeToken<adUser>() {
                }.getType());
            }

            if (bundle.containsKey(REQUEST)) {
                Gson gSon = new Gson();
                rq = gSon.fromJson(bundle.getString(REQUEST), new TypeToken<adFamily>() {
                }.getType());
            }
        }

        Load();
        Fill();
        Add();

        Execute();
        return view;
    }


    public void Execute() {
        Empty_Error(false);
        Empty(false, "");
        super.Run(true, "Cargando");
        if (rq == null) {
            new MessageDAOTask(this).Execute(us);
        }
        getActivity().invalidateOptionsMenu();
    }

    @Override
    protected void Load() {
        View v = View.inflate(getActivity(), R.layout.list, null);
        list = (AbsListView) v.findViewById(R.id.list);
        ((ViewGroup) fragmentView).addView(v);
    }

    @Override
    protected void Fill() {
        adapter = new ObjectAdapter<>(getActivity(), messagelist, R.layout.cell_message, CellMessage.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void Add() {
        list.setOnItemClickListener(this);
    }

    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        adMessage message = adapter.getItem(i);
        FragmentTransaction ft = super.getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, MessageFragment.newInstance(us, message));
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onMessageDAOTaskCallBack(final ArrayList<adMessage> messages) {
        if (messages.isEmpty() && this.messagelist.isEmpty()) {
            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Run(false,"");
                    Empty(true, getActivity().getString(R.string.not_stored_messages));
                    MessageListFragment.this.messagelist.addAll(messages);
                    adapter.notifyDataSetChanged();
                }
            }, 200);
        } else {
            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Run(false, "");
                    Empty(false,"");
                    MessageListFragment.this.messagelist.addAll(messages);
                    adapter.notifyDataSetChanged();
                }
            }, 200);
        }
    }

    @Override
    public void onError(String message) {
        error = true;
        Log.e(getActivity().getClass().getName(), message);
        getActivity().invalidateOptionsMenu();
        new View(getActivity()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Run(false, "");
                Empty_Error(true);
            }
        }, 200);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();

        if (error)
            inflater.inflate(R.menu.reload_list_menu, menu);
        else
            inflater.inflate(R.menu.add_message_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.reload:
                Execute();
                error = false;
                getActivity().invalidateOptionsMenu();
                return true;

            case R.id.add_message:
                FragmentTransaction ft = super.getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_container, MessageFragment.newInstance(us));
                ft.commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}