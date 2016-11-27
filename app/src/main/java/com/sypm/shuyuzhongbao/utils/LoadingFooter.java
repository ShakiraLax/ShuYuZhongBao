package com.sypm.shuyuzhongbao.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 上滑加载更多
 */
public class LoadingFooter extends FrameLayout implements AbsListView.OnScrollListener {

    private static final String TAG = "LoadingFooter";

    private static final int MIN_COUNT = 20;

    OnLoadMoreListener mLoadMoreListener;

    View mViewLoading;
    View mViewEnd;
    View mViewError;

    public enum State {
        Loading, End, Error, Default
    }

    State mCurrentState;

    public LoadingFooter(Context context) {
        super(context);
        init();
    }

    private void init() {
        int height = (int) (getResources().getDisplayMetrics().density * 100.0F);
        setLayoutParams(new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, height));
        int paddingDP = (int) (getResources().getDisplayMetrics().density * 14.0F);
        setPadding(paddingDP, paddingDP, paddingDP, paddingDP);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 屏蔽点击
            }
        });

        mViewLoading = createLoadingView();
        mViewEnd = createEndView();
        mViewError = createErrorView();

        addView(mViewLoading, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        addView(mViewEnd, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        addView(mViewError, new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));

        setLoadState(State.Default);
    }

    private View createErrorView() {
        TextView textView = new TextView(getContext(), null, android.R.attr.textAppearanceMedium);
        textView.setText("出错啦!点击重试");
        return textView;
    }

    private View createEndView() {
        TextView textView = new TextView(getContext(), null, android.R.attr.textAppearanceMedium);
        textView.setText("暂无更多数据");
        return textView;
    }

    private View createLoadingView() {
        ProgressBar progress = new ProgressBar(getContext(), null,
                android.R.attr.progressBarStyleLarge);
        return progress;
    }

    public State getCurrentState() {
        return mCurrentState;
    }

    public void setLoadState(State state) {
        if (mCurrentState == state) {
            return;
        }
        mCurrentState = state;
        switch (state) {
            case Default:
                mViewLoading.setVisibility(View.GONE);
                mViewError.setVisibility(View.GONE);
                mViewEnd.setVisibility(View.GONE);
                setVisibility(View.GONE);
                break;
            case Loading:
                mViewLoading.setVisibility(View.VISIBLE);
                mViewError.setVisibility(View.GONE);
                mViewEnd.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                break;
            case End:
                mViewLoading.setVisibility(View.GONE);
                mViewError.setVisibility(View.GONE);
                mViewEnd.setVisibility(View.VISIBLE);
                setVisibility(View.VISIBLE);
                break;
            case Error:
                mViewLoading.setVisibility(View.GONE);
                mViewError.setVisibility(View.VISIBLE);
                mViewEnd.setVisibility(View.GONE);
                setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.mLoadMoreListener = loadMoreListener;
        if (mLoadMoreListener != null) {
            mViewError.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mLoadMoreListener.onLoadMore();
                }
            });
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view instanceof ListView) {

            ListView listView = (ListView) view;

            if (mCurrentState == State.Loading) {
                return;
            }
            if (mCurrentState == State.End) {
                return;
            }

            if (firstVisibleItem + visibleItemCount >= totalItemCount
                    && totalItemCount != 0
                    && totalItemCount != listView.getHeaderViewsCount()
                    + listView.getFooterViewsCount() && listView.getCount() > 0) {
                if (mLoadMoreListener != null) {
/*                    // start load more when childCount > MIN_COUNT
                    if (listView.getChildCount()<MIN_COUNT){
                        return;
                    }*/
                    mLoadMoreListener.onLoadMore();
                }
            }
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}
