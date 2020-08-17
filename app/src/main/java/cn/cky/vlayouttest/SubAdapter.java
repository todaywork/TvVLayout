package cn.cky.vlayouttest;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.bumptech.glide.Glide;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by office on 2018/6/13.
 */
public class SubAdapter extends DelegateAdapter.Adapter<SubAdapter.MainViewHolder> {
    private static final String TAG = SubAdapter.class.getSimpleName();
    private Context mContext;
    private LayoutHelper mLayoutHelper;
    private VirtualLayoutManager.LayoutParams mLayoutParams;
    private ItemBean itemBean;
    private int mCount = 0;
    RecyclerView.RecycledViewPool viewPool;
    OnItemFocusListener onItemFocusListener;
    BannerScrollListener bannerScrollListener;

    private boolean scrolling = true;

    public interface BannerScrollListener{
        void scroll(ViewPager viewPager,ItemBean itemBean);
    }

    public interface OnItemFocusListener{
        void itemFoucs(View view,View view1,boolean hasFocus);
    }

    public boolean isScrolling() {
        return scrolling;
    }

    public void setScrolling(boolean scrolling) {
        this.scrolling = scrolling;
    }

    public void setBannerScrollListener(BannerScrollListener bannerScrollListener) {
        this.bannerScrollListener = bannerScrollListener;
    }

    public SubAdapter(Context mContext, LayoutHelper mLayoutHelper, VirtualLayoutManager.LayoutParams mLayoutParams, int count, ItemBean itemBean, RecyclerView.RecycledViewPool viewPool, OnItemFocusListener onItemFocusListener){
        this.mContext = mContext;
        this.mLayoutHelper = mLayoutHelper;
        this.mLayoutParams = mLayoutParams;
        this.mCount = count;
        this.itemBean = itemBean;
        this.viewPool = viewPool;
        this.onItemFocusListener = onItemFocusListener;
        initData();
    }


    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MainViewHolder mainViewHolder =null;
        if (viewType == TypeUtils.BANNER_TYPE){
            mainViewHolder = new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_pager, parent, false));
        }else if (viewType == TypeUtils.TITEL_TYPE){
            mainViewHolder = new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.title_layout, parent, false));
        }else if (viewType == TypeUtils.CONNECT_TYPE){
            mainViewHolder = new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.content_item_layout, parent, false));
        }else {
            mainViewHolder = new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.content_item_layout, parent, false));
        }
        return mainViewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        VirtualLayoutManager.LayoutParams layoutParams = new VirtualLayoutManager.LayoutParams(mLayoutParams);
        layoutParams.setMargins(10,5,10,5);
        holder.itemView.setLayoutParams(layoutParams);

        if (itemBean.getType().equals(TypeUtils.BANNER)){
            ViewPager viewPager = (ViewPager) holder.itemView;
            viewPager.setAdapter(new PagerAdapter(mContext,this,viewPool,itemBean));
            viewPager.setCurrentItem(2);
            try {
                Field field = ViewPager.class.getDeclaredField("mScroller");
                field.setAccessible(true);
                FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext(), new AccelerateDecelerateInterpolator());
                field.set(viewPager, scroller);
                scroller.setmDuration(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (bannerScrollListener != null){
                bannerScrollListener.scroll(viewPager,itemBean);
            }
        }else if (itemBean.getType().equals(TypeUtils.TITLE)){
            ((TextView)(holder.itemView.findViewById(R.id.title))).setText(itemBean.getTitle()+"");

        }else {
            Log.d(TAG, "onBindViewHolder: position="+position);
            final ImageView imageView = ((ImageView)(holder.itemView.findViewById(R.id.img)));
            if (scrolling){
                Glide.with(mContext)
                        .load(mArrayList.get(mRandom.nextInt(mArrayList.size())))
                        .into(imageView);
            }
            holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    if (onItemFocusListener!=null){
                        onItemFocusListener.itemFoucs(v,imageView,hasFocus);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemBean.getType().equals(TypeUtils.BANNER)){
            return TypeUtils.BANNER_TYPE;
        }else if (itemBean.getType().equals(TypeUtils.TITLE)){
            return TypeUtils.TITEL_TYPE;
        }else if (itemBean.getType().equals(TypeUtils.CONTENT)){
            return TypeUtils.CONNECT_TYPE;
        }else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return mCount;
    }

    static class MainViewHolder extends RecyclerView.ViewHolder {

        public static volatile int existing = 0;
        public static int createdTimes = 0;

        public MainViewHolder(View itemView) {
            super(itemView);
            createdTimes++;
            existing++;
        }

        @Override
        protected void finalize() throws Throwable {
            existing--;
            super.finalize();
        }
    }
    ArrayList<String> mArrayList ;
    private  Random mRandom;
    private void initData() {
        mRandom = new Random();
        mArrayList = new ArrayList<String>();
        mArrayList.add("http://7img1.tianlaikge.com/images/20200313/71dcd536-f9e9-47e1-b728-f6868436bc49.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/80ced631-819d-4572-a6c7-23d1c905c298.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200816/794dc1f9-2688-4cf3-a0ab-93bf3daa436b.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/1410af8d-38df-4735-a331-e06a2463ed16.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/e83edd4f-c884-46db-8aa7-4a8ea7a6db6b.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180812/3fb7f5c0-4c45-481a-a683-4a1aeb4fdfb7.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/6fabb65f-0d41-43a2-9e28-a30e8851ba0a.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200704/38ffa1e5-c5cd-4934-bfa4-9a79a86237a0.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200809/d0ffb5ad-dcb1-411c-9b7b-b331fdf583dd.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200816/ec7b3df5-5c04-4989-b7db-436a99eae02d.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200816/7f5d84da-83da-4adb-8c0f-26d43c921b2c.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200816/9ed1611e-712f-4c03-bb11-b2350cdb4578.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/27582db3-55da-4a59-89e2-9ccf535d82ef.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200510/8cfbd344-d46e-4068-9829-275bdef2c321.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/6cb0ad93-d4c6-450b-b00f-390b467f8f5a.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200501/4e32d9cf-2ed6-47e9-9c00-9c939213f68c.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200810/6f47fd4b-b6e8-4eae-8364-5b29d391442c.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/db8d6b55-d547-4d95-9e77-6deefad441cb.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/d9adca44-da5d-4dec-ac4a-384966b55be8.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200731/48ef0f3f-6787-4b13-bc35-94ad3033c69f.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/46ac2878-c379-4fe1-8b64-9b4671765969.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://t9.tianlaikge.com/upload_files/images/360_270/20180401/4f79be31-d703-4c6d-9077-2c052d25cc6d.jpg");
        mArrayList.add("https://thirdwx.qlogo.cn/mmopen/vi_32/Q3auHgzwzM784uU9xibKICNLUuOhpia9ynBExcGshBcLCtpBmANEEbQjYSEvTOGZw6WFLxGicKCnBTxW5a6hVRAXQ/132");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180711/edc4ac5d-461a-420d-944f-2e2c5fc31fcd.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20191030/ebfef12a-2194-44be-ae8d-71896bd32280.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200508/a5aa2056-afc2-4ada-bf51-cb29ee630725.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/56f14f8e-1dc8-4fe1-832d-4ab624e57781.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20170519/4b543e60-5377-49f0-85ee-109981f05d20.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200816/f6a500a2-dd70-4822-a9b2-a24cde9dbc33.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20190422/34cab887-3517-43ee-8f26-bd92a823d3f8.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200815/1002f254-7d85-4d51-9329-6d5ef44569cb.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200815/011dd077-9345-4b3d-a75e-d4db9a180b5e.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200815/011dd077-9345-4b3d-a75e-d4db9a180b5e.jpg");
        mArrayList.add("http://t8.tianlaikge.com/upload_files/images/20160923/08493254-d0cb-4d9e-b8a2-8d500a73e5f2.png");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200816/f9fffff7-8d08-490f-9172-b3ef3e7fb3d6.jpg");
        mArrayList.add("http://t9.tianlaikge.com/upload_files/images/20160416/f41cd3a6-09ac-4cfa-aa57-467f3e4a12cf.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20190422/34cab887-3517-43ee-8f26-bd92a823d3f8.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200721/1e69534f-80ad-4482-85d5-be5cebfdf509.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/419613cf-410b-4cc0-98a0-198491dd95c6.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20191015/9bb72f10-2c6c-43d1-b507-ee885448c0cb.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200801/8148348f-a45d-4512-9e97-5cbe73f6ecba.jpg");
        mArrayList.add("http://wx.qlogo.cn/mmopen/JiamdkwmrgoTwsBF8jgnBG0wMtUVyWc3ltibRHWU0ibZ3SaoicXUXibCD7QVfUIQ3otYDclKzBb3PcXpMuqD2PuvVmo7r93VUUW5E/0");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200428/f2f1c011-27f7-4152-a56c-cb93fd2391fd.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200817/4f6cf416-0554-413b-81fe-12fa6e87ea24.jpg");
        mArrayList.add("http://t9.tianlaikge.com/upload_files/images/20180501/65c7ff1b-20f9-4cb2-a903-1a98c4a3be0e.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180812/3fb7f5c0-4c45-481a-a683-4a1aeb4fdfb7.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200815/b3eb1fb1-3511-4054-a0ed-9846fa1c6c9a.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20190912/1cb98890-4249-4549-b61c-c78ebed84db2.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200814/66700d51-b07f-4204-a635-4d8aad1e1244.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180812/3fb7f5c0-4c45-481a-a683-4a1aeb4fdfb7.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200810/a170264b-ca2a-4370-a5f5-6d70c945d201.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200809/054273b3-943a-40cd-9875-11e418f56cec.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200808/5c323c5a-08d3-4b9b-8613-441809d95be0.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://t1.tianlaikge.com/upload_files/images/360_270/20131007/460b5d8f-7241-4f9d-b5aa-9e2b62f0eac6.jpg");
        mArrayList.add("http://t7.tianlaikge.com/upload_files/images/360_270/20141209/fd1963e7-d381-4bfb-89d7-cad97bc152a7.png");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200816/aeb382a3-1fd6-4cd9-be4e-8677c258a878.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180512/319757aa-1a1d-4e5f-a32f-1c3cda395fec.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200811/218f5e8a-0122-4e11-942c-9b3799c8f108.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200619/53fa8bc2-b2f4-4825-8fd5-3c2a917765a2.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://7img1.tianlaikge.com/images/20181129/83db0aa0-5e6c-4218-8a7e-6fccfbe95d6a.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://t7.tianlaikge.com/upload_files/images/360_270/20141130/eb99a4fb-db2f-41ae-8a16-1b4a6eafbb50.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200501/66792502-11f9-46ac-a1a5-e623100f0d2b.jpg?imageMogr2/thumbnail/360x");
        mArrayList.add("http://t6.tianlaikge.com/upload_files/images/360_270/20140905/1b96d0fb-b5bf-457b-9b97-8602c93a5f35.jpg");
        mArrayList.add("http://t8.tianlaikge.com/upload_files/images/20151031/21bf46b3-4863-4ec7-8847-b3b88d894b33.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20191115/6869ad6a-b8fe-4bcc-82fb-9bdb333ec125.jpg");
        mArrayList.add("http://t8.tianlaikge.com/upload_files/images/20160121/0ef8c1bf-6e71-4c4a-8333-633789721522.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20171028/86754713-5882-42f3-a4f5-7fb052759666.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180710/9e6e644b-c6a6-47c5-8148-ca99203ef0c0.jpg");
        mArrayList.add("http://t6.tianlaikge.com/upload_files/images/20141206/caa8e507-9af7-4407-b0a9-8919448c9fd1.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20190830/ed8a9d7d-2540-4a63-923e-ed3f0d7f1806.jpg");
        mArrayList.add("http://t6.tianlaikge.com/upload_files/images/20140423/dba04ee8-c45c-4341-b946-8450e3c425a0.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180726/1a869f22-8eb2-41eb-ae29-fc804bc21682.jpg");
        mArrayList.add("http://t8.tianlaikge.com/upload_files/images/20151031/21bf46b3-4863-4ec7-8847-b3b88d894b33.jpg");
        mArrayList.add("http://t6.tianlaikge.com/upload_files/images/20141206/caa8e507-9af7-4407-b0a9-8919448c9fd1.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20190702/4a5fd87b-5e38-43fa-82cb-bd06e275b315.jpg");
        mArrayList.add("http://t8.tianlaikge.com/upload_files/images/20151031/21bf46b3-4863-4ec7-8847-b3b88d894b33.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200415/f1c33b45-cdcf-44b0-9e19-b11d6e440869.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20200520/e7aadaf1-7d19-498d-bfcc-ae568e8fb5d4.jpg");
        mArrayList.add("http://t1.tianlaikge.com/upload_files/images/20140421/7e759882-b64d-40d8-a2fa-6f706f2c4b95.jpg");
        mArrayList.add("http://7img1.tianlaikge.com/images/20180904/b1e3607b-87bf-4a08-906e-3611d62f5323.jpg");
        mArrayList.add("http://t7.tianlaikge.com/upload_files/images/20150306/569970b3-9f80-4164-8a35-30e7b72458a2.jpg");


        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006121854_erweima7.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091844_编组6.png");
        mArrayList.add("https://system-ws.tlkg.com/bion/share/images/202004071448/banner1.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091749_拼音点歌.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091749_歌手点歌.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091750_分类点歌.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091750_我的收藏.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091750_K歌记录.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091750_K歌记录.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/202006091751_已下载歌曲.jpg");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514519360_1.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514527801_2.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514519360_1.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514534955_3.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514541775_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514541775_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514497408_5.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514675674_6.png");
        mArrayList.add("https://system-ws.tlkg.com/bion/share/images/202004071814/ge1.png");
        mArrayList.add("https://system-ws.tlkg.com/bion/share/images/202004071814/ge2.png");
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
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514424838_3.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514431976_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514439144_5.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514446440_6.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514453465_7.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514490378_4.png");
        mArrayList.add("https://system-ws.tlkg.com/tv50/images/1593514490378_4.png");
    }
}
