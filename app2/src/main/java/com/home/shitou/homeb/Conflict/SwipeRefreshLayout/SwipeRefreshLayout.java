package com.home.shitou.homeb.Conflict.SwipeRefreshLayout;

/**
 * Created by Administrator on 2016/8/9.
 */
public class SwipeRefreshLayout {

//    SwipeRefreshLayout与ViewPager嵌套滑动冲突-----，
// 只需要在ViewPager滑动的时候禁用了SwipeRefreshLayout就可以了
    /*
    ViewPager mViewPager;
    SwipeRefreshLayout mRefreshLayout;
    ...
            mViewPager.setOnTouchListener(new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:// 经测试，ViewPager的DOWN事件不会被分发下来
                case MotionEvent.ACTION_MOVE:
                    mRefreshLayout.setEnabled(false);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    mRefreshLayout.setEnabled(true);
                    break;
            }
            return false;
        }
    });

   */

   /*
    android-Ultra-Pull-To-Refresh与ViewPager嵌套滑动冲突

    　　当我们不亦乐乎的在项目中使用android-Ultra-Pull-To-Refresh时，当我们用android-Ultra-Pull-To-Refresh和ViewPager互相嵌套的时候猛然发现，二者居然有滑动冲突，我们在框架主页的issues中也能看到这个问题，但是一直没有被解决，心中上万只草泥马奔腾而过，这时候达哥我就要出来来拯救世界了，看我们如何来解决这个问题。
            　　我们在android-Ultra-Pull-To-Refresh的源码托管ReadMe的末尾发现work with ViewPager: disableWhenHorizontalMove()，翻译过来就是和ViewPager一起使用，但是我们调用了这个方法后发现然并卵，那么问题在哪里呢？
            　　达哥阅读了android-Ultra-Pull-To-Refresh的PtrFramLayout源码后发现修改了这个bug，这里直接提供修改方法，我们打开PtrFramLayout.java这个类，找到308行代码：

            if (mDisableWhenHorizontalMove && !mPreventForHorizontal && (Math.abs(offsetX) > mPagingTouchSlop && Math.abs(offsetX) > Math.abs(offsetY))) {
        if (mPtrIndicator.isInStartPosition()) {
            mPreventForHorizontal = true;
        }
    }

            　　把上述代码的if判断的Math.abs(offsetX) > mPagingTouchSlop这一句去掉就可以了，完整代码如下：

            if (mDisableWhenHorizontalMove && !mPreventForHorizontal && (Math.abs(offsetX) > mPagingTouchSlop && Math.abs(offsetX) > Math.abs(offsetY))) {
        if (mPtrIndicator.isInStartPosition()) {
            mPreventForHorizontal = true;
        }
        　　原因是，我们既然要禁用横向滑动的拦截，那么判断操作为横向并且要禁用横向拦截时给mPreventForHorizontal赋值为true即可，并不需要判断滑动距离。
        同时PtrFramLayout的第113、114行代码就无用了，可以注释了，第54行mPagingTouchSlop成员变量也无用，可以注释了。
        使用的同学请注意还是需要调用PtrFrameLayout.disableWhenHorizontalMove(true)来灵活控制是否需要拦截。
        */
}
