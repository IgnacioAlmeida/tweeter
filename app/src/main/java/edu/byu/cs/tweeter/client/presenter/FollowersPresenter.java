package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.User;

import java.util.List;

public class FollowersPresenter extends PagedPresenterUser {

    public FollowersPresenter(View view) {
        super(view);
        isInFollowingPresenter = false;
    }
}

