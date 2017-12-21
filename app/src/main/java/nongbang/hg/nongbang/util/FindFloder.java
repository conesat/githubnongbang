package nongbang.hg.nongbang.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-08-16.
 */

public class FindFloder {
    public static ArrayList<File> FindFromFloder(String path) {
        ArrayList<File> files = new ArrayList<File>();
        try {
            getFiles(files, path);
            File latestSavedImage = files.get(0);
            if (latestSavedImage.exists()) {
                for (int i = 1; i < files.size(); i++) {
                    File nextFile = files.get(i);
                    if (nextFile.lastModified() > latestSavedImage.lastModified()) {
                        latestSavedImage = nextFile;
                    }
                }
                Bitmap bm = BitmapFactory.decodeFile(String.valueOf(latestSavedImage));
                // 将图片显示到ImageView中
               // photozs.setImageBitmap(bm);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    public static void getFiles(ArrayList<File> fileList, String path) {
        File[] allFiles = new File(path).listFiles();
        for (int i = 0; i < allFiles.length; i++) {
            File file = allFiles[i];
            if (file.isFile()) {
                fileList.add(file);
            } else if (!file.getAbsolutePath().contains(".thumnail")) {
                getFiles(fileList, file.getAbsolutePath());
            }
        }
    }
}
