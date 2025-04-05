package com.example.readerai.service;

import com.example.readerai.converter.BookConverter;
import com.example.readerai.dto.BookDTO;
import com.example.readerai.dto.FileInfo;
import com.example.readerai.entity.Book;
import com.example.readerai.exception.ResourceNotFoundException;
import com.example.readerai.repository.BookRepository;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class BookService {

    private final MinioService minioService;

    private final UserService userService;

    private final BookRepository bookRepository;

    private final BookConverter bookConverter;

    public List<BookDTO> getAllBooks() {
        // TODO validate authority
        return bookRepository.findAll().stream()
                .map(bookConverter::toDTO)
                .collect(Collectors.toList());
    }

    public BookDTO getBookById(Long id) {
        // TODO validate authority
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return bookConverter.toDTO(book);
    }

    @Transactional
    public BookDTO uploadBook(String title, String author, MultipartFile file) throws IOException, MinioException {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/epub+zip") && !contentType.equals("application/pdf"))) {
            throw new IllegalArgumentException("Only EPUB and PDF files are allowed");
        }

        FileInfo fileInfo = minioService.uploadFile(file);

        Book book = Book.builder()
                .title(title)
                .author(author)
                .userId(userService.getUserId())
                .fileName(fileInfo.getFileName())
                .originalFileName(fileInfo.getOriginalFileName())
                .filePath(fileInfo.getFilePath())
                .fileSize(fileInfo.getFileSize())
                .fileType(fileInfo.getFileType())
                .build();

        book = bookRepository.save(book);

        return bookConverter.toDTO(book);
    }

    public InputStream getBookContent(Long id) throws MinioException {
        // TODO validate authority
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return minioService.getFile(book.getFileName());
    }

    @Transactional
    public BookDTO updateBook(Long id, String title, String author) {
        // TODO validate authority
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        book.setTitle(title);
        book.setAuthor(author);

        book = bookRepository.save(book);
        return bookConverter.toDTO(book);
    }

    @Transactional
    public void deleteBook(Long id) throws MinioException {
        // TODO validate authority
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        minioService.deleteFile(book.getFileName());

        bookRepository.delete(book);
    }
}
