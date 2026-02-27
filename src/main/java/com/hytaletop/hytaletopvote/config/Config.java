package com.hytaletop.hytaletopvote.config;

import com.hypixel.hytale.server.core.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public String secret_key = "303c547d-your-4bd4-key-73588f6f4a59";
    public boolean is_principal_network = true;
    public String secondary_id  = "survivol-01-identificator";
    
    public boolean enable_key_for_vote_claim_menu = true;
    public boolean open_menu_on_claim = true;
    public boolean is_claim_enabled = true;    
    public boolean disable_claim_message = false;    
    public String vote_claim_message = "You claimed the reward! Thanks you for vote";
    public String vote_not_found_message = "You are never voted {server_name}, please vote on {server_link}.";
    public String vote_time_to_wait_message = "You are alreay vote this server. pls wait {time} for new vote.";

    private final Map<String, Integer> rewardItems;

    public Config() {
        this.rewardItems = new HashMap<>();
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