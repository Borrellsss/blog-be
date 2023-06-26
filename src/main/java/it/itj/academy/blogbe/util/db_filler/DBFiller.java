package it.itj.academy.blogbe.util.db_filler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DBFiller {
    private final RoleFiller roleFiller;
    private final UserFiller userFiller;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        roleFiller.fillRoles();
        userFiller.fillUsers();
    }
}
