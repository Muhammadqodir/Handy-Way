package uz.mq.handyway.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.R;

public class CategoryAdapter  extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private List<CategoryModel> items;
    private Context ctx;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public CardView card;
        public ImageView ivPreview;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            card = (CardView) view.findViewById(R.id.card);
            ivPreview = (ImageView) view.findViewById(R.id.ivPreview);

        }
    }

    public CategoryAdapter(List<CategoryModel> categoryModels, Context context) {
        this.items = categoryModels;
        this.ctx = context;
        //Init modules
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        int h = parent.getHeight();
        // margin - activity_vertical_margin
        // rows - number of rows in diffrent display modes
        h = (h - Math.round(2*2))/2;

        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) itemView.getLayoutParams();
        params.width = h;
        itemView.setLayoutParams(params);

        return new CategoryAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CategoryAdapter.MyViewHolder holder, final int position) {
        //Fill views
    }

    public void update(){
        notifyItemRangeChanged(0, items.size());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}