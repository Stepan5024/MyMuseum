package bokarev.stepan.mymuseum;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundedCornerImageView extends AppCompatImageView {
    private Path roundedPath;
    private int rounded;

    public RoundedCornerImageView(Context context) {
        super(context);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedCornerImageView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        //If the application is hardware accelerated,
        //must disable it for this view.
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //Set a default radius
        setRounded(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            roundedPath = new Path();
            roundedPath.addRoundRect(new RectF(0, 0, w, h),
                    rounded, rounded, Path.Direction.CW);
        }
    }

    public int getRounded() {
        return rounded;
    }

    public void setRounded(int rounded) {
        this.rounded = rounded;
        roundedPath = new Path();
        roundedPath.addRoundRect(new RectF(0, 0, getWidth(), getHeight()),
                rounded, rounded, Path.Direction.CW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Apply the clip
        canvas.clipPath(roundedPath);
        //Let the view draw as normal
        super.onDraw(canvas);
    }
}