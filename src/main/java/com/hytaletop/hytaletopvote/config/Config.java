package com.hytaletop.hytaletopvote.config;

import com.hypixel.hytale.server.core.inventory.ItemStack;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("null")
public class Config {

    public String secretKey = "your-secret-key-here";
    public boolean isPrincipalNetwork = true;
    public String secondaryId = "";
    public String errorSecretNotValidMessage = "The secret key is not valid";

    public boolean enableKeyForVoteClaimMenu = true;
    public boolean openMenuOnClaim = true;
    public boolean isClaimEnabled = true;
    public boolean disableClaimMessage = false;
    public String voteClaimMessage = "You claimed the reward! Thanks you for vote";
    public String fullInventory = "Your invenotry is full, free before request the rewards.";
    public String voteNotFoundMessage = "You are never voted {server_name}, please vote on {server_link}.";
    public String voteTimeToWaitMessage = "You are already voted this server. Please wait {time} for new vote.";
    public String rewardUpdateMessage = "<green> The reward for the player are updated! </green>.";

    public record RewardItem(String itemId, int quantity) {}

    public List<RewardItem> rewardItems = new ArrayList<>();

    public void addReward(ItemStack itemStack) {
        rewardItems.add(new RewardItem(itemStack.getItemId(), itemStack.getQuantity()));
    }

    public void clearRewards() {
        rewardItems.clear();
    }

    public ItemStack[] getStacks() {
        return rewardItems.stream()
                .map(r -> new ItemStack(r.itemId(), r.quantity()))
                .toArray(ItemStack[]::new);
    }

    public void printRewards() {
        for (RewardItem item : rewardItems) {
            System.out.printf("  - %s × %d%n", item.itemId(), item.quantity());
        }
    }
    
}