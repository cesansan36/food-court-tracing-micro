package plazadecomidas.tracing.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.response.RecordResponse;
import plazadecomidas.tracing.domain.model.OrderRecord;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IRecordResponseMapper {

    RecordResponse recordToRecordResponse(OrderRecord orderRecord);

    List<RecordResponse> recordListToRecordResponseList(List<OrderRecord> byClientIdOrderByCreatedAtDesc);
}
