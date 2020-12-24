package com.lxkj.shortvideo.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.UUID;


public class BitmapUtil {

    private static final String SD_PATH = "/sdcard/dskqxt/pic/";
    private static final String IN_PATH = "/dskqxt/pic/";
    /**
     * 保存bitmap到本地
     *
     * @param context
     * @param mBitmap
     * @return
     */
    public static String saveBitmap(Context context, Bitmap mBitmap) {
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = SD_PATH;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + generateFileName() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }



    //把布局变成Bitmap
    public static Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    /**
     * 随机生产文件名
     *
     * @return
     */
    private static String generateFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url, Context context) {


        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    /**
     * @param pathName 文件路径
     * @param width    宽度限制
     * @param height   高度限制
     * @return 先解码为尺寸接近的Bitmap，然后inside
     */
    public static Bitmap decodeInside(final String pathName, final int width, final int height) {
        final Bitmap bmp4Crop = decodeSmall(pathName, width, height);
        Bitmap result = null;
        if (null != bmp4Crop) {
            try {
                result = scaledInside(bmp4Crop, width, height);
            } catch (Exception e) {
                UiTool.recycleBitmap(bmp4Crop);
            }
        }
        return result;
    }

    /**
     * @param pathName 文件路径
     * @param width    宽度限制
     * @param height   高度限制
     * @return 先解码为尺寸接近的Bitmap，然后crop
     */
    public static Bitmap decodeCrop(final String pathName, final int width, final int height) {
        final Bitmap bmp4Crop = decodeSmall(pathName, width, height);
        Bitmap result = null;
        if (null != bmp4Crop) {
            try {
                result = scaledCrop(bmp4Crop, width, height);
            } catch (Exception e) {
                UiTool.recycleBitmap(bmp4Crop);
            }
        }
        return result;
    }

    public static Bitmap decodeSquare(final String pathName, final int width) {
        return decodeCrop(pathName, width, width);
    }

    private static Bitmap scaledCrop(final Bitmap source, final int width, final int height) {
        float w = source.getWidth(), h = source.getHeight();
        float scaleW = width / w, scaleH = height / h;
        float scale;
        int x = 0, y = 0;
        if (scaleW - scaleH > 0.01f) {
            scale = scaleW;
            if (height > 0) {
                y = (int) ((h - height / scale) / 2);
            }
            h = height / scale;
        } else if (scaleW - scaleH < -0.01f) {
            scale = scaleH;
            if (w > 0) {
                x = (int) ((w - width / scale) / 2);
            }
            w = width / scale;
        } else {
            scale = scaleH;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(source, x, y, (int) w, (int) h, matrix, true);
        } catch (Exception e) {
            UiTool.recycleBitmap(source);
        }
        return result;
    }

    public static Bitmap scaledInside(final Bitmap source, final int width, final int height) {
        float w = source.getWidth(), h = source.getHeight();
        float scaleW = width / w, scaleH = height / h;
        float scale;
        if (scaleW - scaleH < -0.01f) {
            scale = scaleW;
        } else {
            scale = scaleH;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap result = null;
        try {
            result = Bitmap.createBitmap(source, 0, 0, (int) w, (int) h, matrix, true);
        } catch (Exception e) {
            UiTool.recycleBitmap(source);
        }
        return result;
    }

    @SuppressWarnings(value = "unused")
    @Deprecated
    public static boolean compressImgFile(final String src,
                                          final File dest,
                                          final long maxLenght,
                                          final int maxSize) {

        ByteArrayOutputStream baos = null;
        try {
            baos = compressImageToOS(1000,1000,src, maxLenght, maxSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == baos) {
            return false;
        }
        try {
            return writeBaosToFile(dest, baos);
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    public static ByteArrayOutputStream compressImageToOS(int w, int h, final String src, final long maxPixels, final int maxSide) {
        BitmapFactory.decodeFile("");
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap;
        float hh = 1000;// 设置高度为240f时，可以明显看到图片缩小了
        float ww = 1000;// 设置宽度为120f，可以明显看到图片缩小了
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        bitmap = decodeInside(src, maxSide, maxSide);
        final CompressFormat format = awareFormat(src);
        int options = 100;
        bitmap.compress(format, options, os);
        while (options > 0 && os.toByteArray().length > maxPixels) {
            os.reset();
            options -= 5;
            bitmap.compress(format, options, os);
        }
        bitmap.recycle();
        return os;
    }

    /**
     * 质量压缩方法
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 50) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            image.compress(CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        int length = baos.toByteArray().length;
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 通过uri获取图片并进行压缩
     *
     * @param uri
     */
    public static Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (originalWidth > originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (originalWidth / ww);
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (originalHeight / hh);
        }
        if (be <= 0)
            be = 1;
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();

        return compressImage(bitmap);//再进行质量压缩
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    @SuppressWarnings(value = "unused")
    @Deprecated
    private static boolean writeBaosToFile(final File dest,
                                           final ByteArrayOutputStream baos) throws Exception {
        if (null == dest) {
            return false;
        }
        if (!dest.exists()) {
            dest.createNewFile();
        }
        final FileOutputStream fos = new FileOutputStream(dest);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        return true;
    }

    private static Bitmap decodeSmall(final String pathName, final int width, final int height) {
        final File dst = new File(pathName);
        if (null == dst || !dst.exists()) {
            return null;
        }
        BitmapFactory.Options opts = null;
        if (width > 0 && height > 0) {
            opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(pathName, opts);
            opts.inSampleSize = computeSampleSize(opts, width, height);
            opts.inJustDecodeBounds = false;
            opts.inInputShareable = true;
            opts.inPurgeable = true;
        }
        try {
            return BitmapFactory.decodeFile(pathName, opts);
        } catch (Throwable t) {
        }
        return null;
    }

    private static int computeSampleSize(BitmapFactory.Options options, int aimW, int aimH) {
        double w = options.outWidth;
        double h = options.outHeight;
        int initialSize = (int) Math.min(Math.floor(w / aimW), Math.floor(h / aimH));
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }


    public static CompressFormat awareFormat(final String name) {
        if (!StringUtil.isEmpty(name)) {
            CompressFormat format = awareMagic(name);
            if (null == format) {
                format = awareFileName(name);
            }
            if (null != format) {
                return format;
            }
        }
        return CompressFormat.JPEG;
    }

    /**
     * 分析文件扩展名，只能得到该文件名声称的压缩格式，不准确
     */
    private static CompressFormat awareFileName(final String name) {
        int split = name.lastIndexOf('.');
        if (split > 0 && split < name.length()) {
            if (name.substring(split + 1)
                    .toLowerCase(Locale.getDefault()).contains("png")) {
                return CompressFormat.PNG;
            } else {
                return CompressFormat.JPEG;
            }
        }
        return null;
    }

    /**
     * 分析文件二进制头中的‘魔数’，得到准确的压缩格式
     */
    private static CompressFormat awareMagic(String name) {
        return awareMagic(null, name);
    }

    /**
     * 分析文件二进制头中的‘魔数’，得到准确的压缩格式
     */
    public static CompressFormat awareMagic(Context ctx, String name) {
        final char[] buffer = FileUtil.readMagic(ctx, name, 8);
        if (null != buffer) {
            if (isMagicJpg(buffer)) {
                return CompressFormat.JPEG;
            }
            if (isMagicPng(buffer)) {
                return CompressFormat.PNG;
            }
        }
        return null;
    }

    private static boolean isMagicPng(final char[] buffer) {
        return MAGIC_PNG.equals(buffer);
    }

    private static boolean isMagicJpg(final char[] buffer) {
        return buffer[0] == MAGIC_JPG[0] && buffer[1] == MAGIC_JPG[1];
    }

    private final static char[] MAGIC_JPG = {0xFF, 0xd8};
    private final static char[] MAGIC_PNG = {0x89, 0x50, 0x4E, 0x47,
            0x0D, 0x0A, 0x1A, 0x0A};


}