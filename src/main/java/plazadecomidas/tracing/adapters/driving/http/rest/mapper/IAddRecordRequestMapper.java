package plazadecomidas.tracing.adapters.driving.http.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.request.AddRecordRequest;
import plazadecomidas.tracing.domain.model.OrderRecord;

@Mapper(componentModel = "spring")
public interface IAddRecordRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    OrderRecord addRequestToRecord(AddRecordRequest request);
}
