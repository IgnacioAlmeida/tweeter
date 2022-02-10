package edu.byu.cs.tweeter.client.presenter;

public interface ServiceObserver {
    void handleFailure(String failurePrefix, String message, boolean isLoading);
    void handleException(String exceptionPrefix, Exception ex, boolean isLoading);
}
