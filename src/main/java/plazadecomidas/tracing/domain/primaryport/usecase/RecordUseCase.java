package plazadecomidas.tracing.domain.primaryport.usecase;

import plazadecomidas.tracing.domain.model.OrderRecord;
import plazadecomidas.tracing.domain.primaryport.IRecordPrimaryPort;
import plazadecomidas.tracing.domain.secondaryport.IRecordPersistencePort;

import java.util.List;

public class RecordUseCase implements IRecordPrimaryPort {

    private final IRecordPersistencePort recordPersistencePort;

    public RecordUseCase(IRecordPersistencePort recordPersistencePort) {
        this.recordPersistencePort = recordPersistencePort;
    }

    @Override
    public OrderRecord saveRecord(OrderRecord orderRecord) {
        return recordPersistencePort.saveRecord(orderRecord);
    }

    @Override
    public List<OrderRecord> findByClientIdOrderByCreatedAtDesc(Long id) {
        return recordPersistencePort.findByClientIdOrderByCreatedAtDesc(id);
    }

    @Override
    public OrderRecord findByIdOrderAndStatus(Long orderId, String currentState) {
        return recordPersistencePort.findByIdOrderAndStatus(orderId, currentState);
    }
}
