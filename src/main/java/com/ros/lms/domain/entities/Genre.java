package com.ros.lms.domain.entities;

import com.ros.lms.domain.enums.GenreType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;

@NullMarked
@Entity
@Table(name = "GENRE_TYPE")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GENRE_ID")
    private long id;

    @NotNull
    @Column(name = "LABEL")
    private GenreType label;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "BOOKS_GENRES",
            joinColumns = @JoinColumn(name = "GENRE_ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
    private List<Book> books = new ArrayList<>();


    public Genre(){}

    public Genre(GenreType label, List<Book> books) {
        this.label = label;
        this.books = books;
    }

    public Genre(GenreType label) {
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GenreType getLabel() {
        return label;
    }

    public void setLabel(GenreType label) {
        this.label = label;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "label='" + label.getVal() + '\'' +
                ", books=" + books +
                '}';
    }
}
