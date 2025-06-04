package com.ros.lms.ports.inbound.service_contracts;

import com.ros.lms.domain.exceptions.StorageException;
import com.ros.lms.domain.exceptions.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init() throws StorageException;
    void store(MultipartFile file) throws StorageException;
    Stream<Path> loadAll() throws StorageException;
    Path load(String filename);
    Resource loadAsResource(String filename) throws StorageFileNotFoundException;
    void deleteAll();
}
