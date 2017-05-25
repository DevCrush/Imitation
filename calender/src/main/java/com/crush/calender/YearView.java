package com.crush.calender;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.crush.calender.release.OnMonthChangeListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Crush on 5/25/17.
 */

public class YearView extends FrameLayout {
    MonthView cv0, cv1, cv2;
    Context context;
    int current = 1;
    private VelocityTracker mVelocityTracker;//生命变量

    public YearView(@NonNull Context context) {
        super(context);
        initView();
    }

    public YearView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public YearView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    Calendar currentCalender = Calendar.getInstance();

    public void initView() {
        this.context = getContext();
        LayoutInflater.from(getContext()).inflate(R.layout.cus_year_view, this, true);
        cv0 = (MonthView) findViewById(R.id.cv0);
        cv1 = (MonthView) findViewById(R.id.cv1);
        cv2 = (MonthView) findViewById(R.id.cv2);
        currentCalender.setFirstDayOfWeek(Calendar.SUNDAY);
        freshData();
    }

    private void freshData() {
        currentCalender.add(Calendar.MONTH, -1);
        cv0.freshContentData(currentCalender.get(Calendar.YEAR), currentCalender.get(Calendar.MONTH));
        cv0.setTag(currentCalender.getTime());
        currentCalender.add(Calendar.MONTH, 1);
        cv1.freshContentData(currentCalender.get(Calendar.YEAR), currentCalender.get(Calendar.MONTH));
        cv1.setTag(currentCalender.getTime());
        currentCalender.add(Calendar.MONTH, 1);
        cv2.freshContentData(currentCalender.get(Calendar.YEAR), currentCalender.get(Calendar.MONTH));
        cv2.setTag(currentCalender.getTime());
    }

    int width, height;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
        Log.v("onLayout", "w -> " + width);
        Log.v("onLayout", "h -> " + height);
        if (changed) {
            autoScrollOffset = width / 2;
            cv0.getLayoutParams().width = width;
            cv1.getLayoutParams().width = width;
            cv2.getLayoutParams().width = width;
            reInitContent(current, 0);
        }
    }

    boolean moving = false;


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();//获得VelocityTracker类实例
        }
        if (autoMoving) {
            return true;
        }
        mVelocityTracker.addMovement(event);//将事件加入到VelocityTracker类实例中
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                distance = 0;
                downX = event.getX();
                downY = event.getY();
            case MotionEvent.ACTION_MOVE:
                if (Math.sqrt((event.getX() - downX) * (event.getX() - downX) + (event.getY() - downY) * (event.getY() - downY)) > 5) {
                    moving = true;
                    return true;
                }
                break;
            default:
                break;
        }
        return super.onInterceptTouchEvent(event);
    }

    boolean autoMoving = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (autoMoving) {
            return true;
        }
        mVelocityTracker.addMovement(event);//将事件加入到VelocityTracker类实例中

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                distance = event.getX() - downX;
                moveContent(distance);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1, 8f);
                //                Log.e("mVelocityTracker", "x: " + mVelocityTracker.getXVelocity() + ", y: " + mVelocityTracker.getYVelocity());
                int direction = 0;
                if (Math.abs(distance) > autoScrollOffset) {
                    direction = distance > 0 ? -1 : 1;
                    current += direction;
                } else {
                    if (Math.abs(mVelocityTracker.getXVelocity()) > 1.5) {
                        direction = distance > 0 ? -1 : 1;
                        current += direction;
                    }
                }
                if (current > 2)
                    current = 0;
                else if (current < 0)
                    current = 2;
                changePosition(direction);

                releaseVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    float downX, downY;
    float distance;


    private void moveContent(float offset) {
        cv0.setTranslationX((int) (m0 + offset));
        cv1.setTranslationX((int) (m1 + offset));
        cv2.setTranslationX((int) (m2 + offset));
    }


    int m0, m1, m2;
    int autoScrollLeft;
    int autoScrollOffset;

    private void changePosition(final int direction) {
        if (direction > 0)
            autoScrollLeft = -(int) (width - Math.abs(distance));
        else if (direction < 0)
            autoScrollLeft = (int) (width - Math.abs(distance));
        else
            autoScrollLeft = -(int) distance;
        ObjectAnimator animator0, animator1, animator2;
        AnimatorSet set = new AnimatorSet();
        animator0 = ObjectAnimator.ofFloat(cv0, "translationX", cv0.getTranslationX(), cv0.getTranslationX() + autoScrollLeft);
        animator1 = ObjectAnimator.ofFloat(cv1, "translationX", cv1.getTranslationX(), cv1.getTranslationX() + autoScrollLeft);
        animator2 = ObjectAnimator.ofFloat(cv2, "translationX", cv2.getTranslationX(), cv2.getTranslationX() + autoScrollLeft);
        set.setDuration(Math.abs(autoScrollLeft / autoScrollOffset) * 200);
        set.setInterpolator(new LinearInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                reInitContent(current, direction);
                moving = false;
                autoMoving = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.playTogether(animator0, animator1, animator2);
        set.start();
        autoMoving = true;
    }

    Date currentMonth;

    private void reInitContent(int currentPage, int diriction) {
        if (currentPage == 0) {
            m0 = 0;
            m1 = width;
            m2 = -width;
            currentMonth = (Date) cv0.getTag();
            if (diriction > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, 1);
                cv1.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv1.setTag(c.getTime());
            } else if (diriction < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, -1);
                cv2.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv2.setTag(c.getTime());
            }
        } else if (currentPage == 1) {
            m0 = -width;
            m1 = 0;
            m2 = width;
            currentMonth = (Date) cv1.getTag();
            if (diriction > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, 1);
                cv2.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv2.setTag(c.getTime());
            } else if (diriction < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, -1);
                cv0.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv0.setTag(c.getTime());
            }
        } else if (currentPage == 2) {
            m0 = width;
            m1 = -width;
            m2 = 0;
            currentMonth = (Date) cv2.getTag();
            if (diriction > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, 1);
                cv0.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv0.setTag(c.getTime());
            } else if (diriction < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, -1);
                cv1.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv1.setTag(c.getTime());
            }
        }
        cv0.setTranslationX(m0);
        cv1.setTranslationX(m1);
        cv2.setTranslationX(m2);
        if (null != monthChangeListener) {
            monthChangeListener.currentMonth(currentMonth);
        }
    }

    OnMonthChangeListener monthChangeListener;

    public OnMonthChangeListener getMonthChangeListener() {
        return monthChangeListener;
    }

    public void setMonthChangeListener(OnMonthChangeListener monthChangeListener) {
        this.monthChangeListener = monthChangeListener;
    }
}
