package plazadecomidas.tracing.domain.primaryport.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.tracing.domain.model.OrderRecord;
import plazadecomidas.tracing.domain.secondaryport.IRecordPersistencePort;
import plazadecomidas.tracing.testdata.TestDataDomain;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecordUseCaseTest {

    private RecordUseCase recordUseCase;

    private IRecordPersistencePort recordPersistencePort;

    @BeforeEach
    void setUp() {
        recordPersistencePort = mock(IRecordPersistencePort.class);
        recordUseCase = new RecordUseCase(recordPersistencePort);
    }

    @Test
    void saveRecord() {
        OrderRecord order = TestDataDomain.orderRecord(1L);

        when(recordPersistencePort.saveRecord(order)).thenReturn(order);

        OrderRecord response = recordUseCase.saveRecord(order);

        assertAll(
                () -> assertEquals(order.getId(), response.getId()),
                () -> assertEquals(order.getIdOrder(), response.getIdOrder()),
                () -> assertEquals(order.getIdClient(), response.getIdClient()),
                () -> assertEquals(order.getClientEmail(), response.getClientEmail()),
                () -> assertEquals(order.getCreatedAt(), response.getCreatedAt()),
                () -> assertEquals(order.getPreviousState(), response.getPreviousState()),
                () -> assertEquals(order.getNewState(), response.getNewState()),
                () -> assertEquals(order.getIdEmployee(), response.getIdEmployee()),
                () -> assertEquals(order.getEmployeeEmail(), response.getEmployeeEmail()),
                () -> verify(recordPersistencePort, times(1)).saveRecord(order)
        );
    }

    @Test
    void findByClientIdOrderByCreatedAtDesc() {

        OrderRecord record = TestDataDomain.orderRecord(1L);

        when(recordPersistencePort.findByClientIdOrderByCreatedAtDesc(record.getIdClient())).thenReturn(List.of(record));

        List<OrderRecord> response = recordUseCase.findByClientIdOrderByCreatedAtDesc(record.getIdClient());

        assertAll(
                () -> assertEquals(record.getId(), response.getFirst().getId()),
                () -> assertEquals(record.getIdOrder(), response.getFirst().getIdOrder()),
                () -> assertEquals(record.getIdClient(), response.getFirst().getIdClient()),
                () -> assertEquals(record.getClientEmail(), response.getFirst().getClientEmail()),
                () -> assertEquals(record.getCreatedAt(), response.getFirst().getCreatedAt()),
                () -> assertEquals(record.getPreviousState(), response.getFirst().getPreviousState()),
                () -> assertEquals(record.getNewState(), response.getFirst().getNewState()),
                () -> assertEquals(record.getIdEmployee(), response.getFirst().getIdEmployee()),
                () -> assertEquals(record.getEmployeeEmail(), response.getFirst().getEmployeeEmail()),
                () -> verify(recordPersistencePort, times(1)).findByClientIdOrderByCreatedAtDesc(record.getIdClient())
        );
    }

    @Test
    void findByIdOrderAndStatus() {
        Long orderId = 1L;
        String currentState = "PENDING";
        String newState = "PREPARING";
        OrderRecord orderRecord = TestDataDomain.orderRecord(1L);

        when(recordPersistencePort.findByIdOrderAndStatus(anyLong(), anyString())).thenReturn(orderRecord);

        OrderRecord response = recordUseCase.findByIdOrderAndStatus(orderId, currentState);

        assertAll(
                () -> assertEquals(orderId, response.getIdOrder()),
                () -> assertEquals(currentState, response.getPreviousState()),
                () -> assertEquals(newState, response.getNewState()),
                () -> verify(recordPersistencePort, times(1)).findByIdOrderAndStatus(orderId, currentState)
        );
    }
}