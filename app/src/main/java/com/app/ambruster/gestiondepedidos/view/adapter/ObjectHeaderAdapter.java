package com.app.ambruster.gestiondepedidos.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.app.ambruster.gestiondepedidos.R;
import com.app.ambruster.gestiondepedidos.data.model.adArticle;
import com.app.ambruster.gestiondepedidos.data.model.adArticleAmount;
import com.app.ambruster.gestiondepedidos.data.model.adClient;
import com.app.ambruster.gestiondepedidos.data.model.adFamily;
import com.app.ambruster.gestiondepedidos.data.model.adMessage;
import com.app.ambruster.gestiondepedidos.data.model.adPartObra;
import com.app.ambruster.gestiondepedidos.data.model.adRequest;
import com.app.ambruster.gestiondepedidos.view.fragment.Util.Pending;
import com.app.ambruster.gestiondepedidos.view.fragment.Util.Sended;
import com.app.ambruster.gestiondepedidos.view.view.ViewCell;

import java.util.List;


public class ObjectHeaderAdapter<OBJECT> extends ArrayAdapter<OBJECT> {

    private static final int TYPE_OBJECT = 0;
    private static final int TYPE_DIVIDER_PENDING = 1;
    private static final int TYPE_DIVIDER_SEND = 2;

    public Context context;
    public int layoutId;
    public Class cellClass;

    public ObjectHeaderAdapter(Context context, List<OBJECT> objets, int layoutId, Class<? extends ViewCell> cellClass) {
        super(context, layoutId, objets);
        this.context = context;
        this.layoutId = layoutId;
        this.cellClass = cellClass;
    }

    @Override
    public int getCount() {
        int count = 0;
        try {
            count = super.getCount();
        } catch (Exception e) {
        }
        return count;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) == TYPE_OBJECT);
    }

    @Override
    public int getItemViewType(int position) {

        if (getItem(position) instanceof adArticle ||
                getItem(position) instanceof adRequest ||
                getItem(position) instanceof adFamily ||
                getItem(position) instanceof adClient ||
                getItem(position) instanceof adMessage ||
                getItem(position) instanceof adArticleAmount ||
                getItem(position) instanceof adPartObra) {
            return TYPE_OBJECT;

        } else if (getItem(position) instanceof Pending) {
            return TYPE_DIVIDER_PENDING;
        } else  {
            return TYPE_DIVIDER_SEND;
        }

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        int type = getItemViewType(i);

        if (view == null) {
            switch (type) {
                case TYPE_OBJECT:
                    view = View.inflate(context, layoutId, null);
                    break;

                case TYPE_DIVIDER_PENDING:
                    view = View.inflate(context, R.layout.row_header_pending, null);
                    break;

                case TYPE_DIVIDER_SEND:
                    view = View.inflate(context, R.layout.row_header_send, null);
                    break;
            }
        }

        switch (type) {
            case TYPE_OBJECT:
                ViewCell cell = null;
                if (view.getTag() != null && view.getTag() instanceof ViewCell)
                    cell = (ViewCell) view.getTag();
                if (view.getTag() == null) {
                    try {
                        cell = (ViewCell) Class.forName(cellClass.getName()).newInstance();
                        cell.build(context, view);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    view.setTag(cell);
                }
                cell.build(getItem(i), i);
                break;
            case TYPE_DIVIDER_PENDING:
                break;

            case TYPE_DIVIDER_SEND:
                break;
        }
        return view;
    }




}
