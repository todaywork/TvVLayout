package cn.cky.vlayouttest;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.DefaultLayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        recyclerView = (RecyclerView) findViewById(R.id.rv);

        VirtualLayoutManager layoutManager = new VirtualLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.set(16, 16, 16, 16);
            }
        });

        final GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(5);
        gridLayoutHelper.setItemCount(600);
        gridLayoutHelper.setSpanSizeLookup(new GridLayoutHelper.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position%10==0){
                    return 5;
                }
                return 1;
            }
        });

        final List<LayoutHelper> helpers = new LinkedList<>();
        helpers.add(DefaultLayoutHelper.newHelper(1));
        helpers.add(gridLayoutHelper);

        layoutManager.setLayoutHelpers(helpers);

        recyclerView.setAdapter(
                new VirtualLayoutAdapter(layoutManager) {
                    @Override
                    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view;
                        if (viewType == 0){
                             view = LayoutInflater.from(MainActivity.this).inflate(R.layout.content_item_layout,parent,false);
                        }else {
                            view = LayoutInflater.from(MainActivity.this).inflate(R.layout.title_layout,parent,false);
                        }
                        return new MainViewHolder(view);
                    }

                    @Override
                    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {

                        if (position%10==0){
                            VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            holder.itemView.setLayoutParams(layoutParams);
                            TextView textView = (TextView) holder.itemView.findViewById(R.id.title);
                            textView.setTextColor(Color.BLACK);
                            textView.setText("一周精彩影片");
                        }else {
                            VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300);
                            holder.itemView.setLayoutParams(layoutParams);

                            ImageView imageView = (ImageView) holder.itemView.findViewById(R.id.img);
//                            imageView.setImageResource(R.mipmap.one);
                            Glide.with(MainActivity.this)
                                    .load(mArrayList.get(mRandom.nextInt(mArrayList.size())))
                                    .into(imageView);
                            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View v, boolean hasFocus) {
                                    if (hasFocus){
                                        v.animate().scaleY(1.1f).scaleX(1.1f).setDuration(300).start();
                                        Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
                                    }else {
                                        v.animate().scaleY(1.0f).scaleX(1.0f).setDuration(300).start();
                                    }
                                }
                            });
                        }


                    }

                    @Override
                    public int getItemCount() {
                        List<LayoutHelper> helpers = getLayoutHelpers();
                        if (helpers == null) {
                            return 0;
                        }
                        int count = 0;
                        for (int i = 0, size = helpers.size(); i < size; i++) {
                            count += helpers.get(i).getItemCount();
                        }
                        return count;
                    }

                    @Override
                    public int getItemViewType(int position) {
                        if(position%10==0){
                            return 1;
                        }else {
                            return 0;
                        }


                    }
                });
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {
        public MainViewHolder(View itemView) {
            super(itemView);
        }
    }
    ArrayList<String> mArrayList ;
    private Random mRandom;
    private void initData() {
        mRandom = new Random();
        mArrayList = new ArrayList<String>();
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006121854_erweima7.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091844_编组6.png");
        mArrayList.add("https://system-ws.tlkg.com/bion/share/images/202004071448/banner1.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091749_拼音点歌.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091749_歌手点歌.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091750_分类点歌.jpg");
        mArrayList.add( "https://system-ws.tlkg.com/tv50/images/202006091750_我的收藏.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091750_K歌记录.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091750_K歌记录.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091751_已下载歌曲.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514519360_1.png");
        mArrayList.add( "https://system-ws.tlkg.com/tv50/images/1593514527801_2.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514519360_1.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514534955_3.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514541775_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514541775_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514497408_5.png");
        mArrayList.add( "https://system-ws.tlkg.com/tv50/images/1593514675674_6.png");
        mArrayList.add(  "https://system-ws.tlkg.com/bion/share/images/202004071814/ge1.png");
        mArrayList.add( "https://system-ws.tlkg.com/bion/share/images/202004071814/ge2.png");
        mArrayList.add("https://system-ws.tlkg.com/bion/share/images/202004071814/ge3.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514207662_1.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514384682_2.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514392070_3.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514140984_1.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514149238_2.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514157108_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514163717_3.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514172389_5.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514180002_6.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514187259_7.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514411166_1.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514417091_2.png");
        mArrayList.add( "https://system-ws.tlkg.com/tv50/images/1593514424838_3.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514431976_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514439144_5.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514446440_6.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514453465_7.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514490378_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514490378_4.png");
    }
}
