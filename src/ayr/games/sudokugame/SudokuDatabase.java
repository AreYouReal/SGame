package ayr.games.sudokugame;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

// SudokuDatabase.java


public class SudokuDatabase extends AsyncTask<String, Void, Boolean>
{
	public SudokuDatabase(Context context, Handler handler)
	{
		SudokuDatabase.mContext = context;
		this.handler = handler;
	}

	class PuzzleForSolving
	{
		public int[][] puzzle = new int[9][9];
	}

	private static Context mContext;
	private ArrayList<PuzzleForSolving> puzzles = new ArrayList<PuzzleForSolving>();
	private static int puzzleCount = 0;
	private Handler handler;

	private static int [][] puzzle;
	private static int [][] initPuzzle;
	
	private static boolean vibrate = true;
	
	private static boolean areEquals(int [][] puzzle, int[][] initPuzzle)
	{
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				if(puzzle[i][j] != initPuzzle[i][j])
					return false;
			}
		}
		return true;
	}
	
	public static void saveLastPuzzleState(int[][] puzzle, int[][] initPuzzle) throws IOException
	{
		if(areEquals(puzzle, initPuzzle)) 
		{
			clearSavedPuzzles();
			return;
		}
		File file = new File(mContext.getExternalCacheDir() + "/SLastGame.txt");
		file.createNewFile();
		FileWriter fw = new FileWriter(file);
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				fw.write(puzzle[i][j]);
				fw.write(initPuzzle[i][j]);
			}
		}
		fw.close();
		Log.e("SUDOKU_PATH", "" + file.getAbsolutePath());
	}
	
	public static boolean loadLastPuzzleState() throws IOException
	{
		File file = new File(mContext.getExternalCacheDir() + "/SLastGame.txt");
		if(!file.exists()) return false;
		FileReader fr = new FileReader(file);
		
		puzzle = new int[9][9];
		initPuzzle = new int[9][9];
		
		for(int i = 0; i < 9; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				puzzle[i][j] = fr.read();
				initPuzzle[i][j] = fr.read();
			}
		}
		fr.close();
		Log.e("SUDOKU_PATH", "" + file.getAbsolutePath());
		file.delete();
		return true;
	}
	
	private void readFile(String fileName)
	{
		AssetManager aManager = mContext.getAssets();
		InputStreamReader isr = null;
		try {
			isr = new InputStreamReader(aManager.open(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PuzzleForSolving temp = null;
		int number = -1;
		try {
			while((number = isr.read()) != -1)
			{
				if(puzzles.size() == 30)
				{
					Message msg = new Message();
					Bundle b = new Bundle();

					b.putBoolean("Result", true);

					msg.setData(b);
					handler.sendMessage(msg);
				}
				while(number  == 10)
				{
					number = isr.read();
				};
				while(number == 13)
				{
					number = isr.read();
				};
				if(number == -1) break;
				temp = new PuzzleForSolving();
				for(int i = 0; i < 9; i++)
				{
					for(int j = 0; j < 9; j++)
					{
						temp.puzzle[i][j] = number - 48;
						number = isr.read();
						while(number == 10)
						{
							number = isr.read();
						};
						while(number == 13)
						{
							number = isr.read();
						};
					}
				}
				puzzles.add(temp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		// Read all puzzles according to the filename (difficult)
		// and fill the puzzles variable
	}

	// Method returns next puzzle from arraylist
	public PuzzleForSolving getNextPuzzle()
	{
		if(puzzles.size() == 0) return new PuzzleForSolving();
		Random rnd = new Random();
		return puzzles.get(rnd.nextInt(puzzles.size()));
	}

	public int size()
	{
		return puzzles.size();
	}

	@Override
	protected Boolean doInBackground(String... params) 
	{
		readFile(params[0]);
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result)
	{
		Log.d("ASYNK_PUZZLE", "On Post Execute");
		super.onPostExecute(result);
/*		Message msg = new Message();
		Bundle b = new Bundle();

		b.putBoolean("Result", result);

		msg.setData(b);
		handler.sendMessage(msg);*/
	}
	
	public static int[][] getInitPuzzle()
	{
		return initPuzzle;
	}
	
	public static int[][] getPuzzle()
	{
		return puzzle;
	}
	
	public static void clearSavedPuzzles()
	{
		SudokuDatabase.initPuzzle = null;
		SudokuDatabase.puzzle = null;
	}
	
	public static void setVibrate(boolean b)
	{
		vibrate = b;
	}
	
	public static boolean vibrate()
	{
		return vibrate;
	}
}