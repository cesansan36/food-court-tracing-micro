package plazadecomidas.tracing.adapters.driving.http.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.request.AddRecordRequest;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.response.RecordResponse;
import plazadecomidas.tracing.adapters.driving.http.rest.mapper.IAddRecordRequestMapper;
import plazadecomidas.tracing.adapters.driving.http.rest.mapper.IRecordResponseMapper;
import plazadecomidas.tracing.domain.primaryport.IRecordPrimaryPort;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordControllerAdapter {

    private final IAddRecordRequestMapper addRecordRequestMapper;
    private final IRecordPrimaryPort recordPrimaryPort;
    private final IRecordResponseMapper recordResponseMapper;

    @PostMapping("/create")
    public ResponseEntity<RecordResponse> create(@RequestBody AddRecordRequest request) {

        RecordResponse response = recordResponseMapper.recordToRecordResponse(recordPrimaryPort.saveRecord(addRecordRequestMapper.addRequestToRecord(request)));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    public ResponseEntity<List<RecordResponse>> list(@RequestParam Long clientId) {

        List<RecordResponse> response = recordResponseMapper.recordListToRecordResponseList(
                recordPrimaryPort.findByClientIdOrderByCreatedAtDesc(clientId));

        response.sort(Comparator.comparing(RecordResponse::createdAt).reversed());

        return ResponseEntity.ok(response);
    }
}
