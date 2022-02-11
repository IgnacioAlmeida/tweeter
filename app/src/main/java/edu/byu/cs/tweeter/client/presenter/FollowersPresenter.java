package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FollowersPresenter extends PagedPresenterUser {
    private FollowService followService;

    public FollowersPresenter(View view) {
        super(view);
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingStatus(true);
            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollowerFollowee, new GetFollowersObserver());
        }
    }

    public class GetFollowersObserver implements PagedObserver<User> {

        @Override
        public void handleSuccess(List list, boolean hasMorePages) {
            presenterHandleSuccess(list, hasMorePages);
        }

        @Override
        public void handleFailure(String message) {
            String failurePrefix =  "Failed to get followers: ";
            view.displayErrorMessage(failurePrefix + message);
        }

        @Override
        public void handleException(Exception exception) {
            String exceptionPrefix = "Failed to get followers because of exception: ";
            view.displayErrorMessage(exceptionPrefix + exception.getMessage());
        }


    }
}

