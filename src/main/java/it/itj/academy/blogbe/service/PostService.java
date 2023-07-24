package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;
import jakarta.mail.MessagingException;

import java.lang.reflect.InvocationTargetException;

public interface PostService {
    PostOutputDto create(PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByValidIsTrueOrderByCommentsDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByVotesIsTrueAndValidIsTrueOrderByVotesDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByValidOrderByCreatedAtDesc(String valid, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByCategoryIdAndValidIsTrueOrderByCreatedAtDesc(Long categoryId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByCategoryNameAndValidIsTrueOrderByCreatedAtDesc(String categoryName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByCategoryNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String categoryName, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTagsIdAndValidIsTrueOrderByCreatedAtDesc(Long tagId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTagsNameAndValidIsTrueOrderByCreatedAtDesc(String tagName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByTagsNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String tagName, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserIdAndValidOrderByCreatedAtDesc(Long userId, String valid, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserUsernameAndValidOrderByCreatedAtDesc(String username, Boolean valid, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByUserUsernameAndTitleContainingAndValidOrderByCreatedAtDesc(String username, String title, Boolean valid, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostOutputDto readById(Long id);
    PostOutputDto readByTitleAndValidIsTrue(String title);
    PostOutputDto update(Long id, PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void acceptOrReject(Long id, boolean valid) throws MessagingException;
    void delete(Long id) throws MessagingException;
}
