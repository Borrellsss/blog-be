package it.itj.academy.blogbe.service.impl;

import it.itj.academy.blogbe.dto.ErrorMessageInputDto;
import it.itj.academy.blogbe.dto.ErrorMessageOutputDto;
import it.itj.academy.blogbe.dto.ErrorMessagePageableOutputDto;
import it.itj.academy.blogbe.entity.ErrorMessage;
import it.itj.academy.blogbe.repository.ErrorMessageRepository;
import it.itj.academy.blogbe.repository.ValidationRepository;
import it.itj.academy.blogbe.service.ErrorMessageService;
import it.itj.academy.blogbe.util.PageableUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ErrorMessageServiceImpl implements ErrorMessageService {
    private final ErrorMessageRepository errorMessageRepository;
    private final ValidationRepository validationRepository;
    private final ModelMapper modelMapper;
    private final PageableUtil pageableUtil;

    @Override
    public ErrorMessageOutputDto create(ErrorMessageInputDto errorMessageInputDto) {
        ErrorMessage errorMessage = modelMapper.map(errorMessageInputDto, ErrorMessage.class);
        errorMessage.setValidation(validationRepository.findByCode(errorMessageInputDto.getValidation())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Validation with code %s not found", errorMessageInputDto.getValidation()))));
        return modelMapper.map(errorMessageRepository.save(errorMessage), ErrorMessageOutputDto.class);
    }
    @Override
    public ErrorMessagePageableOutputDto readAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ErrorMessage> errorsMessages = errorMessageRepository.findAll(pageable);
        return pageableUtil.errorMessagePageableOutputDto(errorsMessages);
    }
    @Override
    public ErrorMessageOutputDto readById(Long id) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        return modelMapper.map(errorMessage, ErrorMessageOutputDto.class);
    }
    @Override
    public ErrorMessageOutputDto update(Long id, ErrorMessageInputDto errorMessageInputDto) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        errorMessage.setMessage(errorMessageInputDto.getMessage());
        errorMessage.setValidation(validationRepository.findByCode(errorMessageInputDto.getValidation())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Validation with code %s not found", errorMessageInputDto.getValidation()))));
        return modelMapper.map(errorMessageRepository.save(errorMessage), ErrorMessageOutputDto.class);
    }
    @Override
    public void delete(Long id) {
        ErrorMessage errorMessage = errorMessageRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Error message with id %d not found", id)));
        errorMessageRepository.delete(errorMessage);
    }
}
