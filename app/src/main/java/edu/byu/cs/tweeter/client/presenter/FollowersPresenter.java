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
            followService.getFollowers(Cache.getInstance().getCurrUserAuthToken(), user, PAGE_SIZE, lastFollower, new GetFollowersObserver());
        }
    }

    public class GetFollowersObserver implements PagedObserver<User> {

        @Override
        public void handleFailure(String message) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            isLoading = false;
            view.setLoadingStatus(false);
            view.displayErrorMessage("Failed to get followers because of exception: " + exception.getMessage());
        }

        @Override
        public void handleSuccess(List list, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingStatus(false);
            lastFollower = (list.size() > 0) ? (User) list.get(list.size() - 1) : null;
            setHasMorePages(hasMorePages);
            view.successfulAdd(list);
        }
    }
}

