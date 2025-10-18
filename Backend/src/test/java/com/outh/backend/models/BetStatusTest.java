package com.outh.backend.models;

import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import java.util.EnumMap;
import static org.junit.jupiter.api.Assertions.*;

class BetStatusTest {

    @Test
    void testBetStatusValues() {
        BetStatus[] statuses = BetStatus.values();

        assertEquals(4, statuses.length);
        assertEquals(BetStatus.PENDING, statuses[0]);
        assertEquals(BetStatus.WON, statuses[1]);
        assertEquals(BetStatus.LOST, statuses[2]);
        assertEquals(BetStatus.SETTLED, statuses[3]);
    }

    @Test
    void testBetStatusValueOf() {
        assertEquals(BetStatus.PENDING, BetStatus.valueOf("PENDING"));
        assertEquals(BetStatus.WON, BetStatus.valueOf("WON"));
        assertEquals(BetStatus.LOST, BetStatus.valueOf("LOST"));
        assertEquals(BetStatus.SETTLED, BetStatus.valueOf("SETTLED"));
    }

    @Test
    void testBetStatusToString() {
        assertEquals("PENDING", BetStatus.PENDING.toString());
        assertEquals("WON", BetStatus.WON.toString());
        assertEquals("LOST", BetStatus.LOST.toString());
        assertEquals("SETTLED", BetStatus.SETTLED.toString());
    }

    @Test
    void testBetStatusOrdinal() {
        assertEquals(0, BetStatus.PENDING.ordinal());
        assertEquals(1, BetStatus.WON.ordinal());
        assertEquals(2, BetStatus.LOST.ordinal());
        assertEquals(3, BetStatus.SETTLED.ordinal());
    }

    @Test
    void testBetStatusEnumMethods() {
        // Test enum name() method
        assertEquals("PENDING", BetStatus.PENDING.name());
        assertEquals("WON", BetStatus.WON.name());
        assertEquals("LOST", BetStatus.LOST.name());
        assertEquals("SETTLED", BetStatus.SETTLED.name());

        // Test enum compareTo method
        assertTrue(BetStatus.PENDING.compareTo(BetStatus.WON) < 0);
        assertTrue(BetStatus.WON.compareTo(BetStatus.LOST) < 0);
        assertTrue(BetStatus.LOST.compareTo(BetStatus.SETTLED) < 0);
        assertEquals(0, BetStatus.PENDING.compareTo(BetStatus.PENDING));
    }

    @Test
    void testBetStatusValueOfCaseSensitive() {
        // These should work (exact case match)
        assertEquals(BetStatus.PENDING, BetStatus.valueOf("PENDING"));

        // These should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            BetStatus.valueOf("pending");
        });
        assertThrows(IllegalArgumentException.class, () -> {
            BetStatus.valueOf("Pending");
        });
    }

    @Test
    void testBetStatusEnumSetAndMapCompatibility() {
        // Test that all values are unique and can be used in collections
        assertEquals(4, EnumSet.allOf(BetStatus.class).size());

        // Test EnumMap compatibility
        EnumMap<BetStatus, String> statusMap = new EnumMap<>(BetStatus.class);
        statusMap.put(BetStatus.PENDING, "Waiting for result");
        statusMap.put(BetStatus.WON, "Bet won");
        statusMap.put(BetStatus.LOST, "Bet lost");
        statusMap.put(BetStatus.SETTLED, "Bet settled");

        assertEquals(4, statusMap.size());
        assertTrue(statusMap.containsKey(BetStatus.PENDING));
        assertTrue(statusMap.containsKey(BetStatus.WON));
        assertTrue(statusMap.containsKey(BetStatus.LOST));
        assertTrue(statusMap.containsKey(BetStatus.SETTLED));
    }

    @Test
    void testBetStatusInSwitchStatement() {
        // Test that all enum values work in switch statements
        String result = getStatusDescription(BetStatus.PENDING);
        assertEquals("Bet is pending", result);

        result = getStatusDescription(BetStatus.WON);
        assertEquals("Bet was won", result);

        result = getStatusDescription(BetStatus.LOST);
        assertEquals("Bet was lost", result);

        result = getStatusDescription(BetStatus.SETTLED);
        assertEquals("Bet is settled", result);
    }

    private String getStatusDescription(BetStatus status) {
        switch (status) {
            case PENDING:
                return "Bet is pending";
            case WON:
                return "Bet was won";
            case LOST:
                return "Bet was lost";
            case SETTLED:
                return "Bet is settled";
            default:
                return "Unknown status";
        }
    }
}