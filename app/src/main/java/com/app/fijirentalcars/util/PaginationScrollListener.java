package com.app.fijirentalcars.util;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {

    LinearLayoutManager layoutManager;

    public PaginationScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        if(!recyclerView.canScrollVertically(1)){
            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition;
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            int indexOfLastItemViewVisible = layoutManager.getChildCount() -1;
            View lastItemViewVisible = layoutManager.getChildAt(indexOfLastItemViewVisible);
            int adapterPosition = layoutManager.getPosition(lastItemViewVisible);

            Log.e("TAGS","first "+layoutManager.findFirstVisibleItemPosition()+" fc "+adapterPosition+" last "+layoutManager.findLastVisibleItemPosition()+" ls "+layoutManager.findLastCompletelyVisibleItemPosition());

            Log.e("TAGS","pos visi "+visibleItemCount+" fiv "+firstVisibleItemPosition+" tot "+totalItemCount+" isload "+isLoading()+" islas "+isLastPage());

            if (!isLoading() && !isLastPage()) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    loadMoreItems();
                }
            }
        }
    }

    protected abstract void loadMoreItems();

    public abstract int getTotalPageCount();

    public abstract boolean isLastPage();

    public abstract boolean isLoading();
}