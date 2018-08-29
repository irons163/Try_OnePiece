package com.example.try_onepiece;

import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends Activity {

	private ProgressBar pb;
	private TextView show_RemainTime;
	private CtrlView cv;
	public static final int START_ID = Menu.FIRST;
	public static final int REARRARY_ID = Menu.FIRST + 1;
	public static final int END_ID = REARRARY_ID + 1;
	private int dormant = 1000; //延遲一秒
	private boolean isCancel=true;

	//LinkedList<Line> li;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findViews();//初始化布局元件，等同於initView
		mRedrawHandler.sleep(dormant);

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
	private void findViews() {
		pb = (ProgressBar) findViewById(R.id.pb); //倒數讀秒條
		show_RemainTime = (TextView) findViewById(R.id.show_remainTime); //倒數秒數
		cv = (CtrlView) findViewById(R.id.cv); //中間主要View(一格一格)
		pb.setMax(cv.GAMETIME); //最大秒數
		pb.incrementProgressBy(-1); //每次減1秒
		pb.setProgress(cv.PROCESS_VALUE); //最大秒數
	}
	private RefreshHandler mRedrawHandler = new RefreshHandler();

	class RefreshHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if(isCancel){
				run();
			}else{}						
		}

		public void sleep(long delayMillis) {
			this.removeMessages(0);// 移除訊息柱列中最底部的訊息（從底部取出訊息）
			sendMessageDelayed(obtainMessage(0), delayMillis);// 獲得頂部訊息並延遲發送
		}
	};
	
	public void run() {
		if (cv.PROCESS_VALUE > 0 && cv.much != 0) {
			cv.PROCESS_VALUE--;//每次減一秒
			pb.setProgress(cv.PROCESS_VALUE);//將現在秒數呈現在progress bar上
			show_RemainTime.setText(String.valueOf(cv.PROCESS_VALUE));//數字呈現在textview
			mRedrawHandler.sleep(dormant);//延遲一秒
		} else if (cv.PROCESS_VALUE == 0 && cv.much != 0) {
			cv.setEnabled(false); //CV失去執行能力
			dialogForFail().show();//失敗訊息
		} else if (cv.PROCESS_VALUE != 0 && cv.much == 0) {
			cv.setEnabled(false);
			dialogForSucceed().show(); //成功訊息
		}
	}
	
	public AlertDialog dialogForSucceed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon).setMessage(R.string.succeedInfo)
				.setPositiveButton(R.string.next,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dormant = dormant - 300;//成功後，下次遊戲變難，每0.7秒減1
								newPlay();//啟動新遊戲
							}
						}).setNeutralButton(R.string.again_challenge,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newPlay();//再次挑戰(難度不變)
							}
						});
		return builder.create();
	}

	public AlertDialog dialogForFail() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.icon).setMessage(R.string.failInfo)
				.setPositiveButton(R.string.again_challenge,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								newPlay();
							}
						}).setNegativeButton(R.string.exit,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								isCancel=false;
								finish();//離開遊戲
							}
						});
		return builder.create();
	}

	//新遊戲(重新挑戰)
	public void newPlay() {
		cv.reset();
		pb.setProgress(cv.GAMETIME);
		cv.PROCESS_VALUE = cv.GAMETIME;
		mRedrawHandler.sleep(dormant);
		cv.setEnabled(true);
	}
	

	

	
}
