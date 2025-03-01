package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    
}
