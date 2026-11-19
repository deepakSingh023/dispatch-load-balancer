package com.deepaksingh023.dispatch;


import com.deepaksingh023.dispatch.dto.VehicleList;
import com.deepaksingh023.dispatch.dto.VehicleRequest;
import com.deepaksingh023.dispatch.entity.Vehicle;
import com.deepaksingh023.dispatch.error.exceptions.DuplicateVehicleException;
import com.deepaksingh023.dispatch.repository.VehicleRepository;
import com.deepaksingh023.dispatch.service.VehicleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @InjectMocks
    VehicleService vehicleService;

    @Mock
    VehicleRepository vehicleRepository;

    @Test
    void vehicleCreationLoopDuplicateTest(){

        VehicleRequest vehicleRequest = new VehicleRequest(
                "O1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );
        VehicleRequest vehicleRequest2 = new VehicleRequest(
                "O1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );

        List<VehicleRequest> vehicleRequests = List.of(vehicleRequest,vehicleRequest2);

        VehicleList vehicleList = new VehicleList(vehicleRequests);

        assertThrows(DuplicateVehicleException.class,() ->vehicleService.saveVehicles(vehicleList));

        Mockito.verifyNoInteractions(vehicleRepository);

    }

    @Test
    void testVehicleServiceFullWorking(){

        VehicleRequest vehicleRequest = new VehicleRequest(
                "O1",
                20,
                27.0,
                56.0,
                "New Delhi"

        );
        VehicleRequest vehicleRequest2 = new VehicleRequest(
                "O2",
                20,
                27.0,
                56.0,
                "New Delhi"

        );

        List<VehicleRequest> vehicleRequests = List.of(vehicleRequest,vehicleRequest2);

        VehicleList vehicleList = new VehicleList(vehicleRequests);

        vehicleService.saveVehicles(vehicleList);

        ArgumentCaptor<List<Vehicle>> captor = ArgumentCaptor.forClass(List.class);

        Mockito.verify(vehicleRepository,Mockito.times(1)).bulkUpsert(captor.capture());

        List<Vehicle> vehicles = captor.getValue();

        assertEquals(2,vehicles.size());
        assertEquals("O1",vehicles.get(0).getVehicleId());
        assertEquals("O2",vehicles.get(1).getVehicleId());


    }
}
