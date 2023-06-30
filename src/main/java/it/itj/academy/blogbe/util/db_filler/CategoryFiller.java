package it.itj.academy.blogbe.util.db_filler;

import it.itj.academy.blogbe.entity.Category;
import it.itj.academy.blogbe.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@RequiredArgsConstructor
@Component
public class CategoryFiller {
    private final CategoryRepository categoryRepository;

    public void fillCategories() {
        categoryRepository.deleteAll(categoryRepository.findAll());
        Set<Category> categories = Set.of(
            new Category("Music"),
            new Category("Sport"),
            new Category("Science"),
            new Category("Technology"),
            new Category("Politics"),
            new Category("Economy"),
            new Category("Health"),
            new Category("Food"),
            new Category("Travel"),
            new Category("Fashion"),
            new Category("Art"),
            new Category("Culture"),
            new Category("Education"),
            new Category("History"),
            new Category("Nature"),
            new Category("Religion"),
            new Category("Philosophy"),
            new Category("Psychology"),
            new Category("Sociology"),
            new Category("Literature"),
            new Category("Cinema"),
            new Category("Theatre"),
            new Category("TV"),
            new Category("Radio"),
            new Category("Internet"),
            new Category("Gaming"),
            new Category("Animals"),
            new Category("Plants"),
            new Category("Environment"),
            new Category("Weather"),
            new Category("Astronomy"),
            new Category("Mathematics"),
            new Category("Physics"),
            new Category("Chemistry"),
            new Category("Biology"),
            new Category("Geology"),
            new Category("Medicine"),
            new Category("Engineering"),
            new Category("Architecture"),
            new Category("Agriculture"),
            new Category("Business"),
            new Category("Finance"),
            new Category("Marketing"),
            new Category("Management"),
            new Category("Industry"),
            new Category("Transport"),
            new Category("Aviation"),
            new Category("Automotive")
        );
        categories.forEach(category -> {
            category.setCreatedBy(1L);
            category.setUpdatedBy(1L);
        });
        categoryRepository.saveAll(categories);
    }
}
