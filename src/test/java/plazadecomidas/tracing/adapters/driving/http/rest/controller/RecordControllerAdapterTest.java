package plazadecomidas.tracing.adapters.driving.http.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class RecordControllerAdapterTest {

    private RecordControllerAdapter recordControllerAdapter;

    private IAddRecordRequestMapper addRecordRequestMapper;
    private IRecordPrimaryPort recordPrimaryPort;
    private IRecordResponseMapper recordResponseMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        addRecordRequestMapper = mock(IAddRecordRequestMapper.class);
        recordPrimaryPort = mock(IRecordPrimaryPort.class);
        recordResponseMapper = mock(IRecordResponseMapper.class);

        recordControllerAdapter = new RecordControllerAdapter(addRecordRequestMapper,recordPrimaryPort,recordResponseMapper);

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

        Long idUser = 1L;
        List<OrderRecord> records = List.of(mock(OrderRecord.class));
        List<RecordResponse> recordResponses = new ArrayList<>();
        recordResponses.add(TestDataController.getRecordResponse(1L));

        when(recordPrimaryPort.findByClientIdOrderByCreatedAtDesc(anyLong())).thenReturn(records);
        when(recordResponseMapper.recordListToRecordResponseList(anyList())).thenReturn(recordResponses);

        MockHttpServletRequestBuilder request = get("/record/list")
                                                        .param("clientId", String.valueOf(idUser));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(recordResponses)));

        verify(recordPrimaryPort, times(1)).findByClientIdOrderByCreatedAtDesc(idUser);
        verify(recordResponseMapper, times(1)).recordListToRecordResponseList(records);
    }
}