package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.module.configuration.application.command.CreateConfigurationCommand;
import com.vuog.core.module.configuration.application.dto.ConfigurationDto;
import com.vuog.core.module.configuration.application.query.ConfigurationQuery;
import com.vuog.core.module.configuration.application.service.ConfigurationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/configuration")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<ConfigurationDto>>> getConfigurations(
            ConfigurationQuery query
    ) {
        List<ConfigurationDto> configs = configurationService.getSystemConfiguration(query);

        return ResponseEntity.ok(ApiResponse.success(configs));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ConfigurationDto>> createConfiguration(
            @RequestBody CreateConfigurationCommand command
    ) {
        ConfigurationDto configuration = configurationService.create(command);

        return ResponseEntity.ok(ApiResponse.success("Created successfully", configuration));
    }


}
