package it.itj.academy.blogbe.util.db_filler;

import it.itj.academy.blogbe.entity.Role;
import it.itj.academy.blogbe.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class RoleFiller {
    private final RoleRepository roleRepository;

    public void fillRoles() {
        roleRepository.deleteAll(roleRepository.findAll());
        Set<Role> roles = Set.of(
            new Role("ROLE_USER"),
            new Role("ROLE_MODERATOR"),
            new Role("ROLE_ADMIN"),
            new Role("ROLE_SUPER_ADMIN")
        );
        roleRepository.saveAll(roles);
    }
}
