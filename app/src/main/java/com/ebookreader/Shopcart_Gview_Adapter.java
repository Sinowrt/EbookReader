package com.ebookreader;


        import java.util.List;
        import android.content.Context;

        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.BitmapDrawable;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;


public class Shopcart_Gview_Adapter extends BaseAdapter
{

    private Image_Adapter image_adapter;
    private LayoutInflater mInflater;
    private Context mContext;
    private List<Shopcart_Activity.Content> mDatas;
    private Bitmap mLoadingBitmap;

    public Shopcart_Gview_Adapter(Context context, List<Shopcart_Activity.Content> mDatas)
    {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
        image_adapter=new Image_Adapter(context);
        mLoadingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.filetype_doc);
    }

    @Override
    public int getCount()
    {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position)
    {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.shopcart_gview_item, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.imageView=(ImageView) convertView.findViewById(R.id.shopcart_item_image);
            viewHolder.item_number=(TextView) convertView.findViewById(R.id.item_number);
            viewHolder.bookname = (TextView) convertView.findViewById(R.id.bookname);
            viewHolder.author = (TextView) convertView.findViewById(R.id.author);
            viewHolder.price = (TextView) convertView.findViewById(R.id.scart_price);
            viewHolder.number = (TextView) convertView.findViewById(R.id.number);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.item_number.setText("商品编号："+mDatas.get(position).item_number);
        viewHolder.bookname.setText("书名：《"+mDatas.get(position).bookname+"》");
        viewHolder.author.setText("作者："+mDatas.get(position).author);
        viewHolder.price.setText("价格："+mDatas.get(position).price);
        viewHolder.number.setText("购买数量："+mDatas.get(position).number);

        String image_path=mDatas.get(position).imagePath;

        Log.d("Tag",image_path);

        BitmapDrawable drawable = image_adapter.getBitmapFromMemoryCache(image_path);//先查看缓存是否有



        if (drawable != null) {

            viewHolder.imageView.setImageDrawable(drawable);
        } else if (image_adapter.cancelPotentialWork(image_path, viewHolder.imageView)) {//没有就异步缓存

            Image_Adapter.BitmapWorkerTask task = image_adapter.new BitmapWorkerTask(viewHolder.imageView);
            Image_Adapter.AsyncDrawable asyncDrawable = image_adapter.new AsyncDrawable(mContext.getResources(), mLoadingBitmap, task);
            viewHolder.imageView.setImageDrawable(asyncDrawable);
            task.execute(image_path);
        }


        return convertView;
    }



    private final class ViewHolder
    {
        ImageView imageView;
        TextView item_number;
        TextView bookname;
        TextView author;
        TextView price;
        TextView number;
    }

}