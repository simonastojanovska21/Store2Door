package com.example.store.web;

import com.example.store.model.Category;
import com.example.store.model.User;
import com.example.store.model.enums.Role;
import com.example.store.model.exceptions.InvalidUsernameException;
import com.example.store.model.exceptions.InvalidUsernameOrPasswordException;
import com.example.store.model.exceptions.PasswordsDoNotMatchException;
import com.example.store.service.CategoryService;
import com.example.store.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserController {

    private final CategoryService categoryService;
    private final UserService userService;

    public UserController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model, HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        User user=this.userService.findByUsername(username);
        List<Category> categories=this.categoryService.findAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("user",user);
        model.addAttribute("bodyContent","profile");
        return "master-template";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model)
    {
        List<Category> categories=this.categoryService.findAllCategories();
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","register");
        return "master-template";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password ,
                               @RequestParam String repeatedPassword ,
                               @RequestParam String name,
                               @RequestParam String surname ,
                               @RequestParam String address ,
                               @RequestParam String city ,
                               @RequestParam String telephone ,
                               @RequestParam String image ,
                               @RequestParam Role role)
    {
        try {
            if(image==null || image.isEmpty())
                image="https://www.clipartkey.com/mpngs/m/152-1520367_user-profile-default-image-png-clipart-png-download.png";
            this.userService.register(username,password,repeatedPassword,name,surname,address,city,telephone,image,role);
            return "redirect:/products";
        }
        catch (InvalidUsernameException | InvalidUsernameOrPasswordException | PasswordsDoNotMatchException exception)
        {
            return "redirect:/register?error=" + exception.getMessage();
        }
    }


}
