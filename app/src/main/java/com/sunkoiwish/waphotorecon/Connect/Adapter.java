package com.sunkoiwish.waphotorecon.Connect;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunkoiwish.waphotorecon.R;

/**
 * Created by Xaiyeon on 12/9/2017.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    String[] items;

    public Adapter(Context context, String[] items){
        this.context = context;
        this.items = items;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.layout_analyze_a_photo, parent, false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((Item)holder).txtname.setText(items[position]);

    }

    @Override
    public int getItemCount() {
        return items.length;
    }

    // Now we pass in the objects we used here...
    public class Item extends RecyclerView.ViewHolder {

        TextView txtname;
        TextView txttitle;
        TextView txtdate;
        ImageView imgUsersimg;
        TextView txtanalyzed;
        TextView txtdatainfo;
        Button button_analyze;

        public Item(View itemView) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.lay_analyze_name_txtview);
            txttitle = (TextView) itemView.findViewById(R.id.lay_analyze_title_txtview);
            txtdate = (TextView) itemView.findViewById(R.id.lay_analyze_date_txtview);
            imgUsersimg = (ImageView) itemView.findViewById(R.id.lay_analyze_imageView);
            txtanalyzed = (TextView) itemView.findViewById(R.id.lay_analyze_isanalyzed_txtview);
            txtdatainfo = (TextView) itemView.findViewById(R.id.lay_analyze_data_info_txtview);
            button_analyze = (Button) itemView.findViewById(R.id.lay_analyze_analyzeButton);
        }
    }

}
