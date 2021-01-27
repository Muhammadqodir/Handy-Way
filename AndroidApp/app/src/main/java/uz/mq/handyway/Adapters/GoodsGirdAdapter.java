package uz.mq.handyway.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import uz.mq.handyway.GoodsActivity;
import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.Models.GoodsModel;
import uz.mq.handyway.R;
import uz.mq.handyway.Utils;

public class GoodsGirdAdapter extends BaseAdapter {
    Context context;
    ArrayList<GoodsModel> models;

    public GoodsGirdAdapter(Context context, ArrayList<GoodsModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.goods_item, viewGroup, false);
        final GoodsModel item = models.get(i);

        ((TextView) root.findViewById(R.id.tvTitle)).setText(item.getTitle());
        ((TextView) root.findViewById(R.id.tvPrice)).setText(Utils.convertPriceToString(item.getPrice())+" "+context.getResources().getString(R.string.summ));
        ((TextView) root.findViewById(R.id.tvQuantity)).setText(String.valueOf(item.getMin_quantity()));
        Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+item.getPic_url()).error(R.drawable.no_image).into(((ImageView) root.findViewById(R.id.ivPic)));
        
        return root;
    }
}
