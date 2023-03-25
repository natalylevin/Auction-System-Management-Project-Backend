package com.dev.controllers;

import com.dev.objects.User;
import com.dev.persists.Persist;
import com.dev.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.dev.utils.Constants.*;
import static com.dev.utils.Errors.*;

@RestController
public class LoggedUserController {

    @Autowired
    private Persist persist;

    @Autowired
    private LiveUpdatesController liveUpdatesController;




    @RequestMapping(value = "update-balance", method = RequestMethod.POST)
    public BasicResponse updateBalance(String token, int ownerId, Double newBalance) {
        BasicResponse basicResponse;
        boolean updated = false;
        int errorCode;


        User fromDb = persist.getUserByToken(token);
        if (fromDb != null) {
            if (fromDb.isAdmin()) {
                updated = persist.setNewBalanceById(ownerId, newBalance);
                if (!updated) {
                    errorCode = TRANSACTION_ERROR;
                }
                else {
                    liveUpdatesController.notifyOwner(ownerId, newBalance, NOTIFY_OWNER);
                    errorCode = NO_ERROR;
                }
            }
            else {
                errorCode = NOT_ENOUGH_PRIVILEGES;
            }
        }
        else {
            errorCode = ERROR_NO_SUCH_USER;
        }
        basicResponse = new BasicResponse(updated,  errorCode);

        return basicResponse;
    }

    @RequestMapping(value = "get-all-users", method = RequestMethod.POST)
    public BasicResponse getAllUsers(String token) {

        BasicResponse basicResponse;
        User fromDB = persist.getUserByToken(token);
        if (fromDB == null) {
            basicResponse = new BasicResponse(false, ERROR_NO_SUCH_USER);
        }
        else if (!fromDB.isAdmin()) {
            basicResponse = new BasicResponse(false, NOT_ENOUGH_PRIVILEGES);
        }
        else {
            List<User> allUsers = persist.getAllUsers();
            basicResponse = new AllUsersResponse(allUsers);
        }

        return basicResponse;


    }


}
