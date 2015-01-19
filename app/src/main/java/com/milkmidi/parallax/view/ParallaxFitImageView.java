package com.milkmidi.parallax.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.milkmidi.parallax.R;

import java.util.Arrays;

/**
 * author milkmidi
 */
public class ParallaxFitImageView extends ImageView  {

    private final String TAG = "[" + this.getClass().getSimpleName() + "]";


	private float mScale = 1.0f;
    private Matrix mMatrix;
    private Matrix mInitMatrix;
    private float mOffsetY;
    private float mSpeed = 1.0f;

    public ParallaxFitImageView( Context context ) {
		this( context, null, 0 );
	}

	public ParallaxFitImageView( Context context, AttributeSet attrs ) {
		this( context, attrs, 0 );

	}

	public ParallaxFitImageView( Context context, AttributeSet attrs, int defStyleAttr ) {
		super( context, attrs, defStyleAttr );

		if (attrs != null) {
			final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ParallaxFitImageView );
			this.mScale = a.getFloat( R.styleable.ParallaxFitImageView_scale, 1.0f);
			this.mSpeed = a.getFloat( R.styleable.ParallaxFitImageView_speed, 1.0f);
			a.recycle();
		}
        this.mMatrix = new Matrix();
        this.mInitMatrix = new Matrix();
        trace( "init scale:"+ mScale);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = calculateHeight(w, mScale);
        setMeasuredDimension( w, h );
    }
    private int calculateHeight(int w, float scale) {
        int paddingW = getPaddingLeft() + getPaddingRight();
        int paddingH = getPaddingTop() + getPaddingBottom();

        int dW = w - paddingW;
        int dH = (int) (dW * scale);

        return dH + paddingH;
    }

    private void setParallexMatrix(){
        if ( getDrawable() == null ){
            return;
        }

        mMatrix.reset();
        mInitMatrix.reset();


        float originW = getWidth();
        float originH = getHeight();

        float scaleW = originW;
        float scaleH = originH;

        Drawable drawable = getDrawable();
        float intrinsicW = drawable.getIntrinsicWidth();
        float intrinsicH = drawable.getIntrinsicHeight();


        final float ratioW = intrinsicW / originW;
        final float ratioH = intrinsicH / originH;
        float ratio;
        if (ratioW> ratioH) {
            ratio = originH / intrinsicH;
            mMatrix.setScale( ratio, ratio );
            scaleW = intrinsicW * ratio;
        } else {
            ratio = originW / intrinsicW;
            mMatrix.setScale( ratio, ratio );
            scaleH = intrinsicH * ratio;
        }
        trace("setParallexMatrix()");
        trace("getWidth():"+originW+" , "+originH);
        trace("getIntrinsicWidth():"+intrinsicW+" , "+intrinsicH);


        mOffsetY = (originH - scaleH) / 2;
        mMatrix.preTranslate( (originW - scaleW) / 2, mOffsetY );
        trace( "offsetX:" + (originW - scaleW) / 2, "offsetY:" + mOffsetY );
        setScaleType( ScaleType.MATRIX );
        setImageMatrix( mMatrix );
        mInitMatrix.set( mMatrix );

    }

    @Override
    public void setImageBitmap( Bitmap bm ) {
        super.setImageBitmap( bm );
        if ( bm !=null ) {
            setParallexMatrix();
        }
    }

    /**
     *
     * @param fraction -1 ~ 1
     */
    public void setFraction( float fraction ) {
        if ( getDrawable() == null ){
            return;
        }
        mMatrix.set( mInitMatrix );
        mMatrix.preTranslate( 0 , mOffsetY* fraction * mSpeed);
        setImageMatrix( mMatrix );
    }



    public void reset() {
        mMatrix.set( mInitMatrix );
    }

    protected void trace( Object... objects ) {
        Log.i( TAG, Arrays.toString( objects ) );
    }
}
