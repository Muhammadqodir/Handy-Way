package uz.mq.handyway;

import android.graphics.Color;

public class Utils {

    public static int getListColor(int id){
        String[] colors = {"f94144","f3722c","f8961e","f9844a","f9c74f","90be6d","43aa8b","4d908e","577590","277da1"};
        if (id < 10){
            return Color.parseColor("#"+colors[id]);
        }else{
            return getListColor(id-10);
        }
    }
}
