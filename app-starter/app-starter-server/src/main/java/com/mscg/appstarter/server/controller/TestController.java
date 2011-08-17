package com.mscg.appstarter.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mscg.appstarter.beans.jaxb.ObjectFactory;
import com.mscg.appstarter.beans.jaxb.Wrapper;

@Controller
@RequestMapping("/test")
public class TestController {

    private ObjectFactory objectFactory;

    @Autowired
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
    public String testMethod(Model model) {
        Wrapper wrapper = objectFactory.createWrapper();
        wrapper.setResponse(objectFactory.createResponse());
        wrapper.getResponse().setMessage(objectFactory.createServerMessage());
        wrapper.getResponse().getMessage().setMessageBody("Hi from Spring!!!");
        wrapper.getResponse().setStatus(200);
        model.addAttribute("object", wrapper);
        return "response";
    }

}
