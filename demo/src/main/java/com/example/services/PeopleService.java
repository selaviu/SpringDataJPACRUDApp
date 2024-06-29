package com.example.services;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.models.Book;
import com.example.models.Person;
import com.example.repositories.PeopleRepository;

@Transactional(readOnly = true)
@Service
public class PeopleService {
    
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository){
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        return foundPerson.orElse(null);
    }

    @Transactional
    public void save(Person person){
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson){
        updatedPerson.setId(id);
        peopleRepository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id){
        peopleRepository.deleteById(id);
    }

    public List<Book> getBooksByPersonId(int id){
        Optional<Person> foundPerson = peopleRepository.findById(id);
        List<Book> ownedBooks = foundPerson.get().getBooks();
        Date currentDate = new Date();
        for(Book book : ownedBooks){
            Date bookTakingTime = book.getBookTakingTime(); 

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.WEEK_OF_YEAR, -1); 
            Date aWeekAgo = calendar.getTime();

            if (bookTakingTime.before(aWeekAgo)) {
                System.out.println(book.getTitle());
                book.setExpired(true);
            }
        }
        return ownedBooks;
    }
}
