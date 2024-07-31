package com.holanda.bilecoagmaonlineregistration2021.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.MemoryCategory;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.holanda.bilecoagmaonlineregistration2021.Activities.act_Signature_Chooser;
import com.holanda.bilecoagmaonlineregistration2021.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class sign_Recycler_Adapter extends RecyclerView.Adapter<sign_Recycler_Adapter.sign_viewholder> {

    private Context mContext;
    private ArrayList<sign_chooser_var_link> mFBList;

    private sign_Recycler_Adapter.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);

    }

    public sign_Recycler_Adapter(Context context, ArrayList<sign_chooser_var_link> sampleList, sign_Recycler_Adapter.OnItemClickListener mListener) {
        mContext = context;
        mFBList = sampleList;


        this.mListener = mListener;

    }

    @NonNull
    @Override
    public sign_Recycler_Adapter.sign_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.sig_row_items, parent, false);

        return new sign_Recycler_Adapter.sign_viewholder(v);


    }

    @Override
    public void onBindViewHolder(@NonNull sign_Recycler_Adapter.sign_viewholder holder, int position) {
        sign_chooser_var_link currentItem = mFBList.get(position);

        String ff_img_path = currentItem.getImage_path();
        String ff_img_name = currentItem.getImage_name();



        Glide.with(mContext)
                .load(ff_img_path)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(holder.img_sign);
        holder.txt_filename.setText(ff_img_name);

    }


    public class sign_viewholder extends RecyclerView.ViewHolder {

        public ImageView img_sign;
        public TextView txt_filename;



        public sign_viewholder(@NonNull View itemView) {
            super(itemView);

            img_sign = itemView.findViewById(R.id.img_Signature);
            txt_filename = itemView.findViewById(R.id.tvName);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mListener.onItemClick(getAdapterPosition());

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mFBList.size();
    }
}
