package com.example.taras.forumclient.Model;

/**
 * Created by User on 10.12.2016.
 */

public class Profile {
    private User user;
    private String token;


    public User getUser() {
        return user;
    }



    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public void setUser(User user) {
        this.user = user;
    }

    public Profile(String token, User user){
        this.user = user;
        this.token = token;
    }
}
