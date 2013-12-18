package ayr.games.sudokugame;

import java.io.IOException;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DifficultyDialog extends Dialog implements OnClickListener
{
	private Button easyBtn, mediumBtn, hardBtn, resume;
	private TextView tv;
	private MainMenuActivity ma;
	private Context mContext;
	
	public DifficultyDialog(Context context, MainMenuActivity ma) 
	{
		super(context);
		this.ma = ma;
		this.mContext = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.difficulty);
		
		easyBtn = (Button)findViewById(R.id.easyBtn);
		easyBtn.setOnClickListener(this);
		easyBtn.setTypeface(MainMenuActivity.mainFont);
		mediumBtn = (Button)findViewById(R.id.mediumBtn);
		mediumBtn.setOnClickListener(this);
		mediumBtn.setTypeface(MainMenuActivity.mainFont);
		hardBtn = (Button)findViewById(R.id.hardBtn);
		hardBtn.setOnClickListener(this);
		hardBtn.setTypeface(MainMenuActivity.mainFont);
		resume = (Button)findViewById(R.id.resumeBtn);
		resume.setOnClickListener(this);
		resume.setTypeface(MainMenuActivity.mainFont);
		tv = (TextView)findViewById(R.id.difficultyTitle);
		tv.setTypeface(MainMenuActivity.mainFont);
		tv.setTextSize(25);
	}

	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.easyBtn:
			ma.setDifficulty(0);
			break;
		case R.id.mediumBtn:
			ma.setDifficulty(1);
			break;
		case R.id.hardBtn:
			ma.setDifficulty(2);
			break;
		case R.id.resumeBtn:
			try {
				if(SudokuDatabase.loadLastPuzzleState())
				{
					ma.setDifficulty(3);
				}else
				{
					Toast.makeText(mContext, "There are no saved games:(", Toast.LENGTH_SHORT).show();
					return;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		ma.loadPuzzles();
		this.dismiss();
	}

}
