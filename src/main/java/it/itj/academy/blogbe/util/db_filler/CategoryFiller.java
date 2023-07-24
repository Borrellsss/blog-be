package it.itj.academy.blogbe.util.db_filler;

import it.itj.academy.blogbe.entity.Category;
import it.itj.academy.blogbe.repository.CategoryRepository;
import it.itj.academy.blogbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class CategoryFiller {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public void fillCategories() {
        categoryRepository.deleteAll(categoryRepository.findAll());
        Set<Category> categories = Set.of(
            new Category("Music", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. At, cumque deleniti dolorum,Lorem ipsum dolor sit amet, consectetur adipisicing elit. At, cumque deleniti dolorum,Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Sport", "          est eveniet expedita facilis fugiat incidunt laboriosam molestias          est eveniet expedita facilis fugiat incidunt laboriosam molestiasLorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Science", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Technology", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Politics", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Economy", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Health", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Food", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Travel", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Fashion", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Art", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Culture", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Education", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("History", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Nature", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Religion", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Philosophy", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Psychology", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Sociology", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Literature", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Cinema", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Theatre", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("TV", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Radio", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Internet", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Gaming", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Animals", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Plants", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Environment", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Weather", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Astronomy", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Mathematics", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Physics", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Chemistry", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Biology", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Geology", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Medicine", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Engineering", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Architecture", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Agriculture", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Business", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Finance", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Marketing", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Management", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Industry", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Transport", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Aviation", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate."),
            new Category("Automotive", "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Ad, aliquam animi aspernatur consequuntur cum cupiditate deleniti dignissimos earum eius eligendi fugiat incidunt inventore laborum pariatur provident tempore unde ut voluptate.")
        );
        categories.forEach(category -> {
            category.setCreatedBy(userRepository.findByUsername("d.giannetti").get().getId());
            category.setUpdatedBy(userRepository.findByUsername("d.giannetti").get().getId());
        });
        categoryRepository.saveAll(categories);
    }
}
