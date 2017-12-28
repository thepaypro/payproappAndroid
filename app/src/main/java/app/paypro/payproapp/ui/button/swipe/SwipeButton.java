package app.paypro.payproapp.ui.button.swipe;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
//        ImageView backgroundImageView = new ImageView(context);

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

        swipeButton.setBackgroundResource(R.drawable.swipe_button);

        background.setBackgroundResource(R.drawable.swipe_rounded);

        if (attrs != null && defStyleAttr == -1 && defStyleRes == -1) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SwipeButton,
                    defStyleAttr, defStyleRes);

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
                    if (!TouchUtils.isTouchOutsideInitialPosition(event, swipeButtonInner)) {
                        actionDownPos = mainViewWidth / initialOffsetFrac - event.getX();
                        return true;
                    } else {
                        return false;
                    }
                case MotionEvent.ACTION_UP:
                    if(enabled){
                        if (activated) {
                            swipeButtonInner.setX(0);
                            if (onStateChangeListener != null) {
                                onStateChangeListener.onStateChange(true);
                            }
                        } else {
                            restartSwipeButton();
                        }
                    }
                case MotionEvent.ACTION_MOVE:
                    if(enabled) {
                        if (swipeButtonInner.getX() >= 0 && !activated && !restarted) {
                            activated = true;
                            swipeButtonInner.setX(0);
                        } else {
                            if (swipeButtonInner.getX() <= 0 && (event.getX() - mainViewWidth + actionDownPos) < 0) {
                                swipeButtonInner.setX(event.getX() - mainViewWidth + actionDownPos);
                                activated = false;
                                restarted = false;
                            } else {
                                if (swipeButtonInner.getX() != 0) {
                                    swipeButtonInner.setX(0);
                                }
                            }
                        }
                        return true;
                    }
            }
            return false;
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