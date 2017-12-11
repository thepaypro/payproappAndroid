package app.paypro.payproapp.ui.button.swipe;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.paypro.payproapp.R;

public class SwipeButton extends RelativeLayout {


    private ConstraintLayout swipeButtonInner;
    private float initialX;
    private int initialOffset = 200;
    private boolean active;
    private TextView centerText;
    private ViewGroup background;

    private Drawable disabledDrawable;
    private Drawable enabledDrawable;

    private OnStateChangeListener onStateChangeListener;
    private OnActiveListener onActiveListener;

    private static final int ENABLED = 0;
    private static final int DISABLED = 1;

    private int collapsedWidth;
    private int collapsedHeight;

    private LinearLayout layer;
    private boolean trailEnabled = false;

    public SwipeButton(Context context) {
        super(context);

        init(context, null, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, -1, -1);
    }

    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context, attrs, defStyleAttr, -1);
    }

    @TargetApi(21)
    public SwipeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setText(String text) {
        centerText.setText(text);
    }

    public void setBackground(Drawable drawable) {
        background.setBackground(drawable);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        background = new RelativeLayout(context);

        LayoutParams layoutParamsView = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParamsView.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        addView(background, layoutParamsView);

        final TextView centerText = new TextView(context);
        this.centerText = centerText;
        centerText.setGravity(Gravity.CENTER);

        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        background.addView(centerText, layoutParams);

        final ConstraintLayout swipeButton = new ConstraintLayout(context);

        this.swipeButtonInner = swipeButton;

        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeButton,
                    defStyleAttr, defStyleRes);

            collapsedWidth = (int) typedArray.getDimension(R.styleable.SwipeButton_button_image_width,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            collapsedHeight = (int) typedArray.getDimension(R.styleable.SwipeButton_button_image_height,
                    ViewGroup.LayoutParams.WRAP_CONTENT);

            trailEnabled = typedArray.getBoolean(R.styleable.SwipeButton_button_trail_enabled,
                    false);
            Drawable trailingDrawable = typedArray.getDrawable(R.styleable.SwipeButton_button_trail_drawable);

            Drawable backgroundDrawable = typedArray.getDrawable(R.styleable.SwipeButton_inner_text_background);

            if (backgroundDrawable != null) {
                background.setBackground(backgroundDrawable);
            } else {
                background.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_swipe_rounded));
            }

            if (trailEnabled) {
                layer = new LinearLayout(context);

                if (trailingDrawable != null) {
                    layer.setBackground(trailingDrawable);
                } else {
                    layer.setBackground(typedArray.getDrawable(R.styleable.SwipeButton_button_background));
                }

                layer.setGravity(Gravity.START);
                layer.setVisibility(View.GONE);
                background.addView(layer, layoutParamsView);
            }

            centerText.setText(typedArray.getText(R.styleable.SwipeButton_inner_text));
            centerText.setTextColor(typedArray.getColor(R.styleable.SwipeButton_inner_text_color,
                    Color.WHITE));

            float textSize = DimentionUtils.converPixelsToSp(
                    typedArray.getDimension(R.styleable.SwipeButton_inner_text_size, 0), context);

            if (textSize != 0) {
                centerText.setTextSize(textSize);
            } else {
                centerText.setTextSize(12);
            }

            disabledDrawable = typedArray.getDrawable(R.styleable.SwipeButton_button_image_disabled);
            enabledDrawable = typedArray.getDrawable(R.styleable.SwipeButton_button_image_enabled);
            float innerTextLeftPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_left_padding, 0);
            float innerTextTopPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_top_padding, 0);
            float innerTextRightPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_right_padding, 0);
            float innerTextBottomPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_bottom_padding, 0);

            LayoutParams layoutParamsButton = new LayoutParams(collapsedWidth, collapsedHeight);

            layoutParamsButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParamsButton.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

            swipeButton.animate().translationX(initialX);

            addView(swipeButton, layoutParamsButton);

            final SwipeButton mainView = this;

            post(new Runnable() {
                @Override
                public void run() {
                    int w = mainView.getWidth();
                    swipeButton.animate().translationX(-w + initialOffset);
                }
            });

            active = false;

            centerText.setPadding((int) innerTextLeftPadding,
                    (int) innerTextTopPadding,
                    (int) innerTextRightPadding,
                    (int) innerTextBottomPadding);

            Drawable buttonBackground = typedArray.getDrawable(R.styleable.SwipeButton_button_background);

            if (buttonBackground != null) {
                swipeButton.setBackground(buttonBackground);
            } else {
                swipeButton.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_swipe_button));
            }

            typedArray.recycle();
        }

        setOnTouchListener(getButtonTouchListener());
    }

    private OnTouchListener getButtonTouchListener() {
        return new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return !TouchUtils.isTouchOutsideInitialPosition(event, swipeButtonInner);
                    case MotionEvent.ACTION_MOVE:
                        if (initialX == 0) {
                            initialX = swipeButtonInner.getX();
                        }

                        if(event.getX() < initialOffset){
                            int start = - swipeButtonInner.getWidth() + initialOffset;
                            swipeButtonInner.setX(start);
                            centerText.setAlpha(1);
                        }
                        else if (event.getX() > getWidth() * 0.9) {
                            swipeButtonInner.setX(0);
                            centerText.setAlpha(0);
                            //DO SOMETHING
                        } else {
                            swipeButtonInner.setX(event.getX() - swipeButtonInner.getWidth());
                            centerText.setAlpha(1 - 1.3f * (swipeButtonInner.getX() + swipeButtonInner.getWidth()) / getWidth());
                        }

                        return true;
                }

                return false;
            }
        };
    }
}