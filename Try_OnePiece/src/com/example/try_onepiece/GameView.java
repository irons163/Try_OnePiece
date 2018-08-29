package com.example.try_onepiece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class GameView extends View {
	public final int row = 10; // 總共10行
	public final int col = 10; // 總共10排
	public float width;
	public float height;
	private int selY;
	private int selX;
	public int grid[][] = new int[row][col];
	public List<Integer> type = new ArrayList<Integer>();
	public int much = 0; // 還剩下幾塊圖片尚未完成
	public final int GAMETIME = 300;
	public final int UPTIME = 1;
	public int PROCESS_VALUE = 300;// 最大秒數
	public int lineType = 0;
	public boolean isLine = false;
	private Rect selRect = new Rect();
	public final int V_LINE = 1;
	public final int H_LINE = 1;
	public final int ONE_C_LINE = 2;
	public final int TWO_C_LINE = 3;
	Point[] p;
	public Bitmap[] image;
	
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setFocusable(true);// 是設置是否允許此控件有獲得焦點的能力
		this.setFocusableInTouchMode(true);// 是否允許在觸控模式有獲得交焦點的能力
	}

	// 初始化圖片類型與數量
	public void initType() {
		int size = (row - 2) * (col - 2); // 整個圖片顯示區域8行*8排共64格(上下左右各留一行用來給紅線連)
		int count = size / imageType.length; // 64/16 = 4
		for (int j = 0; j < imageType.length; j++) { // 16張圖片
			for (int i = 0; i < count; i++) { // 4次循環，這邊是為了使每種圖片都剛好4張
				type.add(imageType[j]); // 總共16*4=64張圖片
			}
		}
	}

	// 初始化圖片
	public void initGrid() {
		Random ad = new Random();// 圖片隨機
		for (int i = 0; i < row; i++) {// 雙重迴圈隨機64次
			for (int j = 0; j < col; j++) {
				// i==0表示最上面一行，i == row - 1表示最下面一行，j == 0表示最左邊一排，j == col -
				// 1表示最右邊一排
				if (i == 0 || i == row - 1 || j == 0 || j == col - 1) {
					grid[i][j] = 0;// 這些都是不放圖片，要留給紅線的
				} else {
					if (type != null && type.size() > 0) {// 檢查type內有無圖片
						int index = ad.nextInt(type.size());// 0-63隨機
						grid[i][j] = type.get(index);// 顯示圖片
						type.remove(index);// 移除該圖片編號，避免重複選取，因為64張(每種各4張)要剛好用完
					}
				}
			}
		}
	}

	// 連連看用的圖片
	public int[] imageType = new int[] { R.drawable.aa, R.drawable.bb,
			R.drawable.cc, R.drawable.dd, R.drawable.ee, R.drawable.ff,
			R.drawable.gg, R.drawable.hh, R.drawable.ii, R.drawable.jj,
			R.drawable.kk, R.drawable.ll, R.drawable.mm, R.drawable.nn,
			R.drawable.oo, R.drawable.pp };

	@Override
	protected void onDraw(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(Color.WHITE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.hilite));
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.light));
		// 畫出網格
		for (int i = 0; i <= 9; i++) {
			canvas.drawLine(0, i * height, getWidth(), i * height, light);
			canvas.drawLine(0, i * height + 1, getWidth(), i * height + 1,
					hilite);
			canvas.drawLine(i * width, 0, i * width, getHeight(), light);
			canvas.drawLine(i * width + 1, 0, i * width + 1, getHeight(),
					hilite);
		}

		if (CtrlView.CURRENT_CH) {
			Paint selected = new Paint();
			selected.setColor(getResources().getColor(R.color.puzzle_selected));
			canvas.drawRect(selRect, selected);
		}

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (grid[i][j] != 0) {
					canvas.drawBitmap(
							image[Arrays.binarySearch(imageType, grid[i][j])],
							i * width, j * height, null);
				}
			}
		}

		if (isLine) {
			Paint lineColor = new Paint();
			lineColor.setColor(Color.RED);

			switch (lineType) {
			case V_LINE:
				canvas.drawLine(p[0].x * width + width / 2, p[0].y * height
						+ height / 2, p[1].x * width + width / 2, p[1].y
						* height + height / 2, lineColor);
				break;
			case ONE_C_LINE:
				canvas.drawLine(p[0].x * width + width / 2, p[0].y * height
						+ height / 2, p[1].x * width + width / 2, p[1].y
						* height + height / 2, lineColor);
				canvas.drawLine(p[1].x * width + width / 2, p[1].y * height
						+ height / 2, p[2].x * width + width / 2, p[2].y
						* height + height / 2, lineColor);
				break;
			case TWO_C_LINE:
				canvas.drawLine(p[0].x * width + width / 2, p[0].y * height
						+ height / 2, p[1].x * width + width / 2, p[1].y
						* height + height / 2, lineColor);
				canvas.drawLine(p[1].x * width + width / 2, p[1].y * height
						+ height / 2, p[2].x * width + width / 2, p[2].y
						* height + height / 2, lineColor);
				canvas.drawLine(p[3].x * width + width / 2, p[3].y * height
						+ height / 2, p[2].x * width + width / 2, p[2].y
						* height + height / 2, lineColor);
				break;
			default:
				break;
			}
		}
		super.onDraw(canvas);
	}
	
	//當size改變時進行的動作，在這裡是用來偵測螢幕大小的(android:layout_width="fill_parent" android:layout_height="fill_parent")
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = w / row; //得到每格的寬
		height = h / col; //得到每格的高
		// getRect(1,1,selRect);
		fillImage(this.getContext()); //圖片繪製
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public void fillImage(Context context) {
		int lth = imageType.length; //16
		image = new Bitmap[lth]; //16
		for (int i = 0; i < lth; i++) {
			Bitmap bitmap = Bitmap.createBitmap((int) width, (int) height,
					Bitmap.Config.ARGB_8888); //創建bitmap，設寬高與圖片色彩編碼，這是一個空白的bitmap
			Drawable drw; 
			Canvas canvas = new Canvas(bitmap); //將畫布設在空白bitmap上
			drw = context.getResources().getDrawable(imageType[i]); //得到圖片資源
			drw.setBounds(1, 1, 30, 30);//邊框
			drw.draw(canvas); //將圖片+邊框繪製在畫布上，此畫布是bitmap上，因此bitmap已經被繪製完成
			image[i] = bitmap; //將繪製完成的bitmap存入圖片陣列
		}
	}
}
