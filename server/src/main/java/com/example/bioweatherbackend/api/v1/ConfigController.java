package com.example.bioweatherbackend.api.v1;

import com.example.bioweatherbackend.config.sec.RequireAuth;
import com.example.bioweatherbackend.dto.ConfigDto;
import com.example.bioweatherbackend.service.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/config")
@AllArgsConstructor
public class ConfigController {

    private ConfigService configService;

    @GetMapping
    @RequireAuth
    public ConfigDto getConfig() {
        return configService.getConfig();
    }

    @PutMapping
    @RequireAuth
    public void setConfig(@RequestBody ConfigDto configDto) {
        configService.updateConfig(configDto);
    }

}
