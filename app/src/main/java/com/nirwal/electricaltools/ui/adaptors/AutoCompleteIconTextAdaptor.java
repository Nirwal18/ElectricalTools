package com.nirwal.electricaltools.ui.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.nirwal.electricaltools.R;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteIconTextAdaptor extends ArrayAdapter<ImageTextItem> {
    private static final String TAG = "AutoCompleteIconTextAd";
    private List<ImageTextItem> _itemList;
    public AutoCompleteIconTextAdaptor(@NonNull Context context,  @NonNull List<ImageTextItem> items) {
        super(context,0 ,items);
        this._itemList = new ArrayList<>(items);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return _filter;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null){
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.auto_complete_image_text_row,parent,false);
        }

        TextView title =  convertView.findViewById(R.id.text_view_title);
        ImageView icon = convertView.findViewById(R.id.image_view_icon);
        ImageTextItem item = getItem(position);
        if(item!=null){
            title.setText(item.getTitle());

            if(item.getLogoUrl()!=null && !item.getLogoUrl().isEmpty()){
                Glide.with(convertView)
                        .load(item.getLogoUrl())
                        .fitCenter()
                        .placeholder(R.drawable.ic_baseline_electrical_services_24)
                        .into(icon);
            }

            Log.d(TAG, "getView: ");

        }

        return convertView;
    }

    private Filter _filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<ImageTextItem> suggestions = new ArrayList<>();

            if(constraint== null || constraint.length()==0){
                suggestions.addAll(_itemList);
            }else {
                String filterPattern = constraint.toString().toLowerCase();
                for(ImageTextItem item : _itemList){
                    if(item.title.toLowerCase().contains(filterPattern)){
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList<ImageTextItem>) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((ImageTextItem)resultValue).getTitle();
        }
    };
}
