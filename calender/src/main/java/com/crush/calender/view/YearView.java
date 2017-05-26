package com.crush.calender.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.crush.calender.OnCalendarStateChangeListener;
import com.crush.calender.R;
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
    TextView tvTitle0, tvTitle1, tvTitle2, tvTitle3, tvTitle4, tvTitle5, tvTitle6;
    Date checkedDate;
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

    private void initWeekTitle(boolean firstDayOfWeekIsSun) {
        if (firstDayOfWeekIsSun) {
            tvTitle0.setText("日");
            tvTitle1.setText("一");
            tvTitle2.setText("二");
            tvTitle3.setText("三");
            tvTitle4.setText("四");
            tvTitle5.setText("五");
            tvTitle6.setText("六");
        } else {
            tvTitle0.setText("一");
            tvTitle1.setText("二");
            tvTitle2.setText("三");
            tvTitle3.setText("四");
            tvTitle4.setText("五");
            tvTitle5.setText("六");
            tvTitle6.setText("日");
        }
    }

    Calendar currentCalender = Calendar.getInstance();


    public boolean isFirstDayOfWeekIsSun() {
        return cv0.isFirstDayOfWeekIsSun();
    }

    public void setFirstDayOfWeekIsSun(boolean firstDayOfWeekIsSun) {
        cv0.setFirstDayOfWeekIsSun(firstDayOfWeekIsSun);
        cv1.setFirstDayOfWeekIsSun(firstDayOfWeekIsSun);
        cv2.setFirstDayOfWeekIsSun(firstDayOfWeekIsSun);
        initWeekTitle(firstDayOfWeekIsSun);
    }

    public void initView() {
        this.context = getContext();
        LayoutInflater.from(getContext()).inflate(R.layout.cus_year_view, this, true);
        tvTitle0 = (TextView) findViewById(R.id.tv_title_0);
        tvTitle1 = (TextView) findViewById(R.id.tv_title_1);
        tvTitle2 = (TextView) findViewById(R.id.tv_title_2);
        tvTitle3 = (TextView) findViewById(R.id.tv_title_3);
        tvTitle4 = (TextView) findViewById(R.id.tv_title_4);
        tvTitle5 = (TextView) findViewById(R.id.tv_title_5);
        tvTitle6 = (TextView) findViewById(R.id.tv_title_6);
        cv0 = (MonthView) findViewById(R.id.cv0);
        cv1 = (MonthView) findViewById(R.id.cv1);
        cv2 = (MonthView) findViewById(R.id.cv2);
        cv0.setOnDateCheckedListener(onDateCheckedListener);
        cv1.setOnDateCheckedListener(onDateCheckedListener);
        cv2.setOnDateCheckedListener(onDateCheckedListener);
        initWeekTitle(true);
        freshData();
    }

    OnDateCheckedListener onDateCheckedListener = new OnDateCheckedListener() {
        @Override
        public void onDateChange(Date date) {
            checkedDate = date;
            cv0.chooseDate(date);
            cv1.chooseDate(date);
            cv2.chooseDate(date);
            if (null != cusCalenderListener) {
                cusCalenderListener.onDateSelect(date);
            }
        }
    };

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
//        Log.v("onLayout", "w -> " + width);
//        Log.v("onLayout", "h -> " + height);
        if (changed) {
            autoScrollOffset = width / 2;
            cv0.getLayoutParams().width = width;
            cv1.getLayoutParams().width = width;
            cv2.getLayoutParams().width = width;
            reInitContent(0);
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
                        direction = mVelocityTracker.getXVelocity() > 0 ? -1 : 1;
                        current += direction;
                    }
                }
                fixPosition();
                changePosition(direction);

                releaseVelocityTracker();
                break;
            default:
                break;
        }
        return true;
    }

    private void fixPosition() {
        if (current > 2)
            current = 0;
        else if (current < 0)
            current = 2;
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

    public void lastMonth() {
        forceStopAnim();
        --current;
        fixPosition();
        changePosition(-1);
    }

    public void nextMonth() {
        forceStopAnim();
        ++current;
        fixPosition();
        changePosition(1);
    }

    public void setDate(Date date) {
        forceStopAnim();
        this.currentMonth = date;
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.add(Calendar.MONTH, -1);
        cv0.setTranslationX(-width);
        cv1.setTranslationX(0);
        cv2.setTranslationX(width);
        current = 1;
        cv0.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        cv0.setTag(c.getTime());
        c.add(Calendar.MONTH, 1);
        cv1.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        cv1.setTag(c.getTime());
        c.add(Calendar.MONTH, 1);
        cv2.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        cv2.setTag(c.getTime());
        if (null != monthChangeListener) {
            monthChangeListener.currentMonth(currentMonth);
        }
    }

    private void forceStopAnim() {
        if (null != set && set.isStarted()) {
            set.end();
        }
    }

    AnimatorSet set;

    private void changePosition(final int direction) {
        forceStopAnim();
        if (direction > 0)
            autoScrollLeft = -(int) (width - Math.abs(distance));
        else if (direction < 0)
            autoScrollLeft = (int) (width - Math.abs(distance));
        else
            autoScrollLeft = -(int) distance;
        set = new AnimatorSet();
        set.setDuration(Math.abs(autoScrollLeft / autoScrollOffset) * 200);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                reInitContent(direction);
                moving = false;
                autoMoving = false;
                distance = 0;
                set = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.playTogether(
                ObjectAnimator.ofFloat(cv0, "translationX", cv0.getTranslationX(), cv0.getTranslationX() + autoScrollLeft),
                ObjectAnimator.ofFloat(cv1, "translationX", cv1.getTranslationX(), cv1.getTranslationX() + autoScrollLeft),
                ObjectAnimator.ofFloat(cv2, "translationX", cv2.getTranslationX(), cv2.getTranslationX() + autoScrollLeft)
        );
        set.start();
        autoMoving = true;
    }

    Date currentMonth;

    private void reInitContent(int direction) {
        if (current == 0) {
            m0 = 0;
            m1 = width;
            m2 = -width;
            currentMonth = (Date) cv0.getTag();
            if (direction > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, 1);
                cv1.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv1.setTag(c.getTime());
            } else if (direction < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, -1);
                cv2.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv2.setTag(c.getTime());
            }
        } else if (current == 1) {
            m0 = -width;
            m1 = 0;
            m2 = width;
            currentMonth = (Date) cv1.getTag();
            if (direction > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, 1);
                cv2.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv2.setTag(c.getTime());
            } else if (direction < 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, -1);
                cv0.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv0.setTag(c.getTime());
            }
        } else if (current == 2) {
            m0 = width;
            m1 = -width;
            m2 = 0;
            currentMonth = (Date) cv2.getTag();
            if (direction > 0) {
                Calendar c = Calendar.getInstance();
                c.setTime(currentMonth);
                c.add(Calendar.MONTH, 1);
                cv0.freshContentData(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
                cv0.setTag(c.getTime());
            } else if (direction < 0) {
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
        cv0.chooseDate(checkedDate);
        cv1.chooseDate(checkedDate);
        cv2.chooseDate(checkedDate);
        if (null != monthChangeListener && 0 != direction) {
            monthChangeListener.currentMonth(currentMonth);
        }
        if (null != cusCalenderListener && 0 != direction) {
            Calendar c = Calendar.getInstance();
            c.setTime(currentMonth);
            cusCalenderListener.onMonthChange(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
        }
    }

    OnMonthChangeListener monthChangeListener;

    public OnMonthChangeListener getMonthChangeListener() {
        return monthChangeListener;
    }

    public void setMonthChangeListener(OnMonthChangeListener monthChangeListener) {
        this.monthChangeListener = monthChangeListener;
    }

    public void showLunar() {
        cv0.showLunar(current == 0);
        cv1.showLunar(current == 1);
        cv2.showLunar(current == 2);
    }

    public void goneLunar() {
        cv0.goneLunar(current == 0);
        cv1.goneLunar(current == 1);
        cv2.goneLunar(current == 2);
    }

    OnCalendarStateChangeListener cusCalenderListener;

    public OnCalendarStateChangeListener getCusCalenderListener() {
        return cusCalenderListener;
    }

    public void setCusCalenderListener(OnCalendarStateChangeListener cusCalenderListener) {
        this.cusCalenderListener = cusCalenderListener;
    }

}
