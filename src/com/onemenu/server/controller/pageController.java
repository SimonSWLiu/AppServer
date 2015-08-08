package com.onemenu.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/page")
public class pageController {

    @RequestMapping("/appsDownload")
    public ModelAndView showMainPage() {
        return new ModelAndView("AppsDownload");
    }


}
