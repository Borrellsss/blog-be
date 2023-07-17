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
    private final CategoryFiller categoryFiller;
    private final TagFiller tagFiller;
    private final ValidationFiller validationFiller;
    private final ErrorMessageFiller errorMessageFiller;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        roleFiller.fillRoles();
        userFiller.fillUsers();
        categoryFiller.fillCategories();
        tagFiller.fillTags();
        validationFiller.fillValidations();
        errorMessageFiller.fillErrorMessages();
    }
}
