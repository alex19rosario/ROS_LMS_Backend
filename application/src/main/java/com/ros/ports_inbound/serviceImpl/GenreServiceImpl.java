package com.ros.ports_inbound.serviceImpl;

import com.ros.ports_inbound.service.GenreService;
import com.ros.ports_outbound.dao.GenreDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreDAO genreDAO;

    @Autowired
    public GenreServiceImpl(@Qualifier("genreDAOJpaImpl")GenreDAO genreDAO) {
        this.genreDAO = genreDAO;
    }

    @Override
    public Set<String> getAll() {
        return genreDAO.findAll();
    }
}
