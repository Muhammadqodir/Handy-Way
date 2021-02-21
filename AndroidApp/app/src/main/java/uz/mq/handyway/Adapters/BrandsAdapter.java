package uz.mq.handyway.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.mq.handyway.FillGoods;
import uz.mq.handyway.GoodsActivity;
import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.BrandModel;
import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.R;
import uz.mq.handyway.Utils;

public class BrandsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<BrandModel> items;
    private String ms;
    public int color;
    FillGoods fillGoods;
    public BrandsAdapter(Context context, ArrayList<BrandModel> models, int color, FillGoods fillGoods) {
        this.context = context;
        this.items = models;
        this.color = color;
        this.fillGoods = fillGoods;
        this.ms = "&?="+System.currentTimeMillis();
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.brand_item, parent, false);
        final BrandModel item = items.get(position);
        TextView tvTitle = (TextView) root.findViewById(R.id.tvTitle);
        tvTitle.setText(item.getTitle());
        ((CardView) root.findViewById(R.id.card)).setCardBackgroundColor(color);
        ((CardView) root.findViewById(R.id.card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fillGoods.setBrandId(item.getId());
                fillGoods.run();
            }
        });
        Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+item.getLogo()).error(R.drawable.ic_icons8_error_1).into(((ImageView) root.findViewById(R.id.ivPreview)));
        return root;
    }
}