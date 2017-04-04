package com.app.ambruster.gestiondepedidos.view.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.ArticleServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.ClientServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.CommercialServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.OpersServiceTask;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.view.adapter.CommercialPairAdapter;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectAdapter;
import com.app.ambruster.gestiondepedidos.view.adapter.RequestPairAdapter;
import com.app.ambruster.gestiondepedidos.view.adapter.UtilTextValuePair;
import com.app.ambruster.gestiondepedidos.view.view.CellArticle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ArticleListFragment extends CoreFragment implements AdapterView.OnItemClickListener,
        ObjectAdapter.ObjectAdapterLoadMore,
        AdapterView.OnItemSelectedListener,
        ArticleServiceTask.ArticleServiceTaskCallBack, OpersServiceTask.OpersServiceTaskCallBack,
        CommercialServiceTask.CommercialServiceTaskCallBack {

    public static final String USER = "user";
    public static final String FAMILY = "family";
    public static final String REQUEST = "request";
    public static final String PARTOBRA = "partobra";
    public static final String PARTOBRAOPER = "oper";

    public String searchtext = "";
    public static boolean loadmore = true;
    public adUser us;
    public adFamily fm;
    public adRequest rq;
    public adPartObra po;
    public static Boolean isOper = false;

    public List<adArticle> artlist = new ArrayList<>();
    public int page = 1;
    public ObjectAdapter<adArticle> adapter;
    public AbsListView list;
    private boolean error;
    private int amountid = 1;
    private UtilTextValuePair commercialpair;
    private Spinner spinnercommercial;
    public List<adUser> commerciallist = new ArrayList<>();

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(adUser user, adFamily family) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(FAMILY, gSon.toJson(family));
        fragment.setArguments(args);
        return fragment;
    }

    public static ArticleListFragment newInstance(adUser user, adFamily family, adRequest request) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(FAMILY, gSon.toJson(family));
        args.putString(REQUEST, gSon.toJson(request));
        fragment.setArguments(args);
        return fragment;
    }

    public static ArticleListFragment newInstance(adUser user, adFamily family, adPartObra part, Boolean oper) {
        ArticleListFragment fragment = new ArticleListFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(FAMILY, gSon.toJson(family));
        args.putString(PARTOBRA, gSon.toJson(part));
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

        error = false;
        isOper = false;
        amountid = 0;
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

        searchtext = "";
        loadmore = true;

        Execute("Cargando");
        new CommercialServiceTask(this).CallService(us, 1, 1000);

        Load();
        Fill();
        Add();


        return view;
    }

    @Override
    protected void Load() {
        View v = View.inflate(getActivity(), R.layout.list, null);
        list = (AbsListView) v.findViewById(R.id.list);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Familia " + fm.getCode());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(fm.getName());

        if (rq != null || po != null) {

            if (!isOper)
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Familia " + fm.getCode());
            else
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Mano de obra ");

            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(getActivity().getString(R.string.choose_article));
        }

        ((ViewGroup) fragmentView).addView(v);
    }

    @Override
    protected void Fill() {
        adapter = new ObjectAdapter<>(getActivity(), artlist, R.layout.cell_articles, CellArticle.class);
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
        if (loadmore) {
            Execute("Cargando");
        }
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
            ArticleListFragment.this.artlist.clear();
            Execute("Buscando");
        }
    }

    public void Execute(String Message) {
        Empty_Error(false);
        Empty(false, "");
        super.Run(true, Message);
        error = false;

        if (!isOper)
            new ArticleServiceTask(this).CallService(us, page, fm.getId(), searchtext);
        else
            new OpersServiceTask(this).CallService(us, page, searchtext);

        getActivity().invalidateOptionsMenu();
    }

    @Override
    public void onArticleServiceTaskCallBack(ArrayList<adArticle> articles) {

        if (articles.isEmpty() && this.artlist.isEmpty()) {
            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Empty(true, getActivity().getString(R.string.empty_article_list));
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

        ArticleListFragment.this.artlist.addAll(articles);
        adapter.notifyDataSetChanged();
        super.Run(false, "");
    }

    @Override
    public void onOpersServiceTaskCallBack(ArrayList<adArticle> articles) {
        if (articles.isEmpty() && this.artlist.isEmpty()) {
            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Empty(true, getActivity().getString(R.string.empty_article_list));
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

        ArticleListFragment.this.artlist.addAll(articles);
        adapter.notifyDataSetChanged();
        super.Run(false, "");
    }


    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCommercialServiceTaskCallBack(ArrayList<adUser> commercials) {
        ArticleListFragment.this.commerciallist.addAll(commercials);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        final adArticle art = adapter.getItem(i);
        final adArticleAmount amount = new adArticleAmount();
        amount.setArticle(art);

        if (rq != null || po != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            LayoutInflater inflater = getActivity().getLayoutInflater();
            View titleview = inflater.inflate(R.layout.row_header_pending, null);
            builder.setCustomTitle(titleview);

            TextView titletext = (TextView) titleview.findViewById(R.id.HeaderRequest);
            titletext.setText(art.getArtname());

            View dialogView = inflater.inflate(R.layout.alert_label_editor, null);
            builder.setView(dialogView);

            spinnercommercial = (Spinner) dialogView.findViewById(R.id.clients);

            if (po == null) {
                spinnercommercial.setVisibility(View.GONE);
            } else {
                if (isOper) {
                    spinnercommercial.setVisibility(View.VISIBLE);
                    spinnercommercial.setOnItemSelectedListener(this);

                    if (commerciallist.size() > 0) {
                        CommercialPairAdapter spinnerAdapter = new CommercialPairAdapter(getActivity(),
                                android.R.layout.simple_spinner_dropdown_item, commerciallist);
                        spinnercommercial.setAdapter(spinnerAdapter);
                    }
                } else {
                    spinnercommercial.setVisibility(View.GONE);
                }
            }

            final EditText editText = (EditText) dialogView.findViewById(R.id.amount_text);


            builder.setPositiveButton(getActivity().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.setNegativeButton(getActivity().getString(R.string.denied), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            final AlertDialog dialog = builder.create();
            dialog.show();

            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String ed_text = editText.getText().toString().trim();

                    if (ed_text.isEmpty() || ed_text.length() == 0 || ed_text.equals("") || ed_text == null) {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.specify_amount), Toast.LENGTH_LONG).show();
                    } else {
                        int com = 0;

                        if (po == null) {
                            com = 0;
                        } else {
                            if (isOper) {
                                com = commercialpair.getItem();
                                LogManager.getInstance().info("Value of idcom", String.valueOf(com));
                            }
                        }

                        String cant =  editText.getText().toString();
                        saveArticleToList(amount,cant.replace(".",","), String.valueOf(com));
                        dialog.dismiss();
                    }

                }
            });

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });


        }
    }

    private void saveArticleToList(adArticleAmount a, String c, String comercial) {
        amountid++;
        a.setId(amountid);
        a.setAmount(c);
        a.setIdcomercial(comercial);

        if (rq != null) {
            if (rq.getAmountlist() != null) {
                for (adArticleAmount arm : rq.getAmountlist()) {
                    if (arm.getArticle() == a.getArticle()) {
                        rq.getAmountlist().remove(arm);
                        break;
                    }
                }
                rq.getAmountlist().add(a);
            }
        }

        if (!isOper) {
            if (po != null) {
                if (po.getMaetrialamountlist() != null) {
                    for (adArticleAmount arm : po.getMaetrialamountlist()) {
                        if (arm.getArticle() == a.getArticle()) {
                            po.getMaetrialamountlist().remove(arm);
                            break;
                        }
                    }
                    po.getMaetrialamountlist().add(a);
                }
            }
        } else {
            if (po != null) {
                if (po.getOperamountlist() != null) {
                    for (adArticleAmount arm : po.getOperamountlist()) {
                        if (arm.getArticle() == a.getArticle()) {
                            po.getOperamountlist().remove(arm);
                            break;
                        }
                    }
                    po.getOperamountlist().add(a);
                }
            }
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();

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
            theTextArea.setHint("Artículo");

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

            inflater.inflate(R.menu.back_list_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.ready:

                return true;

            case R.id.back:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
                if (rq != null) {
                    GPApplication.getInstance().cancelPendingRequests("get_article");
                    FragmentTransaction ft = super.getFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, RequestArticleAmountListFragment.newInstance(us, rq));
                    ft.commit();
                } else if (po != null) {
                    GPApplication.getInstance().cancelPendingRequests("get_article");
                    FragmentTransaction ft = super.getFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, PartFragment.newInstance(us, po));
                    ft.commit();
                } else {
                    GPApplication.getInstance().cancelPendingRequests("get_article");
                    FragmentTransaction ft = super.getFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, FamilyListFragment.newInstance(us));
                    ft.commit();
                }
                return true;

            case R.id.reload:
                //Empty_Error(false);
                //super.Run(true, "Cargando");


                Execute("Cargando");


                //error = false;
                //getActivity().invalidateOptionsMenu();
                return true;

            default:
                GPApplication.getInstance().cancelPendingRequests("get_article");
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        commercialpair = (UtilTextValuePair) spinnercommercial
                .getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}