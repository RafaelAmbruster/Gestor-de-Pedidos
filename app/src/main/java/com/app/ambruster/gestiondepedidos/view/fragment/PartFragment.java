package com.app.ambruster.gestiondepedidos.view.fragment;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adUser;
import com.app.ambruster.gestiondepedidos.data.service.task.CreatePartServiceTask;
import com.app.ambruster.gestiondepedidos.util.CustomDateFormat;
import com.app.ambruster.gestiondepedidos.view.adapter.ObjectHeaderAdapter;
import com.app.ambruster.gestiondepedidos.view.textview.TypefacedTextView;
import com.app.ambruster.gestiondepedidos.view.view.CellAmountArticleOperPart;
import com.app.ambruster.gestiondepedidos.view.view.CellAmountArticlePart;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.simplify.ink.InkView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import me.panavtec.drawableview.DrawableView;
import me.panavtec.drawableview.DrawableViewConfig;

public class PartFragment extends CoreFragment implements AdapterView.OnItemClickListener,
        CreatePartServiceTask.CreatePartServiceTaskCallBack {

    public static final String USER = "user";
    public static final String PARTOBRA = "partobra";

    private adUser user;
    private String date;
    private adPartObra partobra;
    private adArticleAmount amMaterial, amOper;
    private EditText work_text;
    private TypefacedTextView txv_date, txv_client, txv_obra;
    //private DrawableView drawableView;
    //private DrawableViewConfig config = new DrawableViewConfig();
    private ImageButton btn_opendate, btn_openclient, btn_openobra, btn_add_material, btn_rem_work, btn_rem_material, btn_add_oper, btn_rem_oper, btn_clean_sign, btn_clean_sign_client;
    private ArrayList<Object> listobjectsmaterials = new ArrayList<>();
    private ObjectHeaderAdapter<Object> adaptermaterial;
    private ArrayList<Object> listobjectsoper = new ArrayList<>();
    private ObjectHeaderAdapter<Object> adapteroper;
    private AbsListView listmaterial, listoper;
    private int status = 0;
    private ImageView signpic, signpic_client;
    private InkView mInkView, mInkView_client;


    public static PartFragment newInstance(adUser user) {
        PartFragment fragment = new PartFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        fragment.setArguments(args);
        return fragment;
    }

    public static PartFragment newInstance(adUser user, adPartObra parte) {
        PartFragment fragment = new PartFragment();
        Bundle args = new Bundle();
        Gson gSon = new Gson();
        args.putString(USER, gSon.toJson(user));
        args.putString(PARTOBRA, gSon.toJson(parte));
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);
        setFragmentView(view);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();

        partobra = null;

        if (bundle != null) {

            if (bundle.containsKey(USER)) {
                Gson gSon = new Gson();
                user = gSon.fromJson(bundle.getString(USER), new TypeToken<adUser>() {
                }.getType());
            }

            if (bundle.containsKey(PARTOBRA)) {
                Gson gSon = new Gson();
                partobra = gSon.fromJson(bundle.getString(PARTOBRA), new TypeToken<adPartObra>() {
                }.getType());
            }
        }

        Load();
        Fill();
        Add();


        return view;
    }

    @Override
    protected void Load() {
        View v = View.inflate(getActivity(), R.layout.activity_tabs_vertical, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.parts));

        Typeface type = Typeface.createFromAsset(getActivity().getAssets(), "fonts/RobotoCondensed-Regular.ttf");

        work_text = (EditText) v.findViewById(R.id.work_text);
        work_text.setTypeface(type);

        txv_date = (TypefacedTextView) v.findViewById(R.id.txv_date);
        txv_client = (TypefacedTextView) v.findViewById(R.id.txv_client);
        txv_obra = (TypefacedTextView) v.findViewById(R.id.txv_obra);

        btn_add_material = (ImageButton) v.findViewById(R.id.btn_add_material);
        btn_rem_work = (ImageButton) v.findViewById(R.id.btn_rem_work);
        btn_rem_material = (ImageButton) v.findViewById(R.id.btn_rem_material);
        btn_add_oper = (ImageButton) v.findViewById(R.id.btn_add_oper);
        btn_rem_oper = (ImageButton) v.findViewById(R.id.btn_rem_oper);
        btn_openobra = (ImageButton) v.findViewById(R.id.btn_openobra);
        btn_openclient = (ImageButton) v.findViewById(R.id.btn_openclient);
        btn_clean_sign = (ImageButton) v.findViewById(R.id.btn_clean_sign);
        btn_clean_sign_client = (ImageButton) v.findViewById(R.id.btn_clean_sign_client);
        btn_opendate = (ImageButton) v.findViewById(R.id.btn_opendate);

        listmaterial = (AbsListView) v.findViewById(R.id.list);
        listoper = (AbsListView) v.findViewById(R.id.listoper);
        signpic = (ImageView) v.findViewById(R.id.signpic);
        signpic_client = (ImageView) v.findViewById(R.id.signpicclient);

        mInkView = (InkView) v.findViewById(R.id.ink);
        mInkView.setMinStrokeWidth(1.5f);
        mInkView.setMaxStrokeWidth(3f);

        mInkView_client = (InkView) v.findViewById(R.id.inkclient);
        mInkView_client.setMinStrokeWidth(1.5f);
        mInkView_client.setMaxStrokeWidth(3f);

        //drawableView = (DrawableView) v.findViewById(R.id.paintView);
       /* config.setStrokeColor(getResources().getColor(android.R.color.black));
        config.setShowCanvasBounds(false);
        config.setStrokeWidth(12.0f);
        config.setMinZoom(1.0f);
        config.setMaxZoom(1.0f);
        config.setCanvasHeight(400);
        config.setCanvasWidth(200);
        config.setStrokeWidth(config.getStrokeWidth() - 10);
        drawableView.setConfig(config);*/


        if (partobra == null) {
            date = CustomDateFormat.getCurrentTimeYMD(new Date());
            txv_date.setText(date);

            partobra = new adPartObra();
            partobra.setId(999999);
            partobra.setCommercial(user);
            partobra.setPartdate(date);
            ArrayList<adArticleAmount> list = new ArrayList<>();
            partobra.setMaetrialamountlist(list);
            partobra.setOperamountlist(list);
            partobra.setState("Pendiente");

            getActivity().invalidateOptionsMenu();
            signpic.setVisibility(View.GONE);
            mInkView.setVisibility(View.VISIBLE);

            signpic_client.setVisibility(View.GONE);
            mInkView_client.setVisibility(View.VISIBLE);

        } else {
            if (partobra.getState().toString().contains("enviado")) {

                mInkView.setVisibility(View.GONE);
                signpic.setVisibility(View.VISIBLE);

                mInkView_client.setVisibility(View.GONE);
                signpic_client.setVisibility(View.VISIBLE);

                String url = partobra
                        .getPartsign();
                if (url != null) {
                    if (url.getBytes().length > 5) {
                        Picasso.with(getActivity()).load(url).into(signpic);
                    }
                }

                String urlclient = partobra
                        .getPartsignclient();
                if (url != null) {
                    if (urlclient.getBytes().length > 5) {
                        Picasso.with(getActivity()).load(urlclient).into(signpic_client);
                    }
                }

                btn_opendate.setEnabled(false);
                btn_add_material.setEnabled(false);
                btn_rem_work.setEnabled(false);
                btn_rem_material.setEnabled(false);
                btn_add_oper.setEnabled(false);
                btn_rem_oper.setEnabled(false);
                btn_openobra.setEnabled(false);
                btn_openclient.setEnabled(false);
                btn_clean_sign.setEnabled(false);
                btn_clean_sign_client.setEnabled(false);
                work_text.setEnabled(false);
            }

            if (partobra.getPartdate() != null) {
                txv_date.setText(partobra.getPartdate());
            }

            if (partobra.getClient() != null) {
                txv_client.setText(partobra.getClient().getClicode() + " | " + partobra.getClient().getCliname());
            }

            if (partobra.getObra() != null) {
                txv_obra.setText(partobra.getObra().getObraCod() + " | " + partobra.getObra().getObraName());
            }

            if (partobra.getDescription() != null) {
                work_text.setText(partobra.getDescription());
            }
        }

        btn_opendate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date = year + "/" + (monthOfYear + 1) + "/" + dayOfMonth;
                                txv_date.setText(date);

                                if (partobra != null) {
                                    partobra.setPartdate(date);
                                }

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });


        btn_openclient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (work_text.getText().toString() != "") {
                    partobra.setDescription(work_text.getText().toString());
                }
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frame_container, ClientListFragment.newInstance(user, partobra));
                ft.commit();
            }
        });


        btn_openobra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partobra.getClient() != null) {
                    if (work_text.getText().toString() != "") {
                        partobra.setDescription(work_text.getText().toString());
                    }
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frame_container, ObrasListFragment.newInstance(user, partobra));
                    ft.commit();
                } else {
                    Toast.makeText(getActivity(), R.string.please_choose_a_client, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_rem_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                work_text.setText("");
            }
        });

        btn_add_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partobra.getClient() != null) {
                    if (work_text.getText().toString() != "") {
                        partobra.setDescription(work_text.getText().toString());
                    }
                    AddArticle(false);
                } else {
                    Toast.makeText(getActivity(), R.string.please_choose_a_client, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_rem_material.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMaterialItem();
            }
        });

        btn_add_oper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (partobra.getClient() != null) {
                    if (work_text.getText().toString() != "") {
                        partobra.setDescription(work_text.getText().toString());
                    }
                    AddArticle(true);
                } else {
                    Toast.makeText(getActivity(), R.string.please_choose_a_client, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_rem_oper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteMaterialOper();
            }
        });

        btn_clean_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // drawableView.undo();
                mInkView.clear();
            }
        });

        btn_clean_sign_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // drawableView.undo();
                mInkView_client.clear();
            }
        });

        ((ViewGroup) fragmentView).addView(v);
    }

    private void SendPart() {
        if (partobra.getClient() != null) {
            if (partobra.getObra() != null) {
                status = 2;
                partobra.setPartsign(getBmpSign());
                partobra.setPartsignclient(getBmpSignClient());
                partobra.setPartdate(txv_date.getText().toString());
                partobra.setDescription(work_text.getText().toString());
                Run(true, getActivity().getResources().getString(R.string.send_loading));
                new CreatePartServiceTask(this).CallService(user, partobra, 2);
            } else {
                Toast.makeText(getActivity(), R.string.warning, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.warning, Toast.LENGTH_SHORT).show();
        }
    }

    private void SavePart() {
        if (partobra.getClient() != null) {
            if (partobra.getObra() != null) {
                status = 1;
                Run(true, getActivity().getResources().getString(R.string.save_loading));
                partobra.setPartsign(getBmpSign());
                partobra.setPartsignclient(getBmpSignClient());
                partobra.setPartdate(txv_date.getText().toString());
                partobra.setDescription(work_text.getText().toString());
                new CreatePartServiceTask(this).CallService(user, partobra, 1);
            } else {
                Toast.makeText(getActivity(), R.string.warning, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.warning, Toast.LENGTH_SHORT).show();
        }
    }

    private String getBmpSign() {
        String encoded;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //drawableView.obtainBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        mInkView.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    private String getBmpSignClient() {
        String encoded;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //drawableView.obtainBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        mInkView_client.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    private void AddArticle(boolean op) {
        if (getFragmentManager().beginTransaction() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, FamilyListFragment.newInstance(user, partobra, op));
            ft.commit();
        }
    }

    private void DeleteMaterialItem() {
        if (partobra.getMaetrialamountlist().size() > 0) {
            if (amMaterial != null) {
                partobra.getMaetrialamountlist().remove(amMaterial);
                fillArticleMaterialList();
            } else {
                Toast.makeText(getActivity(), R.string.choose_art, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void DeleteMaterialOper() {
        if (partobra.getOperamountlist().size() > 0) {
            if (amOper != null) {
                partobra.getOperamountlist().remove(amOper);
                fillArticleOperList();
            } else {
                Toast.makeText(getActivity(), R.string.choose_art, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fillArticleMaterialList() {
        ArrayList<Object> objects = new ArrayList<>();

        if (partobra.getMaetrialamountlist() != null) {
            if (partobra.getMaetrialamountlist().isEmpty()) {

            } else {
                for (adArticleAmount amount : partobra.getMaetrialamountlist()) {
                    objects.add(amount);
                }

                new View(getActivity()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Empty(false, "");
                    }
                }, 200);
            }
        }

        setListViewHeightBasedOnChildren((ListView) listmaterial);
        PartFragment.this.listobjectsmaterials.clear();
        PartFragment.this.listobjectsmaterials.addAll(objects);
        adaptermaterial.notifyDataSetChanged();
    }

    private void fillArticleOperList() {
        ArrayList<Object> objects = new ArrayList<>();

        if (partobra.getOperamountlist() != null) {
            if (partobra.getOperamountlist().isEmpty()) {

            } else {
                for (adArticleAmount amount : partobra.getOperamountlist()) {
                    objects.add(amount);
                }

                new View(getActivity()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Empty(false, "");
                    }
                }, 200);
            }
        }

        setListViewHeightBasedOnChildren((ListView) listoper);
        PartFragment.this.listobjectsoper.clear();
        PartFragment.this.listobjectsoper.addAll(objects);
        adapteroper.notifyDataSetChanged();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 1;
        View view = null;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);

        if (adapteroper != null)
            adapteroper.notifyDataSetChanged();

        if (adaptermaterial != null)
            adaptermaterial.notifyDataSetChanged();
    }

    @Override
    protected void Fill() {
        adaptermaterial = new ObjectHeaderAdapter<>(getActivity(), listobjectsmaterials, R.layout.cell_amount_part, CellAmountArticlePart.class);
        listmaterial.setAdapter(adaptermaterial);
        adaptermaterial.notifyDataSetChanged();
        fillArticleMaterialList();


        adapteroper = new ObjectHeaderAdapter<>(getActivity(), listobjectsoper, R.layout.cell_amount_oper_part, CellAmountArticleOperPart.class);
        listoper.setAdapter(adapteroper);
        adapteroper.notifyDataSetChanged();
        fillArticleOperList();
    }




    @Override
    protected void Add() {
        listmaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                amMaterial = (adArticleAmount) adaptermaterial.getItem(position);
            }
        });

        listoper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                amOper = (adArticleAmount) adapteroper.getItem(position);
            }
        });

    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.back_list_menu, menu);
        if (partobra != null) {
            if (partobra.getState().toString().contains("Pendiente")) {
                inflater.inflate(R.menu.send_part_menu, menu);
                inflater.inflate(R.menu.save_part_menu, menu);
            }
        }


    }

    @Override
    public void onCreatePartServiceTaskCallBack(Boolean response, int id) {
        Run(false, "");
        if (response) {
            switch (status) {
                case 1:
                    if (getFragmentManager().beginTransaction() != null) {
                        partobra = null;
                        Run(false, "");
                        Toast.makeText(getActivity(), R.string.save_secesfully, Toast.LENGTH_SHORT).show();
                        GoBack();
                    }
                    break;

                case 2:
                    if (getFragmentManager().beginTransaction() != null) {
                        partobra = null;
                        Run(false, "");
                        Toast.makeText(getActivity(), R.string.send_succesfully, Toast.LENGTH_SHORT).show();
                        GoBack();
                    }
                    break;
            }
        } else
            Toast.makeText(getActivity(), R.string.dont_send_part, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(String message) {
        Run(false, "");
        switch (status) {
            case 1:
                Toast.makeText(getActivity(), R.string.dont_save_part, Toast.LENGTH_LONG).show();
                break;

            case 2:
                Toast.makeText(getActivity(), R.string.dont_send_part, Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.back:
                GoBack();
                return true;

            case R.id.send_part:
                SendPart();
                return true;

            case R.id.save_part:
                SavePart();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GoBack() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
        FragmentTransaction ft = super.getFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, PartsListFragment.newInstance(user));
        ft.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Log.i("click", " in Item: position " + i);
    }

    @Override
    public void onResume() {

        super.onResume();
        Load();
        Fill();
        Add();
    }
}