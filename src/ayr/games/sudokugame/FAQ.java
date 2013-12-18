package ayr.games.sudokugame;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

public class FAQ extends android.support.v4.app.FragmentActivity {
    static final int NUM_ITEMS = 3;

    MyAdapter mAdapter;
    ViewPager mPager;
    private String[] names = {"faq_1", "faq_2", "faq_3"};
    
    private static LruCache<String, Bitmap> mMemoryCache;
      
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);

        final int memClass = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();  
        final int cacheSize = 1024 * 1024 * memClass / 8;
        
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize)
        {
        protected int sizeOf(String key, Bitmap bitmap)
        	{
        		return bitmap.getRowBytes()*bitmap.getHeight();
        	}
        };
        Bitmap[] temp = Assets.getBitmaps();
        // First two bitmaps - background and paper
/*        for(int i = 2; i < temp.length - 1; i++)
        {
        	mMemoryCache.put(names[i-2], temp[i]);
        }*/
        for(int i = 0; i < 3; i++)
        	mMemoryCache.put(names[i], temp[1]);
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.faq);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

    	Log.e("Size of fOne: ", "" + mMemoryCache.size() );
        
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            return ArrayListFragment.newInstance(position);
        }
    }

    public static class ArrayListFragment extends ListFragment {
        int mNum;

        /**
         * Create a new instance of CountingFragment, providing "num"
         * as an argument.
         */
        static ArrayListFragment newInstance(int num) {
            ArrayListFragment f = new ArrayListFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.faq_list, container, false);
            View iv = v.findViewById(R.id.image);
            TextView tv = (TextView) v.findViewById(R.id.textView);
            tv.setTypeface(MainMenuActivity.faqfont);
            tv.setTextSize(25);
            tv.setTextColor(Color.BLACK);
            switch(mNum)
            {
            case 0:
            	((ImageView)iv).setImageBitmap(mMemoryCache.get("faq_1"));
            	tv.setText(R.string.faq_intro); 
            	break;
            case 1:
            	((ImageView)iv).setImageBitmap(mMemoryCache.get("faq_2"));
            	tv.setText(R.string.faq_rules);
            	break;
            case 2:
            	((ImageView)iv).setImageBitmap(mMemoryCache.get("faq_3"));
            	tv.setText(R.string.faq_tips);
            	break;
            }
            return v;
        }
        /*
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setListAdapter((new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1)));
        }
		*/ 
    }
}