package com.example.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.BooksRepository;
import com.example.repositories.PeopleRepository;

@Transactional(readOnly = true)
@Service
public class BooksService {

    private final BooksRepository booksRepository;
    private final PeopleRepository peopleRepository;

    @Autowired
    public BooksService(BooksRepository booksRepository, PeopleRepository peopleRepository){
        this.booksRepository = booksRepository;
        this.peopleRepository = peopleRepository;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public List<Book> findAll(boolean sortByYear) {
        if(sortByYear){
            return booksRepository.findAll(Sort.by("year"));
        }else{
            return booksRepository.findAll();
        }
    }

    public List<Book> findWithPagination(Integer page, Integer itemsPerPage, boolean sortByYear){
        if(sortByYear){
            return booksRepository.findAll(PageRequest.of(page, itemsPerPage, Sort.by("year"))).getContent();
        }else{
            return booksRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
        }
    }

    public Book findOne(int id){
        Optional<Book> foundBook =  booksRepository.findById(id);

        return foundBook.orElse(null);
    }

    @Transactional
    public void save(Book book){
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook){
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    public void delete(int id){
        booksRepository.deleteById(id);
    }

    public Person getBookOwner(int bookId) {
        Optional<Book> owner = booksRepository.findById(bookId);
        return owner.get().getOwner();
    }

    @Transactional
    public void release(int bookId){
        Optional<Book> bookToBeReleased = booksRepository.findById(bookId);
        bookToBeReleased.get().setOwner(null);
        bookToBeReleased.get().setBookTakingTime(null);
        bookToBeReleased.get().setExpired(false);
    }
    
    @Transactional
    public void appoint(int bookId, int selectedPersonId){
        Optional<Book> bookToBeAppointed = booksRepository.findById(bookId);
        Optional<Person> owner = peopleRepository.findById(selectedPersonId);
        bookToBeAppointed.get().setOwner(owner.get());
        bookToBeAppointed.get().setBookTakingTime(new Date());
    }

    public Book findBookByTitleStarting(String start){
        return booksRepository.findByTitleStartingWith(start);
    }

    
}
