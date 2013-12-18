package ayr.games.sudokugame;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Assets 
{
	private static Bitmap[] bitmaps = new Bitmap[3];
	private static String prefix = "images/";
	private static String[] fileNames = {"background.jpg", "sbg.jpg", "solved.png"};
	
	public static void initializeBitmaps(AssetManager a)
	{
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inPreferredConfig = Bitmap.Config.RGB_565;
		//o.inSampleSize = 2;
		InputStream is = null;
		try
		{
			for(int i = 0; i < fileNames.length; i++)
			{
				is = a.open(prefix + fileNames[i]);
				bitmaps[i] = BitmapFactory.decodeStream(is,null,o);
			}
			if(is != null ) is.close();
		}catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
	}
	
	public static Bitmap[] getBitmaps()
	{
		return bitmaps;
	}
}
