package com.example.springwebclient.domain.product.list;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/list")
public class ProductListController {
    @GetMapping("/index")
    public String getList(Model model) {

        model.addAttribute("aa", "AA");
        return "list/index";
    }
}
