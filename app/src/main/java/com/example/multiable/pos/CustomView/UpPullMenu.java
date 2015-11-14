package com.example.multiable.pos.CustomView;

/**
 * Created by macremote on 2015/10/27.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Scroller;

import com.example.multiable.pos.R;

public class UpPullMenu extends FrameLayout {
    private Context context;
    private final int ANIMATION_TIME = 100;   //下拉栏弹出收回的动画时间：100ms
    private final int THREAD_SLEEP_TIME = 1;
    //从底部边缘滑出的误差距离
    private final int STRIKE_DISTANCE=100;

    private FrameLayout bottomMenu;
    private FrameLayout middleMenu;
    //添加蒙版
    private FrameLayout middleMask;
    //记录当前下拉框的状态
    private boolean status;

    public static final int MIDDLE_ID = 0xaaccbb;
    public static final int BOTTOM_ID = 0xccbbaa;
    private int bottomColor;
    private int middleColor;
    private static final int ANIMATION_MESSAGE = 0;
    //滑动动画
    private Scroller mScroller;

    public UpPullMenu(Context context) {
        super(context);
        this.context = context;
        initView(context);
    }

    public UpPullMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //初始化自定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UpPullMenu);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.UpPullMenu_front_color:
                    middleColor = a.getColor(attr, context.getResources().getColor(R.color.white));
                    break;
                case R.styleable.UpPullMenu_back_color:
                    bottomColor = a.getColor(attr, context.getResources().getColor(R.color.translucence));
                    break;
            }
        }

        //Give back a previously retrieved array, for later re-use.
        a.recycle();
        initView(context);
    }

    public UpPullMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ANIMATION_MESSAGE:
                    //根据滑动的距离计算蒙版的透明度
                    changeMiddleAlpha(distance);

                    bottomMenu.layout(0, middleMenu.getMeasuredHeight() + distance, middleMenu.getMeasuredWidth(),
                            middleMenu.getMeasuredHeight() + bottomMenu.getMeasuredHeight() + distance);
                    //蒙版的位置也要随之移动（像是推上去的感觉）
                    middleMask.layout(0,distance,middleMask.getMeasuredWidth(),
                            middleMask.getMeasuredHeight()+distance);
                    break;
            }
        }
    };

    private void initView(Context context) {
        this.context = context;
        status = false;
        //第二个参数是动画渲染器
        mScroller = new Scroller(context, new DecelerateInterpolator());
        middleMenu = new FrameLayout(context);
        bottomMenu = new FrameLayout(context);
        middleMask = new FrameLayout(context);
        //设置蒙版为灰色透明
        middleMask.setBackgroundColor(context.getResources().getColor(R.color.translucence));
        middleMenu.setBackgroundColor(middleColor);
        bottomMenu.setBackgroundColor(bottomColor);
        middleMenu.setId(MIDDLE_ID);
        bottomMenu.setId(BOTTOM_ID);
        //添加view
        addView(middleMenu);
        addView(bottomMenu);
        addView(middleMask);
        //设置初始透明度，完全透明,0-1
        middleMask.setAlpha(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        middleMenu.measure(widthMeasureSpec, heightMeasureSpec);
        //测量蒙版的宽高
        middleMask.measure(widthMeasureSpec, heightMeasureSpec);
        int realHeight = MeasureSpec.getSize(heightMeasureSpec);
        //设置底部布局的宽高
        int tempHeightMeasure = MeasureSpec.makeMeasureSpec(
                (int) (realHeight * 0.5f), MeasureSpec.EXACTLY
        );
        bottomMenu.measure(widthMeasureSpec, tempHeightMeasure);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //如果当前状态为打开，则直接显示为打开的状态（在适配器notifyDataSetChanged刷新视图，拉出menu后退出程序再返回程序时刷新）
        if(status){
            bottomMenu.layout(0, middleMenu.getMeasuredHeight()-bottomMenu.getMeasuredHeight(),
                    middleMenu.getMeasuredWidth(),
                    middleMenu.getMeasuredHeight());
            //蒙版的位置也要随之移动（像是推上去的感觉）
            middleMask.layout(0,-bottomMenu.getMeasuredHeight(),middleMask.getMeasuredWidth(),
                    middleMask.getMeasuredHeight()-bottomMenu.getMeasuredHeight());
        }
        else{
            //把view填充进去
            middleMenu.layout(l, t, r, b);
            //填充蒙版
            middleMask.layout(l, t, r, b);
            bottomMenu.layout(l, t + middleMenu.getMeasuredHeight(), r, t + middleMenu.getMeasuredHeight()
                    + bottomMenu.getMeasuredHeight());
        }

    }

    //判断是怎样的滑动手势
    private boolean isTestCompete;
    private boolean isUpBottomEvent;
    //记录距离
    private int distance = 0;

    //触摸事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isTestCompete) {
            //判断是何种手势
            getEventType(ev);
            return true;
        }
        //上下滑动
        if (isUpBottomEvent) {
            switch (ev.getActionMasked()) {
                case MotionEvent.ACTION_MOVE:
                    //滑动的距离
                    int dis_y = (int) (ev.getY() - point.y);
                    distance += dis_y;
                    //手指向上滑动
                    if (dis_y < 0) {
                        distance = Math.max(distance, -bottomMenu.getMeasuredHeight());
                    }
                    //手指向下滑动
                    else  {
                        //当超出后，为0，即刚好隐藏bottomMenu的距离
                        if(distance>0){
                            distance = 0;
                        }
                    }
                    //移动到当前的位置
//                    scrollTo(0,finalY);
                    changeMiddleAlpha(distance);
                    bottomMenu.layout(0, middleMenu.getMeasuredHeight() + distance, middleMenu.getMeasuredWidth(),
                            middleMenu.getMeasuredHeight() + bottomMenu.getMeasuredHeight() + distance);
                    //蒙版的位置也要随之移动（像是推上去的感觉）
                    middleMask.layout(0,distance,middleMask.getMeasuredWidth(),
                            middleMask.getMeasuredHeight()+distance);

                    //保证每次滑动都是正常的
                    point.y = (int) ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    /*****************************************************/
                    //添加上下滑动的动画
                    //当滑动的距离超过底部视图的一半时
                    if (Math.abs(distance) > bottomMenu.getMeasuredHeight() >> 1) {
                        status = true;
                        //线程完成滑动动画
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int tempDis = bottomMenu.getMeasuredHeight() + distance;
                                //动画时间设为200，线程睡眠时间是10ms
                                while (distance > -bottomMenu.getMeasuredHeight()) {
                                    try {
                                        Thread.sleep(10);
                                        if (tempDis > 20) {
                                            distance -= tempDis / 20;
                                        } else {
                                            distance = -bottomMenu.getMeasuredHeight();
                                        }
                                        mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                distance = -bottomMenu.getMeasuredHeight();
                                mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                            }
                        }).start();

                    }
                    //滑动距离不足一半时
                    else {
                        status = false;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                int tempDis = 0 - distance;
                                //动画时间设为200，线程睡眠时间是10ms
                                while (distance < 0) {
                                    try {
                                        Thread.sleep(10);
                                        if (tempDis > 20) {
                                            distance += tempDis / 20;
                                        } else {
                                            distance = 0;
                                        }
                                        mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                distance = 0;
                                mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                            }
                        }).start();
                    }

                    //刷新视图（重写computeScroll）
                    invalidate();

                    /*****************************************************/
                    isUpBottomEvent = false;
                    isTestCompete = false;
                    break;
            }
        }
        //左右滑动
        else {
            //手指抬起后，初始化数据
            isTestCompete = false;
            isUpBottomEvent = false;
        }

        return super.dispatchTouchEvent(ev);
    }

    //滑动的回调方法
    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!mScroller.computeScrollOffset()) {
            return;
        }
//        //判断滑动手势（水平还是垂直）
//        int tempX = mScroller.getCurrX();
//        int tempY = mScroller.getCurrY();
//        scrollTo(tempX,tempY);
    }

    //屏幕的点
    private Point point = new Point();
    //定义一个值，判定是否发生了滑动
    private static final int TEST_DIS = 20;
    //当下拉栏处于打开状态时，判断点击的位置是否在middleMenu
    private boolean isTouchMiddle;

    private void getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:

                //手指按下
                //获取按下这个点的坐标
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();

                //当侧边栏处于打开状态时
                if (status) {
                    //获取点击的位置所在的区域（在middleMenu视图中还是在bottomMenu视图中）
                    int tempHeight = middleMenu.getMeasuredHeight() - bottomMenu.getMeasuredHeight();
                    if (point.y > tempHeight) {
                        //触碰点在bottom视图中
                        isTouchMiddle = false;
                        //把事件处理交还系统
                        super.dispatchTouchEvent(ev);
                    } else {
                        //触碰点在middleMenu视图中
                        isTouchMiddle = true;
                    }
                }
                //当下拉栏处于关闭时候
                else {
                    //把事件处理交还系统
                    super.dispatchTouchEvent(ev);
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (status) {
                    //当在middleMenu中滑动时
                    if(point.y<(middleMenu.getMeasuredHeight() - bottomMenu.getMeasuredHeight())){
                        //记录在x,y上活动的距离
                        int dX = Math.abs((int) ev.getX() - point.x);
                        int dY = Math.abs((int) ev.getY() - point.y);
                        if (dX >= TEST_DIS && dX > dY) {
                            //左右滑动
                            isUpBottomEvent = false;
                            isTestCompete = true;
                        }
                        else if (dY >= TEST_DIS && dY > dX) {
                            //上下滑动
                            isUpBottomEvent = true;
                            isTestCompete = true;
                        }
                    }
                    //在bottomMenu中滑动时
                    else{
                        //do nothing
                    }
                }
                //侧边栏处于关闭的状态
                else {
                //手指移动
                //判断是否是在下边缘发生的滑动事件
                int tempErrorDistance = Math.abs(middleMenu.getMeasuredHeight()-point.y) ;
                if(tempErrorDistance<STRIKE_DISTANCE){
                    //记录在x,y上活动的距离
                    int dX = Math.abs((int) ev.getX() - point.x);
                    int dY = Math.abs((int) ev.getY() - point.y);
                    if (dX >= TEST_DIS && dX > dY) {
                        //左右滑动
                        isUpBottomEvent = false;
                        isTestCompete = true;
                    }
                    else if (dY >= TEST_DIS && dY > dX) {
                        //上下滑动
                        isUpBottomEvent = true;
                        isTestCompete = true;
                    }
                }
            }
                //把事件处理交还系统
                super.dispatchTouchEvent(ev);

                //为了滑动之后还能继续滑动
                point.x = (int) ev.getX();
                point.y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                //抬起手指
            case MotionEvent.ACTION_CANCEL:
                //屏幕边缘

                if (status && isTouchMiddle) {
                    //点击middle区域时，关闭下拉栏
                    closeBar();
                }

                //将点击事件返回给系统处理
                super.dispatchTouchEvent(ev);
                //初始化操作
                isUpBottomEvent = false;
                isTestCompete = false;

                break;
        }
    }

    /**根据滑动的距离计算蒙版的透明度
     * @param distance
     */
    public void changeMiddleAlpha(int distance){
        //（distance为0时bottomMenu隐藏，为-bottomMenu.getMeasuredHeight()完全显示）
        int curY = Math.abs(distance);
        float scale = curY / (float) bottomMenu.getMeasuredHeight();
        middleMask.setAlpha(scale);
    }

    /**
     * 打开下拉框
     */
    public void openBar() {
        if (status) {
            //do nothing
        } else {
            status = true ;
            //线程控制下拉栏开启的动画
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int tempDis = bottomMenu.getMeasuredHeight();
                    //动画时间设为100，线程睡眠时间是1ms
                    while (distance > -bottomMenu.getMeasuredHeight()) {
                        try {
                            Thread.sleep(THREAD_SLEEP_TIME);
                            distance -= tempDis / (ANIMATION_TIME/THREAD_SLEEP_TIME);
                            mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    distance = -bottomMenu.getMeasuredHeight();
                    mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                }
            }).start();
        }
    }

    /**
     * 关闭下拉框
     */
    public void closeBar() {
        if (status) {
            status = false ;
            //线程控制关闭的动画
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int tempDis = bottomMenu.getMeasuredHeight();
                    distance = -bottomMenu.getMeasuredHeight();
                    //动画时间设为100，线程睡眠时间是1ms
                    while (distance < 0) {
                        try {
                            Thread.sleep(THREAD_SLEEP_TIME);
                            distance += tempDis/(ANIMATION_TIME/THREAD_SLEEP_TIME);
                            mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //确保刚好收回
                    distance = 0;
                    mHandler.sendEmptyMessage(ANIMATION_MESSAGE);
                }
            }).start();
        } else {
            // do nothing
        }
    }

    /**
     * 记录下拉框的状态，ture为打开，false为关闭
     *
     * @return
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * 切换当前侧边栏的状态
     */
    public void toggle() {
        if (status) {
            closeBar();
        } else {
            openBar();
        }

    }

}