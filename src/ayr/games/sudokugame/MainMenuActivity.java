package ayr.games.sudokugame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout.LayoutParams;

public class MainMenuActivity extends Activity implements OnClickListener
{
	private Button sSolverBtn, h2UseBtn, exitBtn, playBtn;
	private TextView textTitle;
	public static Typeface mainFont, titleFont, faqfont;
	private LinearLayout ll;
	private static SudokuDatabase sdb[] = null;
	private static ProgressDialog progress;
	private boolean loaded[] = {false, false, false, true};
	private static int difficulty = -1;
	
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Bundle b = msg.getData();
			boolean value = b.getBoolean("Result");
			if(value == true)
			{
				progress.dismiss();
				loaded[difficulty] = true;
				startActivity(new Intent(MainMenuActivity.this, PlaySudoku.class));
			}else
			{
				progress.dismiss();
				Toast.makeText(getBaseContext(), "Sorry, can't load sudoku puzzles", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        Assets.initializeBitmaps(getAssets());
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        setContentView(R.layout.mainmenu);
               
        mainFont = Typeface.createFromAsset(getAssets(), "fonts/main_font.ttf");
        titleFont = Typeface.createFromAsset(getAssets(), "fonts/title_font.ttf");
        faqfont = Typeface.createFromAsset(getAssets(), "fonts/faqfont.ttf");
        
    	sdb = new SudokuDatabase[4];
    	for(int i = 0; i < 3; i++)
    		sdb[i] = new SudokuDatabase(this, handler);
        
        Display display = getWindowManager().getDefaultDisplay();
		int margin = display.getHeight() / 20;	
		int padding = display.getHeight() / 30;
        
        ll = (LinearLayout)findViewById(R.id.mainLinLay);

        ll.setBackgroundDrawable(new BitmapDrawable(Assets.getBitmaps()[0]));
        
		LayoutParams layMargins = new LayoutParams();
		layMargins.setMargins(margin, margin, margin, margin);
        
        textTitle = (TextView)findViewById(R.id.textTitle);
        playBtn = (Button)findViewById(R.id.playBtn);
        sSolverBtn = (Button)findViewById(R.id.sSolverBtn);
        h2UseBtn = (Button)findViewById(R.id.h2useBtn);
/*        exitBtn = (Button)findViewById(R.id.exitBtn);*/
        textTitle.setTypeface(titleFont);
        textTitle.setTextSize(55);
        textTitle.setTextColor(Color.BLUE);
        textTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        textTitle.setLayoutParams(layMargins);
        playBtn.setTypeface(mainFont);
        sSolverBtn.setTypeface(mainFont);
        h2UseBtn.setTypeface(mainFont);
       /* exitBtn.setTypeface(mainFont);*/
        playBtn.setTextSize(25);
        sSolverBtn.setTextSize(25);
        h2UseBtn.setTextSize(25);
        /*exitBtn.setTextSize(25);*/
        playBtn.setOnClickListener(this);
        sSolverBtn.setOnClickListener(this);
        h2UseBtn.setOnClickListener(this);
       /* exitBtn.setOnClickListener(this);*/
        playBtn.setLayoutParams(layMargins);
        sSolverBtn.setLayoutParams(layMargins);
        h2UseBtn.setLayoutParams(layMargins);
        /*exitBtn.setLayoutParams(layMargins);*/

		ll.setPadding(padding / 2, padding, padding / 2, padding);
    }

	public void onClick(View view) 
	{
		switch( view.getId() )
		{
/*		case R.id.exitBtn: 
			this.finish();
			break;*/
		case R.id.h2useBtn: 
			startActivity(new Intent(this, FAQ.class));
			break;
		case R.id.sSolverBtn: 
			startActivity(new Intent(this, SSolverInterface.class));
			break;
		case R.id.playBtn:
	        
			DifficultyDialog df = new DifficultyDialog(this, this);
			df.show();
			break;
		}
	}
	
	public void loadPuzzles()
	{
		if(difficulty != -1 && !loaded[difficulty])
		{
			progress = new ProgressDialog(this);
			
			progress.setMessage("Please wait while loading sudoku puzzles...");
					
			progress.setCancelable(false);
			
			progress.show();
				
			switch(difficulty)
			{
			case 0:
				sdb[0].execute("puzzles/easy.txt");
				break;
			case 1:
				sdb[1].execute("puzzles/medium.txt");
				break;
			case 2:
				sdb[2].execute("puzzles/hard.txt");
				break;
		}
		}else
		{
			startActivity(new Intent(MainMenuActivity.this, PlaySudoku.class));
		}

	}
	
	public static SudokuDatabase getSDB()
	{
		return sdb[difficulty];
	}
	
	public void setDifficulty(int n)
	{
		difficulty = n;
	}
	
	   @Override
	    public boolean onCreateOptionsMenu(Menu menu) 
	    {
	       getMenuInflater().inflate(R.layout.vibr, menu);
	       MenuItem item = menu.findItem(R.id.vibr);
	       item.setOnMenuItemClickListener(new OnMenuItemClickListener() 
	       {
	           public boolean onMenuItemClick(MenuItem item) 
	           {
	        	  if(SudokuDatabase.vibrate())
	        	  {
	        		  item.setTitle("VIBRATION: OFF"); 
	        		  SudokuDatabase.setVibrate(false);
	        	  }else
	        	  {
	        		  item.setTitle("VIBRATION: ON"); 
	        		  SudokuDatabase.setVibrate(true);  
	        	  }
	              return true;
	           }
	       });
	       return true;
	    }
}