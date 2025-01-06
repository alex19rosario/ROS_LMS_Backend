package com.ros.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="BOOKS")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private long id;
    @Column(name = "ISBN")
    private long ISBN;
    @Column(name = "TITLE")
    private String title;
    @Column(name = "IS_AVAILABLE")
    private char isAvailable;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "BOOKS_AUTHORS",
            joinColumns = @JoinColumn(name = "BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "AUTHOR_ID"))
    private List<Author> authors;
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "BOOKS_GENRES",
            joinColumns = @JoinColumn(name = "BOOK_ID"),
            inverseJoinColumns = @JoinColumn(name = "GENRE_ID"))
    private List<Genre> genres;

    public Book(){}
    public Book(long ISBN, String title, char isAvailable, List<Author> authors, List<Genre> genres) {
        this.ISBN = ISBN;
        this.title = title;
        this.isAvailable = isAvailable;
        this.authors = authors;
        this.genres = genres;
    }

    public Book(long ISBN, String title, char isAvailable) {
        this.ISBN = ISBN;
        this.title = title;
        this.isAvailable = isAvailable;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getISBN() {
        return ISBN;
    }

    public void setISBN(long ISBN) {
        this.ISBN = ISBN;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public char isAvailable() {
        return isAvailable;
    }

    public void setAvailable(char available) {
        isAvailable = available;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void addAuthor(Author author){
        if(this.authors == null){
            this.authors = new ArrayList<>();
        }
        this.authors.add(author);
    }

    public void addGenre(Genre genre){
        if(this.genres == null){
            this.genres = new ArrayList<>();
        }
        this.genres.add(genre);
    }
}
