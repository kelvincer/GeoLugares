package org.home.geolugares.view;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by Kelvin on 25/11/2017.
 */

public class MultiSpinner extends android.support.v7.widget.AppCompatSpinner implements DialogInterface.OnMultiChoiceClickListener, DialogInterface.OnCancelListener {

    private List<String> items;
    private boolean[] selected;
    private String defaultText;
    private MultiSpinnerListener listener;
    RelativeLayout rtlSpinnerParent;

    public MultiSpinner(Context context) {
        super(context);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1) {
        super(arg0, arg1);
    }

    public MultiSpinner(Context arg0, AttributeSet arg1, int arg2) {
        super(arg0, arg1, arg2);
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        /*if (isChecked)
            selected[which] = true;
        else
            selected[which] = false;*/

        if (isChecked) {

            if (which == 0) {
                for (int i = 1; i < selected.length; i++) {
                    if (selected[i]) {
                        ((AlertDialog) dialog).getListView().setItemChecked(i, false);
                        selected[i] = false;
                    }
                }
            } else {
                ((AlertDialog) dialog).getListView().setItemChecked(0, false);
                selected[0] = false;
                selected[which] = true;
            }
        } else {
            selected[which] = false;
            boolean porLoMenosUnoSelecciondo = false;
            for (int i = 1; i < selected.length; i++) {
                if (selected[i]) {
                    porLoMenosUnoSelecciondo = true;
                    break;
                }
            }

            if (!porLoMenosUnoSelecciondo) {
                ((AlertDialog) dialog).getListView().setItemChecked(0, true);
                selected[0] = true;
            }
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // refresh text on spinner
        StringBuffer spinnerBuffer = new StringBuffer();
        boolean someUnselected = false;
        for (int i = 0; i < items.size(); i++) {
            if (selected[i] == true) {
                spinnerBuffer.append(items.get(i));
                spinnerBuffer.append(", ");
            } else {
                someUnselected = true;
            }
        }
        String spinnerText;
        if (someUnselected) {
            spinnerText = spinnerBuffer.toString();
            if (spinnerText.length() > 2)
                spinnerText = spinnerText.substring(0, spinnerText.length() - 2);
        } else {
            spinnerText = defaultText;
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{spinnerText});
        setAdapter(adapter);
        listener.onItemsSelected(selected);
    }

    @Override
    public boolean performClick() {
        rtlSpinnerParent.requestFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMultiChoiceItems(
                items.toArray(new CharSequence[items.size()]), selected, this);
        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.setOnCancelListener(this);
        builder.show();
        return true;
    }

    public void setItems(List<String> items, String allText,
                         MultiSpinnerListener listener, boolean[] select, RelativeLayout r) {
        this.items = items;
        this.defaultText = allText;
        this.listener = listener;
        rtlSpinnerParent = r;

        // all selected by default
        /*selected = new boolean[items.size()];
        for (int i = 0; i < selected.length; i++)
            selected[i] = true;*/
        selected = select;

        // all text on the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{allText});
        setAdapter(adapter);
    }

    public interface MultiSpinnerListener {
        public void onItemsSelected(boolean[] selected);
    }
}