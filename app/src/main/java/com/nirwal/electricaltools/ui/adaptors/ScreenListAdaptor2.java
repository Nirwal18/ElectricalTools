package com.nirwal.electricaltools.ui.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirwal.electricaltools.Interface.IOnClickListener;
import com.nirwal.electricaltools.MainActivity;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.Constants;
import com.nirwal.electricaltools.ui.model.CategoryItem;
import com.nirwal.electricaltools.ui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class ScreenListAdaptor2 extends RecyclerView.Adapter<ScreenListAdaptor2.ScreenListViewHolder> {

    private List<Screen> _screenList;
    private final Context _context;
    private IOnClickListener _onClickListener;

    public ScreenListAdaptor2(Context context, List<Screen> screenList) {
        this._context = context;
        this._screenList = screenList;
    }

    @NonNull
    @Override
    public ScreenListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ScreenListViewHolder(LayoutInflater.from(parent.getContext()), parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenListViewHolder holder, int position) {
        Screen currentScreen = _screenList.get(position);
        holder.getTitle().setText(currentScreen.getTitle());
        holder.getDescription().setText(currentScreen.getDescription());

        if(currentScreen.getLogoUrl()!=null){
            Glide.with(holder.itemView)
                    .load(currentScreen.getLogoUrl())
                    .placeholder(R.drawable.color_bulb)
                    .fitCenter()
                    .into(holder.getLogo());
        }



        holder.addSubScreensBtn.setEnabled(currentScreen.getUriType() == Constants.URI_TYPE_SCREEN_LIST);

        holder.editBtn.setOnClickListener(v -> {
            if(_onClickListener!=null){
                _onClickListener.onEdit( position);
            }
        });

        holder.addSubScreensBtn.setOnClickListener(v -> {
            if(_onClickListener!=null){
                _onClickListener.onAddScreensList(position);
            }
        });

        holder.deleteBtn.setOnClickListener(v -> {
            removeItem(position);
        });



    }

    public void removeItem(int position){
        notifyItemRemoved(position);
        this._screenList.remove(position);
    }

    public void updateList(List<Screen> screenList){
        this._screenList = screenList;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return _screenList == null ? 0: _screenList.size();
    }


    public void addIonClickListener(IOnClickListener listener){
        this._onClickListener = listener;
    }




    static class ScreenListViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mDesc;
        private ImageView mLogo;
        public Button editBtn, addSubScreensBtn, deleteBtn;
        public Context context;


        public ScreenListViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_screen_item_2,parent, false));
            // itemView is super class variable of passing argument super(itemView)
            mTitle = itemView.findViewById(R.id.list_item_title);
            mDesc = itemView.findViewById(R.id.list_item_desc);
            mLogo = itemView.findViewById(R.id.list_item_logo);
            editBtn = itemView.findViewById(R.id.edit_btn);
            addSubScreensBtn = itemView.findViewById(R.id.add_sub_screen_btn);
            deleteBtn = itemView.findViewById(R.id.delete_btn);
            context = layoutInflater.getContext();
        }
        public TextView getTitle() {
            return mTitle;
        }

        public TextView getDescription() {
            return mDesc;
        }

        public ImageView getLogo() {
            return mLogo;
        }

    }


    public interface IOnClickListener{
        void onEdit(int position);
        void onAddScreensList(int position);
    }

}
