package com.vuog.core.common.initial;

import com.vuog.core.common.annotations.CanAccess;
import com.vuog.core.module.auth.application.service.EndpointSecurityService;
import com.vuog.core.module.auth.domain.model.EndpointSecure;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CanAccessInitializer implements ApplicationRunner {

    private final EndpointSecurityService endpointSecurityService;
    private final RequestMappingHandlerMapping handlerMapping;

    public CanAccessInitializer(EndpointSecurityService endpointSecurityService,
                                @Qualifier("requestMappingHandlerMapping") RequestMappingHandlerMapping handlerMapping) {
        this.endpointSecurityService = endpointSecurityService;
        this.handlerMapping = handlerMapping;
    }

    @Override
    public void run(ApplicationArguments args) {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = entry.getValue();
            Method method = handlerMethod.getMethod();
            CanAccess canAccess = method.getAnnotation(CanAccess.class);

            if (canAccess != null) {
                String role = canAccess.role();
                boolean isRole = canAccess.isRole();
                RequestMappingInfo mappingInfo = entry.getKey();

                Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();

                if (mappingInfo.getPathPatternsCondition() == null) {
                    return;
                }

                Set<String> paths = mappingInfo.getPathPatternsCondition().getPatterns().stream().map(PathPattern::getPatternString).collect(Collectors.toSet());

                for (String path : paths) {
                    for (RequestMethod httpMethod : methods) {
                        String methodName = httpMethod.name();
                        boolean exists = endpointSecurityService
                                .findByEndpoint(path, methodName)
                                .isPresent();

                        if (!exists) {
                            EndpointSecure ep = new EndpointSecure();
                            ep.setMethod(methodName);
                            ep.setEndpointPattern(path);
                            ep.setAuthority(role);
                            ep.setIsRole(isRole);
                            ep.setCreatedBy("SYSTEM");
                            ep.setUpdatedBy("SYSTEM");
                            endpointSecurityService.save(ep);
                        }
                    }
                }
            }
        }
    }
}
