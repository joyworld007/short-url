package com.exam.shorturl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShortUrlController {

  @GetMapping("/main")
  public String init(Model model) {
    return "main";
  }


}
