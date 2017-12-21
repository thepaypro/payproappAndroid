package app.paypro.payproapp.ui.button.swipe;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.paypro.payproapp.R;

public class SwipeButton extends RelativeLayout {


    private ConstraintLayout swipeButtonInner;
    private int mainViewWidth;
    private int startPosition;
    private int initialOffsetFrac = 5;
    private TextView centerText;
    private ViewGroup background;
    private Boolean activated = false;
    private Boolean enabled = true;
    private Boolean restarted = false;
    private float actionDownPos;


    private OnStateChangeListener onStateChangeListener;

//    private int collapsedWidth;
//    private int collapsedHeight;

    private LinearLayout layer;
//    private boolean trailEnabled = false;

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

    public void setEventListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
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
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);

        background.addView(centerText, layoutParams);

        final ConstraintLayout swipeButton = new ConstraintLayout(context);

        this.swipeButtonInner = swipeButton;

        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeButton,
                    defStyleAttr, defStyleRes);

//            collapsedWidth = (int) typedArray.getDimension(R.styleable.SwipeButton_button_image_width,
//                    ViewGroup.LayoutParams.MATCH_PARENT);
//            collapsedHeight = (int) typedArray.getDimension(R.styleable.SwipeButton_button_image_height,
//                    ViewGroup.LayoutParams.WRAP_CONTENT);

//            trailEnabled = typedArray.getBoolean(R.styleable.SwipeButton_button_trail_enabled,
//                    false);
            Drawable trailingDrawable = typedArray.getDrawable(R.styleable.SwipeButton_button_trail_drawable);

            Drawable backgroundDrawable = typedArray.getDrawable(R.styleable.SwipeButton_inner_text_background);

            if (backgroundDrawable != null) {
                background.setBackground(backgroundDrawable);
            } else {
                background.setBackground(ContextCompat.getDrawable(context, R.drawable.shape_swipe_rounded));
            }

//            if (trailEnabled) {
//                layer = new LinearLayout(context);
//
//                if (trailingDrawable != null) {
//                    layer.setBackground(trailingDrawable);
//                } else {
//                    layer.setBackground(typedArray.getDrawable(R.styleable.SwipeButton_button_background));
//                }
//
//                layer.setGravity(Gravity.START);
//                layer.setVisibility(View.GONE);
//                background.addView(layer, layoutParamsView);
//            }

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
            float innerTextLeftPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_left_padding, 0);
            float innerTextTopPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_top_padding, 0);
            float innerTextRightPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_right_padding, 0);
            float innerTextBottomPadding = typedArray.getDimension(
                    R.styleable.SwipeButton_inner_text_bottom_padding, 0);

//            LayoutParams layoutParamsButton = new LayoutParams(collapsedWidth, collapsedHeight);

            LayoutParams layoutParamsButton = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            layoutParamsButton.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            layoutParamsButton.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);

            addView(swipeButton, layoutParamsButton);

            final SwipeButton mainView = this;

            post(new Runnable() {
                @Override
                public void run() {
                    mainViewWidth = mainView.getWidth();
                    startPosition = -mainViewWidth + mainViewWidth/initialOffsetFrac;
                    swipeButton.animate().translationX(startPosition);
                }
            });

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
                if(enabled) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (!TouchUtils.isTouchOutsideInitialPosition(event, swipeButtonInner)) {
                                actionDownPos = mainViewWidth / initialOffsetFrac - event.getX();
                                return true;
                            } else {
                                return false;
                            }
                        case MotionEvent.ACTION_UP:
                            if (activated) {
                                swipeButtonInner.setX(0);
                                if (onStateChangeListener != null) {
                                    onStateChangeListener.onStateChange(true);
                                }
                            } else {
                                restartSwipeButton();
                            }
                        case MotionEvent.ACTION_MOVE:
                            if (swipeButtonInner.getX() >= 0 && !activated) {
                                activated = true;
                                swipeButtonInner.setX(0);
                            } else {
                                if(swipeButtonInner.getX() <= 0 && (event.getX() - mainViewWidth + actionDownPos) < 0 ){
                                    swipeButtonInner.setX(event.getX() - mainViewWidth + actionDownPos);
                                }else{
                                    swipeButtonInner.setX(0);
                                }
                                activated = false;
                                restarted = false;
                            }
                            return true;
                    }
                    return false;
                }else{
                    return false;
                }
            }
        };
    }

    public void restartSwipeButton(){
        swipeButtonInner.animate().translationX(startPosition);
        restarted = true;
    }

    public void enable(){
        enabled = true;
    }

    public void disable(){
        enabled = false;
    }
}