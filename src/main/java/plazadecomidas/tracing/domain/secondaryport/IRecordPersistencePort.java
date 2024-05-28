package plazadecomidas.tracing.domain.secondaryport;

import plazadecomidas.tracing.domain.model.OrderRecord;

import java.util.List;

public interface IRecordPersistencePort {
    OrderRecord saveRecord(OrderRecord orderRecord);

    List<OrderRecord> findByClientIdOrderByCreatedAtDesc(Long clientId);
}
