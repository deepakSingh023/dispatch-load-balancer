package com.deepaksingh023.dispatch;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.deepaksingh023.dispatch.controller.VehicleController;
import com.deepaksingh023.dispatch.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationBindHandler;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    VehicleService vehicleService;

    @Test
    void shouldReturn200ForValidDara() throws Exception{

        String data = """
           {
                "vehicles": [
                    {
                        "vehicleId": "VEH001",
                        "capacity": 100,
                        "currentLatitude": 12.9716,
                        "currentLongitude": 77.6413,
                        "currentAddress": "Indiranagar, Bangalore, Karnataka, India"
                    },
                    {
                        "vehicleId": "VEH002",
                        "capacity": 150,
                        "currentLatitude": 13.0674,
                        "currentLongitude": 80.2376,
                        "currentAddress": "T Nagar, Chennai, Tamil Nadu, India"
                    }
                ]
           }
        """;


        mockMvc.perform(post("/api/dispatch/vehicles").contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(status().isOk());

        Mockito.verify(vehicleService,Mockito.times(1)).saveVehicles(any());


    }

    @Test
    void shouldReturnErrorDueToMissingFields() throws  Exception {

        String data = """
           {
                "vehicles": [
                    {
                        "vehicleId": "VEH001",
                        "currentLatitude": 12.9716,
                        "currentLongitude": 77.6413,
                        "currentAddress": "Indiranagar, Bangalore, Karnataka, India"
                    },
                    {
                        "vehicleId": "VEH002",
                        "capacity": 150,
                        "currentLatitude": 13.0674,
                        "currentAddress": "T Nagar, Chennai, Tamil Nadu, India"
                    }
                ]
           }
        """;

        mockMvc.perform(post("/api/dispatch/vehicles").contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(status().isBadRequest());


        Mockito.verifyNoInteractions(vehicleService);

    }

    @Test
    void shouldThrowExceptionDueToWrngType() throws Exception{

        String data = """
           {
                "vehicles": [
                    {
                        "vehicleId": "VEH001",
                        "capacity": 100,
                        "currentLatitude": 12.9716,
                        "currentLongitude": "Hello wrong",
                        "currentAddress": "27.0000
                    },
                    {
                        "vehicleId": 34,
                        "capacity": "Hello whats up"
                        "currentLatitude": 13.0674,
                        "currentLongitude": 20
                        "currentAddress": "T Nagar, Chennai, Tamil Nadu, India"
                    }
                ]
           }
        """;


        mockMvc.perform(post("/api/dispatch/vehicles").contentType(MediaType.APPLICATION_JSON).content(data)).andExpect(status().isBadRequest());

        Mockito.verifyNoInteractions(vehicleService);



    }


}
