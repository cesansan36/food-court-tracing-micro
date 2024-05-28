package plazadecomidas.tracing.testdata;

import plazadecomidas.tracing.domain.model.OrderRecord;

import java.time.LocalDateTime;

public class TestDataDomain {

    private TestDataDomain() {
        throw new IllegalStateException("Utility class");
    }

    public static OrderRecord orderRecord(Long id) {
        return new OrderRecord(
                id.toString(),
                id,
                id,
                "client%s@email.com".formatted(id),
                LocalDateTime.now(),
                "PENDING",
                "PREPARING",
                id,
                "employee%s@email.com".formatted(id));
    }
}
