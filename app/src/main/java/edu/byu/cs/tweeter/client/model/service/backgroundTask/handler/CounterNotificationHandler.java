package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.CounterObserver;

public class CounterNotificationHandler extends CounterTaskHandler<CounterObserver> {

    public CounterNotificationHandler(CounterObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, CounterObserver observer, int count) {
        observer.handleSuccess(count);
    }
}
