package com.ebookreader;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.LruCache;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Jacky on 2017/11/18.
 */

public class Image_Adapter {

    private ContentResolver contentResolver;
    private Bitmap mLoadingBitmap;
    private Bitmap mVideoBitmap;
    private Bitmap mImageBitmap;
    private Context context;


    public Image_Adapter(Context cont){
        this.context=cont;

        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_doc);
        mImageBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_image);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_doc);
        mVideoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_video);

        contentResolver = cont.getContentResolver();
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        if(mMemoryCache==null){
            mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
                @Override
                protected int sizeOf(String key, BitmapDrawable drawable) {
                    return drawable.getBitmap().getByteCount();
                }
            };}
    }



    public void setDrawable(String picPath,ImageView imageView){
        BitmapDrawable drawable = getBitmapFromMemoryCache(picPath);//先查看缓存是否有

        if (drawable != null) {
            imageView.setImageDrawable(drawable);

        } else if (this.cancelPotentialWork(picPath, imageView)) {//没有就异步缓存

            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mLoadingBitmap, task);
            imageView.setImageDrawable(asyncDrawable);
            task.execute(picPath);
        }
    }

    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
        // bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        // ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }

    /**
     * 图片缩略图获取
     *
     * @param myPath
     * @return
     */
    public Bitmap getImageThumbnail(String myPath) {
        String[] projection = { MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID, };
        String whereClause = MediaStore.Images.Media.DATA + "='" + myPath + "'";
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause,
                null, null);
        int _id = 0;
        String imagePath = "";
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        int id = 0;
        if (cursor.moveToFirst()) {

            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            int _dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);

            _id = cursor.getInt(_idColumn);
            imagePath = cursor.getString(_dataColumn);
            if (imagePath.equals(myPath)) {
                id = _id;
            }
        }
        cursor.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MINI_KIND,
                options);
        return bitmap;
    }


    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private static LruCache<String, BitmapDrawable> mMemoryCache;

    /**
     * 异步下载图片的任务。
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        String imageUrl;

        private WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }
        public BitmapWorkerTask(){

        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            // 在后台开始下载图片
            Bitmap bitmap = null;

            if (getFileType(imageUrl) == 2) {
                bitmap = getImageThumbnail(imageUrl);
                if (bitmap == null) {
                    bitmap = mImageBitmap;
                }
            } else {
                bitmap = getVideoThumbnail(imageUrl);
                if (bitmap == null) {
                    bitmap = mVideoBitmap;
                }
            }

            BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);

            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = getAttachedImageView();
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }

        /**
         * 获取当前BitmapWorkerTask所关联的ImageView。
         */
        private ImageView getAttachedImageView() {
            ImageView imageView = imageViewReference.get();
            BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask) {
                return imageView;
            }
            return null;
        }

    }

    /**
     * 自定义的一个Drawable，让这个Drawable持有BitmapWorkerTask的弱引用。
     */
    class AsyncDrawable extends BitmapDrawable {

        private WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;

        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
            super(res, bitmap);
            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
        }

        public BitmapWorkerTask getBitmapWorkerTask() {
            return bitmapWorkerTaskReference.get();
        }

    }

    /**
     * 获取传入的ImageView它所对应的BitmapWorkerTask。
     */
    private BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AsyncDrawable) {
                AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    /**
     * 取消掉后台的潜在任务，当认为当前ImageView存在着一个另外图片请求任务时 ，则把它取消掉并返回true，否则返回false。
     */
    public boolean cancelPotentialWork(String url, ImageView imageView) {
        BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        if (bitmapWorkerTask != null) {
            String imageUrl = bitmapWorkerTask.imageUrl;
            if (imageUrl == null || !imageUrl.equals(url)) {
                bitmapWorkerTask.cancel(true);
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 将一张图片存储到LruCache中。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @param drawable
     *            LruCache的值，这里传入从网络上下载的BitmapDrawable对象。
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(key) == null) {
            System.out.println(mMemoryCache.size());

            mMemoryCache.put(key, drawable);

        }
    }

    /**
     * 从LruCache中获取一张图片，如果不存在就返回null。
     *
     * @param key
     *            LruCache的键，这里传入图片的URL地址。
     * @return 对应传入键的BitmapDrawable对象，或者null。
     */
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     *
     * @param fileName
     * @return 0表示其他 1表示视屏2表示图片
     */
    public int getFileType(String fileName) {
        int filetype = 0;
        String[] names = fileName.split("\\.");
        if (names.length == 1) {// 没有后缀名
            return 0;
        }
        String type = names[names.length - 1];// 拿到后缀名
        if (type.equalsIgnoreCase("JPGE") || type.equalsIgnoreCase("PNG") || type.equalsIgnoreCase("GIF")
                || type.equalsIgnoreCase("BMP") || type.equals("webp") || type.equalsIgnoreCase("jpg")) {
            return 2;
        } else if (type.equalsIgnoreCase("mp4") || type.equalsIgnoreCase("3gp") || type.equalsIgnoreCase("mpg")
                || type.equalsIgnoreCase("rmvb") || type.equalsIgnoreCase("mov") || type.equalsIgnoreCase("avi")) {
            return 1;
        }

        return filetype;
    }
}
