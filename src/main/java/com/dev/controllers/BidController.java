package com.dev.controllers;


import com.dev.objects.Bid;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.persists.Persist;
import com.dev.responses.BasicResponse;
import com.dev.responses.BidResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dev.utils.Constants.*;
import static com.dev.utils.Errors.*;

@RestController
public class BidController {

    @Autowired
    private Persist persist;


    @Autowired
    private LiveUpdatesController liveUpdatesController;



    @RequestMapping(value = "get-all-bids", method = RequestMethod.GET)
    public BasicResponse getAllBids() {
        BasicResponse basicResponse;
        List<Bid> allBids = persist.getAllBids();
        basicResponse = new BidResponse(allBids);

        return basicResponse;

    }



    @RequestMapping(value = "place-bid", method = RequestMethod.POST)
    public BasicResponse placeBid(String token, Integer userId, Integer productId, Double newBid) {
        BasicResponse basicResponse;
        int errorCode;
        boolean success;

        User user = persist.getUserByToken(token);
        if (user != null) {
           if (user.getBalance() >= newBid + BID_FEE) {

               Product product = persist.getProductById(productId);
               if (product != null) {
                   if (product.getSeller().getId() != userId) {
                       if (product.getMinPrice() >= newBid) {
                           success = false;
                           errorCode = INCORRECT_BID;
                       } else {
                           persist.updateProductMinPrice(productId, newBid);
                           Bid bid = new Bid(user, product, new Date(), newBid);
                           persist.addNewBid(bid);
                           liveUpdatesController.notifyALLUsers(product.getSeller().getId(), product.getProductName() ,newBid, NOTIFY_ALL_USERS_BID_PLACED);
                           boolean updated = persist.setNewBalanceById(userId, user.getBalance()- BID_FEE -newBid);
                           boolean allUpdated = false;
                           if (updated) {
                               allUpdated = updateAllBiddersBalance(productId, newBid);
                           }

                           success = updated&&allUpdated;
                           errorCode = (updated&&allUpdated) ? NO_ERROR : TRANSACTION_ERROR;
                       }

                   } else {
                       success = false;
                       errorCode = ERROR_PLACED_BID_FOR_OWN_PRODUCT;
                   }

               }
               else {
                   success = false;
                   errorCode = ERROR_NO_SUCH_PRODUCT;
               }
           }
           else {
               success = false;
               errorCode = NOT_ENOUGH_MONEY_ON_BALANCE;
           }
        }
        else {
            success = false;
            errorCode = ERROR_NO_SUCH_USER;
        }

        basicResponse = new BasicResponse(success, errorCode);

        return basicResponse;
    }


    private boolean updateAllBiddersBalance(int productId, Double newBid) {
        boolean allUpdated = true;
        List<Bid> bidsByProductId = persist.getAllBidsByProductId(productId);
        if (bidsByProductId != null) {
            Set<Integer> userIds = new HashSet<>();
            for (Bid bidByProductId : bidsByProductId) {
                userIds.add(bidByProductId.getUser().getId());
            }

            for (Integer userId : userIds) {
                Double bidToReturn = persist.getSecondHighestBidByUserId(productId, userId, newBid);
                if (bidToReturn != null) {
                    User user = persist.getUserById(userId);
                    Double newBalance = bidToReturn+user.getBalance();
                    allUpdated = persist.setNewBalanceById(userId, newBalance);
                    liveUpdatesController.notifyOwner(userId, newBalance, NOTIFY_BIDDERS);
                    if (!allUpdated){
                        break;
                    }
                }

            }
            persist.updateBidsValueToZero(productId, newBid);
        }
        return allUpdated;

    }




}
