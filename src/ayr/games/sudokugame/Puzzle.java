package ayr.games.sudokugame;

/*
 * Puzzle.java
 *
 * Implementation of a class that represents a Sudoku puzzle and solves
 * it using recursive backtracking.
 *
 * Modified by:     Alexander Kolesnikov, kolesnikov@fas.harvard.edu
 * Date modified:   Semptember 23, 2012
 */

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Puzzle extends AsyncTask<int[][], Void, String>
{
	private Handler handler;
	// the dimension of the puzzle grid
	public static final int DIM = 9;

	// the dimension of the smaller squares within the grid
	public static final int SQUARE_DIM = 3; // I didn't use it

    // the current contents of the cells of the puzzle values[r][c]
    // gives the value in the cell at row r, column c
	private int values[][];

	// Solution's count
	private int sCount = 0;
	
	// ArrayList contains puzzle solutions
	private static ArrayList<int[][]> solutions = new ArrayList<int[][]>();
	
	/**
	* Default Puzzle constructor
	*/
	public Puzzle(Handler handler)
	{
		this.handler = handler;
	}

	/**
	* Method returns true if there is at least one puzzle solution
	* otherwise - false.
	* Method call tryIt(int row, int number) method which try to solve puzzle
	*/
	public boolean solve()
	{
		clear();
		if(!isValidPuzzle()) return false;
		this.tryIt(0, 1);
		if( sCount > 0 ) 
		{
			return true;
		}
		return false;
	}

	/**
	* This method checks is the number occur in particular row of puzzle
	* If number is not occur method returns true - it means - it's ok.
	*/
	public boolean checkRow(int row, int number)
	{
		for( int i = 0; i < DIM; i++ )
		{
			if( values[row][i] == number )
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
		for( int i = 0; i < DIM; i++ )
		{
			if( values[i][col] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
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
							if( values[i][j] == number ) 
								return false;
						}
					}
					return true;
				}
		}
		return false;
	}

	/**
	* Method which return true if we can put number to the cell
	* according the row and column
	*/
	public boolean isItOk(int row, int col, int number)
	{
		if( checkCol(col, number) 
			&& check3x3(row, col, number)
			&& isEmpty(row, col) ) 
			return true;
		else return false;
	}

	/**
	* Put a number to a cell on row and column
	*/
	public void setNum(int row, int col, int number)
	{
		values[row][col] = number;
	}

	/**
	* Remove a number from a cell
	*/
	public void removeNum(int row, int col)
	{
		values[row][col] = 0;
	}

	/**
	* Method returns true if cell is empty
	* otherwise - false
	*/
	public boolean isEmpty(int row, int col)
	{
		if( values[row][col] == 0 ) return true;
		else return false;
	}

	/**
	* Method which tries to solve a sudoku puzzle
	*/
	private void tryIt(int row, int number)
	{
		
		// If moew that 25 solutions - stop this stuff:)
		if( sCount >= 2 ) return;
		
		// In this case we have found solution for a particular number
		if( row == DIM ) tryIt(0, number + 1); 
		else
		{
			// If number reach 10 value - we have a solution for all numbers!
			// Increase number of solutions and return - try to find more solutions
			if( number == 10 )
			{
				// Increase solution count
				sCount++; 
				//System.out.println("\n Solution " + sCount + "\n");
				//display();
				solutions.add(copySolution());
				
				return;
			}
			// Find appropriate column for particular number
			for( int col = 0; col < DIM; col++ )
			{
				// If there is number on row - let's go to the next row
				if(!checkRow(row, number))
				{
					// If we have backtracking - return and try another combination
					tryIt(row + 1, number);
					return;
				}
				// Check can we put a number to the cell
				if(!isItOk(row, col, number)) continue;
				// Put number to a cell
				setNum(row, col, number);
				// Go forward...
				tryIt(row + 1, number);
				// If we have backtracking - remove number and try another column
				removeNum(row, col);
			}
			return;
		}
	}
	
	/**
	 * Method copy array of values to new array and return it;
	 * */
	public int[][] copySolution()
	{
		int[][] newSolution = new int[9][9];
		
		for( int i = 0; i < DIM; i++ )
		{
			for( int j = 0; j < DIM; j++ )
			{
				newSolution[i][j] = values[i][j];
			}
		}
		return newSolution;
	}
	
	public static int[][] getSolutions()
	{
		int [][] sol = solutions.get(0);
		clear();
		return sol;
	}

	
	// Is it method really need?
	public static void clear()
	{
		for( int i = 0; i < solutions.size(); i++ )
		{
			solutions.remove(i);
		}
	}
	
	public int getSolutionCount()
	{
		return sCount;
	}
	
	public boolean isValidPuzzle()
	{
		for( int number = 1; number <= 9; number++ )
		{
			for( int row = 0; row < DIM; row++ )
			{
				if( numOccureInRow(row, number) > 1 ) return false;
			}
			for( int column = 0; column < DIM; column++ )
			{
				if( numOccureInColumn(column, number) > 1 ) return false;
			}
			for( int row = 0; row < DIM; row += 3 )
			{
				for( int column = 0; column < DIM; column += 3 )
				{
					if( numOccureIn3x3(row, column, number) > 1 ) return false;
				}
			}
		}
		return true;
	}
	
	
	public int numOccureInRow(int row, int num)
	{
		int count = 0;
		for( int column = 0; column < DIM; column++ )
		{
			if( values[row][column] == num ) count++;
		}
		return count;
	}
	
	public int numOccureInColumn(int column, int num)
	{
		int count = 0;
		for( int row = 0; row < DIM; row++ )
		{
			if( values[row][column] == num ) count++;
		}
		return count;
	}
	
	
	public int numOccureIn3x3(int row, int col, int number)
	{
		int count = 0;
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
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
					case 3:
					case 4:
					case 5:
					for( int i = 0; i < 3; i++ )
					{
						for( int j = 3; j < 6; j++ )
						{
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
					case 6:
					case 7:
					case 8:
					for( int i = 0; i < 3; i++ )
					{
						for( int j = 6; j < 9; j++ )
						{
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
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
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
					case 3:
					case 4:
					case 5:
					for( int i = 3; i < 6; i++ )
					{
						for( int j = 3; j < 6; j++ )
						{
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
					case 6:
					case 7:
					case 8:
					for( int i = 3; i < 6; i++ )
					{
						for( int j = 6; j < 9; j++ )
						{
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
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
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
					case 3:
					case 4:
					case 5:
					for( int i = 6; i < 9; i++ )
					{
						for( int j = 3; j < 6; j++ )
						{
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
					case 6:
					case 7:
					case 8:
					for( int i = 6; i < 9; i++ )
					{
						for( int j = 6; j < 9; j++ )
						{
							if( values[i][j] == number ) 
								count++;
						}
					}
					return count;
				}
		}
		return count;
	}


	@Override
	protected String doInBackground(int[][]... params) 
	{
		Log.d("ASYNK_PUZZLE", "In background");
		this.values = params[0];
		this.values = copySolution();
		if(!solve()) 
		{
			Log.d("ASYNK_PUZZLE", "In background Not solved:(((");
			return "oops";
		}else 
		{
			Log.d("ASYNK_PUZZLE", "In background SOLVED");
			return "ok";
		}
	}
	
	@Override
	protected void onPostExecute(String result)
	{
		Log.d("ASYNK_PUZZLE", "On Post Execute");
		super.onPostExecute(result);
		Message msg = new Message();
		Bundle b = new Bundle();
		if( result.equals("ok"))
		{
		b.putBoolean("Result", true);
		} else
		{
			b.putBoolean("Result", false);
		}
		msg.setData(b);
		handler.sendMessage(msg);
	}

}