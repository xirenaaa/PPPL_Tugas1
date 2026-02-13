package org.example;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WalletTest {
    private Wallet wallet;

    @Before
    public void setUp() {
        wallet = new Wallet();
    }

    // ==================== Test setOwner ====================
    @Test
    public void testSetOwner() {
        wallet.setOwner("Budi");
        assertEquals("Budi", wallet.getOwner());
    }

    @Test
    public void testOwnerInitiallyNull() {
        assertNull(wallet.getOwner());
    }

    @Test
    public void testSetOwnerNotNull() {
        wallet.setOwner("Rio");
        assertNotNull(wallet.getOwner());
    }

    // ==================== Test addCard ====================
    @Test
    public void testAddCard() {
        wallet.addCard("KTP");
        assertEquals(1, wallet.getCards().size());
        assertTrue(wallet.getCards().contains("KTP"));
    }

    @Test
    public void testAddMultipleCards() {
        wallet.addCard("KTP");
        wallet.addCard("SIM");
        wallet.addCard("ATM BCA");
        assertEquals(3, wallet.getCards().size());
    }

    @Test
    public void testAddNullCard() {
        wallet.addCard(null);
        assertEquals(0, wallet.getCards().size());
    }

    @Test
    public void testAddEmptyCard() {
        wallet.addCard("");
        assertEquals(0, wallet.getCards().size());
    }

    // ==================== Test takeCard ====================
    @Test
    public void testTakeCard() {
        wallet.addCard("KTP");
        wallet.addCard("SIM");
        String takenCard = wallet.takeCard("KTP");
        assertEquals("KTP", takenCard);
        assertFalse(wallet.getCards().contains("KTP"));
    }

    @Test
    public void testTakeCardNotExists() {
        wallet.addCard("KTP");
        String takenCard = wallet.takeCard("SIM");
        assertNull(takenCard);
    }

    @Test
    public void testTakeCardFromEmptyWallet() {
        String takenCard = wallet.takeCard("KTP");
        assertNull(takenCard);
    }

    // ==================== Test addCash ====================
    @Test
    public void testAddCash() {
        wallet.addCash(50000);
        assertEquals(1, wallet.getCashList().size());
        assertTrue(wallet.getCashList().contains(50000));
    }

    @Test
    public void testAddMultipleCash() {
        wallet.addCash(10000);
        wallet.addCash(20000);
        wallet.addCash(50000);
        assertEquals(3, wallet.getCashList().size());
    }

    @Test
    public void testAddZeroCash() {
        wallet.addCash(0);
        assertEquals(0, wallet.getCashList().size());
    }

    @Test
    public void testAddNegativeCash() {
        wallet.addCash(-10000);
        assertEquals(0, wallet.getCashList().size());
    }

    // ==================== Test takeCash ====================
    @Test
    public void testTakeCash() {
        wallet.addCash(50000);
        wallet.addCash(20000);
        Integer takenCash = wallet.takeCash(50000);
        assertEquals(Integer.valueOf(50000), takenCash);
        assertFalse(wallet.getCashList().contains(50000));
    }

    @Test
    public void testTakeCashNotExists() {
        wallet.addCash(50000);
        Integer takenCash = wallet.takeCash(10000);
        assertNull(takenCash);
    }

    @Test
    public void testTakeCashFromEmptyWallet() {
        Integer takenCash = wallet.takeCash(50000);
        assertNull(takenCash);
    }

    // ==================== Test getTotalCash ====================
    @Test
    public void testGetTotalCash() {
        wallet.addCash(10000);
        wallet.addCash(20000);
        wallet.addCash(50000);
        assertEquals(80000, wallet.getTotalCash());
    }

    @Test
    public void testGetTotalCashEmptyWallet() {
        assertEquals(0, wallet.getTotalCash());
    }

    @Test
    public void testGetTotalCashAfterTakeCash() {
        wallet.addCash(50000);
        wallet.addCash(20000);
        wallet.takeCash(20000);
        assertEquals(50000, wallet.getTotalCash());
    }

    // ==================== Test kombinasi scenario ====================
    @Test
    public void testWalletWithOwnerAndItems() {
        wallet.setOwner("Rakai");
        wallet.addCard("KTP");
        wallet.addCard("SIM");
        wallet.addCash(100000);

        assertNotNull(wallet.getOwner());
        assertEquals("Rakai", wallet.getOwner());
        assertEquals(2, wallet.getCards().size());
        assertEquals(100000, wallet.getTotalCash());
    }

    @Test
    public void testCardListIndependence() {
        wallet.addCard("KTP");
        wallet.getCards().add("SIM"); // modifikasi list yang dikembalikan
        assertEquals(1, wallet.getCards().size()); // list asli tidak berubah
    }

    @Test
    public void testSameCardObject() {
        String card = "KTP";
        wallet.addCard(card);
        String takenCard = wallet.takeCard("KTP");
        assertSame(card, takenCard);
    }

    @Test
    public void testDifferentWalletInstances() {
        Wallet wallet2 = new Wallet();
        wallet.setOwner("Budi");
        wallet2.setOwner("Rio");
        assertNotSame(wallet.getOwner(), wallet2.getOwner());
    }
}

