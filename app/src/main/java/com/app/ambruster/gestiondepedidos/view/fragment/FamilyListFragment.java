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
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.ClientServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.FamilyServiceTask;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectAdapter;
import com.app.ambruster.gestiondepedidos.view.view.CellFamily;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class FamilyListFragment extends CoreFragment implements AdapterView.OnItemClickListener,
        ObjectAdapter.ObjectAdapterLoadMore,
        FamilyServiceTask.FamilyServiceTaskCallBack {

    public static final String USER = "user";
    public static final String FAMILY = "family";
    public static final String REQUEST = "request";
    public static final String PARTOBRA = "partobra";
    public static final String PARTOBRAOPER = "oper";

    public static boolean loadmoreFAM = true;
    public String searchtext = "";
    public adUser us;
    public adFamily fm;
    public adRequest rq;
    public adPartObra po;
    public Boolean isOper = false;

    public List<adFamily> famlist = new ArrayList<>();
    public int page = 1;
    public ObjectAdapter<adFamily> adapterFAM;
    public AbsListView list;
    private boolean error;

    public FamilyListFragment() {

    }

    public static FamilyListFragment newInstance(adUser user) {
        FamilyListFragment fragment = new FamilyListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        fragment.setArguments(args);
        return fragment;
    }

    public static FamilyListFragment newInstance(adUser user, adRequest request) {
        FamilyListFragment fragment = new FamilyListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(REQUEST, gSon.toJson(request));
        fragment.setArguments(args);
        return fragment;
    }

    public static ClientListFragment newInstance(adUser user, adFamily family, adRequest request) {
        ClientListFragment fragment = new ClientListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(FAMILY, gSon.toJson(family));
        args.putString(REQUEST, gSon.toJson(request));
        fragment.setArguments(args);
        return fragment;
    }

    public static FamilyListFragment newInstance(adUser user, adPartObra parto, Boolean oper) {
        FamilyListFragment fragment = new FamilyListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(PARTOBRA, gSon.toJson(parto));
        args.putString(PARTOBRAOPER, gSon.toJson(oper));
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
        page = 1;
        isOper = false;

        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(USER), new TypeToken<adUser>() {
                }.getType());
            }

            if (bundle.containsKey(FAMILY)) {
                Gson gSon = new Gson();
                fm = gSon.fromJson(bundle.getString(FAMILY), new TypeToken<adFamily>() {
                }.getType());
            }

            if (bundle.containsKey(REQUEST)) {
                Gson gSon = new Gson();
                rq = gSon.fromJson(bundle.getString(REQUEST), new TypeToken<adRequest>() {
                }.getType());
            }

            if (bundle.containsKey(PARTOBRA)) {
                Gson gSon = new Gson();
                po = gSon.fromJson(bundle.getString(PARTOBRA), new TypeToken<adPartObra>() {
                }.getType());
            }

            if (bundle.containsKey(PARTOBRAOPER)) {
                Gson gSon = new Gson();
                isOper = gSon.fromJson(bundle.getString(PARTOBRAOPER), new TypeToken<Boolean>() {
                }.getType());
            }
        }

        Load();
        Fill();
        Add();

        loadmoreFAM = true;

        if (isOper) {
            adFamily fmy = new adFamily();
            fmy.setName("Mano de Obra");
            FragmentTransaction ft = super.getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, ArticleListFragment.newInstance(us, fmy, po, isOper));
            ft.commit();
        } else {
            Execute("Cargando");
        }

        return view;
    }

    public void Execute(String Message) {
        Empty_Error(false);
        Empty(false, "");
        error = false;
        super.Run(true, Message);
        new FamilyServiceTask(this).CallService(us, page, searchtext);
        getActivity().invalidateOptionsMenu();
    }

    public void search(String text) {
        text = Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");

        text = text.toUpperCase();

        if (text != null && !text.isEmpty()) {
            page = 1;
            loadmoreFAM = true;
            searchtext = text;
            getActivity().setTitle(text);
            FamilyListFragment.this.famlist.clear();
            Execute("Buscando");
        }
    }

    @Override
    protected void Load() {
        View v = View.inflate(getActivity(), R.layout.list, null);
        list = (AbsListView) v.findViewById(R.id.list);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.fragmen_family));

        if (rq != null || po != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.fragmen_family));
        }

        ((ViewGroup) fragmentView).addView(v);
    }

    @Override
    protected void Fill() {

        adapterFAM = new ObjectAdapter<>(getActivity(), famlist, R.layout.cell_articles, CellFamily.class);
        list.setAdapter(adapterFAM);
        adapterFAM.notifyDataSetChanged();
        adapterFAM.listener = this;

    }

    @Override
    protected void Add() {
        list.setOnItemClickListener(this);
    }

    @Override
    public void loadMore() {
        page++;
        if (loadmoreFAM) {
            Execute("Cargando");
        }
    }

    @Override
    public void onFamilyServiceTaskCallBack(ArrayList<adFamily> families) {

        if (families.isEmpty() && this.famlist.isEmpty()) {
            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Empty(true, "");
                }
            }, 200);
        } else {
            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Empty(false, "");
                }
            }, 200);
        }

        Run(false, "");
        FamilyListFragment.this.famlist.addAll(families);
        adapterFAM.notifyDataSetChanged();
    }

    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adFamily family = adapterFAM.getItem(i);
        fm = null;
        FragmentTransaction ft = super.getFragmentManager().beginTransaction();

        if (rq != null)
            ft.replace(R.id.frame_container, ArticleListFragment.newInstance(us, family, rq));
        else if (po != null)
            ft.replace(R.id.frame_container, ArticleListFragment.newInstance(us, family, po, isOper));
        else
            ft.replace(R.id.frame_container, ArticleListFragment.newInstance(us, family));

        ft.commit();

    }

    @Override
    public void onError(String message) {
        error = true;
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
            theTextArea.setHint("Familia");

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


            if (rq != null) {
                inflater.inflate(R.menu.back_list_menu, menu);
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.back:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
                GPApplication.getInstance().cancelPendingRequests("get_family");
                FragmentTransaction ft = super.getFragmentManager().beginTransaction();

                if (rq != null) {
                    ft.replace(R.id.frame_container, RequestArticleAmountListFragment.newInstance(us, rq));
                    ft.commit();
                } else if (po != null) {
                    ft.replace(R.id.frame_container, PartFragment.newInstance(us, po));
                    ft.commit();
                }
                return true;

            case R.id.reload:
                Execute("Cargando");
                return true;
            default:
                GPApplication.getInstance().cancelPendingRequests("get_family");
                return super.onOptionsItemSelected(item);
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


}