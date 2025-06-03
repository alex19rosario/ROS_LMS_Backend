package com.ros.lms.application;

import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.domain.exceptions.StorageFileNotFoundException;
import com.ros.lms.infraestructure.config.StorageProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FileSystemStorageServiceTest {

    @TempDir
    Path tempDir;

    private FileSystemStorageService storageService;
    private StorageProperties properties;

    @BeforeEach
    void setUp() throws StorageException {
        properties = new StorageProperties();
        properties.setLocation(tempDir.toString());
        storageService = new FileSystemStorageService(properties);
        storageService.init();
    }

    @Test
    void init_ShouldCreateStorageDirectory() {
        assertTrue(Files.exists(tempDir));
    }

    @Test
    void store_WithValidFile_ShouldStoreFile() throws IOException, StorageException {
        // Arrange
        String content = "test content";
        String fileName = "test.txt";
        MultipartFile file = new MockMultipartFile(fileName, fileName, "text/plain", content.getBytes());

        // Act
        storageService.store(file);

        // Assert
        Path storedFile = tempDir.resolve(fileName);
        assertTrue(Files.exists(storedFile));
        assertEquals(content, Files.readString(storedFile));
    }

    @Test
    void store_WithEmptyFile_ShouldThrowException() {
        // Arrange
        MultipartFile emptyFile = new MockMultipartFile("empty", new byte[0]);

        // Act & Assert
        assertThrows(StorageException.class, () -> storageService.store(emptyFile));
    }

    @Test
    void store_WithRelativePath_ShouldThrowSecurityException() {
        // Arrange
        MultipartFile maliciousFile = mock(MultipartFile.class);
        when(maliciousFile.getOriginalFilename()).thenReturn("../malicious.txt");
        when(maliciousFile.isEmpty()).thenReturn(false);

        // Act & Assert
        assertThrows(StorageException.class, () -> storageService.store(maliciousFile));
    }

    @Test
    void loadAll_ShouldReturnAllStoredFiles() throws IOException, StorageException {
        // Arrange
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.txt"));

        // Act
        Stream<Path> loadedFiles = storageService.loadAll();

        // Assert
        assertEquals(2, loadedFiles.count());
    }

    @Test
    void loadAll_WhenIOExceptionOccurs_ShouldThrowStorageException() throws IOException {
        // Arrange
        // Delete the tempDir to force IOException when walking the path
        Files.delete(tempDir);

        // Act & Assert
        assertThrows(StorageException.class, () -> storageService.loadAll());
    }

    @Test
    void load_ShouldReturnCorrectPath() {
        // Arrange
        String fileName = "test.txt";

        // Act
        Path loadedPath = storageService.load(fileName);

        // Assert
        assertEquals(tempDir.resolve(fileName), loadedPath);
    }

    @Test
    void loadAsResource_WithExistingFile_ShouldReturnResource() throws IOException, StorageFileNotFoundException {
        // Arrange
        String fileName = "test.txt";
        Files.createFile(tempDir.resolve(fileName));

        // Act
        Resource resource = storageService.loadAsResource(fileName);

        // Assert
        assertTrue(resource.exists());
        assertTrue(resource.isReadable());
    }

    @Test
    void loadAsResource_WithNonExistingFile_ShouldThrowException() {
        // Arrange
        String fileName = "nonexistent.txt";

        // Act & Assert
        assertThrows(StorageFileNotFoundException.class, () -> storageService.loadAsResource(fileName));
    }

    @Test
    void loadAsResource_WhenMalformedURLExceptionOccurs_ShouldThrowStorageFileNotFoundException() {
        // Arrange
        String badFileName = "invalid://file";

        // Act & Assert
        assertThrows(StorageFileNotFoundException.class, () -> storageService.loadAsResource(badFileName));
    }

    @Test
    void constructor_WithEmptyLocation_ShouldThrowException() {
        // Arrange
        StorageProperties emptyProperties = new StorageProperties();
        emptyProperties.setLocation("");

        // Act & Assert
        assertThrows(StorageException.class, () -> new FileSystemStorageService(emptyProperties));
    }

    @Test
    void store_WhenIOExceptionOccurs_ShouldThrowStorageException() throws IOException {
        // Arrange
        MultipartFile mockFile = mock(MultipartFile.class);
        when(mockFile.isEmpty()).thenReturn(false);
        when(mockFile.getOriginalFilename()).thenReturn("test.txt");
        when(mockFile.getInputStream()).thenThrow(new IOException("Simulated IO Error"));

        // Act & Assert
        StorageException exception = assertThrows(StorageException.class, () -> storageService.store(mockFile));
        assertTrue(exception.getMessage().contains("Failed to store file."));
    }

    @Test
    void deleteAll_ShouldDeleteAllFiles() throws IOException {
        // Arrange
        Files.createFile(tempDir.resolve("file1.txt"));
        assertTrue(Files.exists(tempDir.resolve("file1.txt")));

        // Act
        storageService.deleteAll();

        // Assert
        assertFalse(Files.exists(tempDir.resolve("file1.txt")));
    }

    @Test
    void init_WhenIOExceptionOccurs_ShouldThrowStorageException() throws IOException, StorageException {
        // Arrange
        Path fileInsteadOfDir = tempDir.resolve("notADir.txt");
        Files.createFile(fileInsteadOfDir); // Create a file to simulate path conflict

        StorageProperties badProps = new StorageProperties();
        badProps.setLocation(fileInsteadOfDir.toString());

        FileSystemStorageService faultyStorage = new FileSystemStorageService(badProps);

        // Act & Assert
        assertThrows(StorageException.class, faultyStorage::init);
    }


}
