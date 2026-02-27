package org.example;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    private Wallet wallet;
    private static int testCount;

    @BeforeAll
    static void initAll() {
        testCount = 0;
        System.out.println("=== Mulai test WalletTest ===");
    }

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
        testCount++;
        System.out.println("Test #" + testCount + " dimulai");
    }

    @AfterEach
    void tearDown() {
        System.out.println("Test #" + testCount + " selesai");
        wallet = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("=== Semua " + testCount + " test selesai ===");
    }

    // --- setOwner ---

    @Test
    void testSetOwner() {
        wallet.setOwner("Budi");
        assertEquals("Budi", wallet.getOwner());
    }

    @Test
    void testOwnerAwalnyaNull() {
        assertNull(wallet.getOwner());
    }

    @Test
    void testOwnerTidakNullSetelahDiSet() {
        wallet.setOwner("Rio");
        assertNotNull(wallet.getOwner());
    }

    // --- addCard ---

    @Test
    void testAddCard() {
        wallet.addCard("KTP");
        assertEquals(1, wallet.getCards().size());
        assertTrue(wallet.getCards().contains("KTP"));
    }

    @Test
    void testAddBanyakCard() {
        wallet.addCard("KTP");
        wallet.addCard("SIM");
        wallet.addCard("ATM BCA");
        assertEquals(3, wallet.getCards().size());
    }

    @Test
    void testAddCardNull() {
        wallet.addCard(null);
        assertEquals(0, wallet.getCards().size());
    }

    @Test
    void testAddCardKosong() {
        wallet.addCard("");
        assertEquals(0, wallet.getCards().size());
    }

    // --- takeCard ---

    @Test
    void testTakeCard() {
        wallet.addCard("KTP");
        wallet.addCard("SIM");

        String hasil = wallet.takeCard("KTP");

        assertEquals("KTP", hasil);
        assertFalse(wallet.getCards().contains("KTP"));
    }

    @Test
    void testTakeCardYangTidakAda() {
        wallet.addCard("KTP");
        assertNull(wallet.takeCard("SIM"));
    }

    @Test
    void testTakeCardDariWalletKosong() {
        assertNull(wallet.takeCard("KTP"));
    }

    // --- addCash ---

    @Test
    void testAddCash() {
        wallet.addCash(50000);
        assertEquals(1, wallet.getCashList().size());
        assertTrue(wallet.getCashList().contains(50000));
    }

    @Test
    void testAddBanyakCash() {
        wallet.addCash(10000);
        wallet.addCash(20000);
        wallet.addCash(50000);
        assertEquals(3, wallet.getCashList().size());
    }

    @Test
    void testAddCashNol() {
        wallet.addCash(0);
        assertEquals(0, wallet.getCashList().size());
    }

    @Test
    void testAddCashNegatif() {
        wallet.addCash(-10000);
        assertEquals(0, wallet.getCashList().size());
    }

    // --- takeCash ---

    @Test
    void testTakeCash() {
        wallet.addCash(50000);
        wallet.addCash(20000);

        Integer hasil = wallet.takeCash(50000);

        assertEquals(50000, hasil);
        assertFalse(wallet.getCashList().contains(50000));
    }

    @Test
    void testTakeCashYangTidakAda() {
        wallet.addCash(50000);
        assertNull(wallet.takeCash(10000));
    }

    @Test
    void testTakeCashDariWalletKosong() {
        assertNull(wallet.takeCash(50000));
    }

    // --- getTotalCash ---

    @Test
    void testGetTotalCash() {
        wallet.addCash(10000);
        wallet.addCash(20000);
        wallet.addCash(50000);
        assertEquals(80000, wallet.getTotalCash());
    }

    @Test
    void testTotalCashWalletKosong() {
        assertEquals(0, wallet.getTotalCash());
    }

    @Test
    void testTotalCashSetelahAmbilUang() {
        wallet.addCash(50000);
        wallet.addCash(20000);
        wallet.takeCash(20000);
        assertEquals(50000, wallet.getTotalCash());
    }

    // --- skenario gabungan ---

    @Test
    void testWalletLengkap() {
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
    void testGetCardsReturnSalinan() {
        wallet.addCard("KTP");
        wallet.getCards().add("SIM"); // coba modifikasi dari luar
        assertEquals(1, wallet.getCards().size()); // harusnya tetap 1
    }

    @Test
    void testSameCardReference() {
        String card = "KTP";
        wallet.addCard(card);
        String hasil = wallet.takeCard("KTP");
        assertSame(card, hasil);
    }

    @Test
    void testBedaInstanceWallet() {
        Wallet lain = new Wallet();
        wallet.setOwner("Budi");
        lain.setOwner("Rio");
        assertNotSame(wallet.getOwner(), lain.getOwner());
    }
}
