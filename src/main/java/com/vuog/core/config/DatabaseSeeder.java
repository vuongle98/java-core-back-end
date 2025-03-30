package com.vuog.core.config;

import com.vuog.core.module.auth.domain.model.*;
import com.vuog.core.module.auth.domain.repository.*;
import org.reflections.Reflections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@Configuration
public class DatabaseSeeder {


    @Bean
    CommandLineRunner initPermission(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            UserProfileRepository profileRepository
    ) {
        return args -> {
            System.out.println("Auto initializing permissions...");

            List<String> ACTIONS = List.of("READ", "WRITE", "DELETE");
            Set<String> entities = scanEntities("com.vuog.core.module");
            Set<Role> roles = new HashSet<>();

            List<String> logStoreNewEntity = new ArrayList<>();

            for (String entity : entities) {
                // create role
                String code = "MANAGE_" + entity.toUpperCase();

                Role role = new Role();
                role.setCode(code);
                role.setName("Can manage " + entity);
                role.setDescription("Manage " + entity);

                Set<Permission> permissions = new HashSet<>();
                for (String action : ACTIONS) {
                    String permCode = action.toUpperCase() + "_" + entity.toUpperCase();

                    Optional<Permission> permissionOptional = permissionRepository.findByCode(permCode);
                    if (permissionOptional.isEmpty()) {
                        Permission perm = initPermission(
                                permCode,
                                action + " " + entity + " permission",
                                "Can " + action + " data in " + entity
                        );
                        perm = permissionRepository.save(perm);
                        permissions.add(perm);
                        logStoreNewEntity.add("Store permission: " + permCode);
                    } else {
                        permissions.add(permissionOptional.get());
                    }
                }
                role.setPermissions(permissions);

                role.setCreatedBy("SYSTEM");
                role.setUpdatedBy("SYSTEM");

                if (roleRepository.findByCode(code).isEmpty()) {
                    role = roleRepository.save(role);
                    logStoreNewEntity.add("Store role: " + code);
                }
                roles.add(role);
            }

            Role adminRole = roleRepository.findByCode("SUPER_ADMIN")
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setCode("SUPER_ADMIN");
                        role.setName("Administrator");
                        role.setDescription("This is the administrator");
                        role.setPermissions(Set.of());
                        role.setCreatedBy("SYSTEM");
                        role.setUpdatedBy("SYSTEM");
                        role.setChildRoles(roles);

                        logStoreNewEntity.add("Store new super admin");
                        return roleRepository.save(role);
                    });


            if (userRepository.findByUsername("admin").isEmpty()) {
                System.out.println("Auto initializing master user...");

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("admin"));
                user.setRoles(Set.of(adminRole));
                user.setEmail("admin@admin.com");
                user.setCreatedBy("SYSTEM");
                user.setUpdatedBy("SYSTEM");
                user = userRepository.save(user);

                logStoreNewEntity.add("Store user: " + user.getUsername());


                if (profileRepository.findByUser(user).isEmpty()) {
                    UserProfile userProfile = new UserProfile();
                    userProfile.setUser(user);
                    userProfile.setFirstName("Admin");
                    userProfile.setLastName("Admin");
                    userProfile.setCreatedBy("SYSTEM");
                    userProfile.setUpdatedBy("SYSTEM");

                    profileRepository.save(userProfile);
                    logStoreNewEntity.add("Store user profile: " + userProfile.getUser().getUsername());
                }
            }

            logStoreNewEntity.forEach(System.out::println);

            System.out.println("Setup permissions completed.");
        };
    }

    @Bean
    CommandLineRunner initEndpointSecurity(
            EndpointSecureRepository endpointSecureRepository
    ) {

        return args -> {

            Set<String> entities = scanEntities("com.vuog.core.module");

            for (String entity : entities) {

                entity = entity.substring(0, 1).toLowerCase() + entity.substring(1);
                String withoutId = "/api/" + entity;
                String withId = withoutId + "/{id}";
                String readPerm = "READ_" + entity.toUpperCase();
                String writePerm = "WRITE_" + entity.toUpperCase();

                if (endpointSecureRepository.findByEndpointPatternAndMethod(withoutId, "GET").isEmpty()) {
                    EndpointSecure getAllEndpoint = new EndpointSecure(withoutId, "GET", readPerm, false);
                    endpointSecureRepository.save(getAllEndpoint);
                }

                if (endpointSecureRepository.findByEndpointPatternAndMethod(withId, "GET").isEmpty()) {
                    EndpointSecure getDetailEndpoint = new EndpointSecure(withId, "GET", readPerm, false);
                    endpointSecureRepository.save(getDetailEndpoint);
                }

                if (endpointSecureRepository.findByEndpointPatternAndMethod(withId, "PUT").isEmpty()) {
                    EndpointSecure updateEndpoint = new EndpointSecure(withId, "PUT", writePerm, false);
                    endpointSecureRepository.save(updateEndpoint);
                }

                if (endpointSecureRepository.findByEndpointPatternAndMethod(withoutId, "POST").isEmpty()) {
                    EndpointSecure createAllEndpoint = new EndpointSecure(withoutId, "POST", writePerm, false);
                    endpointSecureRepository.save(createAllEndpoint);
                }

                if (endpointSecureRepository.findByEndpointPatternAndMethod(withId, "DELETE").isEmpty()) {
                    EndpointSecure deleteEndpoint = new EndpointSecure(withId, "DELETE", writePerm, false);
                    endpointSecureRepository.save(deleteEndpoint);
                }
            }

            if (endpointSecureRepository.findByEndpointPatternAndMethod("/api/**", "GET").isEmpty()) {
                EndpointSecure superRoleEndpoint = new EndpointSecure("/api/**", "GET", "SUPER_ADMIN", true);
                endpointSecureRepository.save(superRoleEndpoint);
            }

            System.out.println("Added endpoint secures");
        };
    }

    private Permission initPermission(String code, String name, String description) {
        Permission permission = new Permission();
        permission.setCode(code);
        permission.setName(name);
        permission.setDescription(description);
        permission.setCreatedBy("SYSTEM");
        permission.setUpdatedBy("SYSTEM");
        return permission;
    }

    private Set<String> scanEntities(String packageName) {
        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(jakarta.persistence.Entity.class);
        Set<String> entities = new HashSet<>();

        for (Class<?> entityClass : entityClasses) {
            entities.add(entityClass.getSimpleName());
        }

        return entities;
    }
}
