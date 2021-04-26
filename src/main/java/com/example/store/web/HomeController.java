package com.example.store.web;

import com.example.store.model.*;
import com.example.store.service.CategoryService;
import com.example.store.service.ProductService;
import com.example.store.service.ReviewService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/")
public class HomeController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final ReviewService reviewService;

    public HomeController(CategoryService categoryService, ProductService productService, ReviewService reviewService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.reviewService = reviewService;
    }

    @GetMapping
    public String getHomePage(Model model)
    {
        List<Category> categories=this.categoryService.findAllCategories();
        List<Product> products=this.productService.findProductsOnDiscount();
        List<Category> all=this.categoryService.findSubcategories();
        List<Review> reviews=this.reviewService.find3RandomReviews();
        model.addAttribute("all",all);
        model.addAttribute("products",products);
        model.addAttribute("categories",categories);
        model.addAttribute("reviews",reviews);
        model.addAttribute("bodyContent","home");
        return "master-template";
    }

    @GetMapping("/accessDenied")
    public String getAccessDeniedPage(Model model)
    {
        List<Category> categories=this.categoryService.findAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","accessDenied");
        return "master-template";
    }

    @GetMapping("/calendar")
    public String getCalendar(Model model)
    {
        List<Category> categories=this.categoryService.findAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","calendar");
        return "master-template";
    }

    @PostMapping("/review")
    public String postReview(@RequestParam String rating, @RequestParam String description, HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        this.reviewService.addNewReview(Integer.parseInt(rating),description,username);
        return "redirect:/profile";
    }

    @GetMapping("/addCategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddCategory(Model model)
    {
        model.addAttribute("bodyContent","addCategories");
        return "master-template";
    }
    @GetMapping("/addSubcategory")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddSubcategory(Model model)
    {
        List<Category> categories=this.categoryService.findAll();
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","addSubcategory");
        return "master-template";
    }
    @PostMapping("/addCategory")
    public String addCategory(@RequestParam String name, Model model)
    {
        Category category=new Category(name);
        this.categoryService.addCategory(name);
        return "redirect:/";
    }
    @PostMapping("/addSubcategory")
    public String addSubcategory(@RequestParam Long category, @RequestParam String name, Model model)
    {
        this.categoryService.addSubcategory(category,name);
        return "redirect:/";
    }

}
