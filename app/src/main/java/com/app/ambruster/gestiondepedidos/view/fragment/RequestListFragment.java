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
import android.widget.Toast;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.DeleteRequestServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.GetRequestServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.RequestServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.SendRequestServiceTask;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectAdapter;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectHeaderAdapter;
import com.app.ambruster.gestiondepedidos.view.fragment.Util.Pending;
import com.app.ambruster.gestiondepedidos.view.fragment.Util.Sended;
import com.app.ambruster.gestiondepedidos.view.view.CellRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestListFragment extends CoreFragment implements
        RequestServiceTask.RequestServiceTaskCallBack,
        AdapterView.OnItemClickListener,
        ObjectAdapter.ObjectAdapterLoadMore,
        DeleteRequestServiceTask.DeleteRequestServiceTaskCallBack,
        SendRequestServiceTask.SendRequestServiceTaskCallBack,
        GetRequestServiceTask.GetRequestServiceTaskCallBack {

    public static final String USER = "user";
    public static adUser us;
    public static adRequest req;

    public static boolean loadmore = true;
    public List<adRequest> reqlist = new ArrayList<>();
    public View widget, cancel, edit, send, create;

    public int status;
    public int page = 1;
    public ObjectHeaderAdapter<Object> adapter;
    public AbsListView list;
    public ArrayList<Object> listobjects = new ArrayList<>();

    private boolean error;
    private boolean delete;
    private boolean sended;
    private boolean edited;
    private boolean viewed;

    public static RequestListFragment newInstance(adUser user) {
        RequestListFragment fragment = new RequestListFragment();
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
        delete = false;
        sended = false;
        edited = false;
        viewed = false;

        status = 0;
        page = 1;
        req = null;

        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(USER), new TypeToken<adUser>() {
                }.getType());
            }
        }

        setTittle(status);
        Load();
        Fill();
        Add();
        Execute(status);

        return view;
    }

    private void setTittle(int status) {
        switch (status) {
            case 1:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.requests_pending));
                break;
            case 2:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.requests_sended));
                break;
            case 0:
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.requests));
                break;
        }
    }

    private void changeWidgetVisibility(int status) {
        switch (status) {
            case 1:
                widget.setVisibility(View.VISIBLE);
                break;
            case 2:
                widget.setVisibility(View.GONE);
                break;
        }
    }

    private void Execute(int status) {
        super.Run(true, "Cargando");
        Empty_Error(false);
        setTittle(status);
        new RequestServiceTask(this).CallService(us, 1, status, 100);
    }

    private void Delete() {
        super.Run(true, "Cancelando pedido");
        new DeleteRequestServiceTask(this).CallService(us, req);
        list.clearChoices();
        adapter.notifyDataSetChanged();
    }

    private void Create() {
        adRequest rq = new adRequest();
        ArrayList<adArticleAmount> list = new ArrayList<>();
        rq.setId(999999);
        rq.setUser(us);
        rq.setAmountlist(list);
        rq.setRequestdate(new Date());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, ClientListFragment.newInstance(us, rq));
        ft.commit();
    }

    private void Edit() {
        Run(true, "Cargando pedido");
        new GetRequestServiceTask(this).CallService(us, req);
    }

    private void View() {
        if (req.getStatus() != 3) {
            Run(true, "Cargando pedido");
            new GetRequestServiceTask(this).CallService(us, req);
        } else {
            Toast.makeText(getActivity(), R.string.already_get, Toast.LENGTH_SHORT).show();
            Run(false, "");
        }
    }

    private void Send() {
        super.Run(true, "Enviando pedido");
        new SendRequestServiceTask(this).CallService(us, req);
        list.clearChoices();
        adapter.notifyDataSetChanged();
    }


    @Override
    protected void Load() {

        View v = View.inflate(getActivity(), R.layout.list_request, null);
        list = (AbsListView) v.findViewById(R.id.list);
        widget = v.findViewById(R.id.widget_options);

        create = v.findViewById(R.id.fab);
        create.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Create();
                    }
                });
        send = v.findViewById(R.id.send);
        send.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (req == null) {
                            Toast.makeText(getActivity(), R.string.choose_request, Toast.LENGTH_LONG).show();
                        } else {
                            if (req.getStatus() == 2) {
                                Toast.makeText(getActivity(), R.string.already_send, Toast.LENGTH_LONG).show();
                            } else {
                                sended = true;
                                Send();
                                req = null;
                            }
                        }
                    }
                });
        cancel = v.findViewById(R.id.delete);
        cancel.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (req == null) {
                            Toast.makeText(getActivity(), R.string.choose_request_delete, Toast.LENGTH_LONG).show();
                        } else {
                            if (req.getStatus() == 2) {
                                Toast.makeText(getActivity(), R.string.already_send, Toast.LENGTH_LONG).show();
                            } else {
                                delete = true;
                                Delete();
                                req = null;
                            }
                        }
                    }
                });
        edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (req == null) {
                            Toast.makeText(getActivity(), R.string.choose_request_edit, Toast.LENGTH_LONG).show();
                        } else {
                            if (req.getStatus() == 2) {
                                Toast.makeText(getActivity(), R.string.already_send, Toast.LENGTH_LONG).show();
                            } else {
                                edited = true;
                                Edit();
                            }
                        }
                    }
                });
        ((ViewGroup) fragmentView).addView(v);
    }

    @Override
    protected void Fill() {
        adapter = new ObjectHeaderAdapter<>(getActivity(), listobjects, R.layout.cell_request, CellRequest.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void Add() {
        list.setOnItemClickListener(this);
    }

    @Override
    public void loadMore() {
        page++;
        if (loadmore) {
            new RequestServiceTask(this).CallService(us, page, status, 10);
        }
    }

    @Override
    public void onRequestServiceTaskCallBack(ArrayList<adRequest> request) {

        int cont = 0;
        ArrayList<Object> objects = new ArrayList<>();

        if (request.isEmpty() && this.reqlist.isEmpty()) {

            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Run(false, "");
                    Empty(true, "NO HAY PEDIDOS CREADOS");
                }
            }, 200);
        } else {

            for (adRequest req : request) {
                if (req.getStatus() == 1)
                    cont++;
            }

            if (cont > 0) {
                objects.add(new Pending(1, "Pedidos pendientes"));
                changeWidgetVisibility(1);
            } else {
                changeWidgetVisibility(2);
            }

            for (adRequest req : request) {
                if (req.getStatus() == 1)
                    objects.add(req);
            }

            objects.add(new Sended(2, "Pedidos enviados"));

            for (adRequest req : request) {
                if (req.getStatus() == 2)
                    objects.add(req);
            }

            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Run(false, "");
                    Empty(false, "");
                }
            }, 200);
        }

        Run(false, "");
        RequestListFragment.this.listobjects.clear();
        RequestListFragment.this.listobjects.addAll(objects);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDeleteRequestServiceTaskCallBack(Boolean response) {
        Run(false, "");

        if (response)
            Execute(status);
    }

    @Override
    public void onSendRequestServiceTaskCallBack(Boolean response) {
        Run(false, "");

        if (response) {
            Toast.makeText(getActivity(), R.string.send_succesfully, Toast.LENGTH_SHORT).show();
            Execute(status);
        }
    }

    @Override
    public void onGetRequestServiceTaskCallBack(adRequest request) {
        Run(false, "");
        if (request != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            if (!viewed) {
                ft.replace(R.id.frame_container, RequestArticleAmountListFragment.newInstance(us, request));
            } else {
                ft.replace(R.id.frame_container, RequestArticleAmountListFragment.newInstance(us, request, 2));
            }
            ft.commit();
        }
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onError(String message) {

        if (delete) {
            delete = !delete;
            Toast.makeText(getActivity(), R.string.error_deleting, Toast.LENGTH_LONG).show();
        } else if (sended) {
            sended = !sended;
            Toast.makeText(getActivity(), R.string.error_sending, Toast.LENGTH_LONG).show();
        } else if (edited) {
            edited = !edited;
            Toast.makeText(getActivity(), R.string.error_editing, Toast.LENGTH_LONG).show();
        } else if (viewed) {
            edited = !edited;
            Toast.makeText(getActivity(), R.string.error_viewing, Toast.LENGTH_LONG).show();
        } else {
            error = true;
            getActivity().invalidateOptionsMenu();
            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Empty_Error(true);
                }
            }, 100);
        }
        Run(false, "");
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

        if (error) {
            inflater.inflate(R.menu.reload_list_menu, menu);
            error = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.reload:
                Execute(status);
                getActivity().invalidateOptionsMenu();
                return true;

            case R.id.pend_req:
                Execute(status);
                getActivity().invalidateOptionsMenu();
                return true;

            case R.id.send_req:
                Execute(status);
                getActivity().invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        req = (adRequest) adapter.getItem(i);
        if (req.getStatus() == 1)
            changeWidgetVisibility(1);
        else {
            changeWidgetVisibility(2);
            viewed = true;
            View();
        }
    }


}