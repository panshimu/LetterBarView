package com.miaozij.letterbarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


public class LetterBarView extends View {
    private Paint mSelectedPaint;
    private Paint mUnselectedPaint;
    private int mTextSize;
    private int mUnselectedColor;
    private int mSelectedColor;
    private String mCurrentSelectedLetter;
    private String[] mLetters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};
    public LetterBarView(Context context) {
        this(context,null);
    }

    public LetterBarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LetterBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LetterBarView);
        mUnselectedColor = typedArray.getColor(R.styleable.LetterBarView_unselectedColor, mUnselectedColor);
        mSelectedColor = typedArray.getColor(R.styleable.LetterBarView_selectedColor, mSelectedColor);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.LetterBarView_letterTextSize,sp2px(mTextSize));
        typedArray.recycle();
        mSelectedPaint = getPaintByColor(mSelectedColor,mTextSize);
        mUnselectedPaint = getPaintByColor(mUnselectedColor,mTextSize);
    }
    private Paint getPaintByColor(int color,int mTextSize){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(mTextSize);
        paint.setAntiAlias(true);
        return paint;
    }

    private int sp2px(int mTextSize) {
        return (int)(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,mTextSize,getResources().getDisplayMetrics()));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //高度
        int height = MeasureSpec.getSize(heightMeasureSpec);
        //宽度 padding 加上文字的一半
        int textWidth = (int) mUnselectedPaint.measureText("A");
        int width = getPaddingLeft() + getPaddingRight() + textWidth;
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("TAG","===>");
        int letterHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
        for(int i=0;i<mLetters.length;i++){
            int textWidth = (int) mUnselectedPaint.measureText(mLetters[i]);
            int dx = getWidth()/2 - textWidth/2;
            int dy = i * letterHeight + letterHeight/2 + getPaddingTop();
            Paint.FontMetricsInt fontMetricsInt = mUnselectedPaint.getFontMetricsInt();
            int y = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
            int baseLine = y + dy;
            if(mLetters[i].equals(mCurrentSelectedLetter)){
                canvas.drawText(mLetters[i], dx, baseLine, mSelectedPaint);
            }else {
                canvas.drawText(mLetters[i], dx, baseLine, mUnselectedPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN :
            case MotionEvent.ACTION_MOVE:
                float y = event.getY();
                int letterHeight = (getHeight() - getPaddingTop() - getPaddingBottom()) / mLetters.length;
                int centerY = (int)(y/letterHeight);
                if(centerY >= 0 && centerY < mLetters.length){
                    String mLetter = mLetters[centerY];
                    if(!mLetter.equals(mCurrentSelectedLetter)){
                        mCurrentSelectedLetter = mLetter;
                        if(this.letterListener!=null) {
                            this.letterListener.selected(mCurrentSelectedLetter);
                        }
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(this.letterListener!=null) {
                    mCurrentSelectedLetter = null;
                    this.letterListener.selected(null);
                    invalidate();
                }
                break;
        }
        return true;
    }
    private OnSelectedLetterListener letterListener;
    public void setOnSelectedLetterListener(OnSelectedLetterListener letterListener){
        this.letterListener = letterListener;
    }
    public interface OnSelectedLetterListener{
        void selected(String letter);
    }
}
