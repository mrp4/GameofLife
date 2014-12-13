package scot.provan.gameoflife;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;


/**
 * TODO: document your custom view class.
 */
public class GameView extends View {
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    // Game of Life components

    private int mDrawableWidth;
    private int mDrawableHeight;
    private int mNumberX;
    private int mNumberY;
    private int mSquareSize = 40;
    private GameOfLifeBoard board;

    public GameView(Context context) {
        super(context);
        init(null, 0);
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.GameView, defStyle, 0);

        mExampleString = a.getString(
                R.styleable.GameView_exampleString);
        mExampleColor = a.getColor(
                R.styleable.GameView_exampleColor,
                mExampleColor);
        // Use getDimensionPixelSize or getDimensionPixelOffset when dealing with
        // values that should fall on pixel boundaries.
        mExampleDimension = a.getDimension(
                R.styleable.GameView_exampleDimension,
                mExampleDimension);

        if (a.hasValue(R.styleable.GameView_exampleDrawable)) {
            mExampleDrawable = a.getDrawable(
                    R.styleable.GameView_exampleDrawable);
            mExampleDrawable.setCallback(this);
        }

        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();
    }

    public void performGame() {
        if (board != null) {
            board.performGame();
            invalidate();
        } else {
            Log.e("PERFORM_GAME", "board is null");
        }
    }

    private void invalidateTextPaintAndMeasurements() {
        mTextPaint.setTextSize(mExampleDimension);
        mTextPaint.setColor(mExampleColor);
        mTextWidth = mTextPaint.measureText(mExampleString);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mDrawableHeight = h;
        mDrawableWidth = w;
        mNumberX = mDrawableWidth / mSquareSize;
        mNumberY = mDrawableHeight / mSquareSize;
        Log.i("ON_SIZE_CHANGED", String.format("mNumberX: %d, mNumberY: %d, w: %d, h: %d, oldw: %d, oldh: %d", mNumberX, mNumberY, w, h, oldw, oldh));
        board = new GameOfLifeBoard(mNumberX, mNumberY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float xAcross = event.getX() / (float) mSquareSize;
        float yAcross = event.getY() / (float) mSquareSize;
        int xIndex = Math.round(xAcross);
        int yIndex = Math.round(yAcross);
        Log.i("GAME_VIEW_ON_TOUCH_EVENT", String.format("x (e): %f, x (f): %f, x (i): %d, y (f): %f, y (i): %d", event.getX(), xAcross, xIndex, yAcross, yIndex));
        try {
            GameOfLifeBoard.Cell cell = board.getCell(xIndex, yIndex);
            cell.activateCell();
            invalidate();
        } catch (GameOfLifeBoard.CellOutOfBoundsException e) {
            Log.e("ON_TOUCH_CELL_OOB", "cell out of bounds", e);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (board == null) {
            Log.i("BOARD_NULL", "board is null, not drawing a thing");
            return;
        }

        Paint redPaint = new Paint();
        redPaint.setStyle(Paint.Style.FILL);
        redPaint.setColor(Color.RED);
        Paint greenPaint = new Paint();
        greenPaint.setStyle(Paint.Style.FILL);
        greenPaint.setColor(Color.GREEN);

        Paint currentPaint = redPaint;

        for (int y = 0; y < mNumberY; y++) {
            for (int x = 0; x < mNumberX; x++) {
                try {
                    GameOfLifeBoard.Cell c = board.getCell(x, y);
                    int color = 0;
                    if (c.isActive()) {
                        color = c.getColor();
                    } else {
                        color = Color.argb(255, 0, 0, 0);
                    }
                    float startX = x * mSquareSize;
                    float startY = y * mSquareSize;
                    float endX = startX + mSquareSize;
                    float endY = startY + mSquareSize;
                    currentPaint.setColor(color);
                    canvas.drawRect(startX, startY, endX, endY, currentPaint);
                } catch (GameOfLifeBoard.CellOutOfBoundsException e) {

                }
            }
        }
    }

    /**
     * Gets the example string attribute value.
     *
     * @return The example string attribute value.
     */
    public String getExampleString() {
        return mExampleString;
    }

    /**
     * Sets the view's example string attribute value. In the example view, this string
     * is the text to draw.
     *
     * @param exampleString The example string attribute value to use.
     */
    public void setExampleString(String exampleString) {
        mExampleString = exampleString;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example color attribute value.
     *
     * @return The example color attribute value.
     */
    public int getExampleColor() {
        return mExampleColor;
    }

    /**
     * Sets the view's example color attribute value. In the example view, this color
     * is the font color.
     *
     * @param exampleColor The example color attribute value to use.
     */
    public void setExampleColor(int exampleColor) {
        mExampleColor = exampleColor;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example dimension attribute value.
     *
     * @return The example dimension attribute value.
     */
    public float getExampleDimension() {
        return mExampleDimension;
    }

    /**
     * Sets the view's example dimension attribute value. In the example view, this dimension
     * is the font size.
     *
     * @param exampleDimension The example dimension attribute value to use.
     */
    public void setExampleDimension(float exampleDimension) {
        mExampleDimension = exampleDimension;
        invalidateTextPaintAndMeasurements();
    }

    /**
     * Gets the example drawable attribute value.
     *
     * @return The example drawable attribute value.
     */
    public Drawable getExampleDrawable() {
        return mExampleDrawable;
    }

    /**
     * Sets the view's example drawable attribute value. In the example view, this drawable is
     * drawn above the text.
     *
     * @param exampleDrawable The example drawable attribute value to use.
     */
    public void setExampleDrawable(Drawable exampleDrawable) {
        mExampleDrawable = exampleDrawable;
    }
}
