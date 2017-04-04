package com.app.ambruster.gestiondepedidos.view.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.app.ambruster.gestiondepedidos.GPApplication;
import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.ApplicationDatabaseManager;
import com.app.ambruster.gestiondepedidos.data.dao.IOperationDAO;
import com.app.ambruster.gestiondepedidos.data.dao.MessageDAO;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.data.model.adMessage;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.MessageServiceTask;
import com.app.ambruster.gestiondepedidos.data.service.task.RequestServiceTask;
import com.app.ambruster.gestiondepedidos.view.adapter.RequestPairAdapter;
import com.app.ambruster.gestiondepedidos.view.adapter.UtilTextValuePair;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageFragment extends CoreFragment implements
        MessageServiceTask.MessageServiceTaskCallBack,
        AdapterView.OnItemSelectedListener,
        RequestServiceTask.RequestServiceTaskCallBack {

    public static final String USER = "user";
    public static final String REQUEST = "request";
    public static final String MESSAGE = "message";

    public static adUser us;
    public static adRequest rq;
    public static adMessage ms;

    public MessageDAO messagedao;
    public List<adRequest> reqlist = new ArrayList<>();
    private MaterialDialog mdialog;
    private EditText inputreques;
    private EditText inputclient;
    private EditText inputtext;
    private ActionProcessButton btnSend, btnSave;
    private String request, name, message;
    private Spinner spinnerRequest;
    private UtilTextValuePair requestpair;
    private boolean del_message = false;
    private boolean error, error_loading = false;


    public static MessageFragment newInstance(adUser user) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        fragment.setArguments(args);
        return fragment;
    }

    public static MessageFragment newInstance(adUser user, adMessage ms) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(MESSAGE, gSon.toJson(ms));
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

            if (bundle.containsKey(REQUEST)) {
                Gson gSon = new Gson();
                rq = gSon.fromJson(bundle.getString(REQUEST), new TypeToken<adFamily>() {
                }.getType());
            }

            if (bundle.containsKey(MESSAGE)) {
                Gson gSon = new Gson();
                del_message = true;
                ms = gSon.fromJson(bundle.getString(MESSAGE), new TypeToken<adMessage>() {
                }.getType());
            }
        }

        Load();

        if (!del_message) {
            super.Run(true, "Cargando");
            new RequestServiceTask(this).CallService(us, 1, 0, 1000);
        }


        return view;
    }

    @Override
    protected void Load() {
        View v = View.inflate(getActivity(), R.layout.activity_message_add, null);

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Regular.ttf");

        inputreques = (EditText) v.findViewById(R.id.code);
        inputreques.setTypeface(type);
        inputreques.setEnabled(false);
        inputclient = (EditText) v.findViewById(R.id.name);
        inputclient.setTypeface(type);
        inputclient.setEnabled(false);
        inputtext = (EditText) v.findViewById(R.id.message);
        inputtext.setTypeface(type);

        btnSend = (ActionProcessButton) v.findViewById(R.id.btnSend);
        btnSend.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSend.setOnClickListener(this);

        btnSave = (ActionProcessButton) v.findViewById(R.id.btnsave);
        btnSave.setMode(ActionProcessButton.Mode.ENDLESS);
        btnSave.setOnClickListener(this);

        spinnerRequest = (Spinner) v.findViewById(R.id.sprequest);

        if (!del_message) {
            spinnerRequest.setOnItemSelectedListener(this);
        } else {
            spinnerRequest.setVisibility(View.GONE);
            inputreques.setText(String.valueOf(ms.getRequestid()));
            inputclient.setText(ms.getClientname());
            inputtext.setText(ms.getText());
        }


        ((ViewGroup) fragmentView).addView(v);
    }

    private void Send(adUser user, int reqcod, String Text) {
        btnSend.setProgress(1);
        new MessageServiceTask(this).CallService(user, reqcod, Text);
    }


    private void clean() {
        inputtext.setText("");
    }

    @Override
    protected void Fill() {
        if (reqlist.size() > 0) {
            RequestPairAdapter spinnerAdapter = new RequestPairAdapter(getActivity(),
                    android.R.layout.simple_spinner_dropdown_item, reqlist);
            spinnerRequest.setAdapter(spinnerAdapter);
        }
    }

    @Override
    protected void Add() {
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
    public void onMessageServiceTaskCallBack(boolean send) {
        if (!send) {
            btnSend.setProgress(-1);
            btnSend.setText(R.string.fail);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSend.setEnabled(true);
                    btnSend.setProgress(0);
                }
            }, 1000);
        } else {
            btnSend.setProgress(100);
            clean();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    btnSend.setEnabled(true);
                    btnSend.setProgress(0);
                    if (del_message) {
                        deleteMessage(ms);
                    }
                }
            }, 1000);
        }
    }

    private void deleteMessage(adMessage message) {
        messagedao = new MessageDAO(ApplicationDatabaseManager.getInstance().getHelper());
        messagedao.Delete(message);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goBack();
            }
        }, 1000);
    }

    private void updateMessage(adMessage message) {
        messagedao = new MessageDAO(ApplicationDatabaseManager.getInstance().getHelper());
        messagedao.Update(message);
        btnSave.setProgress(1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goBack();
            }
        }, 1000);
    }

    private void createMessage(String name, int request, String message, int status) {
        messagedao = new MessageDAO(ApplicationDatabaseManager.getInstance().getHelper());
        adMessage msg = new adMessage();

        msg.setStatus(status);
        msg.setClientname(name);
        msg.setCreated(new Date());
        msg.setRequestid(request);
        msg.setUser(us);
        msg.setText(message);

        messagedao.Create(msg, IOperationDAO.OPERATION_INSERT_OR_UPDATE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goBack();
            }
        }, 1000);
    }

    @Override
    public void onRequestServiceTaskCallBack(ArrayList<adRequest> request) {
        MessageFragment.this.reqlist.addAll(request);
        Fill();
        super.Run(false, "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnSend:
                request = inputreques.getText().toString();
                name = inputclient.getText().toString();
                message = inputtext.getText().toString();

                if (request.trim().length() > 0 && name.trim().length() > 0 && message.trim().length() > 0) {
                    Send(us, Integer.parseInt(request), message);
                    btnSend.setEnabled(false);
                } else {
                    mdialog = new MaterialDialog.Builder(getActivity())
                            .content(R.string.fill_fileds)
                            .contentGravity(GravityEnum.CENTER)
                            .contentColor(getResources().getColor(R.color.black95))
                            .positiveText(R.string.agree)
                            .show();
                }

                break;

            case R.id.btnsave:
                request = inputreques.getText().toString();
                name = inputclient.getText().toString();
                message = inputtext.getText().toString();

                if (request.trim().length() > 0 && name.trim().length() > 0 && message.trim().length() > 0) {
                    btnSave.setProgress(1);
                    if (!del_message) {
                        createMessage(name, Integer.parseInt(request), message, 1);
                        btnSave.setEnabled(false);
                    } else {
                        ms.setText(message);
                        updateMessage(ms);
                        btnSave.setEnabled(false);
                    }
                } else {
                    mdialog = new MaterialDialog.Builder(getActivity())
                            .content(R.string.fill_fileds)
                            .contentGravity(GravityEnum.CENTER)
                            .contentColor(getResources().getColor(R.color.black95))
                            .positiveText(R.string.agree)
                            .show();
                }
                break;
        }
    }

    public void goBack() {
        Log.d("Go to message list", "true");
        if (getFragmentManager().beginTransaction() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
            GPApplication.getInstance().cancelPendingRequests("send_message");
            GPApplication.getInstance().cancelPendingRequests("get_request");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, MessageListFragment.newInstance(us));
            ft.commit();
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();

        inflater.inflate(R.menu.back_list_menu, menu);

        if (error_loading)
            inflater.inflate(R.menu.reload_list_menu, menu);

        if (del_message)
            inflater.inflate(R.menu.delete_mesagge_menu, menu);

    }

    @Override
    public void onError(String message) {

        if (reqlist.size() == 0) {
            if (rq == null) {
                error_loading = true;

                new View(getActivity()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Run(false, "");
                        Empty_Error(true);
                    }
                }, 1000);
            }
        } else {
            error = true;
            btnSend.setProgress(-1);
            btnSend.setText(R.string.fail);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Run(false, "");
                    btnSend.setEnabled(true);
                    btnSend.setProgress(0);
                }
            }, 1000);
        }

        super.Run(false, "");

        getActivity().invalidateOptionsMenu();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.reload:
                Run(true, "Cargando");
                new RequestServiceTask(this).CallService(us, 1, 1, 1000);
                error_loading = false;
                getActivity().invalidateOptionsMenu();
                return true;

            case R.id.back:
                goBack();
                return true;

            case R.id.delete:
                deleteMessage(ms);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        requestpair = (UtilTextValuePair) spinnerRequest
                .getItemAtPosition(i);

        adRequest req = new adRequest();
        req.setId(requestpair.getItem());
        adClient cl = new adClient();
        cl.setCliname(requestpair.getText());
        req.setClient(cl);

        inputreques.setText(String.valueOf(req.getId()));
        inputclient.setText(req.getClient().getCliname());
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


}