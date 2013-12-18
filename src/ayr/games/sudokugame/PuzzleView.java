package ayr.games.sudokugame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class PuzzleView extends View
{
	public final int DIM = 9;

	private int width, height;
	private Paint linesPaint = new Paint(), darkPaint = new Paint(), solvePaint = new Paint(), initialPaint = new Paint();
	private int puzzle[][];
	private int initialPuzzle[][];
	private boolean solved = false;
	private Bitmap solvedIm = Assets.getBitmaps()[2];
	private Rect src, dst;
	
	public PuzzleView(Context context, int puzzle[][], int initialPuzzle[][]) 
	{
		super(context);
		setFocusable(true);
		setFocusableInTouchMode(true);		
		
		this.puzzle = puzzle;
		
		src = new Rect(0, 0, solvedIm.getWidth(), solvedIm.getHeight());
		
		linesPaint.setColor(Color.GRAY);
		linesPaint.setStrokeWidth(5);
		darkPaint.setColor(Color.BLACK);
		darkPaint.setStrokeWidth(7);

		solvePaint.setTypeface(MainMenuActivity.mainFont);
		
		initialPaint.setTypeface(MainMenuActivity.mainFont);
		
		this.initialPuzzle = initialPuzzle;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		width = (int) (w / 9f);
		height = (int) (h / 9f);
		super.onSizeChanged(w, h, oldw, oldh);
		
		
		solvePaint.setTextSize(height * 0.60f);
		solvePaint.setColor(Color.BLACK);
		initialPaint.setTextSize(height * 0.70f);
		initialPaint.setColor(Color.rgb(0, 0, 150));
	}
	
	public void onDraw(Canvas canvas)
	{
		canvas.drawColor(Color.TRANSPARENT);
		for( int i = 0; i <= DIM; i++ )
		{
			canvas.drawLine(0, i * height, getWidth() -5, i * height, linesPaint);
			canvas.drawLine(i * width, 0, i * width, getHeight() - 5, linesPaint);
		}
		for( int i = 0; i <= DIM; i++ )
		{
			if( i % 3 != 0 ) continue;
			canvas.drawLine(0, i * height, getWidth() -5, i * height, darkPaint);
			canvas.drawLine(i * width, 0, i * width, getHeight() - 5, darkPaint);
			drawNumbers(canvas);
		}
		if(solved) drawSolvedImage(canvas);
	}
	
	public void drawNumbers(Canvas canvas)
	{
		for( int i = 0; i < DIM; i++ )
		{
			for( int j = 0; j < DIM; j++ )
			{
				if( puzzle[i][j] == 0 ) continue;
				if(Preferences.hideNums[puzzle[i][j]] && initialPuzzle[i][j] == 0 ) continue;
				if( initialPuzzle[i][j] == 0 )
				canvas.drawText("" + puzzle[i][j], width * (j + 0.15f), height* (i + 0.85f), solvePaint);
				else
				canvas.drawText("" + puzzle[i][j], width * (j + 0.15f), height* (i + 0.85f), initialPaint);
			}
		}
	}	
	
	public int getCellWidth()
	{
		return width;
	}
	
	public int getCellHeight()
	{
		return height;
	}
	
	public void clearView()
	{
		for( int i = 0; i < DIM; i++ )
		{
			for( int j = 0; j < DIM; j++ )
			{
				puzzle[i][j] = 0;
			}
		}
	}
	
	/**
	* This method checks is the number occur in particular row of puzzle
	* If number is not occur method returns true - it means - it's ok.
	*/
	public boolean checkRow(int row, int number)
	{
		for( int i = 0; i < 9; i++ )
		{
			if( puzzle[row][i] == number )
			{
				return false;
			}
		}
		return true;
	}

	/**
	* This method checks is the number occur in particular column of puzzle
	* If number is not occur method returns true - it means - it's ok.
	*/
	public boolean checkCol(int col, int number)
	{
		for( int i = 0; i < 9; i++ )
		{
			if( puzzle[i][col] == number ) 
				return false;
		}
		return true;
	}

	/**
	* This method checks is the number occur in 3x3 area of puzzle
	* according to row and column pass in
	*/
	public boolean check3x3(int row, int col, int number)
	{
		switch( row )
		{
			case 0:
			case 1:
			case 2:
				switch( col )
				{
					case 0:
					case 1:
					case 2:
					for( int i = 0; i < 3; i++ )
					{
						for( int j = 0; j < 3; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
					case 3:
					case 4:
					case 5:
					for( int i = 0; i < 3; i++ )
					{
						for( int j = 3; j < 6; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
					case 6:
					case 7:
					case 8:
					for( int i = 0; i < 3; i++ )
					{
						for( int j = 6; j < 9; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
				}
			case 3:
			case 4:
			case 5:
				switch( col )
				{
					case 0:
					case 1:
					case 2:
					for( int i = 3; i < 6; i++ )
					{
						for( int j = 0; j < 3; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
					case 3:
					case 4:
					case 5:
					for( int i = 3; i < 6; i++ )
					{
						for( int j = 3; j < 6; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
					case 6:
					case 7:
					case 8:
					for( int i = 3; i < 6; i++ )
					{
						for( int j = 6; j < 9; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
				}
			case 6:
			case 7:
			case 8:
				switch( col )
				{
					case 0:
					case 1:
					case 2:
					for( int i = 6; i < 9; i++ )
					{
						for( int j = 0; j < 3; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
					case 3:
					case 4:
					case 5:
					for( int i = 6; i < 9; i++ )
					{
						for( int j = 3; j < 6; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
					case 6:
					case 7:
					case 8:
					for( int i = 6; i < 9; i++ )
					{
						for( int j = 6; j < 9; j++ )
						{
							if( puzzle[i][j] == number ) 
								return false;
						}
					}
					return true;
				}
		}
		return false;
	}

	/**
	* Method returns true if cell is empty
	* otherwise - false
	*/
	public boolean isEmpty(int row, int col)
	{
		if( initialPuzzle[row][col] == 0 ) return true;
		else return false;
	}
	/**
	* Method which return true if we can put number to the cell
	* according the row and column
	*/
	public boolean isItOk(int row, int col, int number)
	{
		if( checkCol(col, number) 
			&& check3x3(row, col, number)
			&& isEmpty(row, col) 
			&& checkRow(row, number)) 
			return true;
		else return false;
	}
	
	public void drawSolvedImage(Canvas canvas)
	{
		dst = new Rect(0, 0, this.getWidth(), this.getHeight());
		canvas.drawBitmap(solvedIm, src, dst, null);
	}
	
	public void setSolved(boolean b)
	{
		solved = b; 
	}
	
	public void setNewData(int[][] initPuzzle, int[][] puzzle)
	{
		this.initialPuzzle = initPuzzle;
		this.puzzle = puzzle;
	}
}
