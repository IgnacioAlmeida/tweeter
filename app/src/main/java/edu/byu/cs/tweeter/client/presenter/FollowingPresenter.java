package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenterUser{
    private FollowService followService;

    private User lastFollowee;


    public FollowingPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followService.getFollowing(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());
        }

    }

    public class GetFollowingObserver implements PagedObserver<User> {

        @Override
        public void handleSuccess(List list, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastFollowee = (list.size() > 0) ? (User) list.get(list.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.successfulAdd(list);
        }

        @Override
        public void handleFailure(String message) {
            String failurePrefix =  "Failed to get following: ";
            view.displayErrorMessage(failurePrefix + message);
        }

        @Override
        public void handleException(Exception exception) {
            String exceptionPrefix = "Failed to get following because of exception: ";
            view.displayErrorMessage(exceptionPrefix + exception.getMessage());
        }
    }

}
