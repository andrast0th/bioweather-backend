package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.dto.notifications.DeviceDto;
import com.example.bioweatherbackend.service.DeviceManagementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/devices")
@AllArgsConstructor
public class DevicesController {

    private DeviceManagementService service;

    @GetMapping
    public List<DeviceDto> getDevices(@RequestParam(required = false) String query) {
        return service.getDevices(query);
    }

}
