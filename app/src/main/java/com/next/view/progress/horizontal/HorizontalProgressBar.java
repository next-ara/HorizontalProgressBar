package com.next.view.progress.horizontal;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;

import androidx.annotation.Nullable;

/**
 * ClassName:横向进度条控件类
 *
 * @author Afton
 * @time 2024/2/27
 * @auditor
 */
public class HorizontalProgressBar extends View {

    //圆角半径
    private float radius = 0;

    //是否是模糊进度条
    private boolean indeterminate = false;

    //模糊进度条速率
    private int indeterminateRate = 50;

    //进度条颜色
    private int progressColor = Color.BLACK;

    //背景颜色
    private int backgroundColor = Color.WHITE;

    //精确进度条进度
    private int progress = 50;

    //模糊进度条宽度
    private float indeterminateWidth = -1;

    //模糊进度条起始位置
    private Integer indeterminateStart;

    //进度画笔
    private Paint progressPaint;

    //背景画笔
    private Paint backgroundPaint;

    public HorizontalProgressBar(Context context) {
        super(context);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        //初始化
        this.init(context, attrs);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化
        this.init(context, attrs);
    }

    public HorizontalProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        //初始化
        this.init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param attrs   属性
     */
    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar);
        float radius = ta.getDimension(R.styleable.HorizontalProgressBar_radius, 0);
        this.indeterminate = ta.getBoolean(R.styleable.HorizontalProgressBar_indeterminate, false);
        this.progressColor = ta.getColor(R.styleable.HorizontalProgressBar_progressColor, Color.BLACK);
        this.backgroundColor = ta.getColor(R.styleable.HorizontalProgressBar_backgroundColor, Color.WHITE);
        this.indeterminateRate = ta.getInteger(R.styleable.HorizontalProgressBar_indeterminateRate, 50);
        this.progress = ta.getInteger(R.styleable.HorizontalProgressBar_progress, 50);
        this.indeterminateWidth = ta.getDimension(R.styleable.HorizontalProgressBar_indeterminateWidth, -1);
        ta.recycle();

        //设置圆角半径
        this.setRadius(radius);

        //初始化画笔
        this.initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        this.backgroundPaint = new Paint();
        this.backgroundPaint.setColor(this.backgroundColor);
        this.backgroundPaint.setAntiAlias(true);

        this.progressPaint = new Paint();
        this.progressPaint.setColor(this.progressColor);
        this.progressPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.onDrawBackground(canvas);
        this.onDrawIndeterminate(canvas);
        this.onDrawProgress(canvas);
    }

    /**
     * 绘制背景
     *
     * @param canvas 画布
     */
    private void onDrawBackground(Canvas canvas) {
        int width = this.getWidth();
        int height = this.getHeight();

        Path path = RoundPathTool.getRoundPath(0, 0, width, height, this.radius);
        canvas.drawPath(path, this.backgroundPaint);
    }

    /**
     * 绘制模糊进度条
     *
     * @param canvas 画布
     */
    private void onDrawIndeterminate(Canvas canvas) {
        if (!this.indeterminate) {
            return;
        }

        int width = this.getWidth();
        int height = this.getHeight();

        if (this.indeterminateWidth < 0) {
            this.indeterminateWidth = width / 2.6f;
        }

        //验证进度条起始位置
        if (this.indeterminateStart == null || this.indeterminateStart > width) {
            this.indeterminateStart = -width;
        }

        Path path = RoundPathTool.getRoundPath(this.indeterminateStart, 0, this.indeterminateWidth, height, this.radius);
        canvas.drawPath(path, this.progressPaint);

        //移动进度条起始位置
        this.indeterminateStart = this.indeterminateStart + this.indeterminateRate;

        //刷新
        this.invalidate();
    }

    /**
     * 绘制精确进度条
     *
     * @param canvas 画布
     */
    private void onDrawProgress(Canvas canvas) {
        if (this.indeterminate) {
            return;
        }

        int width = this.getWidth();
        int height = this.getHeight();

        Path path = RoundPathTool.getRoundPath(0, 0, width * this.progress / 100f, height, this.radius);
        canvas.drawPath(path, this.progressPaint);
    }

    /**
     * 设置圆角半径
     *
     * @param radius 圆角半径
     */
    public void setRadius(float radius) {
        if (this.radius == radius) {
            return;
        }

        RoundOutlineProvider roundOutlineProvider = new RoundOutlineProvider(radius);
        this.setOutlineProvider(roundOutlineProvider);
        this.setClipToOutline(true);
        this.radius = radius;
    }

    /**
     * 设置是否是模糊进度条
     *
     * @param indeterminate 是否是模糊进度条
     */
    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
        this.invalidate();
    }

    /**
     * 设置进度条颜色
     *
     * @param progressColor 进度条颜色
     */
    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
        this.progressPaint.setColor(progressColor);
        this.invalidate();
    }

    /**
     * 设置背景颜色
     *
     * @param backgroundColor 背景颜色
     */
    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        this.backgroundPaint.setColor(backgroundColor);
        this.invalidate();
    }

    /**
     * 设置模糊进度条速率
     *
     * @param indeterminateRate 模糊进度条速率
     */
    public void setIndeterminateRate(int indeterminateRate) {
        this.indeterminateRate = indeterminateRate;
        this.invalidate();
    }

    /**
     * 设置进度
     *
     * @param progress 进度
     */
    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }

    /**
     * 设置模糊进度条宽度
     *
     * @param indeterminateWidth 模糊进度条宽度
     */
    public void setIndeterminateWidth(float indeterminateWidth) {
        this.indeterminateWidth = indeterminateWidth;
        this.invalidate();
    }

    public float getRadius() {
        return radius;
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getIndeterminateRate() {
        return indeterminateRate;
    }

    public int getProgress() {
        return progress;
    }

    public float getIndeterminateWidth() {
        return indeterminateWidth;
    }

    //圆角轮廓裁剪类
    private static class RoundOutlineProvider extends ViewOutlineProvider {

        //圆角半径
        private float radius = 0;

        public RoundOutlineProvider(float radius) {
            this.radius = radius;
        }

        @Override
        public void getOutline(View view, Outline outline) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Path path = RoundPathTool.getRoundPath(0, 0, view.getWidth(), view.getHeight(), this.radius);
                outline.setPath(path);
            } else {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), this.radius);
            }
        }
    }

    //圆角路径工具类
    private static class RoundPathTool {

        /**
         * 获取圆角矩形的路径
         *
         * @param rectF  矩形的边框
         * @param radius 圆角的半径
         * @return 圆角矩形的路径
         */
        public static Path getRoundPath(RectF rectF, float radius) {
            return getRoundPath(rectF.left, rectF.top, rectF.width(), rectF.height(), radius);
        }

        /**
         * 获取圆角矩形的路径
         *
         * @param left   矩形的左边
         * @param top    矩形的上边
         * @param width  矩形的宽度
         * @param height 矩形的高度
         * @param radius 圆角的半径
         * @return 圆角矩形的路径
         */
        public static Path getRoundPath(float left, float top, float width, float height, float radius) {
            radius = Math.max(radius, 0);

            //开始画圆角的位置对比圆角大小的偏移比例
            float radiusOffsetRatio = 128f / 100f;
            //靠近圆弧两个端点的点的xy坐标比例
            float endPointRatio = 83f / 100f;
            //左上角第一条曲线第二个点x坐标的比例(其他三个点通过矩阵转换可以使用同样的比例）
            float firstCSecondPXRatio = 67f / 100f;
            //左上角第一条曲线第二个点Y坐标的比例（其他三个点通过矩阵转换可以使用同样的比例)
            float firstCSecondPYRatio = 4f / 100f;
            //左上角第一条曲线第三个点x坐标的比例（其他三个点通过矩阵转换可以使用同样的比例)
            float firstCThirdPXRatio = 51f / 100f;
            //左上角第一条曲线第三个点Y坐标的比例(其他三个点通过矩阵转换可以使用同样的比例)
            float firstCThirdPYRatio = 13f / 100f;
            //左上角第二条曲线第一个点X坐标的比例（其他三个点通过矩阵转换可以使用同样的比例)
            float secondCFirstPXRatio = 34f / 100f;
            //左上角第二条曲线第一个点Y坐标的比例(其他三个点通过矩阵转换可以使用同样的比例)
            float secondCFirstPYRatio = 22f / 100f;

            Path mRoundPath = new Path();
            mRoundPath.reset();
            //顶部直线和右上角圆角
            mRoundPath.moveTo((width / 2.0f) + left, top);

            //顶部直线和右上角圆角
            mRoundPath.lineTo(
                    coerceAtLeast((width / 2.0f), (width - radius * radiusOffsetRatio) + left),
                    top
            );

            mRoundPath.cubicTo(
                    left + width - radius * endPointRatio, top,
                    left + width - radius * firstCSecondPXRatio,
                    top + radius * firstCSecondPYRatio,
                    left + width - radius * firstCThirdPXRatio,
                    top + radius * firstCThirdPYRatio
            );
            mRoundPath.cubicTo(
                    left + width - radius * secondCFirstPXRatio,
                    top + radius * secondCFirstPYRatio,
                    left + width - radius * secondCFirstPYRatio,
                    top + radius * secondCFirstPXRatio,
                    left + width - radius * firstCThirdPYRatio,
                    top + radius * firstCThirdPXRatio
            );
            mRoundPath.cubicTo(
                    left + width - radius * firstCSecondPYRatio,
                    top + radius * firstCSecondPXRatio,
                    left + width,
                    top + radius * endPointRatio,
                    left + width,
                    top + coerceAtMost((height / 2.0f), radius * radiusOffsetRatio)
            );

            //右边直线和右下角圆角
            mRoundPath.lineTo(left + width, coerceAtLeast((height / 2.0f), height - radius * radiusOffsetRatio) + top);
            mRoundPath.cubicTo(
                    left + width,
                    top + height - radius * endPointRatio,
                    left + width - radius * firstCSecondPYRatio,
                    top + height - radius * firstCSecondPXRatio,
                    left + width - radius * firstCThirdPYRatio,
                    top + height - radius * firstCThirdPXRatio
            );
            mRoundPath.cubicTo(
                    left + width - radius * secondCFirstPYRatio,
                    top + height - radius * secondCFirstPXRatio,
                    left + width - radius * secondCFirstPXRatio,
                    top + height - radius * secondCFirstPYRatio,
                    left + width - radius * firstCThirdPXRatio,
                    top + height - radius * firstCThirdPYRatio
            );

            mRoundPath.cubicTo(
                    left + width - radius * firstCSecondPXRatio,
                    top + height - radius * firstCSecondPYRatio,
                    left + width - radius * endPointRatio,
                    top + height,
                    left + coerceAtLeast((width / 2.0f), width - radius * radiusOffsetRatio),
                    top + height
            );

            //底部直线和左下角圆角
            mRoundPath.lineTo(coerceAtMost((width / 2.0f), radius * radiusOffsetRatio) + left, top + height);
            mRoundPath.cubicTo(
                    left + radius * endPointRatio,
                    top + height,
                    left + radius * firstCSecondPXRatio,
                    top + height - radius * firstCSecondPYRatio,
                    left + radius * firstCThirdPXRatio,
                    top + height - radius * firstCThirdPYRatio
            );
            mRoundPath.cubicTo(
                    left + radius * secondCFirstPXRatio,
                    top + height - radius * secondCFirstPYRatio,
                    left + radius * secondCFirstPYRatio,
                    top + height - radius * secondCFirstPXRatio,
                    left + radius * firstCThirdPYRatio,
                    top + height - radius * firstCThirdPXRatio
            );
            mRoundPath.cubicTo(
                    left + radius * firstCSecondPYRatio,
                    top + height - radius * firstCSecondPXRatio,
                    left,
                    top + height - radius * endPointRatio,
                    left,
                    top + coerceAtLeast((height / 2.0f), height - radius * radiusOffsetRatio)
            );
            //左边直线和左上角圆角
            mRoundPath.lineTo(left, coerceAtMost((height / 2.0f), radius * radiusOffsetRatio) + top);
            mRoundPath.cubicTo(
                    left,
                    top + radius * endPointRatio,
                    left + radius * firstCSecondPYRatio,
                    top + radius * firstCSecondPXRatio,
                    left + radius * firstCThirdPYRatio,
                    top + radius * firstCThirdPXRatio
            );
            mRoundPath.cubicTo(
                    left + radius * secondCFirstPYRatio,
                    top + radius * secondCFirstPXRatio,
                    left + radius * secondCFirstPXRatio,
                    top + radius * secondCFirstPYRatio,
                    left + radius * firstCThirdPXRatio,
                    top + radius * firstCThirdPYRatio
            );

            mRoundPath.cubicTo(
                    left + radius * firstCSecondPXRatio,
                    top + radius * firstCSecondPYRatio,
                    left + radius * endPointRatio,
                    top,
                    left + coerceAtMost((width / 2.0f), radius * radiusOffsetRatio),
                    top
            );

            mRoundPath.close();
            return mRoundPath;
        }

        /**
         * 确保value不超过maximumValue
         *
         * @param value
         * @param maximumValue
         * @return
         */
        private static float coerceAtMost(float value, float maximumValue) {
            if (value > maximumValue) {
                return maximumValue;
            }

            return value;
        }

        /**
         * 确保value不小于minimumValue
         *
         * @param value
         * @param minimumValue
         * @return
         */
        private static float coerceAtLeast(float value, float minimumValue) {
            if (value < minimumValue) {
                return minimumValue;
            }

            return value;
        }
    }
}