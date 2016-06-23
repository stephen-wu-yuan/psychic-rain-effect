package particlesystem.example.com.particlesystem.app.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import particlesystem.example.com.particlesystem.R;
import particlesystem.example.com.particlesystem.app.controller.MainActivity;

/**
 * 1.使用mScroller,控制移动的位置和效果
 * 
 * 2.初始化ImageView，作为图层的背景
 * 
 * 3.onTouchEvent处理触摸事件 在移动的时候，不断的移动View 使用scrollTo()
 * 
 * 4.使用invalidate和postInvalidate刷新界面
 * 
 * 
 */
public class PushPullDoorView extends RelativeLayout {
	private final String TAG = PushPullDoorView.class.getSimpleName();
	private Context mContext;

	private Scroller mScroller;// 平滑滚动器

	private int mScreenHeigh = 0;

	private int mLastDownY = 0;

	private int mCurryY;

	private int mDelY;

	private boolean mCloseFlag = false;// 是否隐藏View

	private ImageView mImgView;
	private Interpolator polator;// 滚动效果

	private MainActivity mainActivity;

	private boolean isStateBottom = false;

	private static final int PRESSED_DOWN = 0;
	private static final int PRESSED_DONE = 1;
	private static final int NO_NEED_MOVE = 2;
	private static final int NEED_MOVE = 3;

	private int scrollStatus = PRESSED_DOWN;

	private static final int DRAG_AREA = 180;

	public PushPullDoorView(Context context) {
		super(context);
		mContext = context;
		setupView();
	}

	public PushPullDoorView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setupView();
	}

	@SuppressLint("NewApi")
	private void setupView() {
		initScroller();
		getWindowInfo();
		initImageView();
		scrollTo(0, mScreenHeigh);
	}

	public void initScroller() {
		polator = new BounceInterpolator();// bounce effect
		mScroller = new Scroller(mContext,polator);
	}

	public void getWindowInfo() {

		WindowManager wm = (WindowManager) (mContext
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		mScreenHeigh = dm.heightPixels - DRAG_AREA;
	}

	public void initImageView() {
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		mImgView = new ImageView(mContext);
		mImgView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		mImgView.setScaleType(ImageView.ScaleType.FIT_XY);// fill the whole screen
		mImgView.setImageResource(R.drawable.glass); // set the default background
		addView(mImgView);
	}

	/**
	 * 设置推动门背景
	 * 
	 * @param id
	 */
	public void setBgImage(int id) {
		mImgView.setImageResource(id);
	}

	/**
	 * 设置推动门背景
	 * 
	 * @param drawable
	 */
	public void setBgImage(Drawable drawable) {
		mImgView.setImageDrawable(drawable);
	}

	/**
	 * 推动门的动画
	 * 
	 * @param startY
	 *            Y方向开始的位置
	 * @param dy
	 *            Y方向移动的距离
	 * @param duration
	 *            时间
	 */
	public void startBounceAnim(int startY, int dy, int duration) {
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastDownY = (int) event.getY();
			scrollStatus = PRESSED_DOWN;
			if (isStateBottom && (mLastDownY > mScreenHeigh) || (!isStateBottom && mLastDownY < DRAG_AREA)){
				return true;
			}
		case MotionEvent.ACTION_MOVE:

			//避免下滑到底部还可以滑动，同时也需要避免往上滑能一直滑
			if ((isStateBottom && (mLastDownY <= mScreenHeigh)) || (!isStateBottom && mLastDownY > DRAG_AREA)){
				scrollStatus = NO_NEED_MOVE;
				break;
			}
			mCurryY = (int) event.getY();
			mDelY = mCurryY - mLastDownY;

			if(mDelY != 0){
				scrollStatus = NEED_MOVE;

 			    // push up
				if (mDelY < 0 && Math.abs(mDelY) > mScreenHeigh / 20 && Math.abs(mDelY) <= mScreenHeigh) {
					mainActivity.stopRainDrop();
					scrollTo(0, -mDelY);// scroll with the finger
				}
				else if (mDelY > 0 && Math.abs(mDelY) > mScreenHeigh / 20 && mDelY <= mScreenHeigh ) {
					scrollTo(0, mScreenHeigh - mDelY);
				}
			}

			break;
		case MotionEvent.ACTION_UP:

			//如果手指没有滑动或是不需要滑动的情况
			if (scrollStatus == PRESSED_DOWN || scrollStatus == NO_NEED_MOVE)
				break;
			scrollStatus = PRESSED_DONE;
			mCurryY = (int) event.getY();
			mDelY = mCurryY - mLastDownY;

			if (mDelY < 0) {
				if (Math.abs(mDelY) > mScreenHeigh / 10 && Math.abs(mDelY) > mScreenHeigh / 20) {
					// if scroll up path distance is longer than the half screen height,
					//set the bounce animation to the top
					startBounceAnim(this.getScrollY(), mScreenHeigh, 450);
					isStateBottom = false;
				} else {
					// if scroll up path distance is less than the half screen height,
					// set the bounce animation to the bottom
					startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);
					isStateBottom = true;
					mainActivity.resumeParticle2();
				}
			}
			else if(mDelY > 0) {
				if ( mDelY  < mScreenHeigh / 10 && mDelY > mScreenHeigh/20) {
					startBounceAnim(this.getScrollY(), mScreenHeigh, 450);
					isStateBottom = false;
				}
				else {
					startBounceAnim(this.getScrollY(), -this.getScrollY(), 1000);
					isStateBottom = true;
					mainActivity.resumeParticle2();
				}
			}

			break;
		}
		return super.onTouchEvent(event);
	}


	@Override
	public void computeScroll() {

		if (mScroller.computeScrollOffset()) {

			int scrollY = mScroller.getCurrY() > mScreenHeigh ? mScreenHeigh : mScroller.getCurrY();

			scrollTo(mScroller.getCurrX(), scrollY);
			// refresh ui
			postInvalidate();

		}

	}


	public void setMainActivity(MainActivity thisMainActivity){
		mainActivity = thisMainActivity;
	}

	public void dragDownViewAuto(){
		startBounceAnim(this.getScrollY(),-this.getScrollY(), 1000);
		isStateBottom = true;
	}

	public boolean isStateBottom()
	{
		return isStateBottom;
	}
}
