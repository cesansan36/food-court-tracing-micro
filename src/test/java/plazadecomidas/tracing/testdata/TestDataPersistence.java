package plazadecomidas.tracing.testdata;

import plazadecomidas.tracing.adapters.driven.mongo.entity.RecordEntity;

import java.time.LocalDateTime;

public class TestDataPersistence {

    private TestDataPersistence() {
        throw new IllegalStateException("Utility class");
    }

    public static RecordEntity getRecordEntity(Long id) {
        return new RecordEntity(
                id.toString(),
                id,
                id,
                "client%s@email.com".formatted(id),
                LocalDateTime.now(),
                "PENDING",
                "PREPARING",
                id,
                "employee%s@email.com".formatted(id)
        );
    }
}
