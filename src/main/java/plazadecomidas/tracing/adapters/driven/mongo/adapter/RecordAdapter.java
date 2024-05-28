package plazadecomidas.tracing.adapters.driven.mongo.adapter;

import lombok.RequiredArgsConstructor;
import plazadecomidas.tracing.adapters.driven.mongo.mapper.IRecordEntityMapper;
import plazadecomidas.tracing.adapters.driven.mongo.repository.IRecordRepository;
import plazadecomidas.tracing.domain.model.OrderRecord;
import plazadecomidas.tracing.domain.secondaryport.IRecordPersistencePort;

import java.util.List;

@RequiredArgsConstructor
public class RecordAdapter implements IRecordPersistencePort {

    private final IRecordRepository recordRepository;
    private final IRecordEntityMapper recordEntityMapper;

    @Override
    public OrderRecord saveRecord(OrderRecord orderRecord) {

        return recordEntityMapper.recordEntityToRecord(
                recordRepository.save(
                        recordEntityMapper.recordToRecordEntity(orderRecord)));
    }

    @Override
    public List<OrderRecord> findByClientIdOrderByCreatedAtDesc(Long clientId) {
        return recordEntityMapper.recordEntityListToRecordList(
                recordRepository.findByClientIdOrderByCreatedAtDesc(clientId));
    }
}
