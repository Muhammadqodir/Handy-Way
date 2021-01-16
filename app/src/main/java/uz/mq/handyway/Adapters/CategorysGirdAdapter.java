package uz.mq.handyway.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;

import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.R;
import uz.mq.handyway.Utils;

public class CategorysGirdAdapter  extends BaseAdapter {

    private Context context;
    private ArrayList<CategoryModel> items;

    public CategorysGirdAdapter(Context context, ArrayList<CategoryModel> models) {
        this.context = context;
        this.items = models;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = layoutInflater.inflate(R.layout.category_item, parent, false);

        CategoryModel item = items.get(position);

        TextView tvTitle = (TextView) root.findViewById(R.id.tvTitle);
        tvTitle.setText(item.getTitle());
        ((CardView) root.findViewById(R.id.card)).setCardBackgroundColor(Utils.getListColor(item.getId()));
        return root;
    }
}