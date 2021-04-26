package com.example.store.web;


import com.example.store.model.Category;
import com.example.store.model.Event;
import com.example.store.model.ShoppingCart;
import com.example.store.model.User;
import com.example.store.service.CategoryService;
import com.example.store.service.EventService;
import com.example.store.service.ShoppingCartService;
import com.example.store.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final CategoryService categoryService;
    private final EventService eventService;
    private final UserService userService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, CategoryService categoryService, EventService eventService, UserService userService) {
        this.shoppingCartService = shoppingCartService;
        this.categoryService = categoryService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/shopping-cart")
    public String getShoppingCart(Model model, HttpServletRequest req)
    {
        String username=req.getRemoteUser();
        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(username);
        List<Category> categories=this.categoryService.findAllCategories();
        double total=this.shoppingCartService.calculateTotal(username);
        model.addAttribute("total",total);
        model.addAttribute("orders",shoppingCart.getOrderedProducts());
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","shopping-cart");
        return "master-template";
    }

    @GetMapping("/shopping-cart/add/{code}")
    public String addToCart(@PathVariable Long code,@RequestParam String quantity,HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        this.shoppingCartService.addOrderToShoppingCart(username,code,Integer.parseInt(quantity));
        return "redirect:/products";
    }

    @GetMapping("/shopping-cart/delete/{orderId}")
    public String deleteOrderFromShoppingCart(@PathVariable Long orderId, HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        this.shoppingCartService.removeOrderFromShoppingCart(username,orderId);
        return "redirect:/shopping-cart";
    }

    @GetMapping("/shopping-cart/check-out")
    public String checkOutShoppingCart(Model model,HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        Event event=this.eventService.getEventForCustomer(username);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy");
        if(event==null)
        {
            LocalDateTime now=LocalDateTime.now().plusDays(1);
            String formatDateTime =now.format(formatter);
            model.addAttribute("start",formatDateTime);
            formatDateTime=now.plusHours(1).format(formatter);
            model.addAttribute("finish",formatDateTime);
        }
        else
        {
            String formatDateTime =event.getStart().format(formatter);
            model.addAttribute("start",formatDateTime);
            formatDateTime=event.getFinish().format(formatter);
            model.addAttribute("finish",formatDateTime);
        }
        ShoppingCart shoppingCart=this.shoppingCartService.getActiveShoppingCart(username);
        List<Category> categories=this.categoryService.findAllCategories();
        User user=this.userService.findByUsername(username);
        double total=this.shoppingCartService.calculateTotal(username);
        double withDelivery=total;
        if(total<20.0)
            withDelivery=total+5;
        model.addAttribute("user",user);
        model.addAttribute("total",total);
        model.addAttribute("withDelivery",withDelivery);
        model.addAttribute("orders",shoppingCart.getOrderedProducts());
        model.addAttribute("event",event);
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","check-out");
        return "master-template";
    }

    @PostMapping("/shopping-cart/finish")
    public String finishOrder(HttpServletRequest request)
    {
        this.shoppingCartService.closeActiveShoppingCart(request.getRemoteUser());
        return "redirect:/products";
    }

    @RequestMapping(value = "/shopping-cart/plus/{orderId}", method = RequestMethod.GET)
    public String plusQuantity(@PathVariable Long orderId,HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        this.shoppingCartService.plusQuantity(username,orderId);
        return "redirect:/shopping-cart";
    }

    @RequestMapping(value = "/shopping-cart/minus/{orderId}", method = RequestMethod.GET)
    public String minusQuantity(@PathVariable Long orderId,HttpServletRequest request)
    {
        String username=request.getRemoteUser();
        this.shoppingCartService.minusQuantity(username,orderId);
        return "redirect:/shopping-cart";
    }

}
