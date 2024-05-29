package plazadecomidas.tracing.adapters.driven.mongo.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import plazadecomidas.tracing.adapters.driven.mongo.entity.RecordEntity;
import plazadecomidas.tracing.adapters.driven.mongo.exception.DocumentNotFoundException;
import plazadecomidas.tracing.adapters.driven.mongo.mapper.IRecordEntityMapper;
import plazadecomidas.tracing.adapters.driven.mongo.repository.IRecordRepository;
import plazadecomidas.tracing.domain.model.OrderRecord;
import plazadecomidas.tracing.testdata.TestDataDomain;
import plazadecomidas.tracing.testdata.TestDataPersistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RecordAdapterTest {

    private RecordAdapter recordAdapter;

    private IRecordEntityMapper recordEntityMapper;
    private IRecordRepository recordRepository;

    @BeforeEach
    void setUp() {
        recordEntityMapper = mock(IRecordEntityMapper.class);
        recordRepository = mock(IRecordRepository.class);
        recordAdapter = new RecordAdapter(recordRepository, recordEntityMapper);
    }

    @Test
    void saveRecord() {
        OrderRecord orderRecord = TestDataDomain.orderRecord(1L);
        RecordEntity recordEntity = TestDataPersistence.getRecordEntity(1L);

        when(recordEntityMapper.recordToRecordEntity(orderRecord)).thenReturn(recordEntity);
        when(recordRepository.save(recordEntity)).thenReturn(recordEntity);
        when(recordEntityMapper.recordEntityToRecord(recordEntity)).thenReturn(orderRecord);

        OrderRecord finalRecord = recordAdapter.saveRecord(orderRecord);

        assertAll(
            () -> assertNotNull(finalRecord),
            () -> assertEquals(orderRecord, finalRecord),
            () -> verify(recordRepository, times(1)).save(recordEntity),
            () -> verify(recordEntityMapper, times(1)).recordToRecordEntity(orderRecord),
            () -> verify(recordEntityMapper, times(1)).recordEntityToRecord(recordEntity)
        );

    }

    @Test
    void findByClientIdOrderByCreatedAtDesc() {
        Long id = 1L;
        OrderRecord orderRecord = TestDataDomain.orderRecord(1L);
        RecordEntity recordEntity = TestDataPersistence.getRecordEntity(1L);

        when(recordRepository.findByClientIdOrderByCreatedAtDesc(id)).thenReturn(List.of(recordEntity));
        when(recordEntityMapper.recordEntityListToRecordList(anyList())).thenReturn(List.of(orderRecord));

        List<OrderRecord> records = recordAdapter.findByClientIdOrderByCreatedAtDesc(id);

        assertAll(
            () -> assertNotNull(records),
            () -> assertEquals(1, records.size()),
            () -> assertEquals(orderRecord, records.getFirst()),
            () -> verify(recordRepository, times(1)).findByClientIdOrderByCreatedAtDesc(id),
            () -> verify(recordEntityMapper, times(1)).recordEntityListToRecordList(anyList())
        );
    }

    @Test
    void findByIdOrderAndStatusSuccess() {
        Long orderId = 1L;
        String currentState = "PENDING";
        OrderRecord orderRecord = TestDataDomain.orderRecord(1L);
        RecordEntity recordEntity = TestDataPersistence.getRecordEntity(1L);

        when(recordRepository.findOneByOrderAndState(orderId, currentState)).thenReturn(recordEntity);
        when(recordEntityMapper.recordEntityToRecord(recordEntity)).thenReturn(orderRecord);

        OrderRecord record = recordAdapter.findByIdOrderAndStatus(orderId, currentState);

        assertAll(
            () -> assertNotNull(record),
            () -> assertEquals(orderRecord, record),
            () -> verify(recordRepository, times(1)).findOneByOrderAndState(orderId, currentState),
            () -> verify(recordEntityMapper, times(1)).recordEntityToRecord(recordEntity)
        );
    }

    @Test
    void findByIdOrderAndStatusFailBecauseNotFound() {
        Long orderId = 1L;
        String currentState = "PENDING";

        when(recordRepository.findOneByOrderAndState(orderId, currentState)).thenReturn(null);

        assertAll(
            () -> assertThrows(DocumentNotFoundException.class, () -> recordAdapter.findByIdOrderAndStatus(orderId, currentState)),
            () -> verify(recordRepository, times(1)).findOneByOrderAndState(orderId, currentState),
            () -> verify(recordEntityMapper, times(0)).recordEntityToRecord(null)
        );
    }
}