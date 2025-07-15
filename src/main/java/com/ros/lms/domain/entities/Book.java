package com.ros.lms.domain.entities;

import com.ros.lms.domain.converters.CharToBooleanConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="BOOKS")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOK_ID")
    private long id;

    @Size(min = 10, max = 13, message = "ISBN must be between 10 and 13 characters long")
    @Column(name = "ISBN")
    private String isbn;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "IS_AVAILABLE")
    @Convert(converter = CharToBooleanConverter.class)
    private boolean isAvailable;

    @Column(name = "COVER_IMAGE_PATH")
    private String coverImagePath;

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
    public Book(String isbn, String title, boolean isAvailable, String coverImagePath, List<Author> authors, List<Genre> genres) {
        this.isbn = isbn;
        this.title = title;
        this.isAvailable = isAvailable;
        this.coverImagePath = coverImagePath;
        this.authors = authors;
        this.genres = genres;
    }

    public Book(String isbn, String title, boolean isAvailable) {
        this.isbn = isbn;
        this.title = title;
        this.isAvailable = isAvailable;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
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

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", ISBN=" + isbn +
                ", title='" + title + '\'' +
                ", isAvailable=" + isAvailable +
                ", coverImagePath='" + coverImagePath + '\'' +
                ", authors=" + authors +
                ", genres=" + genres +
                '}';
    }
}
