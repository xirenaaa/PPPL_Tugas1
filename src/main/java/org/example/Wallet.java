package org.example;

import java.util.ArrayList;
import java.util.List;

public class Wallet {

    private String ownerName;
    private Owner owner;
    private final List<String> cards = new ArrayList<>();
    private final List<Integer> cashList = new ArrayList<>();

    // --- Owner ---

    public void setOwner(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwner() {
        return ownerName;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
        this.ownerName = owner.getName();
    }

    public Owner getOwnerObject() {
        return owner;
    }

    // --- Card ---

    public void addCard(String card) {
        if (card != null && !card.isEmpty()) {
            cards.add(card);
        }
    }

    public String takeCard(String card) {
        if (cards.remove(card)) {
            return card;
        }
        return null;
    }

    public List<String> getCards() {
        return new ArrayList<>(cards);
    }

    // --- Cash ---

    public void addCash(int amount) {
        if (amount > 0) {
            cashList.add(amount);
        }
    }

    public Integer takeCash(int amount) {
        if (cashList.remove(Integer.valueOf(amount))) {
            return amount;
        }
        return null;
    }

    public void withdrawCash(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Jumlah withdraw harus lebih dari 0");
        }
        int total = getTotalCash();
        if (amount > total) {
            throw new InsufficientFundsException("Saldo tidak cukup");
        }
        int remaining = amount;
        List<Integer> toRemove = new ArrayList<>();
        for (int cash : cashList) {
            if (remaining <= 0) break;
            toRemove.add(cash);
            remaining -= cash;
        }
        cashList.removeAll(toRemove);
        int change = -remaining;
        if (change > 0) {
            cashList.add(change);
        }
    }

    public List<Integer> getCashList() {
        return new ArrayList<>(cashList);
    }

    public int getTotalCash() {
        return cashList.stream().mapToInt(Integer::intValue).sum();
    }
}
