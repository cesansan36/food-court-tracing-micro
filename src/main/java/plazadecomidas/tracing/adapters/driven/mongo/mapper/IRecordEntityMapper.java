package plazadecomidas.tracing.adapters.driven.mongo.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.tracing.adapters.driven.mongo.entity.RecordEntity;
import plazadecomidas.tracing.domain.model.OrderRecord;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRecordEntityMapper {

    RecordEntity recordToRecordEntity(OrderRecord orderRecord);

    OrderRecord recordEntityToRecord(RecordEntity recordEntity);

    List<OrderRecord> recordEntityListToRecordList(List<RecordEntity> byClientIdOrderByCreatedAtDesc);
}
