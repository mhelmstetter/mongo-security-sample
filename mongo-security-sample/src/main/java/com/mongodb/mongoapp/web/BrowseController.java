package com.mongodb.mongoapp.web;

import java.util.Map;

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

    private static boolean flipFlop = false;       // to flipFlop thru 2 different CAPCO levels

    @RequestMapping(value="/browse", method = RequestMethod.GET)
    public ModelAndView browse(Map<String, Object> model) {

        // new security repo in mongo will feed the user perhaps in a Spring User object,
        // this flipFlop is here to show a demo of 2 different CAPCO security settings in an
        // easy manner... just refresh the browser and we toggle the security setting
        flipFlop = !flipFlop;

        if (flipFlop) {
            String capcoVisibilityString = "[ { c:\"TS\" }, { c:\"S\" }, { c:\"U\" }, { c:\"C\" }, { sci:\"TK\" }, { sci:\"SI\" }, { sci:\"G\" }, { sci:\"HCS\" } ]";
            personRepository.setCapcoVisibilityString(capcoVisibilityString);
        } else {
            // just for testing now use U
            personRepository.setCapcoVisibilityString("[ { c:\"U\" } ]");
        }

        Iterable<Person> persons = personRepository.findPersons(new PageRequest(1, 100));
        
        //Iterable<Person> persons = personRepository.findAll(new PageRequest(1, 10));
        
        //Iterable<Person> persons = personRepository.findAll();
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("browse");
        mav.addObject("persons", persons);
        return mav;
    }
}
