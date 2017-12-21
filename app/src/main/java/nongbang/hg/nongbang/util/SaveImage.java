package nongbang.hg.nongbang.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2017-08-16.
 */

public class SaveImage {
    String  part=null;
    public SaveImage(String fil){
        part=fil;
        File file=new File(part);
        if(!file.exists()){
            try {
                file.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 存储资源为ID的图片
     * @param
     * @param name
     */
    public void saveDrawable(Drawable dr, String name, Bitmap.CompressFormat format) {
        Drawable drawable = dr;
        Bitmap bitmap = drawableToBitmap(drawable);
        saveBitmap(bitmap, name, format);
    }


    /**
     * 将Drawable转化为Bitmap
     * @param drawable
     * @return
     */
    public Bitmap drawableToBitmap(Drawable drawable) {
        if(drawable == null)
            return null;
        return ((BitmapDrawable)drawable).getBitmap();
    }

    /**
     * 将Bitmap以指定格式保存到指定路径
     * @param bitmap
     * @param
     */
    public void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format) {
        // 创建一个位于SD卡上的文件
        File file = new File(part,
                name);

        FileOutputStream out = null;
        try{
            // 打开指定文件输出流
            out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(format, 100,
                    out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
