package com.example.readerai.service;

import com.example.readerai.converter.AccessConverter;
import com.example.readerai.converter.BookConverter;
import com.example.readerai.dto.AccessDTO;
import com.example.readerai.dto.BookDTO;
import com.example.readerai.dto.BookWithDetails;
import com.example.readerai.dto.FileInfo;
import com.example.readerai.entity.Access;
import com.example.readerai.entity.Book;
import com.example.readerai.exception.NotFoundException;
import com.example.readerai.exception.PermissionDeniedException;
import com.example.readerai.exception.ResourceNotFoundException;
import com.example.readerai.repository.AccessRepository;
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
import java.util.Objects;
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

    private final AccessRepository accessRepository;

    private final AccessConverter accessConverter;

    public List<BookDTO> getAllBooks() {
        return bookRepository.findByUserId(userService.getUserId()).stream()
                .map(bookConverter::toDTO)
                .collect(Collectors.toList());
    }

    public List<BookDTO> getAllBooksByParticipantId(Long participantId) {
        List<Access> accesses = accessRepository.findByParticipant_Id(participantId);
        return accesses.stream().map((access -> bookConverter.toDTO(access.getBook()))).toList();
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id: " + id));
        if (!Objects.equals(book.getUserId(), userService.getUserId())) {
            throw new PermissionDeniedException("You do not have permission to access this book");
        }
        return bookConverter.toDTO(book);
    }

    public BookWithDetails getBookWithDetails(Long bookId) {
        BookDTO book = getBookById(bookId);
        BookWithDetails bookWithDetails = bookConverter.fromDTOtoWithDetails(book);


        List<Access> accesses = accessRepository.findByBookId(bookId);
        List<AccessDTO> accessDTOS = accesses.stream()
                .map(accessConverter::toDTO)
                .toList();
        bookWithDetails.setAccesses(accessDTOS);

        return bookWithDetails;
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
