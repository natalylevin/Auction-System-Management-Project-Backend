package com.dev.responses;

import com.dev.models.UserModel;
import com.dev.objects.User;

import java.util.ArrayList;
import java.util.List;

public class AllUsersResponse extends BasicResponse {
    private List<UserModel> users;

    public AllUsersResponse(List<User> users) {
        this.users = new ArrayList<>();
        for (User user: users) {
            this.users.add(new UserModel(user));
        }
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
