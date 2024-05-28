package plazadecomidas.tracing.domain.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderRecordTest {

    @Test
    void createOrderRecord() {

        LocalDateTime now = LocalDateTime.now();

        OrderRecord orderRecord = new OrderRecord(
                "1",
                1L,
                1L,
                "cliente@email.com",
                now,
                "PENDING",
                "PREPARING",
                1L,
                "employee@email.com");

        assertAll(
                () -> assertEquals("1", orderRecord.getId()),
                () -> assertEquals(1L, orderRecord.getIdOrder()),
                () -> assertEquals(1L, orderRecord.getIdClient()),
                () -> assertEquals("cliente@email.com", orderRecord.getClientEmail()),
                () -> assertEquals(now, orderRecord.getCreatedAt()),
                () -> assertEquals("PENDING", orderRecord.getPreviousState()),
                () -> assertEquals("PREPARING", orderRecord.getNewState()),
                () -> assertEquals(1L, orderRecord.getIdEmployee()),
                () -> assertEquals("employee@email.com", orderRecord.getEmployeeEmail()));
    }
}