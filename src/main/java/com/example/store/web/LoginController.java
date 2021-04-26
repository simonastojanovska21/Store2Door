package com.example.store.web;

import com.example.store.model.Category;
import com.example.store.service.CategoryService;
import org.springframework.social.connect.Connection;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Operations;
import org.springframework.social.oauth2.OAuth2Parameters;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class LoginController {

    private final CategoryService categoryService;

    public LoginController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/login")
    public String getLogin(Model model)
    {
        List<Category> categories=this.categoryService.findAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","login");
        return "master-template";
    }

}
