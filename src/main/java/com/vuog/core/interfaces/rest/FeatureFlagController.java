package com.vuog.core.interfaces.rest;

import com.vuog.core.common.dto.ApiResponse;
import com.vuog.core.module.configuration.application.dto.FeatureFlagDto;
import com.vuog.core.module.configuration.domain.model.FeatureFlag;
import com.vuog.core.module.configuration.domain.service.impl.LocalFeatureToggleService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/featureFlag")
@AllArgsConstructor
public class FeatureFlagController {

    private final LocalFeatureToggleService featureToggleService;

    @GetMapping("/{key}")
    public ResponseEntity<ApiResponse<FeatureFlagDto>> getFeatureFlag(
            @PathVariable String key
    ) {
        FeatureFlag featureFlag = featureToggleService.getByKey(key);

        return ResponseEntity.ok(ApiResponse.success(new FeatureFlagDto(featureFlag)));
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<FeatureFlagDto>> toggleFeature(
            @PathVariable Long id
    ) {
        FeatureFlag featureFlag = featureToggleService.toggleFeature(id);

        return ResponseEntity.ok(ApiResponse.success(new FeatureFlagDto(featureFlag)));
    }

    @GetMapping("{id}/is-enabled")
    public ResponseEntity<ApiResponse<Boolean>> isFeatureEnabled(
            @PathVariable Long id
    ) {
        boolean isEnabled = featureToggleService.isFeatureEnabled(id);

        return ResponseEntity.ok(ApiResponse.success(isEnabled));
    }
}
