package com.sincerly.fightcontentview.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;


import com.sincerly.fightcontentview.bean.ChartBean;
import com.sincerly.fightcontentview.data.TrendData;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 *
 */
public class DDTrendChart extends ATrendChart {
	private static final boolean DEBUG = false;
	private static final String TAG = "DDTrendChart";
	private int blueCount = 16;
	final int mDefDLTBlueCount = 12;
	final int mDefDLTRedCount = 35;
	final int mDefSSQBlueCount = 16;
	final int mDefSSQRedCount = 33;
	private boolean mDrawLine = true;
	private String mLotteryType;
	final int mMaxSignleNum = 9;
	private Path mPathPoint = new Path();
	private Path mPath2Point = new Path();
	private Path mPath3Point = new Path();
	private TreeSet<Integer> mSelectedBlue = new TreeSet();
	private ISelectedChangeListener mSelectedChangeListener;
	private TreeSet<Integer> mSelectedRed = new TreeSet();
	private boolean mShowYilou = true;
	private ArrayList<ChartBean> mTrendData;
	private int redCount = 12;

	private String[] titles = new String[]{"时时彩", "五星走势图", "五星组选形态", "0路号码个数", "1路号码个数", "2路号码个数"};
	private String[] label1 = new String[]{"开奖期数", "号码"};
	private String[] label2 = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};//五星走势图
	private String[] label3 = new String[]{"组5", "组10", "组20", "组30", "组60", "组120"};//五星组选形态
	private String[] label4 = new String[]{"0", "1", "2", "3", "4", "5"};//0路号码个数  1路号码个数 2路号码个数
	private int type1ColCount = 10;//五星走势图列数
	private int type2ColCount = 6;//五星组选形态列数
	private int type3ColCount = 6;//0路号码个数列数
	private int type4ColCount = 6;//1路号码个数列数
	private int type5ColCount = 6;//2路号码个数列数

	public interface ISelectedChangeListener {
		void onSelectedChange(TreeSet<Integer> treeSet, TreeSet<Integer> treeSet2);
	}

	public DDTrendChart(Context context, LottoTrendView lottoTrendView) {
		super(context, lottoTrendView);
		this.mPaint.setTextAlign(Align.CENTER);
	}

	/**
	 * @param str       种类（大乐透或者双色球）
	 * @param arrayList 数据
	 */
	public void updateData(String str, ArrayList<ChartBean> arrayList) {
		if (arrayList != null && arrayList.size() != 0) {
			if ("01".equals(str) || "50".equals(str)) {
				this.mLotteryType = str;
			} else {
				this.mLotteryType = "01";
			}
			this.mTrendData = arrayList;
			this.mSelectedRed.clear();
			this.mSelectedBlue.clear();
			this.mPathPoint.reset();
			this.mPath2Point.reset();
			this.mPath3Point.reset();
			this.mScaleRange = new float[]{0.0f, 2.0f};
			if ("01".equals(this.mLotteryType)) {
				for (int i = 0; i < arrayList.size(); i++) {
					ChartBean trendData = (ChartBean) arrayList.get(i);

					ChartBean.Data3 d = trendData.getData3();
					ChartBean.Data4 d2 = trendData.getData4();
					ChartBean.Data5 d3 = trendData.getData5();

					int[] aaa = new int[]{d.getC1(), d.getC2(), d.getC3(), d.getC4(), d.getC5(), d.getC6()};
					for (int j = 0; j < aaa.length; j++) {
						if (aaa[j] == -1) {//选中的那个
							float f = (((((float) this.type1ColCount + this.type2ColCount) + 0.5f) + (float) j) * ((float) this.mXItemWidth)) + ((float) this.mDivWidth * 2);
							float f2 = (((float) i) + 0.5f) * ((float) this.mXItemHeight);
							if (i == 0) {
								this.mPathPoint.moveTo(f, f2);
							} else {
								this.mPathPoint.lineTo(f, f2);
							}
						}
					}

					/**
					 * 同时画3点
					 */
					int[] aa = new int[]{d2.getD1(), d2.getD2(), d2.getD3(), d2.getD4(), d2.getD5(), d2.getD6()};
					for (int j = 0; j < aa.length; j++) {
						if (aa[j] == -1) {
							float f = (((((float) this.type1ColCount + this.type2ColCount + this.type3ColCount) + 0.5f) + (float) j) * ((float) this.mXItemWidth)) + ((float) this.mDivWidth * 3);
							float f2 = (((float) i) + 0.5f) * ((float) this.mXItemHeight);
							if (i == 0) {
								this.mPath2Point.moveTo(f, f2);
							} else {
								this.mPath2Point.lineTo(f, f2);
							}
						}
					}

					int[] a = new int[]{d3.getE1(), d3.getE2(), d3.getE3(), d3.getE4(), d3.getE5(), d3.getE6()};
					for (int j = 0; j < a.length; j++) {
						if (a[j] == -1) {
							float f = (((((float) this.type1ColCount + this.type2ColCount + this.type3ColCount + this.type4ColCount) + 0.5f) + (float) j) * ((float) this.mXItemWidth)) + ((float) this.mDivWidth * 4);
							float f2 = (((float) i) + 0.5f) * ((float) this.mXItemHeight);
							if (i == 0) {
								this.mPath3Point.moveTo(f, f2);
							} else {
								this.mPath3Point.lineTo(f, f2);
							}
						}
					}
				}
			}
			if (this.mTrendView != null) {
				initChart(this.mTrendView.getContext(), this.mTrendView.getWidth(), this.mTrendView.getHeight(), this.mTrendView.getScale());
				this.mTrendView.invalidate();
			}
			if (this.mSelectedChangeListener != null) {
				this.mSelectedChangeListener.onSelectedChange(this.mSelectedRed, this.mSelectedBlue);
			}
		}
	}

	public void initChart(Context context, int i, int i2, float f) {
		if (i != 0 && i2 != 0 && this.mTrendData != null) {
			super.initChart(context, i, i2, f);
			if (this.mTrendView != null) {
//				this.mTrendView.setNowY((float) (-this.mPicY.getHeight()));
				this.mTrendView.setNowY((float) 0);
				this.mTrendView.setNowX((float) 0);
			}
		}
	}

	public void setSelectedChangeListener(ISelectedChangeListener iSelectedChangeListener) {
		this.mSelectedChangeListener = iSelectedChangeListener;
	}

	/**
	 * 画线
	 *
	 * @param z
	 */
	public void setDrawLine(boolean z) {
		if ((this.mDrawLine != z ? 1 : null) != null) {
			this.mDrawLine = z;
			if (this.mTrendData != null) {
				drawContent();
			}
			if (this.mTrendView != null) {
				this.mTrendView.invalidate();
			}
		}
	}

	/**
	 * 遗漏
	 *
	 * @param z
	 */
	public void setShowYilou(boolean z) {
		if ((this.mShowYilou != z ? 1 : null) != null) {
			this.mShowYilou = z;
			if (this.mTrendData != null) {
				drawContent();
			}
			if (this.mTrendView != null) {
				this.mTrendView.invalidate();
			}
		}
	}


	public boolean onClick(MotionEvent motionEvent, float f, float f2, int i, int i2, float f3) {
		if (motionEvent.getY() <= ((float) i2) - (((float) this.mXItemHeight) * f3) || motionEvent.getX() <= ((float) this.mYItemWidth) * f3) {
			return false;
		}
		int x = (int) ((motionEvent.getX() - f) / (((float) this.mXItemWidth) * f3));
		if (x >= this.redCount) {
			x = ((int) (((motionEvent.getX() - f) - ((float) this.mDivWidth)) / (((float) this.mXItemWidth) * f3))) - this.redCount;
			if (this.mSelectedBlue.contains(Integer.valueOf(x))) {
				this.mSelectedBlue.remove(Integer.valueOf(x));
			} else {
				this.mSelectedBlue.add(Integer.valueOf(x));
			}
			if (this.mSelectedChangeListener != null) {
				this.mSelectedChangeListener.onSelectedChange(this.mSelectedRed, this.mSelectedBlue);
			}
		} else {
			if (this.mSelectedRed.contains(Integer.valueOf(x))) {
				this.mSelectedRed.remove(Integer.valueOf(x));
			} else {
				this.mSelectedRed.add(Integer.valueOf(x));
			}
			if (this.mSelectedChangeListener != null) {
				this.mSelectedChangeListener.onSelectedChange(this.mSelectedRed, this.mSelectedBlue);
			}
		}
		drawXBottom();
		return true;
	}

//    private List<String> data = new ArrayList<>();

	/**
	 * 画y轴
	 */
	public void drawY() {
		Canvas beginRecording = mPicY.beginRecording(mYItemWidth * 2, (mYItemHeight * mTrendData.size()) + mDivHeight);
		mPaint.setStyle(Paint.Style.FILL);

		int size = mTrendData.size();
		for (int i = 0; i < size; i++) {
			int i2 = i * mYItemHeight;
			this.mRect.set(0, i2, this.mYItemWidth, this.mYItemHeight + i2);
			if (i % 2 == 0) {
				this.mPaint.setColor(this.mCEvenY);
			} else {
				this.mPaint.setColor(this.mCOddY);
			}
			beginRecording.drawRect(mRect, mPaint);
			mPaint.setColor(mCYText);

			mPaint.setTextSize((float) mYTextSize);
			drawText2Rect(mTrendData.get(i).getNo(), beginRecording, mRect, mPaint);
		}

		mPaint.setColor(Color.parseColor("#80545454"));
		beginRecording.drawLine((float) mYItemWidth, (float) mYItemHeight, (float) mYItemWidth, (float) mYItemHeight * mTrendData.size(), this.mPaint);

		for (int i = 0; i < size; i++) {
			int i2 = i * mYItemHeight;
			mRect.set(mYItemWidth, i2, mYItemWidth * 2, mYItemHeight + i2);
			if (i % 2 == 0) {
				mPaint.setColor(mCEvenY);
			} else {
				mPaint.setColor(mCOddY);
			}
			beginRecording.drawRect(mRect, mPaint);
			mPaint.setColor(mCYText);

			mPaint.setTextSize((float) mYTextSize);
			drawText2Rect(mTrendData.get(i).getNumber(), beginRecording, mRect, mPaint);
		}

		mPicY.endRecording();
	}

	/**
	 * 左下角
	 */
	public void drawLeftBottom() {
		int i = this.mXItemHeight + this.mBottomMargin;
		Canvas beginRecording = this.mPicLeftBottom.beginRecording(this.mYItemWidth, i);
		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setColor(-1);
		this.mRect.set(0, 0, this.mYItemWidth, i);
		beginRecording.drawRect(this.mRect, this.mPaint);
		this.mPaint.setColor(this.mCDiv);
		beginRecording.drawLine(0.0f, 2.0f, (float) this.mYItemWidth, 2.0f, this.mPaint);
		this.mPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
		this.mPaint.setTextSize((float) this.mLcTextSize);
		this.mRect.set(0, this.mBottomMargin, this.mYItemWidth, i);
		drawText2Rect("预选区", beginRecording, this.mRect, this.mPaint);
		this.mPicLeftBottom.endRecording();
	}

	/**
	 * 画左上角
	 */
	public void drawLeftTop() {
		Canvas beginRecording = this.mPicLeftTop.beginRecording(this.mYItemWidth * 2, this.mXItemHeight * 2);
		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setColor(this.mCXTitleBg);
		this.mRect.set(0, 0, this.mYItemWidth * 2, this.mXItemHeight * 2);
		beginRecording.drawRect(this.mRect, this.mPaint);

		this.mPaint.setColor(-1);
		this.mPaint.setTextSize((float) this.mLcTextSize);
		this.mRect.set(0, 0, this.mYItemWidth * 2, this.mXItemHeight);
		drawText2Rect(titles[0], beginRecording, this.mRect, this.mPaint);

		mPaint.setColor(this.mCDiv);
		beginRecording.drawLine(0, mXItemHeight, (float) mYItemWidth * 2, (float) mXItemHeight, mPaint);

		this.mRect.set(0, mXItemHeight, this.mYItemWidth, this.mXItemHeight * 2);
		this.mPaint.setColor(-1);
		this.mPaint.setTextSize((float) this.mLcTextSize);
		drawText2Rect("期数", beginRecording, this.mRect, this.mPaint);
//        this.mRect.set(0, 0, this.mYItemWidth * 2, this.mXItemHeight);

		mPaint.setColor(this.mCDiv);
		beginRecording.drawLine(mYItemWidth, mXItemHeight, (float) mYItemWidth, (float) mXItemHeight * 2, mPaint);

		this.mRect.set(mYItemWidth, mXItemHeight, this.mYItemWidth * 2, this.mXItemHeight * 2);
		drawText2Rect("号码", beginRecording, this.mRect, this.mPaint);

		//画左侧两个y轴title 与滑动标签的中间线
		this.mPicLeftTop.endRecording();
	}

	/**
	 * 画上边
	 */
	public void drawXTop() {
		int i;
		int i2 = mDivWidth * 4 + (mXItemWidth * (type1ColCount + type2ColCount + type3ColCount + type4ColCount + type5ColCount));
		int i3 = mXItemHeight * 2;
		Canvas beginRecording = mPicXTop.beginRecording(i2, i3);

		//绘制title
		mRect.set(0, 0, i2, i3);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(this.mCXTitleBg);
		beginRecording.drawRect(mRect, mPaint);

		int w1 = mXItemWidth * type1ColCount;
		int w2 = mDivWidth * 1 + (mXItemWidth * (type1ColCount + type2ColCount));
		int w3 = mDivWidth * 2 + (mXItemWidth * (type1ColCount + type2ColCount + type3ColCount));
		int w4 = mDivWidth * 3 + (mXItemWidth * (type1ColCount + type2ColCount + type3ColCount + type4ColCount));
		int w5 = mDivWidth * 4 + (mXItemWidth * (type1ColCount + type2ColCount + type3ColCount + type4ColCount + type5ColCount));

//        mPaint.setColor(this.mCDiv);
		//beginRecording.drawLine((float) i4, 0.0f, (float) i4, (float) i3, mPaint);
		mPaint.setColor(-1);
		mRect.set(0, 0, w1, mXItemHeight);
		drawText2Rect(titles[1], beginRecording, mRect, mPaint);
		mRect.set(w1, 0, w2, mXItemHeight);
		drawText2Rect(titles[2], beginRecording, mRect, mPaint);
		mRect.set(w2, 0, w3, mXItemHeight);
		drawText2Rect(titles[3], beginRecording, mRect, mPaint);
		mRect.set(w3, 0, w4, mXItemHeight);
		drawText2Rect(titles[4], beginRecording, mRect, mPaint);
		mRect.set(w4, 0, w5, mXItemHeight);
		drawText2Rect(titles[5], beginRecording, mRect, mPaint);


		//绘制title下方线
		mPaint.setColor(this.mCDiv);
		beginRecording.drawLine(0, mXItemHeight, (float) i2, (float) mXItemHeight, mPaint);
		//绘制二级title 空白间距
		mPaint.setColor(-1);
		mRect.set(type1ColCount * mXItemWidth, 0, (type1ColCount * mXItemWidth) + mDivWidth, i3);
		beginRecording.drawRect(mRect, mPaint);

		mPaint.setColor(-1);
		mRect.set(w2, 0, w2 + mDivWidth, i3);
		beginRecording.drawRect(mRect, mPaint);

		mPaint.setColor(-1);
		mRect.set(w3, 0, w3 + mDivWidth, i3);
		beginRecording.drawRect(mRect, mPaint);

		mPaint.setColor(-1);
		mRect.set(w4, 0, w4 + mDivWidth, i3);
		beginRecording.drawRect(mRect, mPaint);

		mPaint.setColor(-1);
		mRect.set(w5, 0, w5 + mDivWidth, i3);
		beginRecording.drawRect(mRect, mPaint);

//        this.mPaint.setColor(-1);
//        this.mRect.set((this.type1ColCount+type2ColCount) * this.mXItemWidth, 0, (this.type1ColCount * this.mXItemWidth) + (this.mDivWidth*2), i3);
//        beginRecording.drawRect(this.mRect, this.mPaint);

		mPaint.setTextSize((float) this.mLcTextSize);
		//1.五星走势图title
		for (i = 1; i <= type1ColCount; i++) {
			String str;
			int i4 = i * mXItemWidth;
			mPaint.setColor(this.mCDiv);
			beginRecording.drawLine((float) i4, mXItemHeight, (float) i4, (float) i3, mPaint);
			mRect.set(i4 - mXItemWidth, mXItemHeight, i4, i3);
			mPaint.setColor(-1);
			str = label2[i - 1];
			drawText2Rect(str, beginRecording, mRect, mPaint);
		}


		//2.五星组选形态title
		for (i = 1; i <= type2ColCount; i++) {
			String str;
			int i4 = ((type1ColCount + i) * mXItemWidth) + mDivWidth;
			beginRecording.drawLine((float) i4, mXItemHeight, (float) i4, (float) i3, mPaint);
			mRect.set(i4 - mXItemWidth, mXItemHeight, i4, i3);
			mPaint.setColor(-1);
			str = label3[i - 1];
			drawText2Rect(str, beginRecording, mRect, mPaint);
		}
		//3.0路个数title
		for (i = 1; i <= type3ColCount; i++) {
			String str;
			int i4 = ((type1ColCount + type2ColCount + i) * mXItemWidth) + mDivWidth * 2;
			beginRecording.drawLine((float) i4, mXItemHeight, (float) i4, (float) i3, mPaint);
			mRect.set(i4 - mXItemWidth, mXItemHeight, i4, i3);
			mPaint.setColor(-1);
			str = label4[i - 1];
			drawText2Rect(str, beginRecording, mRect, mPaint);
		}
		//4.1路个数title
		for (i = 1; i <= type4ColCount; i++) {
			String str;
			int i4 = ((type1ColCount + type2ColCount + type3ColCount + i) * mXItemWidth) + mDivWidth * 3;
			beginRecording.drawLine((float) i4, mXItemHeight, (float) i4, (float) i3, mPaint);
			mRect.set(i4 - mXItemWidth, mXItemHeight, i4, i3);
			mPaint.setColor(-1);
			str = label4[i - 1];
			drawText2Rect(str, beginRecording, mRect, mPaint);
		}
		//5.2路个数title
		for (i = 0; i < type5ColCount; i++) {
			String str;
			int i4 = ((type1ColCount + type2ColCount + type3ColCount + type4ColCount + i) * mXItemWidth) + mDivWidth * 4;
			beginRecording.drawLine((float) i4, mXItemHeight, (float) i4, (float) i3, mPaint);
			mRect.set(i4, mXItemHeight, mXItemWidth + i4, i3);
			mPaint.setColor(-1);
			str = label4[i];
			drawText2Rect(str, beginRecording, mRect, mPaint);
		}
		mPicXTop.endRecording();
	}

	/**
	 * 画下面
	 */
	public void drawXBottom() {
		int i = 1;
		int i2 = (this.mXItemWidth * (this.redCount + this.blueCount)) + this.mDivWidth;
		int i3 = this.mXItemHeight + this.mBottomMargin;
		Canvas beginRecording = this.mPicXBottom.beginRecording(i2, i3);
		this.mRect.set(0, 0, i2, i3);
		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setColor(-1);
		beginRecording.drawRect(this.mRect, this.mPaint);
		this.mPaint.setColor(this.mCDiv);
		beginRecording.drawLine(0.0f, 2.0f, (float) i2, 2.0f, this.mPaint);
		this.mPaint.setTextSize((float) this.mXTextSize);
		for (int i4 = 1; i4 <= this.redCount; i4++) {
			String str;
			this.mRect.set((i4 - 1) * this.mXItemWidth, this.mBottomMargin, this.mXItemWidth * i4, i3);
			if (this.mSelectedRed.contains(Integer.valueOf(i4 - 1))) {
				this.mPaint.setStyle(Style.FILL);
				this.mPaint.setColor(this.mCBallSelectedRed);
				beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
				this.mPaint.setColor(-1);
			} else {
				this.mPaint.setStyle(Style.STROKE);
				this.mPaint.setColor(this.mCBallSelectedStroke);
				beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
				this.mPaint.setColor(this.mCXbottomTextRed);
				this.mPaint.setStyle(Style.FILL);
			}
			if (i4 <= 9) {
				str = "0" + i4;
			} else {
				str = "" + i4;
			}
			drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
		}
		while (i <= this.blueCount) {
			String str2;
			this.mRect.set((((this.redCount + i) - 1) * this.mXItemWidth) + this.mDivWidth, this.mBottomMargin, ((this.redCount + i) * this.mXItemWidth) + this.mDivWidth, i3);
			if (this.mSelectedBlue.contains(Integer.valueOf(i - 1))) {
				this.mPaint.setStyle(Style.FILL);
				this.mPaint.setColor(this.mCBallSelectedBlue);
				beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
				this.mPaint.setColor(-1);
			} else {
				this.mPaint.setStyle(Style.STROKE);
				this.mPaint.setColor(this.mCBallSelectedStroke);
				beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
				this.mPaint.setColor(this.mCXbottomTextBlue);
				this.mPaint.setStyle(Style.FILL);
			}
			if (i <= 9) {
				str2 = "0" + i;
			} else {
				str2 = "" + i;
			}
			drawText2Rect(str2, beginRecording, this.mRect, this.mPaint);
			i++;
		}
		this.mPicXBottom.endRecording();
	}


	/**
	 * 画球
	 */
	public void drawContent() {
		int i;
		int i2 = (this.mXItemWidth * ( this.type1ColCount + type2ColCount + type3ColCount + type4ColCount + type5ColCount))+mDivWidth*4;
		Canvas beginRecording = this.mPicContent.beginRecording(i2, (this.mYItemHeight * this.mTrendData.size()) + this.mDivHeight);
		this.mPaint.setTextSize((float) this.mCTextSize);
		this.mPaint.setStyle(Style.FILL);
		int i3 = this.type1ColCount + type2ColCount + type3ColCount + type4ColCount + type5ColCount;
		int size = this.mTrendData.size();
		//画横线
		for (i = 0; i <= size; i++) {
			int i4 = i * this.mXItemHeight;
			if (i != size) {
				this.mRect.set(0, i4, i2, this.mXItemHeight + i4);
				if (i % 2 == 0) {
					this.mPaint.setColor(-1);
				} else {
					this.mPaint.setColor(this.mCOddContent);
				}
				beginRecording.drawRect(this.mRect, this.mPaint);
				this.mPaint.setColor(this.mCDiv);
				beginRecording.drawLine(0.0f, (float) i4, (float) i2, (float) i4, this.mPaint);
			}
		}
		int step = 0;
		//画竖线
		int size2 = this.mTrendData.size() * this.mXItemWidth;
		for (i = 0; i <= i3; i++) {
			int i5 = i * this.mXItemWidth;
			if (i == this.type1ColCount) {//画五星走势图与五星组选形态中间的空隙
				step = 1;
				this.mPaint.setColor(-1);
				this.mRect.set(i5, 0, this.mDivWidth + i5, size2);
				beginRecording.drawRect(this.mRect, this.mPaint);
				this.mPaint.setColor(this.mCDiv);
				beginRecording.drawLine((float) i5, 0.0f, (float) i5, (float) size2, this.mPaint);
				beginRecording.drawLine((float) (this.mDivWidth + i5), 0.0f, (float) (this.mDivWidth + i5), (float) size2, this.mPaint);
			} else if (i == (this.type1ColCount + this.type2ColCount)) {//第二个空隙
				step = 2;
				this.mPaint.setColor(-1);
				this.mRect.set(i5 + this.mDivWidth, 0, this.mDivWidth * 2 + i5, size2);
				beginRecording.drawRect(this.mRect, this.mPaint);
				this.mPaint.setColor(this.mCDiv);
				beginRecording.drawLine((float) (i5 + this.mDivWidth), 0.0f, (float) (i5 + this.mDivWidth), (float) size2, this.mPaint);
				beginRecording.drawLine((float) (this.mDivWidth * 2 + i5), 0.0f, (float) (this.mDivWidth * 2 + i5), (float) size2, this.mPaint);
			} else if (i == (this.type1ColCount + this.type2ColCount + type3ColCount)) {//第三个空隙
				step = 3;
				this.mPaint.setColor(-1);
				this.mRect.set(i5 + this.mDivWidth * 2, 0, this.mDivWidth * 3 + i5, size2);
				beginRecording.drawRect(this.mRect, this.mPaint);
				this.mPaint.setColor(this.mCDiv);
				beginRecording.drawLine((float) (i5 + this.mDivWidth * 2), 0.0f, (float) (i5 + this.mDivWidth * 2), (float) size2, this.mPaint);
				beginRecording.drawLine((float) (this.mDivWidth * 3 + i5), 0.0f, (float) (this.mDivWidth * 3 + i5), (float) size2, this.mPaint);
			} else if (i == (this.type1ColCount + this.type2ColCount + type3ColCount + type4ColCount)) {
				step = 4;
				this.mPaint.setColor(-1);
				this.mRect.set(i5 + this.mDivWidth * 3, 0, this.mDivWidth * 4 + i5, size2);
				beginRecording.drawRect(this.mRect, this.mPaint);
				this.mPaint.setColor(this.mCDiv);
				beginRecording.drawLine((float) (i5 + this.mDivWidth * 3), 0.0f, (float) (i5 + this.mDivWidth * 3), (float) size2, this.mPaint);
				beginRecording.drawLine((float) (this.mDivWidth * 4 + i5), 0.0f, (float) (this.mDivWidth * 4 + i5), (float) size2, this.mPaint);
			} else {
				this.mPaint.setColor(this.mCDiv);
				if (step == 1) {//正在绘制第二个
					beginRecording.drawLine((float) (i5 + this.mDivWidth), 0.0f, (float) (this.mDivWidth * 2 + i5), (float) size2, this.mPaint);
				} else if (step == 2) {//正在绘制第三个
					beginRecording.drawLine((float) (i5 + this.mDivWidth * 2), 0.0f, (float) (this.mDivWidth * 3 + i5), (float) size2, this.mPaint);
				} else if (step == 3) {//正在绘制第四个
					beginRecording.drawLine((float) (i5 + this.mDivWidth * 3), 0.0f, (float) (this.mDivWidth * 4 + i5), (float) size2, this.mPaint);
				} else if (step == 4) {//正在绘制第五个
					beginRecording.drawLine((float) (i5 + this.mDivWidth * 4), 0.0f, (float) (this.mDivWidth * 5 + i5), (float) size2, this.mPaint);
				} else {//正在绘制第一个
					beginRecording.drawLine((float) (i5), 0.0f, (float) (this.mDivWidth + i5), (float) size2, this.mPaint);
				}
			}
		}

		/**
		 * 画链接线
		 */
		this.mPaint.setStyle(Style.STROKE);
		this.mPaint.setStrokeCap(Paint.Cap.ROUND);
		this.mPaint.setStrokeWidth(10);
		this.mPaint.setColor(mCBallRed);//红色
		beginRecording.drawPath(this.mPathPoint, this.mPaint);
		this.mPaint.setColor(type1_3Color);//蓝色
		beginRecording.drawPath(this.mPath2Point, this.mPaint);
		this.mPaint.setColor(type1_2Color);//绿色
		beginRecording.drawPath(this.mPath3Point, this.mPaint);
		this.mPaint.setStyle(Style.FILL);
		this.mPaint.setStrokeWidth(3);

		/**
		 * 画数据
		 */
		i = this.mTrendData.size();
		this.mPaint.setStyle(Style.FILL);
		for (int p = 0; p < mTrendData.size(); p++) {
			int height = p * mXItemHeight;
			/**
			 * 五星走势图
			 */
			for (int j = 0; j < type1ColCount; j++) {//type数组里边放的是当前数字选中个数  例如：88812   [0,1,1,0,0,0,0,0,3,0]
				this.mRect.set(this.mXItemWidth * j, height, (j + 1) * this.mXItemWidth, this.mXItemHeight + height);
				//绘制图形
				int[] type = mTrendData.get(p).getData1().getType();
				if (type[j] == 1) {
					this.mPaint.setStyle(Style.STROKE);
					this.mPaint.setColor(this.type1_4Color);//   外圆色
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					//beginRecording.drawArc(rectf, 0, 360, false, mPaint);
				} else if (type[j] == 2) {
					this.mPaint.setColor(this.type1_3Color);//蓝色
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					this.mPaint.setColor(mCDiv);
				} else if (type[j] == 3) {
					this.mPaint.setColor(this.type1_1Color);//黄色
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					this.mPaint.setColor(mCDiv);

				} else if (type[j] == 4) {
					this.mPaint.setColor(this.mCBallRed);//红色
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					this.mPaint.setColor(mCDiv);
				} else if (type[j] == 5) {
					this.mPaint.setColor(this.type1_2Color);//绿色
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					this.mPaint.setColor(mCDiv);
				} else {
//                    this.mPaint.setColor(this.mCYilou);
				}
				this.mPaint.setStyle(Style.FILL);

				//绘制文字
				int a0 = 0;
				if (j == 0) {
					a0 = mTrendData.get(p).getData1().getA0();
				}
				if (j == 1) {
					a0 = mTrendData.get(p).getData1().getA1();
				}
				if (j == 2) {
					a0 = mTrendData.get(p).getData1().getA2();
				}
				if (j == 3) {
					a0 = mTrendData.get(p).getData1().getA3();
				}
				if (j == 4) {
					a0 = mTrendData.get(p).getData1().getA4();
				}
				if (j == 5) {
					a0 = mTrendData.get(p).getData1().getA5();
				}
				if (j == 6) {
					a0 = mTrendData.get(p).getData1().getA6();
				}
				if (j == 7) {
					a0 = mTrendData.get(p).getData1().getA7();
				}
				if (j == 8) {
					a0 = mTrendData.get(p).getData1().getA8();
				}
				if (j == 9) {
					a0 = mTrendData.get(p).getData1().getA9();
				}

				if (a0 == -1) {
					this.mPaint.setColor(-1);//设置透明色
					if(type[j] == 1){//外圆字体颜色
						this.mPaint.setColor(mCYilou);
						drawText2Rect(j + "", beginRecording, this.mRect, this.mPaint);
					}else{
						drawText2Rect(j + "", beginRecording, this.mRect, this.mPaint);
					}
				} else {
					this.mPaint.setColor(mCYilou);
					drawText2Rect(a0 + "", beginRecording, this.mRect, this.mPaint);
				}
			}
			mPaint.setTextSize((float) this.mLcTextSize);

			/**
			 * 五星组选形态
			 */
			for (int h = 0; h < type2ColCount; h++) {//有6种可能  1组5 组20 组30 组60 组120 组150
				this.mRect.set(this.mXItemWidth * (h + type1ColCount) + mDivWidth, height, this.mXItemWidth * (1 + h + type1ColCount) + mDivWidth, this.mXItemHeight + height);
				this.mPaint.setColor(this.mCYilou);
				int b1 = 0;
				if (h == 0) {
					b1 = mTrendData.get(p).getData2().getB1();
				}
				if (h == 1) {
					b1 = mTrendData.get(p).getData2().getB2();
				}
				if (h == 2) {
					b1 = mTrendData.get(p).getData2().getB3();
				}
				if (h == 3) {
					b1 = mTrendData.get(p).getData2().getB4();
				}
				if (h == 4) {
					b1 = mTrendData.get(p).getData2().getB5();
				}
				if (h == 5) {
					b1 = mTrendData.get(p).getData2().getB6();
				}

				String str = "";
				if (b1 == -1) {
					this.mPaint.setColor(this.mCBallBlue);
					beginRecording.drawRect(mRect, mPaint);
					this.mPaint.setColor(-1);
					str = getData2Type(h);
				} else {
					str = "" + b1;
				}
				drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
			}
			this.mPaint.setTextSize((float) this.mCTextSize);

			/**
			 * 0路号码个数
			 */
			for (int k = 0; k < type3ColCount; k++) {//0 1 2 3 4 5   选中的话  对应的字段 Data3.getCxx()  为-1
				this.mRect.set(this.mXItemWidth * (k + type1ColCount + type2ColCount) + mDivWidth * 2, height, this.mXItemWidth * (1 + k + type2ColCount + type1ColCount) + mDivWidth * 2, this.mXItemHeight + height);
				this.mPaint.setColor(this.mCYilou);

				int c1 = 0;
				if (k == 0) {
					c1 = mTrendData.get(p).getData3().getC1();
				}
				if (k == 1) {
					c1 = mTrendData.get(p).getData3().getC2();
				}
				if (k == 2) {
					c1 = mTrendData.get(p).getData3().getC3();
				}
				if (k == 3) {
					c1 = mTrendData.get(p).getData3().getC4();
				}
				if (k == 4) {
					c1 = mTrendData.get(p).getData3().getC5();
				}
				if (k == 5) {
					c1 = mTrendData.get(p).getData3().getC6();
				}

				String str = "";
				if (c1 == -1) {//如果是当前选中  就变色
					this.mPaint.setColor(mCBallRed);
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					this.mPaint.setColor(-1);
					str = "" + k;
				} else {
					str = "" + c1;
				}
				drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
			}

			/**
			 * 1路号码个数
			 */
			for (int k = 0; k < type4ColCount; k++) {//0 1 2 3 4 5   选中的话  对应的字段 Data4.getCxx()  为-1
				this.mRect.set(this.mXItemWidth * (k + type1ColCount + type2ColCount + type3ColCount) + mDivWidth * 3, height, this.mXItemWidth * (1 + k + type1ColCount + type2ColCount + type3ColCount) + mDivWidth * 3, this.mXItemHeight + height);
				this.mPaint.setColor(this.mCYilou);
				int d1 = 0;
				if (k == 0) {
					d1 = mTrendData.get(p).getData4().getD1();
				}
				if (k == 1) {
					d1 = mTrendData.get(p).getData4().getD2();
				}
				if (k == 2) {
					d1 = mTrendData.get(p).getData4().getD3();
				}
				if (k == 3) {
					d1 = mTrendData.get(p).getData4().getD4();
				}
				if (k == 4) {
					d1 = mTrendData.get(p).getData4().getD5();
				}
				if (k == 5) {
					d1 = mTrendData.get(p).getData4().getD6();
				}
				String str = "";
				if (d1 == -1) {//如果是当前选中  就变色
					this.mPaint.setColor(this.type1_3Color);
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					this.mPaint.setColor(-1);
					str = "" + k;
				} else {
					str = "" + d1;
				}
				drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
//				if (k == 2) {
//					this.mPaint.setColor(this.type1_3Color);
//					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
//					this.mPaint.setColor(-1);
//				} else if (k == 3) {
//					this.mPaint.setColor(this.type1_3Color);
//					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
//					this.mPaint.setColor(-1);
//				}
//				drawText2Rect(k + "", beginRecording, this.mRect, this.mPaint);
			}

			/**
			 * 2路号码个数
			 */
			for (int k = 0; k < type5ColCount; k++) {
				this.mRect.set(this.mXItemWidth * (k + type1ColCount + type2ColCount + type3ColCount + type4ColCount) + mDivWidth * 4, height, this.mXItemWidth * (1 + k + type1ColCount + type2ColCount + type3ColCount + type4ColCount) + mDivWidth * 4, this.mXItemHeight + height);
				this.mPaint.setColor(this.mCYilou);


				int e1 = 0;
				if (k == 0) {
					e1 = mTrendData.get(p).getData5().getE1();
				}
				if (k == 1) {
					e1 = mTrendData.get(p).getData5().getE2();
				}
				if (k == 2) {
					e1 = mTrendData.get(p).getData5().getE3();
				}
				if (k == 3) {
					e1 = mTrendData.get(p).getData5().getE4();
				}
				if (k == 4) {
					e1 = mTrendData.get(p).getData5().getE5();
				}
				if (k == 5) {
					e1 = mTrendData.get(p).getData5().getE6();
				}
				String str = "";
				if (e1 == -1) {//如果是当前选中  就变色
					this.mPaint.setColor(this.type1_2Color);
					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
					this.mPaint.setColor(-1);
					str = "" + k;
				} else {
					str = "" + e1;
				}
				drawText2Rect(str, beginRecording, this.mRect, this.mPaint);
//				if (k == 2) {
//					this.mPaint.setColor(this.type1_2Color);
//					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
//					this.mPaint.setColor(-1);
//				} else if (k == 3) {
//					this.mPaint.setColor(this.type1_2Color);
//					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
//					this.mPaint.setColor(-1);
//				} else if (k == 4) {
//					this.mPaint.setColor(this.type1_2Color);
//					beginRecording.drawCircle(this.mRect.exactCenterX(), (float) this.mRect.centerY(), (float) this.mDefBallSize, this.mPaint);
//					this.mPaint.setColor(-1);
//				}
//				drawText2Rect(k + "", beginRecording, this.mRect, this.mPaint);
			}
		}
		this.mPicContent.endRecording();
	}

	/**
	 * 根据下标获取当前类型
	 *
	 * @param type
	 * @return
	 */
	private String getData2Type(int type) {
		String str = "";
		switch (type) {
			case 0:
				str = "组5";
				break;
			case 1:
				str = "组10";
				break;
			case 2:
				str = "组20";
				break;
			case 3:
				str = "组30";
				break;
			case 4:
				str = "组60";
				break;
			case 5:
				str = "组120";
				break;
		}
		return str;
	}

	protected CharSequence getKuaiPingLeftTime() {
		return null;
	}
}
