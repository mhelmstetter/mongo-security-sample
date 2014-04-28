package com.mongodb.mongoapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.mongoapp.domain.Person;
import com.mongodb.mongoapp.repository.PersonRepository;
import com.mongodb.mongoapp.service.BrowseService;

@Controller
public class BrowseController {
    
    @Autowired
    private BrowseService browseService;
    
    @Autowired
    PersonRepository personRepository;

    @RequestMapping(value="/browse", method = RequestMethod.GET)
    public ModelAndView helloWorld() {
        
        Iterable<Person> persons = personRepository.findPersons(new PageRequest(1, 10));
        
        
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("helloWorld");
        mav.addObject("message", "Hello World!");
        mav.addObject("persons", persons);
        return mav;
    }
}
