package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface PostService {
    PostOutputDto create(PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByOrderByCommentsDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByVotesIsTrueOrderByVotesDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByOrderByCreatedAtDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTitleContainingOrderByCreatedAtDesc(String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByCategoryIdOrderByCreatedAtDesc(Long categoryId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByCategoryNameOrderByCreatedAtDesc(String categoryName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByCategoryNameAndTitleContainingOrderByCreatedAtDesc(String categoryName, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTagsIdOrderByCreatedAtDesc(Long tagId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTagsNameOrderByCreatedAtDesc(String tagName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTagsNameAndTitleContainingOrderByCreatedAtDesc(String tagName, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserIdOrderByCreatedAtDesc(Long userId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserUsernameOrderByCreatedAtDesc(String username, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByUserUsernameAndTitleContainingOrderByCreatedAtDesc(String username, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByValidOrderByCreatedAtDesc(Boolean valid, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostOutputDto readById(Long id);
    PostOutputDto readByTitle(String title);
    PostOutputDto update(Long id, PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void acceptOrReject(Long id, boolean valid);
    void delete(Long id);
}
