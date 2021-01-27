package uz.mq.handyway.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.mq.handyway.GoodsActivity;
import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.R;
import uz.mq.handyway.Utils;

public class CategorysGirdAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<CategoryModel> items;
    private String ms;
    public CategorysGirdAdapter(Context context, ArrayList<CategoryModel> models) {
        this.context = context;
        this.items = models;
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
        View root = layoutInflater.inflate(R.layout.category_item, parent, false);
        final CategoryModel item = items.get(position);
        TextView tvTitle = (TextView) root.findViewById(R.id.tvTitle);
        tvTitle.setText(item.getTitle());
        ((CardView) root.findViewById(R.id.card)).setCardBackgroundColor(Utils.getListColor(item.getId()));
        ((CardView) root.findViewById(R.id.card)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).startActivity(new Intent(context, GoodsActivity.class).putExtra("category_id", item.getId()).putExtra("title", item.getTitle()));
            }
        });
        Picasso.get().load(HandyWayAPI.BASE_URL+"category_preview_master/get_preview.php?category_id="+item.getId()+ms).error(R.drawable.ic_icons8_error_1).into(((ImageView) root.findViewById(R.id.ivPreview)));
        return root;
    }
}