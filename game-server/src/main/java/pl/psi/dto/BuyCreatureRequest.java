package pl.psi.dto;

import lombok.Data;

@Data
public class BuyCreatureRequest {
    private String stats;
    private int amount;
    private int goldCost;
}
