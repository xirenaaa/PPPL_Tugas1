package org.example;

import java.util.ArrayList;
import java.util.List;

public class Wallet {
    private String owner;
    private List<String> cards;
    private List<Integer> cashList;

    public Wallet() {
        this.cards = new ArrayList<>();
        this.cashList = new ArrayList<>();
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwner() {
        return owner;
    }

    public void addCard(String card) {
        if (card != null && !card.isEmpty()) {
            cards.add(card);
        }
    }

    public String takeCard(String card) {
        if (cards.contains(card)) {
            cards.remove(card);
            return card;
        }
        return null;
    }

    public List<String> getCards() {
        return new ArrayList<>(cards);
    }

    public void addCash(int amount) {
        if (amount > 0) {
            cashList.add(amount);
        }
    }

    public Integer takeCash(int amount) {
        if (cashList.contains(amount)) {
            cashList.remove(Integer.valueOf(amount));
            return amount;
        }
        return null;
    }

    public List<Integer> getCashList() {
        return new ArrayList<>(cashList);
    }

    public int getTotalCash() {
        int total = 0;
        for (int cash : cashList) {
            total += cash;
        }
        return total;
    }
}

