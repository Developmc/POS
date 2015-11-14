package com.example.multiable.pos.CustomView;

/**从左侧拉出菜单
 * Created by macremote on 2015/11/6.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.example.multiable.pos.R;

public class LeftMenu extends RelativeLayout {
    //记录侧边栏的打开状态（true为打开）
    private boolean status ;
    private Context context ;
    private FrameLayout leftMenu ;
    private FrameLayout middleMenu ;
    //添加蒙版
    private FrameLayout middleMask ;
    //声明ID  (注意fragment凭借ID找到对应视图，请勿重复，也不要与其他自定义的视图的id重复)
    public static final int LEFT_MENU_ID=0xababab;
    public static final int MIDDLE_MENU_ID=0xbababa;
    private int leftMenuColor;
    private int middleMenuColor;

    //从左边边缘滑出的误差距离
    private int left_margin_distance ;
    //侧边框与内容框宽度的比例：0.1-1.0
    private float left_scale ;

    //滑动动画
    private Scroller mScroller ;
    public LeftMenu(Context context) {
        super(context);
        initView(context);
    }

    public LeftMenu(Context context, AttributeSet attrs) {
        super(context, attrs);

        //初始化自定义的属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LeftMenu) ;
        int n = a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = a.getIndex(i);
            switch (attr){
                case R.styleable.LeftMenu_content_color:
                    //白色是默认值
                    middleMenuColor = a.getColor(attr,context.getResources().getColor(R.color.white)) ;
                case R.styleable.LeftMenu_menu_color:
                    //默认值是白色
                    leftMenuColor = a.getColor(attr,context.getResources().getColor(R.color.white)) ;
                case R.styleable.LeftMenu_left_margin_distance:
                    //默认值是100
                    left_margin_distance = a.getInteger(attr,100) ;
                case R.styleable.LeftMenu_left_scale:
                    //默认值是0.8
                    left_scale = a.getFloat(attr,0.8F) ;
            }
        }

        initView(context);
    }
    private void initView(Context context){
        this.context = context ;
        status = false ;
        //第二个参数是动画渲染器
        mScroller = new Scroller(context,new DecelerateInterpolator()) ;
        leftMenu = new FrameLayout(context) ;
        middleMenu=new FrameLayout(context) ;
        middleMask=new FrameLayout(context) ;
        //设置蒙版为灰色透明
        middleMask.setBackgroundColor(context.getResources().getColor(R.color.translucence));
        leftMenu.setBackgroundColor(leftMenuColor);
        middleMenu.setBackgroundColor(middleMenuColor);
        leftMenu.setId(LEFT_MENU_ID);
        middleMenu.setId(MIDDLE_MENU_ID);
        //将view 添加进来
        addView(leftMenu);
        addView(middleMenu);
        addView(middleMask);
        //初始化蒙版的透明度
        middleMask.setAlpha(0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //先测量中间view的高度和宽度
        middleMenu.measure(widthMeasureSpec, heightMeasureSpec);
        middleMask.measure(widthMeasureSpec,heightMeasureSpec);
        //获取屏幕整体的宽度
        int realWidth = MeasureSpec.getSize(widthMeasureSpec);

        //设置左部布局的宽高
        int tempWidthMeasure = MeasureSpec.makeMeasureSpec(
                (int)(realWidth*left_scale),MeasureSpec.EXACTLY
        );
        leftMenu.measure(tempWidthMeasure, heightMeasureSpec);
    }

    //放置布局
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        //把view填充进去
        middleMenu.layout(l,t,r,b);
        middleMask.layout(l,t,r,b);
        leftMenu.layout(l-leftMenu.getMeasuredWidth(),t,r,b);
    }

    //判断是怎样的滑动手势
    private boolean isTestCompete ;
    private boolean isLeftRightEvent ;
    private boolean isUpBottomEvent ;
    //触摸事件分发
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!isTestCompete){
            //判断是何种手势
            getEventType(ev) ;
            return true ;
        }
        //如果是左右滑动
        if(isLeftRightEvent){
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_MOVE:
                    //滚动的距离
                    int curScrollX = getScrollX();
                    //滑动的距离
                    int dis_x = (int)(ev.getX()-point.x);
                    //判断是向左还是向右
                    int expectX = -dis_x + curScrollX ;
                    //记录最终的距离
                    int finalX = 0;
                    //如果是向左
                    if(expectX<0){
                        finalX = Math.max(expectX,-leftMenu.getMeasuredWidth());
                    }
                    //向右滑动
                    else{
//                        finalX = Math.min(expectX,leftMenu.getMeasuredWidth()) ;
                    }
                    //移动到当前的位置,上下没有滑动，传0
                    scrollTo(finalX,0);
                    //保证每次滑动都是正常的
                    point.x = (int)ev.getX();
                    break ;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:

                    /*****************************************************/
                    //添加动画（滑动不够一半时缩回去，反之全弹出）
                    //获得滚动的距离(向左滑动getScrollX是正数)
                    curScrollX = getScrollX() ;
                    //有可能为负数（滑动的方向导致的）,当超过一半宽度时
                    if(Math.abs(curScrollX)>leftMenu.getMeasuredWidth()>>1){
                        //判断是向左还是向右
                        if(curScrollX<0){
                            //手指向右滑动，出现左菜单
                            //让view可以滑动
                            mScroller.startScroll(curScrollX,0,
                                    -leftMenu.getMeasuredWidth()-curScrollX,0,200);
                        }
                        else{
                            //向左滑动 ,200是动画执行的时间
                            mScroller.startScroll(curScrollX,0,
                                    leftMenu.getMeasuredWidth()-curScrollX,0,200);
                        }
                        //侧边栏为打开状态
                        status = true ;
                    }
                    //当长度不足一半时
                    else{
                        mScroller.startScroll(curScrollX,0,-curScrollX,0,200);
                        status = false ;
                    }
                    //刷新视图
                    postInvalidate() ;
//                    invalidate();
                    /*****************************************************/

                    //手指抬起后，初始化数据
                    isLeftRightEvent = false ;
                    isTestCompete = false ;

                    isUpBottomEvent = false ;
                    break ;
            }
        }
        //上下滑动
        else if(isUpBottomEvent){
            switch (ev.getActionMasked()){
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    isUpBottomEvent = false ;
                    isLeftRightEvent = false ;
                    isTestCompete = false ;
                    break ;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    //滑动的回调方法
    @Override
    public void computeScroll() {
        super.computeScroll();
        //动画结束后返回true
        if(!mScroller.computeScrollOffset()){
            return ;
        }
        //判断滑动手势（水平还是垂直）
        int tempX = mScroller.getCurrX();
        scrollTo(tempX,0);
        postInvalidate() ;
    }
    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
        //根据滑动距离计算蒙版的透明度
        int curX = Math.abs(getScrollX());
        float scale = curX/(float)leftMenu.getMeasuredWidth();
        middleMask.setAlpha(scale);

    }

    //屏幕的点
    private Point point = new Point() ;
    //定义一个值，判定是否发生了滑动
    private static final int TEST_DIS=10;
    //当下拉栏处于打开状态时，判断点击的位置是否在middleMenu
    private boolean isTouchMiddle;

    private void getEventType(MotionEvent ev) {
        switch (ev.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                //手指按下
                //获取按下这个点的坐标
                point.x = (int)ev.getX();
                point.y = (int)ev.getY();

                //侧边栏为打开状态时
                if(status){
                    //TODO 完全覆盖的情况（暂不考虑），左menu为整个布局的0.8
                    //判断点击位置所在的区域
                    if(point.x>leftMenu.getMeasuredWidth()){
                        //落在middleMenu内
                        isTouchMiddle = true ;
                    }
                    else{
                        //落在leftMenu中
                        isTouchMiddle = false ;
                        //将事件处理交给leftMenu（响应leftmenu中的点击滑动等事件）
                        super.dispatchTouchEvent(ev) ;
                    }
                }
                //侧边栏为关闭状态时
                else{
                    //把事件处理交还系统
                    super.dispatchTouchEvent(ev);
                }

                break ;
            case MotionEvent.ACTION_MOVE:
                //手指移动
                //侧边栏为打开状态时
                if(status){
                    //当在middleMenu中滑动时
                    if(point.x>leftMenu.getMeasuredWidth()){
                        //记录在x,y上活动的距离
                        int dX = Math.abs((int)ev.getX()-point.x) ;
                        int dY = Math.abs((int)ev.getY()-point.y) ;
                        if(dX>=TEST_DIS&& dX>dY){
                            //左右滑动
                            isLeftRightEvent = true ;
                            isUpBottomEvent = false ;
                            isTestCompete = true ;
                        }
                        else if(dY>=TEST_DIS && dY>dX){
                            //上下滑动
                            isUpBottomEvent = true ;
                            isLeftRightEvent = false ;
                            isTestCompete = true ;
                        }
                    }
                    //在leftMenu中滑动时
                    else{
                        //do nothing
                        //当left_scale>=1.0,middleMenu完全被覆盖时，滑动leftMenu关闭leftMenu
                        if(left_scale>=1.0){
                            //记录在x,y上活动的距离
                            int dX = Math.abs((int)ev.getX()-point.x) ;
                            int dY = Math.abs((int)ev.getY()-point.y) ;
                            if(dX>=TEST_DIS&& dX>dY){
                                //左右滑动
                                isLeftRightEvent = true ;
                                isUpBottomEvent = false ;
                                isTestCompete = true ;
                            }
                            else if(dY>=TEST_DIS && dY>dX){
                                //上下滑动
                                isUpBottomEvent = true ;
                                isLeftRightEvent = false ;
                                isTestCompete = true ;
                            }
                        }
                    }
                }
                //侧边栏为关闭状态时
                else{
                    //判断是否是在左边缘的滑动事件
                    if(point.x<left_margin_distance){
                        //记录在x,y上活动的距离
                        int dX = Math.abs((int) ev.getX() - point.x);
                        int dY = Math.abs((int) ev.getY() - point.y);
                        if (dX >= TEST_DIS && dX > dY) {
                            //左右滑动
                            isLeftRightEvent = true ;
                            isUpBottomEvent = false;
                            isTestCompete = true;
                        }
                        else if (dY >= TEST_DIS && dY > dX) {
                            //上下滑动
                            isUpBottomEvent = true;
                            isLeftRightEvent = false ;
                            isTestCompete = true;
                        }
                    }
                    else{
                        //do nothing
                    }
                }

                //把事件处理交还系统
                super.dispatchTouchEvent(ev);
                //为了滑动之后还能继续滑动
                point.x = (int)ev.getX();
                point.y = (int)ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                //抬起手指
            case MotionEvent.ACTION_CANCEL:
                if(status&&isTouchMiddle){
                    //点击阴影部分，关闭侧边栏
                    closeMenu();
                }

                //将点击事件返回给系统处理
                super.dispatchTouchEvent(ev);
                //初始化操作
                isLeftRightEvent = false ;
                isUpBottomEvent = false ;
                isTestCompete = false ;
                break;
        }
    }
    public void openMenu(){
        if(status){
            //do nothing
        }
        else{
            mScroller.startScroll(0,0,
                    -leftMenu.getMeasuredWidth(),0,200);
            status = true ;
            //刷新视图(这样才会调用computeScroll)
            postInvalidate() ;
        }
        Log.d("status",status+"") ;
    }
    public void closeMenu(){
        if(status){
            mScroller.startScroll(-leftMenu.getMeasuredWidth(),0,
                    leftMenu.getMeasuredWidth(),0,200);
            //刷新视图(这样才会调用computeScroll)
            postInvalidate() ;
            status = false ;
        }
        else{
            //do nothing
        }
        Log.d("status", status + "") ;
    }
}
