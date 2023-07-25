package it.itj.academy.blogbe.service;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;

import java.lang.reflect.InvocationTargetException;

public interface PostService {
    PostOutputDto create(PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    PostPageableOutputDto readAllByValidIsTrueOrderByCommentsDesc(int page, int size);
    PostPageableOutputDto readAllByVotesIsTrueAndValidIsTrueOrderByVotesDesc(int page, int size);
    PostPageableOutputDto readAllByValidOrderByCreatedAtDesc(String valid, int page, int size);
    PostPageableOutputDto readAllByTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String title, int page, int size);
    PostPageableOutputDto readAllByCategoryIdAndValidIsTrueOrderByCreatedAtDesc(Long categoryId, int page, int size);
    PostPageableOutputDto readAllByCategoryNameAndValidIsTrueOrderByCreatedAtDesc(String categoryName, int page, int size);
    PostPageableOutputDto readAllByCategoryNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String categoryName, String title, int page, int size);
    PostPageableOutputDto readAllByTagsIdAndValidIsTrueOrderByCreatedAtDesc(Long tagId, int page, int size);
    PostPageableOutputDto readAllByTagsNameAndValidIsTrueOrderByCreatedAtDesc(String tagName, int page, int size);
    PostPageableOutputDto readAllByTagsNameAndTitleContainingAndValidIsTrueOrderByCreatedAtDesc(String tagName, String title, int page, int size);
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserIdAndValidOrderByCreatedAtDesc(Long userId, String valid, int page, int size);
    it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserUsernameAndValidOrderByCreatedAtDesc(String username, Boolean valid, int page, int size);
    PostPageableOutputDto readAllByUserUsernameAndTitleContainingAndValidOrderByCreatedAtDesc(String username, String title, Boolean valid, int page, int size);
    PostOutputDto readById(Long id);
    PostOutputDto readByTitleAndValidIsTrue(String title);
    PostOutputDto update(Long id, PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException;
    void acceptOrReject(Long id, boolean valid);
    void delete(Long id);
}
