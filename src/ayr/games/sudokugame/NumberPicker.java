package ayr.games.sudokugame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class NumberPicker extends Dialog implements OnClickListener
{
	private int puzzle[][];
	private int initialPuzzle[][];
	private Button keys[] = new Button[11];
	private int x, y;
	private PuzzleView pv;
	private Vibrator v;
	private Context mContext; 
	
	public NumberPicker(Context context, int puzzle[][], int initialPuzzle[][], int x, int y, PuzzleView pv) 
	{
		super(context);
		
		this.puzzle = puzzle;
		this.initialPuzzle = initialPuzzle;
		this.mContext = context;
		
		this.x = x;
		this.y = y;
		
		this.pv = pv;
		
		v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	private void findKeys()
	{
		keys[0] = (Button)findViewById(R.id.key_0);
		keys[1] = (Button)findViewById(R.id.key_1);
		keys[2] = (Button)findViewById(R.id.key_2);
		keys[3] = (Button)findViewById(R.id.key_3);
		keys[4] = (Button)findViewById(R.id.key_4);
		keys[5] = (Button)findViewById(R.id.key_5);
		keys[6] = (Button)findViewById(R.id.key_6);
		keys[7] = (Button)findViewById(R.id.key_7);
		keys[8] = (Button)findViewById(R.id.key_8);
		keys[9] = (Button)findViewById(R.id.key_9);
		keys[10] = (Button)findViewById(R.id.key_remove);
		
		for(int i = 0; i < keys.length; i++ )
		{
			keys[i].setOnClickListener(this);
			keys[i].setTypeface(MainMenuActivity.mainFont);
			keys[i].setTextSize(25);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.numpicker);
		
		findKeys();
		
		this.setTitle("Put the number to the cell");
		if(SudokuDatabase.vibrate() && v != null)
		v.vibrate(25);
	}

	public void onClick(View view) 
	{
		switch( view.getId() )
		{
		case R.id.key_0: this.dismiss();
		break;
		case R.id.key_1: 
			setNumber(1);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_2: 
			setNumber(2);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_3: 
			setNumber(3);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_4: 
			setNumber(4);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_5: 
			setNumber(5);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_6: 
			setNumber(6);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_7: 
			setNumber(7);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_8: 
			setNumber(8);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_9: 
			setNumber(9);
			pv.invalidate();
			this.dismiss();
		break;
		case R.id.key_remove: 
			setNumber(0);
			pv.invalidate();
			this.dismiss();
		break;
		}
		if(mContext.getClass() == PlaySudoku.class && ((PlaySudoku)mContext).isItSolved())
		{
			pv.setSolved(true);
			pv.setOnTouchListener(null);
			pv.invalidate();
			if(SudokuDatabase.vibrate() && v != null)
				v.vibrate(1000);
		}
	}
	
	private void setNumber(int num)
	{
		int row = y / (pv.getCellHeight() + 1);
		int col = x / (pv.getCellWidth() + 1);
		if((num == 0 &&  initialPuzzle[row][col] == 0) || pv.isItOk(row, col, num))
		{
			puzzle[y / (pv.getCellHeight() + 1)][x / (pv.getCellWidth() + 1)] = num;
		}
		else
		{
			Toast.makeText(this.getContext(), "Sorry, can't do this:(", Toast.LENGTH_SHORT).show();
		}
	}
	
}
