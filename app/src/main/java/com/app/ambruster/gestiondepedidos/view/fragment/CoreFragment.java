package com.app.ambruster.gestiondepedidos.view.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.ambruster.gestiondepedidos.R;


public abstract class CoreFragment extends Fragment implements View.OnClickListener {

    protected View fragmentView;
    private MaterialDialog dialog;

    public void setFragmentView(View fragmentView) {
        this.fragmentView = fragmentView;
    }

    public View findViewById(int id) {
        if (fragmentView != null)
            return fragmentView.findViewById(id);
        else
            return null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    protected abstract void Load();

    protected abstract void Fill();

    protected abstract void Add();

    public void Empty(boolean information, String message) {
        try {
            if (information) {
                fragmentView.findViewById(R.id.empty_fragment).setVisibility(View.VISIBLE);
                TextView text = (TextView) fragmentView.findViewById(R.id.empty_text);

                if (!message.equals(""))
                    text.setText(message);

            } else {
                fragmentView.findViewById(R.id.empty_fragment).setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public void Empty_Error(boolean information) {
        try {
            if (information) {
                fragmentView.findViewById(R.id.empty_fragment_retry).setVisibility(View.VISIBLE);
            } else {
                fragmentView.findViewById(R.id.empty_fragment_retry).setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    protected void Run(boolean process, String message) {
        Run(process, R.id.loader, message);
    }

    protected void Run(boolean process, int id, String message) {

        final View runningView = findViewById(id);
        runningView.setVisibility(View.GONE);

        if (process) {
            this.dialog = new MaterialDialog.Builder(getActivity())
                    .content(message)
                    .cancelable(false)
                    .widgetColor(getActivity().getResources().getColor(R.color.bg_main))
                    .progress(true, 0)
                    .show();
        } else {
            this.dialog.dismiss();
        }
    }

    abstract void onError(String message);
}
