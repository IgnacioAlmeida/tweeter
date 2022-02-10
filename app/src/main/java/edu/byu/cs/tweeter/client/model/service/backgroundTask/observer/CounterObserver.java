package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

import java.util.List;

public interface CounterObserver extends ServiceObserver{
    void handleSuccess(int count);

}
