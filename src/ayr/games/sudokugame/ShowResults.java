package ayr.games.sudokugame;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.ads.*;

public class ShowResults extends Activity implements OnClickListener
{
	private AdView adView;
	private int[][] initialPuzzle;

	public ShowResults()
	{
		super();
		this.initialPuzzle = new int[9][9];
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		for(int i = 0; i < 9; i++)
		{
			String name = "" + i;
			initialPuzzle[i] = getIntent().getIntArrayExtra(name);
		}
	    
		PuzzleView solutionView = new PuzzleView(this, Puzzle.getSolutions(), initialPuzzle);
		
		//Button exit = new Button(this);
		
		TextView textView = new TextView(this);

		textView.setTypeface(MainMenuActivity.mainFont);
		textView.setTextSize(35);
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
		textView.setTextColor(Color.BLACK);
		textView.setText("Here is the Solution!");
		
		
		Display display = getWindowManager().getDefaultDisplay();
		int margin = display.getHeight() / 75;	
		
		adView = new AdView(this, AdSize.BANNER, "a150b5a6291a01a");
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2,-2, 1f);
		lp.setMargins(margin, margin, margin, margin);
		
		solutionView.setLayoutParams(lp);
		
		ll.setBackgroundColor(Color.WHITE);
		ll.setBackgroundDrawable(new BitmapDrawable(Assets.getBitmaps()[1]));
		ll.addView(textView);
		ll.addView(solutionView);
		ll.addView(adView);
		adView.loadAd(new AdRequest());
		//ll.setPadding(padding, padding, padding, padding);
		//ll.addView(exit);
		setContentView(ll);		
	}
	
	public void onClick(View v) 
	{
		finish();
	}
	@Override
	public void onDestroy() 
	{
		if (adView != null) 
		{
			adView.destroy();
		}
		super.onDestroy();
	}
}
