package com.quanzi.tvswitch;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

public class TextSwitch extends View {

    private Paint paintBack, paintText;
    private RectF backRect, secondRect;
    private float borderSize;
    private float scPx;

    //背景
    private int bgColor, secondBgColor, secondBorderColor;

    //文本
    private String leftContent, rightContent;
    private int leftSize, rightSize;
    private int leftColor, rightColor;
    //左边为false，右边为true
    private boolean state;
    private boolean canMove = true, outCanMove = true;
    //动画
    private ValueAnimator animationOn, animationOff;
    private int duration;
    private final float maxValue = 100;

    private OnTextSwitchSelectChangedListener onTextSwitchSelectChangedListener;

    public void setOnTextSwitchSelectChangedListener(OnTextSwitchSelectChangedListener onTextSwitchSelectChangedListener) {
        this.onTextSwitchSelectChangedListener = onTextSwitchSelectChangedListener;
    }

    public TextSwitch(Context context) {
        this(context, null);
    }

    public TextSwitch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextSwitch);
        leftContent = array.getString(R.styleable.TextSwitch_left_content);
        leftSize = array.getDimensionPixelSize(R.styleable.TextSwitch_left_size, 0);
        leftColor = array.getColor(R.styleable.TextSwitch_left_color, ResourcesCompat.getColor(getResources(), R.color.text_switch_text_color, null));
        rightContent = array.getString(R.styleable.TextSwitch_right_content);
        rightSize = array.getDimensionPixelSize(R.styleable.TextSwitch_right_size, 0);
        rightColor = array.getColor(R.styleable.TextSwitch_right_color, ResourcesCompat.getColor(getResources(), R.color.text_switch_text_color, null));
        borderSize = array.getDimensionPixelOffset(R.styleable.TextSwitch_second_border_width, dip2px(2));
        duration = array.getInteger(R.styleable.TextSwitch_duration, 300);

        bgColor = array.getColor(R.styleable.TextSwitch_bg, ResourcesCompat.getColor(getResources(), R.color.text_switch_bg, null));
        secondBgColor = array.getColor(R.styleable.TextSwitch_second_bg, ResourcesCompat.getColor(getResources(), R.color.text_switch_second, null));
        secondBorderColor = array.getColor(R.styleable.TextSwitch_second_border_color, ResourcesCompat.getColor(getResources(), R.color.text_switch_second_border, null));

        array.recycle();
        init();
        initPaint();
    }

    private void init() {
        if (leftContent == null)
            leftContent = "左边";
        if (rightContent == null)
            rightContent = "右边";
        if (leftSize == 0 || rightSize == 0) {
            int tempSize = Math.max(leftSize, rightSize);
            if (tempSize == 0)
                tempSize = sp2px();
            leftSize = tempSize;
            rightSize = tempSize;
        }

        backRect = new RectF();
        backRect.left = borderSize;
        backRect.top = borderSize;
        secondRect = new RectF();
        secondRect.top = borderSize;
        scPx = borderSize;
    }

    private void initPaint() {
        //背景
        paintBack = new Paint();
        paintBack.setAntiAlias(true);
        paintBack.setDither(true);
        //文字
        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setDither(true);
        //自身的点击事件
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!canMove || !outCanMove)
                    return;
                toggle();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float defaultWidth;
        float defaultHeight;
        paintText.setTextSize(leftSize);
        float leftWidth = paintText.measureText(leftContent) + dip2px(20);
        Paint.FontMetrics fontMetrics = paintText.getFontMetrics();
        float leftHeight = fontMetrics.descent - fontMetrics.ascent + dip2px(10);
        paintText.setTextSize(rightSize);
        float rightWidth = paintText.measureText(rightContent) + dip2px(20);
        fontMetrics = paintText.getFontMetrics();
        float rightHeight = fontMetrics.descent - fontMetrics.ascent + dip2px(10);
        defaultWidth = leftWidth + rightWidth;
        defaultHeight = Math.max(leftHeight, rightHeight);

        setMeasuredDimension(
                measure(widthMeasureSpec, defaultWidth),
                measure(heightMeasureSpec, defaultHeight)
        );
    }

    private int measure(int size, float defaultSize) {
        int specMode = MeasureSpec.getMode(size);
        int specSize = MeasureSpec.getSize(size);
        //设置一个默认值，就是这个View的默认宽度为500，这个看我们自定义View的要求
        int result = 0;
        if (specMode == MeasureSpec.AT_MOST) {//相当于我们设置为wrap_content
            result = specSize;
        } else if (specMode == MeasureSpec.EXACTLY) {//相当于我们设置为match_parent或者为一个具体的值
            result = specSize;
        }
        return (int) Math.max(result, defaultSize);
    }

    /**
     * 设置是否允许开关
     *
     * @param outCanMove true为可以开关，false为不可以开关，默认为true
     */
    public void setCanToggle(boolean outCanMove) {
        this.outCanMove = outCanMove;
    }

    /**
     * 设置当前开关状态
     *
     * @param state false为左边，true为右边
     */
    public void setState(boolean state) {
        if (this.state == state)
            return;
        this.state = state;
        toggle(false);
    }

    /**
     * 开关
     */
    public void toggle() {
        toggle(true);
    }

    private void toggle(boolean isChangeState) {
        if (animationOn == null)
            initAnimation();
        if (state)
            animationOff.start();
        else
            animationOn.start();
        if (isChangeState)
            state = !state;
    }

    private void initAnimation() {
        animationOn = ValueAnimator.ofFloat(0f, maxValue);
        animationOn.setDuration(duration);
        animationOn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scPx = (getWidth() - borderSize) / 2f * ((float) animation.getAnimatedValue() / maxValue);
                if (scPx < borderSize)
                    scPx = borderSize;
                System.out.println("---" + scPx);
                postInvalidate();
            }
        });
        animationOff = ValueAnimator.ofFloat(0f, maxValue);
        animationOff.setDuration(duration);
        animationOff.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scPx = getWidth() / 2f - (getWidth() - borderSize) / 2f * ((float) animation.getAnimatedValue() / maxValue);
                if (scPx < borderSize)
                    scPx = borderSize;
                System.out.println("---" + scPx);
                postInvalidate();
            }
        });
        animationOn.addListener(animationListener);
        animationOff.addListener(animationListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //背景
        paintBack.setColor(bgColor);
        paintBack.setStyle(Paint.Style.FILL);
        paintBack.setStrokeWidth(0);
        backRect.right = getWidth() - borderSize;
        backRect.bottom = getHeight() - borderSize;
        canvas.drawRoundRect(backRect, 100, 100, paintBack);
        //第二层背景
        paintBack.setColor(secondBgColor);
        secondRect.left = scPx;
        secondRect.right = (getWidth() - borderSize) / 2f + scPx;
        secondRect.bottom = getHeight() - borderSize;
        canvas.drawRoundRect(secondRect, 100, 100, paintBack);
        //第二层边框
        paintBack.setColor(secondBorderColor);
        paintBack.setStrokeWidth(borderSize);
        paintBack.setStyle(Paint.Style.STROKE);
        canvas.drawRoundRect(secondRect, 100, 100, paintBack);
        //文本绘制
        //左边
        paintText.setTextSize(leftSize);
        paintText.setColor(leftColor);
        float currentWidth = paintText.measureText(leftContent);
        Paint.FontMetrics fontMetrics = paintText.getFontMetrics();
        float distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        float baseline = getHeight() / 2f + distance;
        canvas.drawText(leftContent, (getWidth() / 2f - currentWidth) / 2f + borderSize, baseline, paintText);
        //右边
        paintText.setTextSize(rightSize);
        paintText.setColor(rightColor);
        currentWidth = paintText.measureText(rightContent);
        fontMetrics = paintText.getFontMetrics();
        distance = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        baseline = getHeight() / 2f + distance;
        canvas.drawText(rightContent, getWidth() / 2f + ((getWidth() - borderSize) / 2f - currentWidth) / 2f, baseline, paintText);
    }

    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int sp2px() {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getContext().getResources().getDisplayMetrics());
    }

    private Animator.AnimatorListener animationListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            canMove = false;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            canMove = true;
            if (onTextSwitchSelectChangedListener != null)
                onTextSwitchSelectChangedListener.onTextSwitchSelectChanged(state);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

}
