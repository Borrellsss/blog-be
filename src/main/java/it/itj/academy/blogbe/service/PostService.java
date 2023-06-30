package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface PostService {
    PostOutputDto create(PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByOrderByCreatedAtDesc(int page, int size);
    PostOutputDto readByTitle(String title);
    PostPageableOutputDto readByTitleContaining(String title, int page, int size);
    PostPageableOutputDto readByCategoryId(Long categoryId, int page, int size);
    PostPageableOutputDto readByCategoryName(String categoryName, int page, int size);
    PostPageableOutputDto readByTagId(Long tagId, int page, int size);
    PostPageableOutputDto readByTagName(String tagName, int page, int size);
    PostPageableOutputDto readByUserId(Long userId, int page, int size);
    PostPageableOutputDto readByUsername(String username, int page, int size);
    PostPageableOutputDto readByValidIsFalse(int page, int size);
    PostOutputDto update(Long id, PostInputDto postInputDto);
    void delete(Long id);
}
