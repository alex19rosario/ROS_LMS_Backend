package com.ros.lms.application;

import com.ros.lms.domain.enums.GenreType;
import com.ros.lms.ports.inbound.service_contracts.GenreService;
import com.ros.lms.ports.outbound.repository_contracts.GenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDAO genreDAO;

    @Autowired
    public GenreServiceImpl(@Qualifier("genreDAOJpaImpl")GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    @Override
    public Set<String> getAll() {
        return genreDAO.findAll().stream()
                .map(GenreType::getVal)
                .collect(Collectors.toSet());
    }
}
