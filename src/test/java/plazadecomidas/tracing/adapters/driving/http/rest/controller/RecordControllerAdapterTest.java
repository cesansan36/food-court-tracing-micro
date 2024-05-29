package plazadecomidas.tracing.adapters.driving.http.rest.controller;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.request.AddRecordRequest;
import plazadecomidas.tracing.adapters.driving.http.rest.dto.response.RecordResponse;
import plazadecomidas.tracing.adapters.driving.http.rest.mapper.IAddRecordRequestMapper;
import plazadecomidas.tracing.adapters.driving.http.rest.mapper.IRecordResponseMapper;
import plazadecomidas.tracing.domain.model.OrderRecord;
import plazadecomidas.tracing.domain.primaryport.IRecordPrimaryPort;
import plazadecomidas.tracing.testdata.TestDataController;
import plazadecomidas.tracing.testdata.TestDataDomain;
import plazadecomidas.tracing.util.ITokenUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RecordControllerAdapterTest {

    @InjectMocks private RecordControllerAdapter recordControllerAdapter;

    @Mock private IAddRecordRequestMapper addRecordRequestMapper;
    @Mock private IRecordPrimaryPort recordPrimaryPort;
    @Mock private IRecordResponseMapper recordResponseMapper;
    @Mock private ITokenUtils tokenUtils;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(recordControllerAdapter).build();
    }

    @Test
    void create() throws Exception {
        Object inputObject = new Object() {
            public final Long idOrder = 1L;
            public final Long idClient = 1L;
            public final String clientEmail = "client@email.com";
            public final String previousState = "PENDING";
            public final String newState = "READY";
            public final Long idEmployee = 1L;
            public final String employeeEmail = "employee@email.com";
        };
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String inputJson = objectMapper.writeValueAsString(inputObject);

        RecordResponse recordResponse = TestDataController.getRecordResponse(1L);

        when(addRecordRequestMapper.addRequestToRecord(any(AddRecordRequest.class))).thenReturn(mock(OrderRecord.class));
        when(recordPrimaryPort.saveRecord(any(OrderRecord.class))).thenReturn(mock(OrderRecord.class));
        when(recordResponseMapper.recordToRecordResponse(any(OrderRecord.class))).thenReturn(recordResponse);

        MockHttpServletRequestBuilder request = post("/record/create")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(inputJson);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(recordResponse)));

        verify(addRecordRequestMapper, times(1)).addRequestToRecord(any(AddRecordRequest.class));
        verify(recordPrimaryPort, times(1)).saveRecord(any(OrderRecord.class));
        verify(recordResponseMapper, times(1)).recordToRecordResponse(any(OrderRecord.class));
    }

    @Test
    void list() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();

//        Long idUser = 1L;
        Claim claim = TestDataController.getIdClaim(1L);
        List<OrderRecord> records = List.of(mock(OrderRecord.class));
        List<RecordResponse> recordResponses = new ArrayList<>();
        recordResponses.add(TestDataController.getRecordResponse(1L));
        String bearerToken = "bearerToken";

        when(recordPrimaryPort.findByClientIdOrderByCreatedAtDesc(anyLong())).thenReturn(records);
        when(recordResponseMapper.recordListToRecordResponseList(anyList())).thenReturn(recordResponses);
        when(tokenUtils.validateToken(any(String.class))).thenReturn(mock(DecodedJWT.class));
        when(tokenUtils.getSpecificClaim(any(DecodedJWT.class), any(String.class))).thenReturn(claim);

        MockHttpServletRequestBuilder request = get("/record/list")
                .header("Authorization", bearerToken);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recordResponses)));

        verify(recordPrimaryPort, times(1)).findByClientIdOrderByCreatedAtDesc(anyLong());
        verify(recordResponseMapper, times(1)).recordListToRecordResponseList(anyList());
        verify(tokenUtils, times(1)).validateToken(any(String.class));
        verify(tokenUtils, times(1)).getSpecificClaim(any(DecodedJWT.class), any(String.class));
    }

    @Test
    void getOrder() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String bearerToken = "bearerToken";
        Long idOrder = 1L;
        String currentState = "PENDING";

        OrderRecord orderRecord = TestDataDomain.orderRecord(idOrder);
        RecordResponse recordResponse = TestDataController.getRecordResponse(idOrder);

        when(recordPrimaryPort.findByIdOrderAndStatus(anyLong(), any(String.class))).thenReturn(orderRecord);
        when(recordResponseMapper.recordToRecordResponse(any(OrderRecord.class))).thenReturn(recordResponse);

        MockHttpServletRequestBuilder request = get("/record/get-order")
                .header("Authorization", bearerToken)
                .param("orderId", String.valueOf(idOrder))
                .param("currentState", currentState);

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recordResponse)));

        verify(recordPrimaryPort, times(1)).findByIdOrderAndStatus(anyLong(), any(String.class));
        verify(recordResponseMapper, times(1)).recordToRecordResponse(any(OrderRecord.class));
    }
}