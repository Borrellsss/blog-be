package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;
import it.itj.academy.blogbe.entity.Category;
import it.itj.academy.blogbe.entity.Tag;
import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.repository.CategoryRepository;
import it.itj.academy.blogbe.repository.PostRepository;
import it.itj.academy.blogbe.repository.TagRepository;
import it.itj.academy.blogbe.service.PostService;
import it.itj.academy.blogbe.util.OutputDtoResponseUtil;
import it.itj.academy.blogbe.util.PageableUtil;
import it.itj.academy.blogbe.util.ValidatorUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final ModelMapper modelMapper;
    private final OutputDtoResponseUtil outputDtoResponseUtil;
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;

    @Override
    public PostOutputDto create(PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Map<String, String> errors = validatorUtil.validate(postInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        if (postRepository.existsByTitle(postInputDto.getTitle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Post with title %s already exists", postInputDto.getTitle()));
        }
        if (postInputDto.getValid() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid field cannot be set during creation");
        }
        Category category = categoryRepository.findById(postInputDto.getCategory())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Category with id %d not found", postInputDto.getCategory())));
        List<Long> tagIds = category.getTags().stream()
            .map(Tag::getId)
            .toList();
        List<Long> remainingTagIds = postInputDto.getTags().stream()
            .filter(tagId -> !tagIds.contains(tagId))
            .toList();
        if (!remainingTagIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Tag with ids %s do not match the category %s", remainingTagIds, category.getName()));
        }
        return null;
    }

    @Override
    public PostPageableOutputDto readAllByOrderByCreatedAtDesc(int page, int size) {
        return null;
    }

    @Override
    public PostOutputDto readByTitle(String title) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByTitleContaining(String title, int page, int size) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByCategoryId(Long categoryId, int page, int size) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByCategoryName(String categoryName, int page, int size) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByTagId(Long tagId, int page, int size) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByTagName(String tagName, int page, int size) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByUserId(Long userId, int page, int size) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByUsername(String username, int page, int size) {
        return null;
    }

    @Override
    public PostPageableOutputDto readByValidIsFalse(int page, int size) {
        return null;
    }

    @Override
    public PostOutputDto update(Long id, PostInputDto postInputDto) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
