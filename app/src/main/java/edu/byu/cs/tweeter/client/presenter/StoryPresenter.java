package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StoryPresenter {

    public interface View {
        void handleSuccess(User user);
        void displayErrorMessage(String message);
    }

    private View view;
    private UserService userService;

    public StoryPresenter(View view){
        this.view = view;
        userService = new UserService();
    }

    public void loadUser(String userAlias) {
        userService.getUser(Cache.getInstance().getCurrUserAuthToken(), userAlias, new GetUserObserver());
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
