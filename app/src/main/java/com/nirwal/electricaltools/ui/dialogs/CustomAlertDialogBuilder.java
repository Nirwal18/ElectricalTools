package com.nirwal.electricaltools.ui.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.adaptors.AutoCompleteIconTextAdaptor;
import com.nirwal.electricaltools.ui.adaptors.ImageTextItem;
import com.nirwal.electricaltools.ui.model.Load;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class CustomAlertDialogBuilder {
  private final Context _context;

  public CustomAlertDialogBuilder(Context context) {
    this._context = context;
  }

    public MaterialAlertDialogBuilder AddLoadInputDialog(List<ImageTextItem> loadTypes, OnClick handler){

    View v = LayoutInflater.from(_context).inflate(R.layout.dialog_load_data_input,null, false);
    AtomicReference<AutoCompleteTextView> title = new AtomicReference<>();
    AtomicReference<EditText> powerConsumption = new AtomicReference<>();
    AtomicReference<EditText>  quantity = new AtomicReference<>();
    AtomicReference<AutoCompleteTextView> unit = new AtomicReference<>();

    title.set(v.findViewById(R.id.load_title_input));
    powerConsumption.set(v.findViewById(R.id.power_cons_input));
    quantity.set(v.findViewById(R.id.login_phone_input));
    unit.set(v.findViewById(R.id.unit_input));

    title.get().setAdapter(new AutoCompleteIconTextAdaptor(_context,loadTypes));

    unit.get().setAdapter(new ArrayAdapter(
            _context,
            R.layout.auto_complete_text_row,
            new String[]{"mW", "W", "KW"}));


    MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(_context);
    materialAlertDialogBuilder.setTitle("Add Load");
    materialAlertDialogBuilder.setView(v);
    materialAlertDialogBuilder.setNegativeButton("Dismiss", (dialog, which) -> {
      handler.onNegativeBtnClick();
    });
    materialAlertDialogBuilder.setPositiveButton("Add", ((dialog, which) -> {

      ImageTextItem selectedLoadType = new ImageTextItem();

      for (ImageTextItem loadType :loadTypes)
      {
        if(loadType.getTitle().contains(title.get().getText())){
          selectedLoadType = loadType;
        }
      }

      String titleTxt= title.get().getText().toString();
      String quantityTxt = quantity.get().getText().toString();
      String powerTxt = powerConsumption.get().getText().toString();
      String unitTxt = unit.get().getText().toString();
      if(titleTxt.isEmpty()) titleTxt = "Generic device";
      if(quantityTxt.isEmpty()) quantityTxt = "1";
      if(powerTxt.isEmpty()) powerTxt = "0";
      if(unitTxt.isEmpty() ) unitTxt="W";

      // Positive click handler
      handler.onPositiveBtnClick(new Load(
              titleTxt,
              Integer.parseInt(quantityTxt),
              Float.parseFloat(powerTxt),
              unitTxt,
              selectedLoadType
      ));

    }));

      return materialAlertDialogBuilder;
  }

  public interface OnClick{
    void onPositiveBtnClick(Load load);
    void onNegativeBtnClick();
  }

}
