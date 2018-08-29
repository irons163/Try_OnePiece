package com.example.try_onepiece;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;

public class CtrlView extends GameView{

	public static boolean CURRENT_CH = false;
	public int CURRENT_TYPE = 0;
	private Point C_POINT;
	private Point P_POINT;
	
	public CtrlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initType();
		initGrid();
		much = (row - 2) * (col - 2);
		
	}
	
	public void reset() {
		CURRENT_CH = false;
		CURRENT_TYPE = 0;
		C_POINT = null;
		P_POINT = null;
		lineType = 0;
		isLine = false;
		Point[] p = null;
		initType();
		initGrid();
		much = (row - 2) * (col - 2);
		invalidate();
	}

	class Line {
		public Point a;
		public Point b;
		public int direct;

		public Line() {
		}

		public Line(int direct, Point a, Point b) {
			this.direct = direct;
			this.a = a;
			this.b = b;
		}
	}

}
