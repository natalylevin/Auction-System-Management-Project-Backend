package com.dev.responses;

import com.dev.models.BidModel;
import com.dev.objects.Bid;

import java.util.ArrayList;
import java.util.List;

public class BidResponse extends BasicResponse{
    List<BidModel> bidModels;

    public BidResponse(List<Bid> bids) {
        bidModels = new ArrayList<>();
        for (Bid bid: bids) {
            this.bidModels.add(new BidModel(bid));
        }

    }

    public List<BidModel> getBidModels() {
        return bidModels;
    }

    public void setBidModels(List<BidModel> bidModels) {
        this.bidModels = bidModels;
    }
}
