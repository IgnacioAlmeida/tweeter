package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;

public class PagedNotificationHandler extends PagedTaskHandler<PagedObserver> {

    public PagedNotificationHandler(PagedObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver observer, List<PagedObserver> list, boolean hasMorePages) {
        observer.handleSuccess(list, hasMorePages);
    }
}
