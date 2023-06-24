package com.example.instagramclone.Models;

import java.util.ArrayList;

public class StoryModel {

    private String StoryBy;
    private long storyAt;

    ArrayList<UserStories>userStories;

    public StoryModel(String storyBy, long storyAt, ArrayList<UserStories> userStories) {
        StoryBy = storyBy;
        this.storyAt = storyAt;
        this.userStories = userStories;
    }

    public StoryModel() {
    }

    public String getStoryBy() {
        return StoryBy;
    }

    public void setStoryBy(String storyBy) {
        StoryBy = storyBy;
    }

    public long getStoryAt() {
        return storyAt;
    }

    public void setStoryAt(long storyAt) {
        this.storyAt = storyAt;
    }

    public ArrayList<UserStories> getUserStories() {
        return userStories;
    }

    public void setUserStories(ArrayList<UserStories> userStories) {
        this.userStories = userStories;
    }
}
