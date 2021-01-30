package uz.mq.handyway.Adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.ZipFile;

import uz.mq.handyway.CartUtils;
import uz.mq.handyway.GoodsActivity;
import uz.mq.handyway.HandyWayAPI;
import uz.mq.handyway.Models.CartModel;
import uz.mq.handyway.Models.CategoryModel;
import uz.mq.handyway.Models.GoodsModel;
import uz.mq.handyway.R;
import uz.mq.handyway.Utils;
import uz.mq.handyway.ViewGoodActivity;

public class GoodsGirdAdapter extends BaseAdapter {
    Context context;
    ArrayList<GoodsModel> models;
    View cartParent;
    TextView tvCart;

    public GoodsGirdAdapter(Context context, ArrayList<GoodsModel> models, View cartParent, TextView tvCart) {
        this.context = context;
        this.models = models;
        this.cartParent = cartParent;
        this.tvCart = tvCart;
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
        final View root = layoutInflater.inflate(R.layout.goods_item, viewGroup, false);
        final GoodsModel item = models.get(i);

        final TextView tvQuantity = (TextView) root.findViewById(R.id.tvQuantity);
        if (item.getMax_quantity() > 0){
            tvQuantity.setTextColor(Color.parseColor("#444444"));
            tvQuantity.setText(item.getMin_quantity()+"");
            ((ImageView) root.findViewById(R.id.btnMinus)).setVisibility(View.VISIBLE);
            ((ImageView) root.findViewById(R.id.btnPlus)).setVisibility(View.VISIBLE);
            ((Button) root.findViewById(R.id.btnBuy)).setEnabled(true);
            ((Button) root.findViewById(R.id.btnBuy)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addToCart(item.getId(), Integer.parseInt(tvQuantity.getText().toString()), item.getPrice());
                }
            });
            ((ImageButton) root.findViewById(R.id.btnMinus)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = Integer.parseInt(tvQuantity.getText().toString()) - 1;
                    if (quantity >= item.getMin_quantity()){
                        tvQuantity.setText(quantity+"");
                    }else {
                        Toast.makeText(context, context.getResources().getString(R.string.min_quanity)+": "+item.getMin_quantity(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ((ImageButton) root.findViewById(R.id.btnPlus)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int quantity = Integer.parseInt(tvQuantity.getText().toString()) + 1;
                    if (quantity <= item.getMax_quantity()){
                        tvQuantity.setText(quantity+"");
                    }else {
                        Toast.makeText(context, context.getResources().getString(R.string.max_quanity)+": "+item.getMin_quantity(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            tvQuantity.setTextColor(context.getResources().getColor(R.color.colorDanger));
            tvQuantity.setText(R.string.not_available);
            ((Button) root.findViewById(R.id.btnBuy)).setEnabled(false);
            ((ImageView) root.findViewById(R.id.btnMinus)).setVisibility(View.GONE);
            ((ImageView) root.findViewById(R.id.btnPlus)).setVisibility(View.GONE);
        }

        ((TextView) root.findViewById(R.id.tvTitle)).setText(item.getTitle());
        ((ImageView) root.findViewById(R.id.ivPic)).setBackgroundColor(Color.parseColor("#00ffffff"));
        ((TextView) root.findViewById(R.id.tvPrice)).setText(Utils.convertPriceToString(item.getPrice())+" "+context.getResources().getString(R.string.summ));
        Picasso.get().load(HandyWayAPI.BASE_URL_MEDIA+item.getPic_url()).error(R.drawable.no_image).into(((ImageView) root.findViewById(R.id.ivPic)), new Callback() {
            @Override
            public void onSuccess() {
                ((ImageView) root.findViewById(R.id.ivPic)).setBackgroundColor(Color.parseColor("#ffffff"));
            }

            @Override
            public void onError(Exception e) {

            }
        });

        ((CardView) root.findViewById(R.id.cardParent)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).startActivity(new Intent(context, ViewGoodActivity.class).putExtra("id", item.getId()).putExtra("title", item.getTitle()));
            }
        });
        return root;
    }

    private void addToCart(int id, int quantity, int price){
        CartUtils.addToCart(context, new CartModel(id, quantity, price));
        tvCart.setText(CartUtils.getCartQuantity(context)+"");
        cartParent.animate().scaleX(1.5f).scaleY(1.5f).setDuration(100).withEndAction(new Runnable() {
            @Override
            public void run() {
                cartParent.animate().scaleX(1).scaleY(1).setDuration(500);
            }
        });
    }
}
