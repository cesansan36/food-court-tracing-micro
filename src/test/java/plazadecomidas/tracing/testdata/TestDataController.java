package plazadecomidas.tracing.testdata;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.request.AddRecordRequest;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.response.RecordResponse;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

    public static Claim getIdClaim (Long id) {
        return new Claim() {
            @Override
            public boolean isNull() {
                return false;
            }

            @Override
            public boolean isMissing() {
                return false;
            }

            @Override
            public Boolean asBoolean() {
                return null;
            }

            @Override
            public Integer asInt() {
                return 0;
            }

            @Override
            public Long asLong() {
                return id;
            }

            @Override
            public Double asDouble() {
                return 0.0;
            }

            @Override
            public String asString() {
                return "";
            }

            @Override
            public Date asDate() {
                return null;
            }

            @Override
            public <T> T[] asArray(Class<T> aClass) throws JWTDecodeException {
                return null;
            }

            @Override
            public <T> List<T> asList(Class<T> aClass) throws JWTDecodeException {
                return List.of();
            }

            @Override
            public Map<String, Object> asMap() throws JWTDecodeException {
                return Map.of();
            }

            @Override
            public <T> T as(Class<T> aClass) throws JWTDecodeException {
                return null;
            }
        };
    }
}
