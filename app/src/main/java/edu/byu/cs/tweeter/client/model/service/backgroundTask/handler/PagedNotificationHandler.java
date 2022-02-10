package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedNotificationHandler<T extends ServiceObserver> extends Handler {
    private T observer;

    public PagedNotificationHandler(T observer) {
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
        if (success) {
            List<T> list = (List<T>) msg.getData().getSerializable(PagedTask.ITEMS_KEY);
            boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);
            handleSuccess(msg.getData(), observer, list, hasMorePages);

        } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
            observer.handleFailure(message);
        } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
            observer.handleException(ex);
        }
    }

    protected abstract void handleSuccess(Bundle data, T observer, List<T> list, boolean hasMorePages);

}
