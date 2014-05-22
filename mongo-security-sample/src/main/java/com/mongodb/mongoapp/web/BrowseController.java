package com.mongodb.mongoapp.web;

import java.util.Map;

import com.mongodb.mongoapp.domain.CapcoUser;
import com.mongodb.mongoapp.service.CapcoSecurityService;
import com.mongodb.mongoapp.service.UserContext;
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

    private final CapcoSecurityService capcoSecurityService;
    private final UserContext userContext;
    
    @Autowired
    PersonRepository personRepository;


    @Autowired
    public BrowseController(CapcoSecurityService calendarService, UserContext userContext) {
        this.capcoSecurityService = calendarService;
        this.userContext = userContext;
    }

    private static boolean flipFlop = false;       // to flipFlop thru 2 different CAPCO levels in method flipFlop()

    @RequestMapping(value="/flipFlop", method = RequestMethod.GET)
    public ModelAndView flipFlop(Map<String, Object> model) {

        // new security repo in mongo will feed the user perhaps in a Spring User object,
        // this flipFlop is here to show a demo of 2 different CAPCO security settings in an
        // easy manner... just refresh the browser and we toggle the security setting
        flipFlop = !flipFlop;
        CapcoUser capcoUser = null;

        if (flipFlop) {
            capcoUser = CapcoUser.TestCapcoUsers.TS_USER;
        } else {
            // just for testing now use U
            capcoUser = CapcoUser.TestCapcoUsers.UNCLASS_USER;
        }

        String capcoVisibilityString = capcoUser.getCapcoUserString();
        personRepository.setCapcoVisibilityString(capcoVisibilityString);

        Iterable<Person> persons = personRepository.findPersons(new PageRequest(1, 100));

        //Iterable<Person> persons = personRepository.findAll(new PageRequest(1, 10));

        //Iterable<Person> persons = personRepository.findAll();

        ModelAndView mav = new ModelAndView();
        mav.setViewName("browse");
        mav.addObject("persons", persons);
        mav.addObject("currentUser", capcoUser);
        return mav;
    }


    @RequestMapping(value="/browse", method = RequestMethod.GET)
    public ModelAndView browse(Map<String, Object> model) {

        CapcoUser currentUser = userContext.getCurrentUser();
        String capcoVisibilityString = currentUser.getCapcoUserString();
        personRepository.setCapcoVisibilityString(capcoVisibilityString); // TODO: later on the findPersons will fetch

        Iterable<Person> persons = personRepository.findPersons(new PageRequest(1, 100));
        
        //Iterable<Person> persons = personRepository.findAll(new PageRequest(1, 10));
        
        //Iterable<Person> persons = personRepository.findAll();
        
        ModelAndView mav = new ModelAndView();
        mav.setViewName("browse");
        mav.addObject("persons", persons);
        mav.addObject("currentUser", currentUser);
        return mav;
    }
}
