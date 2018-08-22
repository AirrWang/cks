package com.ckjs.ck.Tool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;

import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ckjs.ck.R;
/**
 * Created by NiPing and Airr Wang
 * Copyright  www.chaokongs.com. All rights reserved.
 */
/**
 * 线框图
 *
 * 
 */
public class HomeDiagram extends View {

	private List<Integer> milliliter;
	private float tb;
	private float interval_left_right;
	private float interval_left;
	private Paint paint_date, paint_brokenLine, paint_dottedline,
			paint_brokenline_big, framPanint;

	private int time_index;
	private Path path;
	private float dotted_text;

	public float getDotted_text() {
		return dotted_text;
	}

	public void setDotted_text(float dotted_text) {
		this.dotted_text = dotted_text;
	}

	private int blueLineColor = 0xffffffff; // 蓝色

	public HomeDiagram(Context context, List<Integer> milliliter) {
		super(context);
		init(milliliter);
	}

	public void init(List<Integer> milliliter) {
		if (null == milliliter || milliliter.size() == 0)
			return;
		this.milliliter = delZero(milliliter);
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb);
		interval_left_right = tb * 2.0f;
		interval_left = tb * 0.5f;

		paint_brokenLine = new Paint();
		paint_brokenLine.setStrokeWidth(tb * 0.1f);
		paint_brokenLine.setColor(blueLineColor);
		paint_brokenLine.setAntiAlias(true);

		framPanint = new Paint();
		framPanint.setAntiAlias(true);
		framPanint.setStrokeWidth(2f);

		path = new Path();
//		bitmap_point = BitmapFactory.decodeResource(getResources(),
//				R.drawable.icon_point_blue);
		setLayoutParams(new LayoutParams(
				(int) (this.milliliter.size() * interval_left_right),
				LayoutParams.MATCH_PARENT));
	}

	/**
	 * 移除左右为零的数据
	 * 
	 * @return
	 */
	public List<Integer> delZero(List<Integer> milliliter) {
		List<Integer> list = new ArrayList<Integer>();
		int sta = 0;
		int end = 0;
		for (int i = 0; i < milliliter.size(); i++) {
			if (milliliter.get(i) != 0) {
				sta = i;
				break;
			}
		}
		for (int i = milliliter.size() - 1; i >= 0; i--) {
			if (milliliter.get(i) != 0) {
				end = i;
				break;
			}
		}
		for (int i = 0; i < milliliter.size(); i++) {
			if (i >= sta && i <= end) {
				list.add(milliliter.get(i));
			}
		}
		time_index = sta;
		dotted_text = ((Collections.max(milliliter) - Collections
				.min(milliliter)) / 12.0f * 5.0f);
		return list;
	}

	protected void onDraw(Canvas c) {
		if (null == milliliter || milliliter.size() == 0)
			return;
		drawBrokenLine(c);
	}


	/**
	 * 绘制折线
	 * 
	 * @param c
	 */
	public void drawBrokenLine(Canvas c) {
		int index = 0;
		float temp_x = 0;
		float temp_y = 0;
		float base = (getHeight() - tb * 3.0f)
				/ (Collections.max(milliliter) - Collections.min(milliliter));

		Shader mShader = new LinearGradient(0, 0, 0, getHeight(), new int[] {
				Color.argb(255, 255, 255, 255), Color.argb(255, 255, 255, 255),
				Color.argb(255, 255, 255, 255) }, null, Shader.TileMode.CLAMP);
		framPanint.setShader(mShader);

		for (int i = 0; i < milliliter.size() - 1; i++) {
			float x1 = interval_left_right * i;
			float y1 = getHeight() - tb * 1.5f - (base * milliliter.get(i));
			float x2 = interval_left_right * (i + 1);
			float y2 = getHeight() - tb * 1.5f - (base * milliliter.get(i + 1));

			if ((int) (base * milliliter.get(i + 1)) == 0 && index == 0) {
				index++;
				temp_x = x1;
				temp_y = y1;
			}
			if ((int) (base * milliliter.get(i + 1)) != 0 && index != 0) {
				index = 0;
				x1 = temp_x;
				y1 = temp_y;
			}
			if (index == 0) {
				c.drawLine(x1, y1, x2, y2, paint_brokenLine);
				path.lineTo(x1, y1);
//				if (i != 0)
//					c.drawBitmap(null,
//							x1 - bitmap_point.getWidth() / 2,
//							y1 - bitmap_point.getHeight() / 2, null);
				if (i == milliliter.size() - 2) {
					path.lineTo(x2, y2);
					path.lineTo(x2, getHeight());
					path.lineTo(0, getHeight());
					path.close();
					c.drawPath(path, framPanint);
//					c.drawBitmap(null,
//							x2 - bitmap_point.getWidth() / 2,
//							y2 - bitmap_point.getHeight() / 2, null);
				}
			}
		}

	}
}
