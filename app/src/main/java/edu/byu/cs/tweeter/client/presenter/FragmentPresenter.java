package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FragmentPresenter {

    public interface View {
        void displayErrorMessage(String message);
        void handleSuccess(User user);
    }

    private View view;
    private UserService userService;

    public FragmentPresenter(View view) {
        this.view = view;
        userService = new UserService();
    }

    public void loadUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new FragmentPresenter.GetUserObserver());
    }

    public class GetUserObserver implements UserService.GetUserObserver {
        @Override
        public void handleSuccess(User user) {
            view.handleSuccess(user);
        }

        @Override
        public void handleFailure(String message) {
            view.displayErrorMessage("Failed to get user's profile: " + message);

        }

        @Override
        public void handleException(Exception exception) {
            view.displayErrorMessage("Failed to get user's profile because of exception: " + exception.getMessage());
        }
    }
}
