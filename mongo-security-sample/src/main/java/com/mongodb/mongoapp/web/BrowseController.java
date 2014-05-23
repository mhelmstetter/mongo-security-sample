package com.mongodb.mongoapp.web;

import java.util.Map;

import com.mongodb.BasicDBObject;
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




    /**
     * find all Person records that are visible to your SecurityAttributes
     *
     * @param model
     * @return
     */
    @RequestMapping(value="/browse", method = RequestMethod.GET)
    public ModelAndView browse(Map<String, Object> model) {

        CapcoUser currentUser = userContext.getCurrentUser();
        String capcoVisibilityString = currentUser.getUserSecurityAttributes().getCapcoUserString();
        personRepository.setCapcoVisibilityString(capcoVisibilityString); // TODO: later on the findPersons will fetch


        ////
        // You can specify a criteria to filter results by defining something like:
        //  BasicDBObject criteria = new BasicDBObject("lastName", "Best");
        // You can specify a criteria of null to find all documents
        BasicDBObject criteria = null;
        ////

        Iterable<Person> persons = personRepository.findPersons(criteria, new PageRequest(1, 100));


        // display results
        ModelAndView mav = new ModelAndView();
        mav.setViewName("browse");
        mav.addObject("persons", persons);
        mav.addObject("currentUser", currentUser);
        return mav;
    }
}
