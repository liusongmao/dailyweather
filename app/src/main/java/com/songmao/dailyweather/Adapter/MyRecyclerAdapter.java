package com.songmao.dailyweather.Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.songmao.dailyweather.R;
import com.songmao.dailyweather.util.NowTmp;

import java.util.List;

/**
 * Created by Acer on 2017/5/10.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private HeaderTmpListener listener;


    public MyRecyclerAdapter(HeaderTmpListener listener){
        this.listener = listener;
    }

    public void setListener(HeaderTmpListener listener){
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView headerTmp;
        private TextView headerName;

        public ViewHolder(View itemView) {
            super(itemView);
            headerTmp = (TextView) itemView.findViewById(R.id.header_tmp);
            headerName = (TextView) itemView.findViewById(R.id.header_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.headerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getViewPager().setCurrentItem(viewHolder.getAdapterPosition());
            }
        });
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NowTmp nowTmp = listener.headerTmp().get(position);
        String tmp = nowTmp.getTmp();;
        String name = nowTmp.getCity();
        holder.headerTmp.setText(name);
        holder.headerName.setText(tmp);
    }

    @Override
    public int getItemCount() {
        return listener.headerTmp().size();
    }

}
