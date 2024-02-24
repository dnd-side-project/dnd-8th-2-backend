package com.dnd.reetplace.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TermsController {

    @GetMapping("/terms2")
    public String terms() {
        return "terms";
    }
}
