package com.hytaletop.hytaletopvote.config;

import com.hypixel.hytale.server.core.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RewardConfig {
    private String rewardMessage = "Congratulations Reward Claimed!";
    private final Map<String, Integer> rewardItems;

    public RewardConfig() {
        this.rewardItems = new HashMap<>();
                
        // Add example
        rewardItems.put("example", 1);
    }

    public void addReward(ItemStack itemStack) {
        String id = itemStack.getItemId();
        int quantity = itemStack.getQuantity();
        rewardItems.merge(id, quantity, Integer::sum);
    }


    public ItemStack[] getStacks() {
        if (rewardItems.isEmpty()) {
            return new ItemStack[0];
        }

        ItemStack[] stacks = new ItemStack[rewardItems.size()];
        int index = 0;

        for (Map.Entry<String, Integer> entry : rewardItems.entrySet()) {
            String itemId = entry.getKey();
            int quantity = entry.getValue();

            stacks[index] = new ItemStack(itemId, quantity);
            index++;
        }

        return stacks;
    }

    public void printRewards() {
        if (rewardItems.isEmpty()) {
            System.out.println("Nessun reward configurato.");
            return;
        }

        System.out.println("Reward configurati:");
        for (Map.Entry<String, Integer> entry : rewardItems.entrySet()) {
            System.out.printf("  - %s × %d%n", entry.getKey(), entry.getValue());
        }
    }

    public void clear() {
        rewardItems.clear();
    }

    public int getRewardCount() {
        return rewardItems.size();
    }
}