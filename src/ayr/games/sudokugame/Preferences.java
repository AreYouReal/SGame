package ayr.games.sudokugame;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Preferences extends Dialog implements android.view.View.OnClickListener
{
	public static boolean hideNums[] = new boolean[10];
	public Button num[] = new Button[11];
	
	public Preferences(Context context) 
	{
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.hidenums);
		
		findKeys();
		
		this.setTitle("Hide numbers in final solution");
	}
	
	public void findKeys()
	{
		num[0] = (Button)findViewById(R.id.hn_0);
		num[1] = (Button)findViewById(R.id.hn_1);
		num[2] = (Button)findViewById(R.id.hn_2);
		num[3] = (Button)findViewById(R.id.hn_3);
		num[4] = (Button)findViewById(R.id.hn_4);
		num[5] = (Button)findViewById(R.id.hn_5);
		num[6] = (Button)findViewById(R.id.hn_6);
		num[7] = (Button)findViewById(R.id.hn_7);
		num[8] = (Button)findViewById(R.id.hn_8);
		num[9] = (Button)findViewById(R.id.hn_9);
		num[10] = (Button)findViewById(R.id.hn_reset);
		
		for( int i = 0; i < num.length; i++ )
		{
			num[i].setOnClickListener(this);
			num[i].setTypeface(MainMenuActivity.mainFont);
			num[i].setTextSize(25);
		}
		for( int i =0; i < hideNums.length; i++ )
		{
			if(hideNums[i] == true)
			{
				num[i].setVisibility(Button.INVISIBLE);
			}
		}
	}

	public void onClick(View v) 
	{
		switch( v.getId() )
		{
		case R.id.hn_0: this.dismiss();
		break;
		case R.id.hn_1: 
			hideNumber(1);
			this.onCreate(null);
		break;
		case R.id.hn_2: 
			hideNumber(2);
			this.onCreate(null);
		break;
		case R.id.hn_3: 
			hideNumber(3);
			this.onCreate(null);
		break;
		case R.id.hn_4: 
			hideNumber(4);
			this.onCreate(null);
		break;
		case R.id.hn_5: 
			hideNumber(5);
			this.onCreate(null);
		break;
		case R.id.hn_6: 
			hideNumber(6);
			this.onCreate(null);
		break;
		case R.id.hn_7: 
			hideNumber(7);
			this.onCreate(null);
		break;
		case R.id.hn_8: 
			hideNumber(8);
			this.onCreate(null);
		break;
		case R.id.hn_9: 
			hideNumber(9);
			this.onCreate(null);
		break;
		case R.id.hn_reset: 
			reset();
			this.onCreate(null);
		break;
		}
	}
	
	public void hideNumber(int number)
	{
		if(hideNums[number] == false)
		{
			hideNums[number] = true;
		}else
		{
			hideNums[number] = false;
		}
	}
	
	public void reset()
	{
		for( int i = 0; i < hideNums.length; i++ )
			hideNums[i] = false;
	}
	
}
