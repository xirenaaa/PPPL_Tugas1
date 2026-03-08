package org.example;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class WalletTest {

    private Wallet wallet;

    @BeforeAll
    static void initAll() {
        System.out.println("=== Mulai test WalletTest ===");
    }

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
    }

    @AfterEach
    void tearDown() {
        wallet = null;
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("=== Semua test WalletTest selesai ===");
    }

    // ==================== Owner (String) ====================

    @Test
    void testSetOwner() {
        wallet.setOwner("Budi");
        assertEquals("Budi", wallet.getOwner());
    }

    @Test
    void testOwnerAwalnyaNull() {
        assertNull(wallet.getOwner(), "Owner harus null saat wallet baru dibuat");
    }

    @Test
    void testOwnerTidakNullSetelahDiSet() {
        wallet.setOwner("Rio");
        assertNotNull(wallet.getOwner());
    }

    // ==================== Owner (Object) via MethodSource ====================

    static Stream<Arguments> ownerProvider() {
        return Stream.of(
                Arguments.of(new Owner("Budi", "budi@mail.com")),
                Arguments.of(new Owner("Sari", "sari@mail.com")),
                Arguments.of(new Owner("Andi", "andi@mail.com"))
        );
    }

    @ParameterizedTest
    @MethodSource("ownerProvider")
    void testSetOwnerObject(Owner owner) {
        wallet.setOwner(owner);

        assertNotNull(wallet.getOwnerObject(), "Owner object tidak boleh null");
        assertSame(owner, wallet.getOwnerObject());
        assertEquals(owner.getName(), wallet.getOwner());
        assertEquals(owner.getEmail(), wallet.getOwnerObject().getEmail());
    }

    // ==================== addCard ====================

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
    void testAddCardNullDanKosong() {
        wallet.addCard(null);
        wallet.addCard("");
        assertEquals(0, wallet.getCards().size());
    }

    // ==================== takeCard ====================

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

    @Test
    void testSameCardReference() {
        String card = "KTP";
        wallet.addCard(card);
        String hasil = wallet.takeCard("KTP");
        assertSame(card, hasil);
    }

    @Test
    void testGetCardsReturnSalinan() {
        wallet.addCard("KTP");
        wallet.getCards().add("SIM");
        assertEquals(1, wallet.getCards().size(), "getCards() harus return salinan");
    }

    // ==================== addCash — ValueSource (valid positif) ====================

    @ParameterizedTest
    @ValueSource(ints = {1000, 5000, 10000, 50000, 100000})
    void testAddCashValid(int amount) {
        wallet.addCash(amount);
        assertEquals(1, wallet.getCashList().size());
        assertTrue(wallet.getCashList().contains(amount));
    }

    // ==================== addCash — ValueSource (invalid negatif/nol) ====================

    @ParameterizedTest
    @ValueSource(ints = {0, -1000, -50000})
    void testAddCashInvalid(int amount) {
        wallet.addCash(amount);
        assertEquals(0, wallet.getCashList().size(), "Cash negatif/nol tidak boleh masuk");
    }

    // ==================== takeCash ====================

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

    // ==================== getTotalCash ====================

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

    // ==================== withdrawCash — CsvFileSource valid ====================

    @ParameterizedTest
    @CsvFileSource(resources = "/withdraw-valid.csv", numLinesToSkip = 1)
    void testWithdrawCashValid(int deposit, int withdraw, int expectedTotal) {
        wallet.addCash(deposit);

        if (withdraw > 0) {
            wallet.withdrawCash(withdraw);
        }

        assertEquals(expectedTotal, wallet.getTotalCash(),
                "Deposit " + deposit + " - withdraw " + withdraw + " harus = " + expectedTotal);
    }

    // ==================== withdrawCash — CsvFileSource invalid ====================

    @ParameterizedTest
    @CsvFileSource(resources = "/withdraw-invalid.csv", numLinesToSkip = 1)
    void testWithdrawCashInvalid(int initialDeposit, int withdraw, String exceptionType) {
        if (initialDeposit > 0) {
            wallet.addCash(initialDeposit);
        }

        if (exceptionType.equals("InsufficientFundsException")) {
            assertThrows(InsufficientFundsException.class,
                    () -> wallet.withdrawCash(withdraw),
                    "Harus throw InsufficientFundsException");
        } else {
            assertThrows(IllegalArgumentException.class,
                    () -> wallet.withdrawCash(withdraw),
                    "Harus throw IllegalArgumentException");
        }
    }

    // ==================== Skenario gabungan ====================

    @Test
    void testWalletLengkap() {
        Owner owner = new Owner("Rakai", "rakai@mail.com");
        wallet.setOwner(owner);
        wallet.addCard("KTP");
        wallet.addCard("SIM");
        wallet.addCash(100000);

        assertEquals("Rakai", wallet.getOwner());
        assertSame(owner, wallet.getOwnerObject());
        assertEquals(2, wallet.getCards().size());
        assertEquals(100000, wallet.getTotalCash());
    }

    @Test
    void testBedaInstanceWallet() {
        Wallet lain = new Wallet();
        wallet.setOwner("Budi");
        lain.setOwner("Rio");
        assertNotSame(wallet.getOwner(), lain.getOwner());
    }
}
