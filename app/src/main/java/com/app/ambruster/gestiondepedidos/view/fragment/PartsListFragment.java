package com.app.ambruster.gestiondepedidos.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.DeletePartServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.GetPartsServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.PartsServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.SendPartServiceTask;
import com.app.ambruster.gestiondepedidos.log.LogManager;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectHeaderAdapter;
import com.app.ambruster.gestiondepedidos.view.fragment.Util.Pending;
import com.app.ambruster.gestiondepedidos.view.fragment.Util.Sended;
import com.app.ambruster.gestiondepedidos.view.view.CellParts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simplify.ink.InkView;

import java.util.ArrayList;
import java.util.List;

public class PartsListFragment extends CoreFragment implements
        PartsServiceTask.PartsServiceTaskCallBack,
        DeletePartServiceTask.DeletePartServiceTaskTaskCallBack,
        GetPartsServiceTask.GetPartsServiceTaskCallBack,
        SendPartServiceTask.SendPartServiceTaskCallBack,
        AdapterView.OnItemClickListener {

    public static final String USER = "user";
    public static adUser us;
    public static adPartObra part;
    public static boolean loadmore = true;
    public List<adPartObra> partlist = new ArrayList<>();
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

    public static PartsListFragment newInstance(adUser user) {
        PartsListFragment fragment = new PartsListFragment();
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
        part = null;

        Bundle bundle = getArguments();
        if (bundle != null) {

            if (bundle.containsKey(USER)) {
                Gson gSon = new Gson();
                us = gSon.fromJson(bundle.getString(USER), new TypeToken<adUser>() {
                }.getType());
            }
        }

        Load();
        Fill();
        Add();
        Execute(status);
        return view;
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
        new PartsServiceTask(this).CallService(us, 1, status, 1000);
    }

    @Override
    protected void Load() {

        View v = View.inflate(getActivity(), R.layout.list_parts, null);
        list = (AbsListView) v.findViewById(R.id.list);
        widget = v.findViewById(R.id.widget_options);

        create = v.findViewById(R.id.fab);
        create.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.frame_container, PartFragment.newInstance(us));
                        ft.commit();
                    }
                });

        cancel = v.findViewById(R.id.delete);
        cancel.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (part == null) {
                            Toast.makeText(getActivity(), R.string.choose_part_delete, Toast.LENGTH_LONG).show();
                        } else {
                            if (part.getState() == "enviado") {
                                Toast.makeText(getActivity(), R.string.already_send_part, Toast.LENGTH_LONG).show();
                            } else {
                                delete = true;
                                Delete();
                                part = null;
                            }
                        }
                    }
                });

        send = v.findViewById(R.id.send);
        send.setOnClickListener(
                new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (part == null) {
                            Toast.makeText(getActivity(), R.string.choose_part, Toast.LENGTH_LONG).show();
                        } else {
                            if (part.getState() == "enviado") {
                                Toast.makeText(getActivity(), R.string.already_send_part, Toast.LENGTH_LONG).show();
                            } else {
                                sended = true;
                                Send();
                                part = null;
                            }
                        }
                    }
                });
        edit = v.findViewById(R.id.edit);
        edit.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (part == null) {
                            Toast.makeText(getActivity(), R.string.choose_request_edit, Toast.LENGTH_LONG).show();
                        } else {
                            if (part.getState() == "enviado") {
                                Toast.makeText(getActivity(), R.string.already_send_part, Toast.LENGTH_LONG).show();
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
        adapter = new ObjectHeaderAdapter<>(getActivity(), listobjects, R.layout.cell_parts, CellParts.class);
        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void Add() {
        list.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
    }

    private void Delete() {
        super.Run(true, "Cancelando parte");
        new DeletePartServiceTask(this).CallService(us, part);
        list.clearChoices();
        adapter.notifyDataSetChanged();
    }

    private void Edit() {
        Run(true, "Cargando parte");
        new GetPartsServiceTask(this).CallService(us, part);
    }

    private void View() {
        if (part.getState() != "recogido") {
            Run(true, "Cargando parte");
            new GetPartsServiceTask(this).CallService(us, part);
        } else {
            Toast.makeText(getActivity(), R.string.already_get_part, Toast.LENGTH_SHORT).show();
            Run(false, "");
        }
    }

    private void Send() {
        super.Run(true, "Enviando parte");
        new SendPartServiceTask(this).CallService(us, part);
        list.clearChoices();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPartsServiceTaskCallBack(ArrayList<adPartObra> parts) {
        int cont_p = 0, cont_s = 0;
        ArrayList<Object> objects = new ArrayList<>();
        LogManager.getInstance().info("List Size ", parts.size() + "");

        if (parts.isEmpty() && this.partlist.isEmpty()) {

            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Run(false, "");
                    Empty(true, "NO HAY PARTES CREADOS");
                }
            }, 200);
        } else {

            for (adPartObra part : parts) {
                if (part.getState() == "Pendiente")
                    cont_p++;
            }

            if (cont_p > 0) {
                objects.add(new Pending(1, "Partes pendientes"));

                for (adPartObra part : parts) {
                    if (part.getState() == "Pendiente")
                        objects.add(part);
                }
            }

            for (adPartObra part : parts) {
                if (part.getState() == "enviado")
                    cont_s++;
            }

            if (cont_s > 0) {

                objects.add(new Sended(2, "Partes enviados"));

                for (adPartObra part : parts) {
                    if (part.getState() == "enviado")
                        objects.add(part);
                }
            }


            new View(getActivity()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Run(false, "");
                    Empty(false, "");
                }
            }, 100);
        }

        Run(false, "");
        PartsListFragment.this.listobjects.clear();
        PartsListFragment.this.listobjects.addAll(objects);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDeletePartServiceTaskTaskCallBack(Boolean response) {
        Run(false, "");

        if (response)
            Execute(status);
    }

    @Override
    public void onGetPartsServiceTaskCallBack(adPartObra parte) {
        FragmentTransaction ft = super.getFragmentManager().beginTransaction();
        if (parte != null) {
            ft.replace(R.id.frame_container, PartFragment.newInstance(us, parte));
            ft.commit();
        }
        Run(false, "");
    }

    @Override
    public void onSendPartServiceTaskCallBack(Boolean response) {
        Run(false, "");
        if (response) {
            Toast.makeText(getActivity(), R.string.send_succesfully, Toast.LENGTH_SHORT).show();
            Execute(status);
        }
    }

    @Override
    public void onError(String message) {

        if (delete) {
            delete = !delete;
            Toast.makeText(getActivity(), R.string.error_deleting_part, Toast.LENGTH_SHORT).show();
        } else if (sended) {
            sended = !sended;
            Toast.makeText(getActivity(), R.string.error_sending_part, Toast.LENGTH_SHORT).show();
        } else if (edited) {
            edited = !edited;
            Toast.makeText(getActivity(), R.string.error_editing_parte, Toast.LENGTH_SHORT).show();
        } else if (viewed) {
            edited = !edited;
            Toast.makeText(getActivity(), R.string.error_viewing_parte, Toast.LENGTH_SHORT).show();
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

        if (error) {

            error = false;
        }
        inflater.inflate(R.menu.reload_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.reload:
                Execute(status);
                getActivity().invalidateOptionsMenu();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        part = (adPartObra) adapter.getItem(i);
        if (part.getState() == "Pendiente")
            changeWidgetVisibility(1);
        else {
            viewed = true;
            View();

        }
    }

}