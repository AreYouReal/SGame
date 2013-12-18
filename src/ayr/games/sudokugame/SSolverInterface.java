package ayr.games.sudokugame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.Toast;

public class SSolverInterface extends Activity implements OnTouchListener, OnClickListener
{
	public static final int DIM = 9;
	private static ProgressDialog progress;
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
				
				Intent intent = new Intent(getBaseContext(), ShowResults.class);
				for(int i = 0; i < 9; i++)
				{
					String name = "" + i;
					intent.putExtra(name, puzzle[i]);
				}
				startActivity(intent);
			}else
			{
				progress.dismiss();
				Toast.makeText(getBaseContext(), "Sorry, there is no solution!:(", Toast.LENGTH_SHORT).show();
			}
		}
	};
	
	private int margin, padding;
	private static int puzzle[][] = new int[9][9];
	private PuzzleView pv;
	private LinearLayout linLay;
	private TableLayout bottomTL;
	private Button solveButton, settingsButton, clearButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		Display display = getWindowManager().getDefaultDisplay();
		margin = display.getHeight() / 100;	
		padding = display.getWidth() / 50;
		
		LayoutParams layMargins = new LayoutParams();
		layMargins.setMargins(margin, margin, margin, margin);
		
		solveButton = new Button(this);
		solveButton.setTypeface(MainMenuActivity.mainFont);
		solveButton.setText("Solve!");
		solveButton.setTextSize(25);
		solveButton.setOnClickListener(this);
		solveButton.setLayoutParams(layMargins);
		
		settingsButton = new Button(this);
		settingsButton.setTypeface(MainMenuActivity.mainFont);
		settingsButton.setText("Settings");
		settingsButton.setTextSize(25);
		settingsButton.setOnClickListener(this);
		
		clearButton = new Button(this);
		clearButton.setTypeface(MainMenuActivity.mainFont);
		clearButton.setText("Clear");
		clearButton.setTextSize(25);
		clearButton.setOnClickListener(this);
		
		pv = new PuzzleView(this, puzzle, puzzle);
		
		pv.setOnTouchListener(this);

		createLayout();
		linLay.setPadding(padding, padding, padding, padding);
		bottomTL.setLayoutParams(layMargins);
		
		linLay.setBackgroundDrawable(new BitmapDrawable(Assets.getBitmaps()[1]));
		
		if(getIntent().getIntArrayExtra("1") != null)
		for(int i = 0; i < 9; i++)
			puzzle[i] = getIntent().getIntArrayExtra("" + i);
		
		
		setContentView(linLay);
	}
	
	public boolean onTouch(View v, MotionEvent event) 
	{	
		int touchX = (int)event.getX();
		int touchY = (int)event.getY();
		if( touchX / pv.getCellWidth() >= 9 || touchY / pv.getCellHeight() >= 9) return false;
		if( event.getAction() == MotionEvent.ACTION_UP )
		{
			NumberPicker np = new NumberPicker(this, puzzle, new int[9][9], touchX, touchY, pv);
			np.show();
		}
		return true;
	}
	
	public void createLayout()
	{	
		linLay = new LinearLayout(this);
		
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-2,-2, 1f);
		
		
		linLay.setOrientation(LinearLayout.VERTICAL);

		linLay.addView(solveButton);
		
		pv.setLayoutParams(lp);
		
		linLay.addView(pv);
		
		bottomTL = new TableLayout(this);
		bottomTL.setColumnStretchable(0, true);
		bottomTL.setColumnStretchable(1, true);
		
		TableRow tr = new TableRow(this);
		
		
		tr.addView(settingsButton);
		tr.addView(clearButton);
		
		bottomTL.addView(tr);
		
		linLay.addView(bottomTL);
	}

	public void onClick(View v) 
	{
		if( v == solveButton )
		{
			progress = new ProgressDialog(this);
			
			progress.setMessage("Please wait...");
					
			progress.setCancelable(false);
			
			progress.show();

			Puzzle puzzleSolver = new Puzzle(handler);
			
			puzzleSolver.execute(puzzle);		
		}
		
		if( v == settingsButton )
		{
			Preferences pref = new Preferences(this);
			pref.show();
		}		
		if( v == clearButton )
		{
			pv.clearView();
			pv.invalidate();
		}
	}
	
	public static int[][] getInitialPuzzle()
	{
		return puzzle;
	}
}
