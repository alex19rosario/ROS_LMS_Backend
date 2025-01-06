package com.ros.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "GENRES")
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GENRE_ID")
    private long id;
    @Column(name = "DESCRIPTION")
    private String description;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "BOOKS_GENRES",
            joinColumns = @JoinColumn(name = "GENRE_ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
    private List<Book> books;

    public Genre(){}

    public Genre(String description, List<Book> books) {
        this.description = description;
        this.books = books;
    }

    public Genre(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
