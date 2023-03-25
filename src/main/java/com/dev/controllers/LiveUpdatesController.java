package com.dev.controllers;


import com.dev.models.NotificationModel;
import com.dev.objects.User;
import com.dev.persists.Persist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;


@RestController
public class LiveUpdatesController {

    @Autowired
    private Persist persist;

    private Map<Integer, SseEmitter> emitterMap = new HashMap<>();


    @RequestMapping(value = "/stream", method = RequestMethod.GET)
    public SseEmitter streamUpdates(int userId) {
        SseEmitter emitter = null;
        User seller = persist.getUserById(userId);
        if(seller != null) {
            emitter = new SseEmitter();
            emitterMap.put(userId, emitter);
        }


        return emitter;
    }

    @RequestMapping(value = "/remove-sse",method = RequestMethod.GET)
    public void removeSSE(int userId) {
        SseEmitter sellerEmitter = this.emitterMap.get(userId);
        if (sellerEmitter != null) {
            this.emitterMap.remove(userId);
        }

    }



    public void notifyAllAdmins(int eventCode) {
        List<User> allUsers = persist.getAllUsers();
        for (User user : allUsers) {
            if (user.isAdmin()) {
                int adminId = user.getId();
                SseEmitter sseEmitter = this.emitterMap.get(adminId);
                if (sseEmitter != null) {
                    NotificationModel notificationModel = new NotificationModel(eventCode);
                    try {
                        sseEmitter.send(notificationModel);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
    public void notifyOwner(int ownerId, Double newBalance, int eventCode) {
        SseEmitter sellerEmitter = this.emitterMap.get(ownerId);
        if (sellerEmitter != null) {
            try {
                NotificationModel notificationModel = new NotificationModel(eventCode, newBalance, ownerId);
                sellerEmitter.send(notificationModel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void notifyALLUsers(int sellerId, String productName, Double newPrice, int eventCode) {
        emitterMap.forEach((userId, sseEmitter) ->
        {
            try {
                NotificationModel notificationModel = new NotificationModel(productName, newPrice ,eventCode, sellerId/* productId*/);
                sseEmitter.send(notificationModel);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
