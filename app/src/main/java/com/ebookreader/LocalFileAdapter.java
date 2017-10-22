package com.ebookreader;

/**
 * Created by Jacky on 2017/10/18.
 */

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class LocalFileAdapter extends BaseAdapter {
    private Context context;
    private List<File> files;
    private LayoutInflater layout;
    private List<String> checkeds;
    private Bitmap mLoadingBitmap;
    private Bitmap mVideoBitmap;

    private ContentResolver contentResolver;

    public LocalFileAdapter(Context cont, List<File> f) {
        this.context = cont;
        this.files = f;

        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_doc);
        mVideoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_video);

        layout = LayoutInflater.from(context);
        checkeds = new ArrayList<String>();
        contentResolver = cont.getContentResolver();
        // 获取应用程序最大可用内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable drawable) {
                return drawable.getBitmap().getByteCount();
            }
        };
    }

    public boolean isChecked() {
        return !checkeds.isEmpty();
    }

    public void clearSelectStatus() {
        checkeds.clear();
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public Object getItem(int position) {
        return files.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewH vh = null;
        if (convertView == null) {
            vh = new ViewH();
            convertView = layout.inflate(R.layout.fragment_pic_item, parent, false);
            vh.list_file_imag = (ImageView) convertView.findViewById(R.id.pic_grid_item);
            vh.list_file_name = (TextView) convertView.findViewById(R.id.text_grid_item);
            convertView.setTag(vh);

        } else {
            vh = (ViewH) convertView.getTag();
        }
        File curFile = files.get(position);
        String fileName = files.get(position).getName();

        String[] names = fileName.split("\\.");

        String fileOtherName=names[0];     //得到不带后缀的文件名

        vh.list_file_name.setText(fileOtherName);

            vh.list_file_imag.setImageResource(R.drawable.filetype_doc);


            if (getFileType(curFile.getName()) != 0) {//判断是否是图片或者是视屏
                BitmapDrawable drawable = getBitmapFromMemoryCache(curFile.getAbsolutePath());//先查看缓存是否有
                if (drawable != null) {
                    vh.list_file_imag.setImageDrawable(drawable);
                } else if (cancelPotentialWork(curFile.getAbsolutePath(), vh.list_file_imag)) {//没有就异步缓存
                    BitmapWorkerTask task = new BitmapWorkerTask(vh.list_file_imag);
                    AsyncDrawable asyncDrawable = new AsyncDrawable(context.getResources(), mLoadingBitmap, task);
                    vh.list_file_imag.setImageDrawable(asyncDrawable);
                    task.execute(curFile.getAbsolutePath());
                }
            }
        return convertView;
    }

    public static class ViewH {
        ImageView list_file_imag;
        TextView list_file_name;
    }


    /**
     * 获取视频缩略图
     *
     * @param videoPath
     * @return
     */
    private Bitmap getVideoThumbnail(String videoPath) {
        Bitmap bitmap = null;
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, Images.Thumbnails.MINI_KIND);
        // bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
        // ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
    }


    /**
     * 图片缓存技术的核心类，用于缓存所有下载好的图片，在程序内存达到设定值时会将最少最近使用的图片移除掉。
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;

    /**
     * 异步下载图片的任务。
     *
     * @author guolin
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        String imageUrl;

        private WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            // 在后台开始下载图片
            Bitmap bitmap = null;

                bitmap=getVideoThumbnail(imageUrl);
                if (bitmap == null) {
                    bitmap = mVideoBitmap;
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
    private int getFileType(String fileName) {
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