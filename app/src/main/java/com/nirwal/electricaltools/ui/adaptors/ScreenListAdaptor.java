package com.nirwal.electricaltools.ui.adaptors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirwal.electricaltools.Interface.IOnClickListener;
import com.nirwal.electricaltools.MainActivity;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.Constants;
import com.nirwal.electricaltools.ui.screen.Screen;

import java.util.ArrayList;
import java.util.List;

public class ScreenListAdaptor extends RecyclerView.Adapter<ScreenListAdaptor.ScreenListViewHolder> {

    private List<Screen> _screenList;
    private Context _context;
    private IOnScreenListClickListener _screenListClickListener ;

    public ScreenListAdaptor(Context context, List<Screen> screenList) {
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

        holder.itemView.setOnClickListener(v -> {

//            if(_context instanceof MainActivity){
//                ((MainActivity)_context).navigateToURI(currentScreen.getUriType(), currentScreen.getUri(), currentScreen);
//            }
            if(_screenListClickListener  != null){
                _screenListClickListener.onListItemClicked(currentScreen);
            }

        });



    }




    @Override
    public int getItemCount() {
        return _screenList == null ? 0: _screenList.size();
    }





    static class ScreenListViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mDesc;
        private ImageView mLogo;
        public Context context;


        public ScreenListViewHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.list_screen_item,parent, false));
            // itemView is super class variable of passing argument super(itemView)
            mTitle = itemView.findViewById(R.id.list_item_title);
            mDesc = itemView.findViewById(R.id.list_item_desc);
            mLogo = itemView.findViewById(R.id.list_item_logo);
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
    public void addOnScreenListClickListener(IOnScreenListClickListener screenListClickListener){
        this._screenListClickListener = screenListClickListener;
    }

    public interface IOnScreenListClickListener{
        void onListItemClicked(Screen screen);
    }



}
