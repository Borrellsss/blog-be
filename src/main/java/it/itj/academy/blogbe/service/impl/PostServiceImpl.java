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
import it.itj.academy.blogbe.util.OutputDtoResponseUtil;
import it.itj.academy.blogbe.util.PageableUtil;
import it.itj.academy.blogbe.util.ValidatorUtil;
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
        Post post = modelMapper.map(postInputDto, Post.class);
        post.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        post.setCategory(category);
        post.setTags(tagRepository.findAllById(postInputDto.getTags()));
        return modelMapper.map(postRepository.save(post), PostOutputDto.class);
    }
    @Override
    public PostPageableOutputDto readAllByOrderByCommentsDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByCommentsDesc(pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByVotesIsTrueOrderByVotesDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByVotesIsTrueOrderByVotesDesc(pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByOrderByCreatedAtDesc(int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByTitleContainingOrderByCreatedAtDesc(String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTitleContainingOrderByCreatedAtDesc(title, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByCategoryIdOrderByCreatedAtDesc(Long categoryId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCategoryIdOrderByCreatedAtDesc(categoryId, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByCategoryNameOrderByCreatedAtDesc(String categoryName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCategoryNameOrderByCreatedAtDesc(categoryName, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByCategoryNameAndTitleContainingOrderByCreatedAtDesc(String categoryName, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByCategoryNameAndTitleContainingOrderByCreatedAtDesc(categoryName, title, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByTagsIdOrderByCreatedAtDesc(Long tagId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTagsIdOrderByCreatedAtDesc(tagId, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByTagsNameOrderByCreatedAtDesc(String tagName, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTagsNameOrderByCreatedAtDesc(tagName, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByTagsNameAndTitleContainingOrderByCreatedAtDesc(String tagName, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByTagsNameAndTitleContainingOrderByCreatedAtDesc(tagName, title, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserIdOrderByCreatedAtDesc(Long userId, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto postPageableOutputDto = new it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto();
        postPageableOutputDto.setPosts(posts.getContent()
            .stream()
            .map(post -> modelMapper.map(post,
                it.itj.academy.blogbe.dto.output.user.PostOutputDto.class))
            .toList());
        return postPageableOutputDto;
    }
    @Override
    public it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto readAllByUserUsernameOrderByCreatedAtDesc(String username, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByUserUsernameOrderByCreatedAtDesc(username, pageable);
        it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto postPageableOutputDto = new it.itj.academy.blogbe.dto.output.user.PostPageableOutputDto();
        postPageableOutputDto.setPosts(posts.getContent()
            .stream()
            .map(post -> modelMapper.map(post,
                it.itj.academy.blogbe.dto.output.user.PostOutputDto.class))
            .toList());
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByUserUsernameAndTitleContainingOrderByCreatedAtDesc(String username, String title, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByUserUsernameAndTitleContainingOrderByCreatedAtDesc(username, title, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostPageableOutputDto readAllByValidOrderByCreatedAtDesc(Boolean valid, int page, int size) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAllByValidOrderByCreatedAtDesc(valid, pageable);
        PostPageableOutputDto postPageableOutputDto = pageableUtil.postPageableOutputDto(posts);
        outputDtoResponseUtil.filter(postPageableOutputDto);
        return postPageableOutputDto;
    }
    @Override
    public PostOutputDto readById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        return modelMapper.map(post, PostOutputDto.class);
    }
    @Override
    public PostOutputDto readByTitle(String title) {
        Post post = postRepository.findByTitle(title)
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
        post.setTitle(postInputDto.getTitle());
        post.setContent(postInputDto.getContent());
        return modelMapper.map(postRepository.save(post), PostOutputDto.class);
    }
    @Override
    public void acceptOrReject(Long id, boolean valid) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        post.setValid(valid);
        postRepository.save(post);
    }
    @Override
    public void delete(Long id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Post with id %d not found", id)));
        postRepository.delete(post);
    }
}
