package com.booleanuk.extension;

import com.booleanuk.core.*;

import java.time.LocalDateTime;
import java.util.HashMap;

public class Receipt {
    private String receipt;
    private LocalDateTime dateAndTime;
    private Basket basket;

    public Receipt(Basket basket) {
        this.basket = basket;
        this.dateAndTime = LocalDateTime.now();
        this.dateAndTime = this.dateAndTime.minusNanos(this.dateAndTime.getNano());
        this.receipt = "";
    }

    public String generateReceipt() {
        if (basket.getBasketContent().isEmpty()) {
            return "Basket is empty.";
        }
        StringBuilder receipt = new StringBuilder();
        receipt.append(createHeader());
        for (Item item : basket.getBasketContent().keySet()) {
            // Assumption, fillings can only be bought on a bagel, and will be added to receipt further down.
            if (item.getClass() == Filling.class) {continue;}

            // To make the printed receipt more clear.
            String tabsAfterItem;
            int variantLength = item.getVariant().length() + item.getName().length() + 1;
            if (variantLength < 12){tabsAfterItem = "\t\t\t\t";}
            else if (variantLength < 16){tabsAfterItem = "\t\t\t";}
            else if (variantLength < 20){tabsAfterItem = "\t\t";}
            else {tabsAfterItem = "\t";}

            int amount = basket.getBasketContent().get(item);
            String itemRow = item.getVariant() + " " + item.getName() + tabsAfterItem +  amount + "\t£" + item.getPrice() * amount + "\n";

            // Check for fillings to bagel.
            if(item.getClass() == Bagel.class) {
                if (!((Bagel) item).getFillings().isEmpty()) {
                    StringBuilder fillingsRows = new StringBuilder();
                    for (Filling filling : ((Bagel) item).getFillings()) {
                        String tabs;
                        int fillingLength = filling.getVariant().length();
                        if (fillingLength < 6){tabs = "\t\t\t\t";}
                        else if (fillingLength < 10) {tabs = "\t\t\t";}
                        else {tabs = "\t\t";}
                        String fillingRow = "+ " + filling.getVariant() + " Filling" + tabs + "£" + filling.getPrice() * amount + "\n";
                        fillingsRows.append(fillingRow);
                    }
                    itemRow += fillingsRows.toString();
                }
            }

            receipt.append(itemRow);
        }
        receipt.append("\n---------------------------------\nTotal                       £") ;
        receipt.append(basket.totalCost());
        receipt.append(createFooter());
        this.receipt = receipt.toString();
        return receipt.toString();
    }

    public String createHeader() {
        String header = "";
        header += "      ~~~ Bob's Bagels ~~~\n\n       ";
        header += this.dateAndTime.toString().replace("T", " ");
        header += "\n\n---------------------------------\n\n";
        return header;
    }

    public String createFooter() {
        return "\n\n           Thank you\n        for your order!";
    }

    public boolean printReceipt() {
        if (this.receipt.isEmpty()) {
            return false;
        }
        System.out.println(this.receipt);
        return true;
    }

    public LocalDateTime getDateAndTime() {
        return this.dateAndTime;
    }

    public String generateReceiptWithDiscount() {
        if (basket.getBasketContent().isEmpty()) {
            return "Basket is empty.";
        }
        StringBuilder receipt = new StringBuilder();
        receipt.append(createHeader());

        HashMap<Item, int[]> mapPriceAndSavings = this.basket.discountPerItem();
        double totalSavings = 0.0;
        for (Item item : basket.getBasketContent().keySet()) {
            // Assumption, fillings can only be bought on a bagel.
            if (item.getClass() == Filling.class) {continue;}

            String tabsAfterItem;
            int variantLength = item.getVariant().length() + item.getName().length() + 1;
            if (variantLength < 12){tabsAfterItem = "\t\t\t\t";}
            else if (variantLength < 16){tabsAfterItem = "\t\t\t";}
            else if (variantLength < 20){tabsAfterItem = "\t\t";}
            else {tabsAfterItem = "\t";}

            int amount = basket.getBasketContent().get(item);
            String itemRow = item.getVariant() + " " + item.getName() + tabsAfterItem +  amount + "\t£" + mapPriceAndSavings.get(item)[0]/100.0 + "\n";

            // Check for fillings to bagel.
            if(item.getClass() == Bagel.class) {
                if (!((Bagel) item).getFillings().isEmpty()) {
                    StringBuilder fillingsRows = new StringBuilder();
                    for (Filling filling : ((Bagel) item).getFillings()) {
                        String tabs;
                        int fillingLength = filling.getVariant().length();
                        if (fillingLength < 6){tabs = "\t\t\t\t";}
                        else if (fillingLength < 10) {tabs = "\t\t\t";}
                        else {tabs = "\t\t";}
                        String fillingRow = "+ " + filling.getVariant() + " Filling" + tabs + "£" + filling.getPrice() * amount + "\n";
                        fillingsRows.append(fillingRow);
                    }
                    itemRow += fillingsRows.toString();
                }
            }

            // Check if there are any savings.
            double saving = mapPriceAndSavings.get(item)[1]/100.0;
            if (saving != 0.0){
                itemRow += "                          (-£" + saving + ")\n";
                totalSavings += saving;
            }

            // Add all information about item to the receipt.
            receipt.append(itemRow);
        }
        receipt.append("\n---------------------------------\nTotal                       £") ;
        receipt.append(basket.totalCostWithDiscount(mapPriceAndSavings));
        receipt.append("\n\n   You saved a total of £");
        receipt.append(totalSavings);
        receipt.append("\n       on this purchase.");
        receipt.append(createFooter());
        this.receipt = receipt.toString();
        return receipt.toString();
    }
}
