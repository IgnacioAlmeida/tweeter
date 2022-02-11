package edu.byu.cs.tweeter.client.model.service.backgroundTask.observer;

public interface CounterObserver extends ServiceObserver{
    void handleSuccess(int count);

}
