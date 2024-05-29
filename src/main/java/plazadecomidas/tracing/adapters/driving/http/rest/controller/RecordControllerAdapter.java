package plazadecomidas.tracing.adapters.driving.http.rest.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.request.AddRecordRequest;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.response.RecordResponse;
import plazadecomidas.tracing.adapters.driving.http.rest.mapper.IAddRecordRequestMapper;
import plazadecomidas.tracing.adapters.driving.http.rest.mapper.IRecordResponseMapper;
import plazadecomidas.tracing.adapters.driving.http.rest.util.ControllerConstants;
import plazadecomidas.tracing.domain.primaryport.IRecordPrimaryPort;
import plazadecomidas.tracing.util.ITokenUtils;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/record")
@RequiredArgsConstructor
public class RecordControllerAdapter {

    private final IAddRecordRequestMapper addRecordRequestMapper;
    private final IRecordPrimaryPort recordPrimaryPort;
    private final IRecordResponseMapper recordResponseMapper;
    private final ITokenUtils tokenUtils;

    @PostMapping("/create")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RecordResponse> create(@RequestBody AddRecordRequest request) {

        RecordResponse response = recordResponseMapper.recordToRecordResponse(
                recordPrimaryPort.saveRecord(
                        addRecordRequestMapper.addRequestToRecord(request)));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<RecordResponse>> list(@RequestHeader("Authorization") String token) {
        Long clientId = getUserId(token);

        List<RecordResponse> response = recordResponseMapper.recordListToRecordResponseList(
                recordPrimaryPort.findByClientIdOrderByCreatedAtDesc(clientId));

        response.sort(Comparator.comparing(RecordResponse::createdAt).reversed());

        return ResponseEntity.ok(response);
    }

    private Long getUserId(String token) {
        String jwt = token.substring(7);
        return tokenUtils.getSpecificClaim(tokenUtils.validateToken(jwt), ControllerConstants.USER_CLAIM).asLong();
    }

    @GetMapping("get-order")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<RecordResponse> getOrder(@RequestParam Long orderId, @RequestParam String currentState) {

        RecordResponse response = recordResponseMapper.recordToRecordResponse(
                recordPrimaryPort.findByIdOrderAndStatus(orderId, currentState));

        return ResponseEntity.ok(response);
    }
}
