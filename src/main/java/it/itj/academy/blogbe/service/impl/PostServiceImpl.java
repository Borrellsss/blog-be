package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.input.PostInputDto;
import it.itj.academy.blogbe.dto.output.post.PostOutputDto;
import it.itj.academy.blogbe.dto.output.post.PostPageableOutputDto;
import it.itj.academy.blogbe.entity.Category;
import it.itj.academy.blogbe.entity.Post;
import it.itj.academy.blogbe.entity.Tag;
import it.itj.academy.blogbe.entity.User;
import it.itj.academy.blogbe.exception.CustomInvalidFieldException;
import it.itj.academy.blogbe.repository.CategoryRepository;
import it.itj.academy.blogbe.repository.PostRepository;
import it.itj.academy.blogbe.repository.TagRepository;
import it.itj.academy.blogbe.service.PostService;
import it.itj.academy.blogbe.util.PageableUtil;
import it.itj.academy.blogbe.util.ValidatorUtil;
import it.itj.academy.blogbe.util.email.post.PostDeletedEmailUtil;
import it.itj.academy.blogbe.util.email.post.PostValidatedEmailUtil;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Collection;
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
    private final ValidatorUtil validatorUtil;
    private final PageableUtil pageableUtil;
    private final PostValidatedEmailUtil postValidatedEmailUtil;
    private final PostDeletedEmailUtil postDeletedEmailUtil;

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                String.format(
                    "Tag with ids %s do not match the category %s",
                    remainingTagIds.stream()
                        .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                String.format("Tag with id %d not found", tagId)))
                                .getName())
                        .toList(),
                    category.getName())
            );
        }
        Post post = modelMapper.map(postInputDto, Post.class);
        post.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        post.setCategory(category);
        post.setTags(tagRepository.findAllById(postInputDto.getTags()));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRole().getAuthority().equals("ROLE_SUPER_ADMIN")) {
            post.setValid(true);
            post.setVerifiedBy(user.getId());
            post.setVerifiedAt(LocalDate.now());
        }
        return modelMapper.map(postRepository.save(post), PostOutputDto.class);
    }
    @Override
    public PostPageableOutputDto readAllByValidIsTrueAndUserDeletedIsFalseOrderByCommentsDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByValidIsTrueAndUserDeletedIsFalseOrderByCommentsDesc(pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByVotesIsTrueAndValidIsTrueAndUserDeletedIsFalseOrderByVotesDesc(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByVotesIsTrueAndValidIsTrueAndUserDeletedIsFalseOrderByVotesDesc(pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String valid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Boolean validBoolean = switch (valid) {
            case "true" -> true;
            case "false" -> false;
            case "null" -> null;
            default ->
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Valid field must be true, false or null, not %s", valid));
        };
        Page<Post> posts = postRepository.findAllByValidAndUserDeletedIsFalseOrderByCreatedAtDesc(validBoolean, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(title, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByCategoryIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCategoryIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(categoryId, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByCategoryNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCategoryNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(categoryName, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByCategoryNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String categoryName, String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCategoryNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(categoryName, title, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByTagsIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(Long tagId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTagsIdAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(tagId, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByTagsNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String tagName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTagsNameAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(tagName, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostPageableOutputDto readAllByTagsNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(String tagName, String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTagsNameAndTitleContainingAndValidIsTrueAndUserDeletedIsFalseOrderByCreatedAtDesc(tagName, title, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserIdAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(Long userId, String valid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Boolean validBoolean = switch (valid) {
            case "true" -> true;
            case "false" -> false;
            case "null" -> null;
            default ->
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Valid field must be true, false or null, not %s", valid));
        };
        Page<Post> posts = postRepository.findAllByUserIdAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(userId, validBoolean, pageable);
        it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto postPageableOutputDto = new it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto();
        postPageableOutputDto.setPosts(posts.getContent()
            .stream()
            .map(post -> modelMapper.map(post,
                it.itj.academy.blogbe.dto.output.user.PostOutputDto.class))
            .toList());
        return postPageableOutputDto;
    }
    @Override
    public it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserUsernameAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String username, Boolean valid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByUserUsernameAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(username, valid, pageable);
        it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto postPageableOutputDto = new it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto();
        postPageableOutputDto.setPosts(posts.getContent()
            .stream()
            .map(post -> modelMapper.map(post,
                it.itj.academy.blogbe.dto.output.user.PostOutputDto.class))
            .toList());
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByUserUsernameAndTitleContainingAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(String username, String title, Boolean valid, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByUserUsernameAndTitleContainingAndValidAndUserDeletedIsFalseOrderByCreatedAtDesc(username, title, valid, pageable);
        return pageableUtil.postPageableOutputDto(posts);
    }
    @Override
    public PostOutputDto readById(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        return modelMapper.map(post, PostOutputDto.class);
    }

    @Override
    public PostOutputDto readByIdAndUserDeletedIsFalse(Long id) {
        Post post = postRepository.findByIdAndUserDeletedIsFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        return modelMapper.map(post, PostOutputDto.class);
    }

    @Override
    public PostOutputDto readByTitleAndValidIsTrueAndUserDeletedIsFalse(String title) {
        Post post = postRepository.findByTitleAndValidIsTrueAndUserDeletedIsFalse(title)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with title %s not found", title)));
        return modelMapper.map(post, PostOutputDto.class);
    }
    @Override
    public PostOutputDto update(Long id, PostInputDto postInputDto) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        Map<String, String> errors = validatorUtil.validate(postInputDto);
        if (!errors.isEmpty()) {
            throw new CustomInvalidFieldException(errors);
        }
        if (!postInputDto.getTitle().equals(post.getTitle()) && postRepository.existsByTitle(postInputDto.getTitle())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format("Post with title %s already exists", postInputDto.getTitle()));
        }
        post.setContent(postInputDto.getContent());
        if (post.getValid() != null && !post.getValid()) {
            post.setValid(null);
        }
        return modelMapper.map(postRepository.save(post), PostOutputDto.class);
    }
    @Override
    public void acceptOrReject(Long id, boolean valid) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getId().equals(post.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot accept or reject your own post");
        }
        post.setValid(valid);
        post.setVerifiedBy(user.getId());
        post.setVerifiedAt(LocalDate.now());
        try {
            postValidatedEmailUtil.sendEmail(post.getUser().getEmail(), post);
        } catch (MessagingException e) {
            log.error("Error sending email: {}", e.getMessage());
        }
        postRepository.save(post);
    }
    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!post.getUser().getId().equals(user.getId()) && user.getRole().getAuthority().equals("ROLE_MODERATOR")) {
            try {
                postDeletedEmailUtil.sendEmail(post.getUser().getEmail(), post);
            } catch (MessagingException e) {
                log.error("Error sending email: {}", e.getMessage());
            }
        }
        postRepository.delete(post);
    }
}
