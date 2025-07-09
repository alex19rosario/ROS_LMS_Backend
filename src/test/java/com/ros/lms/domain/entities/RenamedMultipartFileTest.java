package com.ros.lms.domain.entities;

import com.ros.lms.domain.dtos.RenamedMultipartFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RenamedMultipartFileTest {

    private MultipartFile mockFile;
    private RenamedMultipartFile renamedFile;
    private final String renamedFilename = "renamed.txt";

    @BeforeEach
    void setUp() {
        mockFile = mock(MultipartFile.class);
        renamedFile = new RenamedMultipartFile(mockFile, renamedFilename);
    }

    @Test
    void getName_ShouldReturnOriginalName() {
        when(mockFile.getName()).thenReturn("originalName");
        assertEquals("originalName", renamedFile.getName());
    }

    @Test
    void getOriginalFilename_ShouldReturnRenamedFilename() {
        assertEquals(renamedFilename, renamedFile.getOriginalFilename());
    }

    @Test
    void getContentType_ShouldReturnOriginalContentType() {
        when(mockFile.getContentType()).thenReturn("text/plain");
        assertEquals("text/plain", renamedFile.getContentType());
    }

    @Test
    void isEmpty_ShouldReturnOriginalIsEmpty() {
        when(mockFile.isEmpty()).thenReturn(true);
        assertTrue(renamedFile.isEmpty());

        when(mockFile.isEmpty()).thenReturn(false);
        assertFalse(renamedFile.isEmpty());
    }

    @Test
    void getSize_ShouldReturnOriginalSize() {
        when(mockFile.getSize()).thenReturn(12345L);
        assertEquals(12345L, renamedFile.getSize());
    }

    @Test
    void getBytes_ShouldDelegateToOriginalFile() throws Exception {
        byte[] data = "hello".getBytes();
        when(mockFile.getBytes()).thenReturn(data);
        assertArrayEquals(data, renamedFile.getBytes());
    }

    @Test
    void getInputStream_ShouldDelegateToOriginalFile() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("data".getBytes());
        when(mockFile.getInputStream()).thenReturn(inputStream);
        assertEquals(inputStream, renamedFile.getInputStream());
    }

    @Test
    void transferTo_ShouldDelegateToOriginalFile() throws Exception {
        File dest = mock(File.class);
        doNothing().when(mockFile).transferTo(dest);

        renamedFile.transferTo(dest);

        verify(mockFile).transferTo(dest);
    }
}
