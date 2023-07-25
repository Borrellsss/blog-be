package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface PostService {
    PostOutputDto create(PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByValidIsTrueAndUserDeletedIsFalseOrderByCommentsDesc(int page, int size);
    PostPageableOutputDto readAllByVotesIsTrueAndValidIsTrueAndUserDeletedIsFalseOrderByVotesDesc(int page, int size);
    PostPageableOutputDto readAllByValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String valid, int page, int size);
    PostPageableOutputDto readAllByTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String title, int page, int size);
    PostPageableOutputDto readAllByCategoryIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(Long categoryId, int page, int size);
    PostPageableOutputDto readAllByCategoryNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String categoryName, int page, int size);
    PostPageableOutputDto readAllByCategoryNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String categoryName, String title, int page, int size);
    PostPageableOutputDto readAllByTagsIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(Long tagId, int page, int size);
    PostPageableOutputDto readAllByTagsNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String tagName, int page, int size);
    PostPageableOutputDto readAllByTagsNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String tagName, String title, int page, int size);
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserIdAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(Long userId, String valid, int page, int size);
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserUsernameAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String username, Boolean valid, int page, int size);
    PostPageableOutputDto readAllByUserUsernameAndTitleContainingAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String username, String title, Boolean valid, int page, int size);
    PostOutputDto readById(Long id);
    PostOutputDto readByIdAndUserDeletedIsFalse(Long id);
    PostOutputDto readByTitleAndValidIsTrueAndUserDeletedIsFalse(String title);
    PostOutputDto update(Long id, PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void acceptOrReject(Long id, boolean valid);
    void delete(Long id);
}
