package com.ebookreader;


        import java.text.DecimalFormat;
        import java.util.ArrayList;
        import java.util.List;

        import android.content.ContentValues;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.drawable.BitmapDrawable;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.BaseAdapter;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.TextView;


public class Shopcart_Gview_Adapter extends BaseAdapter
{

    private Image_Adapter image_adapter;
    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Shopcart_Activity.Content> mDatas;
    private DBOpenHelper dbOpenHelper;

    public Shopcart_Gview_Adapter(Context context, ArrayList<Shopcart_Activity.Content> mDatas)
    {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mDatas = mDatas;
        image_adapter=new Image_Adapter(context);
        dbOpenHelper=new DBOpenHelper(this.mContext);
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
    public View getView(final int position, View convertView, ViewGroup parent)
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
            viewHolder.item_increase=(Button) convertView.findViewById(R.id.item_increase);
            viewHolder.item_decrease=(Button) convertView.findViewById(R.id.item_decrease);
            viewHolder.item_delete=(Button) convertView.findViewById(R.id.item_delete);

            convertView.setTag(viewHolder);
        } else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.item_number.setText("商品编号："+mDatas.get(position).item_number);
        //Log.d("Tag",""+position);

        viewHolder.bookname.setText("书名：《"+mDatas.get(position).bookname+"》");
        viewHolder.author.setText("作者："+mDatas.get(position).author);
        //Log.d("Tag",""+mDatas.get(position).author);
        DecimalFormat form=new DecimalFormat("0.00");
        String price=form.format(mDatas.get(position).price);
        viewHolder.price.setText("价格："+price);
        viewHolder.number.setText(""+mDatas.get(position).number);

        viewHolder.item_increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num=mDatas.get(position).number;
                if(num<999){
                    String where=mDatas.get(position).item_number;
                    ContentValues values=new ContentValues();
                    values.put("购买数量",++num);
                    int i=dbOpenHelper.update("shopcart",values,"商品编号=?",new String[]{where});
                    mDatas.get(position).number=num;
                    Shopcart_Gview_Adapter.super.notifyDataSetChanged();

                    Shopcart_Activity.addup_update(mDatas);
                }
            }
        });

        viewHolder.item_decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num=mDatas.get(position).number;
                if(num>0){
                    String where=mDatas.get(position).item_number;
                    ContentValues values=new ContentValues();
                    values.put("购买数量",--num);
                    dbOpenHelper.update("shopcart",values,"商品编号=?",new String[]{where});
                    mDatas.get(position).number=num;
                    Shopcart_Gview_Adapter.super.notifyDataSetChanged();
                    Shopcart_Activity.addup_update(mDatas);


                }
            }
        });

        viewHolder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String where=mDatas.get(position).item_number;
                dbOpenHelper.delete("shopcart","商品编号=?",new String[]{where});
                mDatas.remove(position);
                Shopcart_Activity.addup_update(mDatas);
                Shopcart_Gview_Adapter.super.notifyDataSetChanged();
            }
        });

        String image_path=mDatas.get(position).imagePath;


        image_adapter.setDrawable(image_path,viewHolder.imageView);


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
        Button item_increase;
        Button item_decrease;
        Button item_delete;
    }

}