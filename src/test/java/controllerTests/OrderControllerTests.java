package controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.shop.displayordersstatus.JavaReflectStatusOfOrdersApplication;
import ru.shop.displayordersstatus.controllers.OrderController;
import ru.shop.displayordersstatus.services.OrderServiceImpl;
import ru.shop.displayordersstatus.webClients.WebClientOkhttp;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JavaReflectStatusOfOrdersApplication.class)
@AutoConfigureMockMvc
public class OrderControllerTests {

    @InjectMocks
    private OrderController orderController;
    @Mock
    private OrderServiceImpl orderService;
    @Mock
    private WebClientOkhttp webClientOkhttp;
    private MockMvc mvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void init() {
        mvc = MockMvcBuilders
                .standaloneSetup(orderController)
                .build();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void getOrdersWithConfigurationSuccessTest() throws Exception {
        // Create a mock ResponseEntity<String>
        ResponseEntity<String> mockResponse = ResponseEntity.ok("Success");
        // Arrange: Mock the behavior of webClientOkhttp
        Mockito.when(webClientOkhttp.getInformationAboutOrdersFromExternalResource())
                .thenReturn("Some mock response"); // Mock the response you expect from webClientOkhttp
        // Arrange: Mock the behavior of orderService
        Mockito.when(orderService.getOrdersWithConfiguration("Some mock response"))
                .thenReturn(mockResponse);
        // Act and Assert
        mvc.perform(get("/")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
