package plazadecomidas.tracing.domain.primaryport;

import plazadecomidas.tracing.domain.model.OrderRecord;

import java.util.List;

public interface IRecordPrimaryPort {
    OrderRecord saveRecord(OrderRecord orderRecord);

    List<OrderRecord> findByClientIdOrderByCreatedAtDesc(Long id);

    OrderRecord findByIdOrderAndStatus(Long orderId, String currentState);
}
