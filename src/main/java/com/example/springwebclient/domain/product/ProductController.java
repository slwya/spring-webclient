package com.example.springwebclient.domain.product;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    private final ProductService productListService;

    public ProductController(ProductService productListService) {
        this.productListService = productListService;
    }

    @GetMapping("/list/index")
    public String getList(Model model) {

        productListService.getProductList();

        model.addAttribute("aa", "AA");
        return "list/index";
    }

    @GetMapping("/detail/index")
    public String getdetail(Model model) {

        productListService.getProduct();

        model.addAttribute("aa", "AA");
        return "detail/index";
    }
}
