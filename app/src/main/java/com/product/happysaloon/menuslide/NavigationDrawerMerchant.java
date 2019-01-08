package com.product.happysaloon.menuslide;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.product.happysaloon.R;
import com.product.happysaloon.cview.TypefaceTextview;
import com.product.happysaloon.utils.SharedPref;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerMerchant extends RecyclerView.Adapter<NavigationDrawerMerchant.MyViewHolder> {

    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;
    Typeface gothic1, gothicb;
    public static TextView txt_user;
    public static ImageView img_profile;
    public static int selected_item = 0;
    public static Boolean onetime = false;
    Animation move_right, move_left;
    //    int[] myImageList = new int[]{R.mipmap.ic_home, R.mipmap.ic_bookings, R.mipmap.ic_my_account,
//            R.mipmap.ic_chat, R.mipmap.ic_help, R.mipmap.ic_setting, R.mipmap.ic_logout}; //R.drawable.logout_icon
    int[] myImageList = new int[]{R.mipmap.ic_home, R.mipmap.ic_my_account, R.mipmap.ic_logout};
//            , R.mipmap.ic_launcher,
//            R.mipmap.ic_launcher,   R.mipmap.ic_launcher};

    public NavigationDrawerMerchant(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public NavigationDrawerMerchant.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final NavigationDrawerMerchant.MyViewHolder holder, int position) {

        NavDrawerItem current = data.get(position);
        SharedPref mSharedPref = new SharedPref(context);

        holder.title.setText(current.getTitle());
        holder.image.setImageResource(myImageList[position]);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TypefaceTextview title;
        ImageView image;
//        LinearLayout image_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TypefaceTextview) itemView.findViewById(R.id.title);
            image = (ImageView) itemView.findViewById(R.id.icon_image);
        }
    }
}

