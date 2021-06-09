package com.nirwal.electricaltools.ui.adaptors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.nirwal.electricaltools.MainActivity;
import com.nirwal.electricaltools.R;
import com.nirwal.electricaltools.ui.screen.Screen;
import com.nirwal.electricaltools.ui.bottomsheet.ScreenListBottomSheet;
import com.nirwal.electricaltools.ui.model.CategoryItem;

import java.util.List;

public class CategoryAdaptor extends RecyclerView.Adapter<CategoryAdaptor.CategoryViewHolder> {
    private static final String TAG = "CategoryAdaptor";
    private List<CategoryItem> _items;
    private Context _context;
    private boolean _isEditing = false;
    private IOnButtonClickListener _buttonClickListener;
    private IOnEditPnlListener _editPnlListener;

    public CategoryAdaptor(Context context,List<CategoryItem> items) {

        this._items = items;
        this._context = context;
    }
    public CategoryAdaptor(Context context,List<CategoryItem> items, boolean isEditing) {
        this._items = items;
        this._context = context;
        this._isEditing = isEditing;
    }



    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate( R.layout.item_catagory,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem item = _items.get(position);
        Log.d(TAG, "onBindViewHolder: "+item.title);
        holder.title.setText(item.title);

        ((TextView)holder.btn8.getChildAt(1)).setText("See more");

        //SEE ALL Button clicked
        holder.btn8.setOnClickListener(v -> {

            if(_buttonClickListener!=null){
                _buttonClickListener.onSeeAllBtnClicked(item.getTitle(), item.screens);
            }

        });

        ((TextView)holder.btn1.getChildAt(1)).setText(item.screens.get(0).getTitle());
        ((TextView)holder.btn2.getChildAt(1)).setText(item.screens.get(1).getTitle());
        ((TextView)holder.btn3.getChildAt(1)).setText(item.screens.get(2).getTitle());
        ((TextView)holder.btn4.getChildAt(1)).setText(item.screens.get(3).getTitle());
        ((TextView)holder.btn5.getChildAt(1)).setText(item.screens.get(4).getTitle());
        ((TextView)holder.btn6.getChildAt(1)).setText(item.screens.get(5).getTitle());
        ((TextView)holder.btn7.getChildAt(1)).setText(item.screens.get(6).getTitle());

        Glide.with(_context).load(item.screens.get(0).getLogoUrl()).fitCenter().placeholder(R.drawable.ic_build_circle_24).into((ImageView) holder.btn1.getChildAt(0));
        Glide.with(_context).load(item.screens.get(1).getLogoUrl()).fitCenter().placeholder(R.drawable.ic_build_circle_24).into((ImageView) holder.btn2.getChildAt(0));
        Glide.with(_context).load(item.screens.get(2).getLogoUrl()).fitCenter().placeholder(R.drawable.ic_build_circle_24).into((ImageView) holder.btn3.getChildAt(0));
        Glide.with(_context).load(item.screens.get(3).getLogoUrl()).fitCenter().placeholder(R.drawable.ic_build_circle_24).into((ImageView) holder.btn4.getChildAt(0));
        Glide.with(_context).load(item.screens.get(4).getLogoUrl()).fitCenter().placeholder(R.drawable.ic_build_circle_24).into((ImageView) holder.btn5.getChildAt(0));
        Glide.with(_context).load(item.screens.get(5).getLogoUrl()).fitCenter().placeholder(R.drawable.ic_build_circle_24).into((ImageView) holder.btn6.getChildAt(0));
        Glide.with(_context).load(item.screens.get(6).getLogoUrl()).fitCenter().placeholder(R.drawable.ic_build_circle_24).into((ImageView) holder.btn7.getChildAt(0));

        setOnBtnClick(holder.btn1,item.screens.get(0));
        setOnBtnClick(holder.btn2,item.screens.get(1));
        setOnBtnClick(holder.btn3,item.screens.get(2));
        setOnBtnClick(holder.btn4,item.screens.get(3));
        setOnBtnClick(holder.btn5,item.screens.get(4));
        setOnBtnClick(holder.btn6,item.screens.get(5));
        setOnBtnClick(holder.btn7,item.screens.get(6));

        if(_isEditing){
            holder.editBtnCont.setVisibility(View.VISIBLE);
            holder.editBtn.setOnClickListener(v -> {
                if(_editPnlListener!=null) _editPnlListener.onEditClick(item);
            });

            holder.deleteBtn.setOnClickListener(v -> {
                if(_editPnlListener!=null) _editPnlListener.onDeleteClick(item);
            });

        }




    }

    private void setOnBtnClick(View v, Screen screen){
        v.setOnClickListener(v1 -> {
//            if(_context instanceof MainActivity){
//                ((MainActivity)_context).navigateToURI(screen.getUriType(), screen.getUri(), screen);
//            }

            if(_buttonClickListener!=null){
                _buttonClickListener.onFeatureBtnClick(screen);
            }


        });
    }

    @Override
    public int getItemCount() {
        return _items==null ? 0 : _items.size();
    }


    public void updateData(List<CategoryItem> data){
        this._items = data;
        notifyDataSetChanged();
    }


    class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        LinearLayout btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, editBtnCont;
        Button editBtn, deleteBtn;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_item_title);
            btn1 = itemView.findViewById(R.id.category_btn_1);
            btn2 = itemView.findViewById(R.id.category_btn_2);
            btn3 = itemView.findViewById(R.id.category_btn_3);
            btn4 = itemView.findViewById(R.id.category_btn_4);
            btn5 = itemView.findViewById(R.id.category_btn_5);
            btn6 = itemView.findViewById(R.id.category_btn_6);
            btn7 = itemView.findViewById(R.id.category_btn_7);
            btn8 = itemView.findViewById(R.id.category_btn_8);

            editBtnCont = itemView.findViewById(R.id.btn_layout_admin);
            editBtn = itemView.findViewById(R.id.edit_catagory_btn);
            deleteBtn= itemView.findViewById(R.id.delete_catagory_btn);


        }
    }

    public void addOnButtonClickListener(IOnButtonClickListener buttonClickListener){
        this._buttonClickListener = buttonClickListener;
    }

    public void addOnEditPNLClickListener(IOnEditPnlListener editPnlListener){
        this._editPnlListener = editPnlListener;
    }




    public interface IOnEditPnlListener{
        void onEditClick(CategoryItem categoryItem);
        void onDeleteClick(CategoryItem categoryItem);
    }

    public interface IOnButtonClickListener{
        void onFeatureBtnClick(Screen screen);
        void onSeeAllBtnClicked(String title, List<Screen> screenList);
    }
}
