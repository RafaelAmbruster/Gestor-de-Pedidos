package com.app.ambruster.gestiondepedidos.view.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adObra;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.ObrasServiceTask;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectAdapter;
import com.app.ambruster.gestiondepedidos.view.view.CellObras;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ObrasListFragment extends CoreFragment implements AdapterView.OnItemClickListener,
        ObjectAdapter.ObjectAdapterLoadMore,
        ObrasServiceTask.ObrasServiceTaskCallBack{

    public static final String USER = "user";
    public static final String PARTOBRA = "partobra";

    public static adUser us;
    public static adPartObra po;
    public static adClient client;
    public static String searchtext = "";
    public static boolean loadmore = true;
    public List<adObra> obraslist = new ArrayList<>();
    public ObjectAdapter<adObra> adapter;
    public AbsListView list;
    public int page = 1;
    private boolean error;

    public ObrasListFragment() {
    }

    public static ObrasListFragment newInstance(adUser user) {
        ObrasListFragment fragment = new ObrasListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        fragment.setArguments(args);
        return fragment;
    }

    public static ObrasListFragment newInstance(adUser user, adPartObra parto) {
        ObrasListFragment fragment = new ObrasListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(PARTOBRA, gSon.toJson(parto));
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
        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(USER), new TypeToken<adUser>() {
                }.getType());
            }

            if (bundle.containsKey(PARTOBRA)) {
                Gson gSon = new Gson();
                po = gSon.fromJson(bundle.getString(PARTOBRA), new TypeToken<adPartObra>() {
                }.getType());
            }
        }

        Load();
        Fill();
        Add();
        searchtext = "";
        loadmore = true;
        Execute("Cargando");

        if (po != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.choose_obra));
        else
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.obras));

        return view;
    }

    @Override
    protected void Load() {
        View v = View.inflate(getActivity(), R.layout.list, null);
        list = (AbsListView) v.findViewById(R.id.list);
        ((ViewGroup) fragmentView).addView(v);
    }

    @Override
    protected void Fill() {
        adapter = new ObjectAdapter<>(getActivity(), obraslist, R.layout.cell_obras, CellObras.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        adapter.listener = this;
    }

    @Override
    protected void Add() {
        list.setOnItemClickListener(this);
    }

    @Override
    public void loadMore() {
        page++;
        Log.d("LOAD MORE TIME ", page + "");

        if (loadmore)
            Execute("Cargando");

    }

    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (po != null) {
            po.setObra(adapter.getItem(i));
            FragmentTransaction ft = super.getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, PartFragment.newInstance(us, po));
            ft.commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void Execute(String Message) {
        Empty_Error(false);
        Empty(false, "");
        super.Run(true, Message);
        error = false;
        new ObrasServiceTask(this).CallService(us, page, po.getClient().getClicode(), searchtext);
        getActivity().invalidateOptionsMenu();
    }

    public void search(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        text = text.toUpperCase();

        if (text != null && !text.isEmpty()) {
            page = 1;
            loadmore = true;
            searchtext = text;
            getActivity().setTitle(text);
            ObrasListFragment.this.obraslist.clear();
            Execute("Buscando");
        }
    }

    @Override
    public void onObrasServiceTaskCallBack(ArrayList<adObra> obras) {
        if (obras.isEmpty() && this.obraslist.isEmpty()) {
            Run(false, "");
            Empty(true, "");
        } else {
            Run(false, "");
            Empty(false, "");
        }

        ObrasListFragment.this.obraslist.addAll(obras);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        error = true;
        page = 1;
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

        if (error)
            inflater.inflate(R.menu.reload_list_menu, menu);
        else {

            inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.search_list_menu, menu);
            final MenuItem searchItem = menu.findItem(R.id.search);
            final SearchView searchView = (SearchView) searchItem.getActionView();
            SearchView.SearchAutoComplete theTextArea = (SearchView.SearchAutoComplete) searchView.findViewById(R.id.search_src_text);
            theTextArea.setTextColor(Color.WHITE);
            theTextArea.setHintTextColor(getResources().getColor(R.color.white55));
            theTextArea.setHint("Obras");

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String text) {
                    return false;
                }

                public boolean onQueryTextSubmit(String text) {
                    search(text);
                    searchItem.collapseActionView();
                    return false;
                }
            });

            if (po == null) {
            } else {
                inflater.inflate(R.menu.back_list_menu, menu);
            }

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.reload:
                Execute("Cargando");
                return true;

            case R.id.back:
                goBack();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void goBack() {
        if (getFragmentManager().beginTransaction() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, PartFragment.newInstance(us, po));
            ft.commit();
        }
    }
}