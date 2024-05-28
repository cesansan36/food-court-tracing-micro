package plazadecomidas.tracing.testdata;

import plazadecomidas.tracing.adapters.driving.http.rest.dto.request.AddRecordRequest;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.response.RecordResponse;

import java.time.LocalDateTime;

public class TestDataController {

    private TestDataController() {
        throw new IllegalStateException("Utility class");
    }

    public static AddRecordRequest getAddRecordRequest(Long id) {
        return new AddRecordRequest(
            id,
            id,
            "client%s@email.com".formatted(id),
            "PENDING",
            "READY",
            id,
            "employee%s@email.com".formatted(id)
        );
    }

    public static RecordResponse getRecordResponse(Long id) {
        return new RecordResponse(
            id,
            id,
            "client%s@email.com".formatted(id),
            LocalDateTime.now(),
            "PENDING",
            "READY",
            id,
            "employee%s@email.com".formatted(id)
        );
    }
}
