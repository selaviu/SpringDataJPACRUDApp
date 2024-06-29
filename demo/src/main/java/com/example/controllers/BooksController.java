package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;



import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.models.Book;
import com.example.models.Person;
import com.example.services.BooksService;
import com.example.services.PeopleService;

@RequestMapping(value = "/books")
@Controller
public class BooksController {
    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BooksController(BooksService booksService, PeopleService peopleService){
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(@RequestParam(required = false, name = "page") Integer page,
                        @RequestParam(required = false, name = "books_per_page") Integer itemPerPage,
                        @RequestParam(required = false, name = "sort_by_year") boolean sortByYear,
                        Model model) {
        System.out.println("book");
        if(page == null && itemPerPage == null){
            model.addAttribute("books", booksService.findAll(sortByYear));
        }else{
            model.addAttribute("books", booksService.findWithPagination(page, itemPerPage, sortByYear));
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model){
        model.addAttribute("book", booksService.findOne(id));

        Person bookOwner = booksService.getBookOwner(id);
        if(bookOwner != null){
            model.addAttribute("owner", bookOwner);
        }else{
            model.addAttribute("people", peopleService.findAll());
        }
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(Model model){
        model.addAttribute("book", new Book());
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book, 
                            BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "books/new";
        }
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", booksService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                            BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors()){
            return "books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        booksService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String appoint(@PathVariable("id") int bookId) {
        booksService.release(bookId);
        return "redirect:/books/" + bookId;
    }

    @PatchMapping("/{id}/appoint")
    public String appoint(@PathVariable("id") int bookId,
                        @RequestParam("personId") int personId) {
        System.out.println("Book ID: " + bookId);
        System.out.println("Person ID: " + personId);
        booksService.appoint(bookId, personId);
        return "redirect:/books/" + bookId;
    }

    @GetMapping("/search")
    public String searchBook() {
        return "books/search";
    }

    @PostMapping("/search")
    public String search(@RequestParam("prefix") String prefix, Model model) {
        Book book = booksService.findBookByTitleStarting(prefix);
        model.addAttribute("book", book);
        return "books/search";
    }





    
}
