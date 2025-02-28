package com.project.bookapp.dto;

public record BookSearchParametersDto(String title, String author, String isbn) {
    public static final String AUTHOR = "author";
    public static final String ISBN = "isbn";
    public static final String TITLE = "title";
}
