package com.booleanuk.extension;

import com.booleanuk.core.Basket;
import com.booleanuk.core.Item;

import java.time.LocalDateTime;

public class Receipt {
    private String receipt;
    private LocalDateTime dateAndTime;
    private Basket basket;

    public Receipt(Basket basket) {
        this.basket = basket;
        this.dateAndTime = LocalDateTime.now();
    }

    public String generateReceipt() {
        if (basket.getBasketContent().isEmpty()) {
            return "Basket is empty.";
        }
        StringBuilder receipt = new StringBuilder();
        receipt.append(createHeader());
        for (Item item : basket.getBasketContent().keySet()) {
            String itemRow = item.getSku() + "\n";
            receipt.append(itemRow);
        }
        receipt.append(createFooter());
        this.receipt = receipt.toString();
        return receipt.toString();
    }

    public String createHeader() {
        return "Header\n";
    }

    public String createFooter() {
        return "Footer";
    }

    public boolean printReceipt() {
        return false;
    }
}
