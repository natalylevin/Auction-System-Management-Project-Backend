package com.dev.controllers;

import com.dev.objects.Bid;
import com.dev.objects.Product;
import com.dev.objects.User;
import com.dev.persists.Persist;
import com.dev.responses.BasicResponse;
import com.dev.responses.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.dev.utils.Constants.*;
import static com.dev.utils.Constants.NOTIFY_ALL_USERS_PRODUCT_SOLD;
import static com.dev.utils.Errors.*;

@RestController
public class ProductsController {

    @Autowired
    Persist persist;

    @Autowired
    LiveUpdatesController liveUpdatesController;

    @RequestMapping(value = "get-all-products", method = RequestMethod.GET)
    public BasicResponse getAllProducts() {
        BasicResponse basicResponse;
        List<Product> allProducts = persist.getAllProducts();
        basicResponse = new ProductResponse(allProducts);
        return basicResponse;
    }


    @RequestMapping(value = "add-product", method = RequestMethod.POST)
    public BasicResponse addProduct(String token,
                                    String productName, Double productMinBid,
                                    String productImageURL, String productDescription
    ) {
        BasicResponse basicResponse;
        int errorCode;
        boolean success;

        User fromDB = persist.getUserByToken(token);
        if (fromDB == null) {
            errorCode = ERROR_NO_SUCH_USER;
            success = false;
        }
        else {
            if (fromDB.getBalance() < PRODUCT_FEE) {
                errorCode = NOT_ENOUGH_MONEY_ON_BALANCE;
                success = false;
            }
            else {
                if (token.length() > 255 || productName.length() > 255 ||
                        productImageURL.length() > 255 || productDescription.length() > 255) {
                    errorCode = MAX_STRING_ARGUMENT_LEN;
                    success = false;
                } else {
                    Product product = new Product(productName, productDescription,
                            productImageURL, productMinBid,  fromDB);

                    persist.saveProduct(product);
                    Double oldBalance = fromDB.getBalance();
                    boolean updated = persist.setNewBalanceById(fromDB.getId(), oldBalance- PRODUCT_FEE);
                    success = updated;
                    errorCode = updated ? NO_ERROR : TRANSACTION_ERROR;
                    if (updated) {
                        liveUpdatesController.notifyALLUsers(fromDB.getId(), productName, productMinBid, NOTIFY_ALL_USERS_PRODUCT_ADDED);
                    }
                }
            }

        }
        basicResponse = new BasicResponse(success, errorCode);

        return basicResponse;
    }

    @RequestMapping(value = "sell-product", method = RequestMethod.POST)
    public BasicResponse sellProductByToken(String token, int productId, String productName) {
        BasicResponse basicResponse;
        boolean success;
        int errorCode;

        User seller = persist.getUserByToken(token);
        Bid highestBid = persist.getHighestBid(productId);
        if (highestBid != null) {
            if (seller != null) {
                User buyer = persist.getUserById(highestBid.getUser().getId());
                if (buyer != null) {
                    if (buyer.getId() != seller.getId()) {
                        Double highestBidValue = highestBid.getNewBid();
                        Double sellerNewBalance = seller.getBalance() + (highestBidValue - highestBidValue * 0.05);
                        boolean sellerUpdated = persist.setNewBalanceById(seller.getId(), sellerNewBalance);
                        if (sellerUpdated) {
                            liveUpdatesController.notifyALLUsers(buyer.getId(), productName, highestBidValue, NOTIFY_ALL_USERS_PRODUCT_SOLD);
                            success = true;
                            errorCode = NO_ERROR;
                            persist.sellProductById(productId);

                        } else {
                            success = false;
                            errorCode = TRANSACTION_ERROR;
                        }
                    }
                    else {
                        success = false;
                        errorCode = ERROR_SELLING_TO_YOUR_SELF;
                    }
                }
                else {
                    success = false;
                    errorCode = ERROR_NO_SUCH_BUYER;
                }
            }
            else {
                success = false;
                errorCode = ERROR_NO_SUCH_SELLER;
            }
        }
        else {
            success = false;
            errorCode = ERROR_NO_SUCH_BID;
        }

        basicResponse = new BasicResponse(success, errorCode);
        return basicResponse;
    }

}
