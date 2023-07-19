package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface PostService {
    PostOutputDto create(PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByOrderByCreatedAtDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostOutputDto readByTitle(String title);
    PostPageableOutputDto readByTitleContaining(String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readByCategoryId(Long categoryId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readByCategoryName(String categoryName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readByTagId(Long tagId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readByTagName(String tagName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readByUserId(Long userId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readByUserUsername(String username, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readByValidIsFalse(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostOutputDto update(Long id, PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void acceptOrReject(Long id, boolean valid);
    void delete(Long id);
}
