package com.example.bookify.service.impl;

import com.example.bookify.model.Author;
import com.example.bookify.repository.AuthorRepository;
import com.example.bookify.service.AuthorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public Author create(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author getById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Author with id " + id + " not found"));
    }

    @Override
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @Override
    public Author update(Long id, Author updatedAuthor) {
        Author existing = getById(id);
        existing.setFullName(updatedAuthor.getFullName());
        existing.setBooks(updatedAuthor.getBooks());
        return authorRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        authorRepository.delete(getById(id));
    }
}
