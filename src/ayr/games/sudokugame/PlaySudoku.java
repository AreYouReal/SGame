package ayr.games.sudokugame;

import java.io.IOException;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;

public class PlaySudoku extends Activity implements OnTouchListener, OnClickListener
{
	private AdView adView_1;
	private PuzzleView pz;
	private Button nextPuzzleBtn;
	private int[][] initPuzzle;	
	private int[][] puzzle = new int[9][9];
	SudokuDatabase sdb = MainMenuActivity.getSDB();
	private PowerManager.WakeLock wl;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		
		LinearLayout ll = new LinearLayout(this);
		
		nextPuzzleBtn = new Button(this);
		nextPuzzleBtn.setText("Next Puzzle");
		nextPuzzleBtn.setTextSize(25);
		nextPuzzleBtn.setTypeface(MainMenuActivity.mainFont);
		nextPuzzleBtn.setOnClickListener(this);
		
		initData();
		
		pz = new PuzzleView(this, puzzle, initPuzzle);
		pz.setOnTouchListener(this);

		adView_1 = new AdView(this, AdSize.BANNER, "a150b5a6291a01a");
		
		ll.setBackgroundDrawable(new BitmapDrawable(Assets.getBitmaps()[1]));
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2,-2, 1f);
		pz.setLayoutParams(lp);
		
		ll.addView(nextPuzzleBtn);
		ll.addView(pz);
		ll.addView(adView_1);
		adView_1.loadAd(new AdRequest());
		
		
		Display display = getWindowManager().getDefaultDisplay();
		int margin = display.getHeight() / 35;	
		int padding = display.getHeight() / 55;
		
		LayoutParams layMargins = new LayoutParams();
		layMargins.setMargins(margin, margin, margin, margin);
		
		ll.setLayoutParams(layMargins);
		ll.setPadding(padding, padding, padding, padding);
			
		setContentView(ll);		
	}
	
	@Override
	public void onDestroy() 
	{
		if (adView_1 != null) 
		{
			adView_1.destroy();
		}
		super.onDestroy();
	}

	public boolean onTouch(View v, MotionEvent event) 
	{	
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();
		if( touchX / pz.getCellWidth() >= 9 || touchY / pz.getCellHeight() >= 9) return false;
		if( event.getAction() == MotionEvent.ACTION_UP )
		{
			NumberPicker np = new NumberPicker(this, puzzle,initPuzzle, touchX, touchY, pz);
			np.show();
		}
		return true;
	}

	public boolean isItSolved()
	{
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				if(puzzle[i][j] == 0) 
				{
					return false;
				}
			}
		}
		return true;
	}

	public void onClick(View v) 
	{
		pz.setSolved(false);
		int[][] oldInitPuzzle = initPuzzle;
		int[][] oldPuzzle = puzzle;
		initData();
		pz.setNewData(initPuzzle, puzzle);
		pz.invalidate();
		if(oldInitPuzzle == initPuzzle && oldPuzzle == puzzle) Toast.makeText(this, "There are no more puzzles", Toast.LENGTH_SHORT).show();		
	}
	
	private void initData()
	{
		if(SudokuDatabase.getInitPuzzle() != null && SudokuDatabase.getPuzzle() != null)
		{
			initPuzzle = SudokuDatabase.getInitPuzzle();
			puzzle = SudokuDatabase.getPuzzle();
			SudokuDatabase.clearSavedPuzzles();
			nextPuzzleBtn.setEnabled(false);
			nextPuzzleBtn.setText("Resumed puzzle");
		}else
		{
			initPuzzle = sdb.getNextPuzzle().puzzle;
			for(int i = 0; i < 9; i++)
			{
				for(int j = 0; j < 9; j++)
				{
					puzzle[i][j] = initPuzzle[i][j];
				}
			}
			nextPuzzleBtn.setEnabled(true);
			nextPuzzleBtn.setText("Next Puzzle");
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
		
		try {
			SudokuDatabase.saveLastPuzzleState(puzzle, initPuzzle);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(wl != null)
		{
			wl.release();
		}
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

		wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "MyLock");
		if(wl != null)
		{
			wl.acquire();
		}
	}
	
   @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
       getMenuInflater().inflate(R.layout.solve_menu, menu);
       MenuItem item = menu.findItem(R.id.menu_solve);
       item.setOnMenuItemClickListener(new OnMenuItemClickListener() 
       {
           public boolean onMenuItemClick(MenuItem item) 
           {
        	   Intent intent = new Intent(PlaySudoku.this, SSolverInterface.class);
        	   for(int i = 0; i < 9; i++)
        		   intent.putExtra("" + i, initPuzzle[i]);
        	   startActivity(intent);
               return true;
           }
       });
       return true;
    }
}