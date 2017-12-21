package nongbang.hg.nongbang.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;

/**
 * Created by Administrator on 2017-07-21.
 */

public class ZoomImageView extends android.support.v7.widget.AppCompatImageView implements ViewTreeObserver.OnGlobalLayoutListener,ScaleGestureDetector.OnScaleGestureListener,
        View.OnTouchListener{


    private boolean mOnce=false;
    private float mInitScale;//初始缩放
    private float mMidScale;//双击缩放大小
    private float mMaxSclae;//放大最大值



    private Matrix mScaleMatrix;

    private ScaleGestureDetector mScaleGestureDetector;//多点触控比例


    //自由移动
    private int mLastPointerCount=0;
    private float mLastX,mLastY;
    private int mTouchSlop;
    private boolean isCanDrag;

    private boolean isCheckLeftAndRight;
    private boolean isCheckTopAndBottom;

    //双击放大缩小
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;


    public ZoomImageView(Context context) {
        this(context,null);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ZoomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleMatrix=new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector=new ScaleGestureDetector(context,this);
        setOnTouchListener(this);
        mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();
        mGestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                if (isAutoScale)
                    return true;
                float x=e.getX();
                float y=e.getY();

                if (getScale()<mMidScale){
                    postDelayed(new AutoScaleRunnable(mMidScale,x,y),16);
                    isAutoScale=true;
                }else {
                    postDelayed(new AutoScaleRunnable(mInitScale,x,y),16);
                    isAutoScale=true;
                }

                return true;
            }
        });
    }

    private class AutoScaleRunnable implements Runnable{
        private float mTargetScale;
        private float x;
        private float y;
        private final float BIGGER=1.07f;
        private final float SMALL=0.93f;

        private float tempScale;
        public AutoScaleRunnable(float mt,float x,float y){
            mTargetScale=mt;
            this.x=x;
            this.y=y;
            if (getScale()<mTargetScale){
                tempScale=BIGGER;
            }
            if (getScale()>mTargetScale){
                tempScale=SMALL;
            }
        }

        @Override
        public void run() {
            mScaleMatrix.postScale(tempScale,tempScale,x,y);
            check();
            setImageMatrix(mScaleMatrix);
            float currentScale=getScale();
            if ((tempScale>1.0f && currentScale<mTargetScale) ||
                    (tempScale<1.0f && currentScale>mTargetScale) ){
                postDelayed(this,16);
            }else {
                float scale=mTargetScale/currentScale;
                mScaleMatrix.postScale(scale,scale,x,y);
                check();
                setImageMatrix(mScaleMatrix);
                isAutoScale=false;
            }
        }
    }

    /*
    获取imageview图片
     */

    @Override
    public void onGlobalLayout() {
        if (!mOnce){
            int width=getWidth();
            int height=getHeight();
            //得到图片和宽高
            Drawable drawable=getDrawable();
            if (drawable==null)
                return;
            int dw=drawable.getIntrinsicWidth();
            int dh=drawable.getIntrinsicHeight();
            float scale=1.0f;
            if (dw>width && dh<height){
                scale=width*1.0f/dw;
            }
            if (dh>height && dw<width)
            {
                scale=height*1.0f/dh;
            }
            if ((dw>width && dh>height)|| (dw<width && dh<height))
            {
                scale=Math.min(width*1.0f/dw,height*1.0f/dh);
            }

            mInitScale=scale;
            mMaxSclae=scale*4;
            mMidScale=scale*2;

            //图片居中
            int dx=getWidth()/2-dw/2;
            int dy=getHeight()/2-dh/2;

            mScaleMatrix.postTranslate(dx,dy);
            mScaleMatrix.postScale(mInitScale,mInitScale,width/2,height/2);
            setImageMatrix(mScaleMatrix);

            mOnce=true;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    public float getScale(){
        float[] values=new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float scale=getScale();
        float scaleFactor=detector.getScaleFactor();
        if (getDrawable()==null)
            return true;
        if ((scale<mMaxSclae && scaleFactor>1.0f) || (scale>mInitScale && scaleFactor<1.0f)){
            if (scale*scaleFactor<mInitScale)
            {
                scaleFactor=mInitScale/scale;
            }
            if (scale*scaleFactor>mMaxSclae)
            {
                scaleFactor=mMaxSclae/scale;
            }


            mScaleMatrix.postScale(scaleFactor,scaleFactor,detector.getFocusX(),detector.getFocusY());
            check();

            setImageMatrix(mScaleMatrix);
        }
        return false;
    }

    //获得图片放大高宽矩阵信息
    private RectF getMatrixRectF(){
        Matrix matrix=mScaleMatrix;
        RectF rectF=new RectF();
        Drawable d=getDrawable();
        if (d!=null){
            rectF.set(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
            matrix.mapRect(rectF);
        }
        return rectF;
    }

    //检测缩放边界
    public void check(){
        RectF rectF= getMatrixRectF();
        float deltaX=0;
        float deltaY=0;
        int width=getWidth();
        int height=getHeight();

        if (rectF.width()>=width){
            if (rectF.left>0){
                deltaX=-rectF.left;
            }
            if (rectF.right<width){
                deltaX=width-rectF.right;
            }
        }
        if (rectF.height()>=height){
            if (rectF.top>0){
                deltaY=-rectF.top;
            }
            if (rectF.bottom<height){
                deltaY=height-rectF.bottom;
            }
        }

        if (rectF.width()<width){
            deltaX=width/2-rectF.right+rectF.width()/2;
        }
        if (rectF.height()<height){
            deltaY=height/2-rectF.bottom+rectF.height()/2;
        }
        mScaleMatrix.postTranslate(deltaX,deltaY);
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)){
            return true;
        }
        mScaleGestureDetector.onTouchEvent(event);

        float x=0;
        float y=0;
        int pointerCount=event.getPointerCount();
        for (int i=0;i<pointerCount;i++){
            x+=event.getX(i);
            y+=event.getY(i);
        }
        x/=pointerCount;
        y/=pointerCount;

        if (mLastPointerCount!=pointerCount){
            isCanDrag=false;
            mLastX=x;
            mLastY=y;
        }
        mLastPointerCount=pointerCount;
        RectF rectf=getMatrixRectF();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
               if (rectf.width()>getWidth() || rectf.height()>getHeight()){
                   getParent().requestDisallowInterceptTouchEvent(true);
               }
                break;
            case MotionEvent.ACTION_MOVE:
                if (rectf.width()>getWidth()+0.01 || rectf.height()>getHeight()+0.01){
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                float dx=x-mLastX;
                float dy=y-mLastY;
                if (!isCanDrag){
                    isCanDrag=isMoveAction(dx,dy);
                }
                if (isCanDrag){
                    if (getDrawable()!=null){
                        isCheckLeftAndRight=isCheckTopAndBottom=true;
                        if (rectf.width()<getWidth()){
                            isCheckLeftAndRight=false;
                            dx=0;
                        }
                        if (rectf.height()<getHeight()){
                            isCheckTopAndBottom=false;
                            dy=0;
                        }
                        mScaleMatrix.postTranslate(dx,dy);
                        checkBordWhenMove();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX=x;
                mLastY=y;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mLastPointerCount=0;

                break;

//            case MotionEvent.ACTION_DOWN:
//                mLastX=event.getX();
//                mLastY=event.getY();
//                break;
        }


        return true;
    }

    private void checkBordWhenMove() {
        RectF rectf=getMatrixRectF();
        float deltaX=0;
        float deltaY=0;
        int width=getWidth();
        int height=getHeight();
        if (rectf.top>0 && isCheckTopAndBottom){
            deltaY=-rectf.top;
        }
        if (rectf.bottom<height && isCheckTopAndBottom){
            deltaY=height-rectf.bottom;
        }
        if (rectf.left>0 && isCheckLeftAndRight){
            deltaX=-rectf.left;
        }
        if (rectf.right<width && isCheckLeftAndRight){
            deltaX=width-rectf.right;
        }
        mScaleMatrix.postTranslate(deltaX,deltaY);

    }

    private boolean isMoveAction(float dx, float dy) {
        return Math.sqrt(dx*dx+dy*dy)>mTouchSlop;
    }
}
