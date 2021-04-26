package com.example.store.web;

import com.example.store.model.Category;
import com.example.store.model.Product;
import com.example.store.model.enums.Measure;
import com.example.store.service.CategoryService;
import com.example.store.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class ProductsController {
    private final CategoryService categoryService;
    private final ProductService productService;

    public ProductsController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProductsFromCategory(Model model,
                                          @RequestParam("catId") Optional<Long> catId,
                                          @RequestParam("discount") Optional<String> discount,
                                          @RequestParam("min") Optional<Double> min,
                                          @RequestParam("max") Optional<Double> max,
                                          @RequestParam("name") Optional<String> name,
                                          @RequestParam("page") Optional<Integer> page,
                                          @RequestParam("size") Optional<Integer> size)
    {
        int currentPage = page.orElse(1);
        model.addAttribute("currentPage",currentPage);
        int pageSize = size.orElse(12);
        Page<Product> productPage;
        if(catId.isPresent())
        {
            long id=catId.get();
            model.addAttribute("catId",id);
        }
        if(min.isPresent())
        {
            double minimum=min.get();
            model.addAttribute("min",minimum);
        }
        if(max.isPresent())
        {
            double maximum=max.get();
            model.addAttribute("max",maximum);
        }
        productPage=this.productService.findProducts(PageRequest.of(currentPage-1,pageSize),catId,discount,name,min,max);
        Double priceFrom=this.productService.minPrice();
        Double priceTo=this.productService.maxPrice();
        model.addAttribute("priceFrom",priceFrom);
        model.addAttribute("priceTo",priceTo);
        productPage.getTotalElements();
        if(discount.isPresent() && discount.get().equals("yes"))
        {
            model.addAttribute("discount","yes");
        }
        model.addAttribute("productPage",productPage);
        int totalPages = productPage.getTotalPages();
        model.addAttribute("totalPages",totalPages);
        List<Category> categories=this.categoryService.findAllCategories();

        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","products");
        return "master-template";
    }

    @PostMapping("/products/search")
    public String searchProducts(@RequestParam  Optional<Boolean> discount,
                                 @RequestParam (required = false) Long category,
                                 @RequestParam (required = false) String amount)
    {
        String path="redirect:/products?";
        if(category!=-1)
            path+="catId="+category;

        if(discount.isPresent())
            path+="&discount=yes";

        String []tmp=amount.split(" ");
        Double min=Double.parseDouble(tmp[0].substring(1));
        path+="&min="+min;
        Double max=Double.parseDouble(tmp[2].substring(1));
        path+="&max="+max;
        return path;
    }

    @PostMapping("/products/filter")
    public String filterByName(@RequestParam String name)
    {
        return "redirect:/products?name="+name;
    }

    @GetMapping("/products/addProduct")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddProduct(Model model)
    {
        List<Category> categories=this.categoryService.findAllCategories();
        List<Category> subcategories=this.categoryService.findAll();
        model.addAttribute("subcategories",subcategories);
        model.addAttribute("measures",Measure.values());
        model.addAttribute("bodyContent","addProduct");
        model.addAttribute("categories",categories);
        return "master-template";
    }

    @GetMapping("/products/edit/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditProduct(@PathVariable Long code, Model model)
    {
        Product product=this.productService.findById(code);
        List<Category> subcategories=this.categoryService.findAll();
        List<Category> categories=this.categoryService.findAllCategories();
        model.addAttribute("subcategories",subcategories);
        model.addAttribute("measures",Measure.values());
        model.addAttribute("product",product);
        model.addAttribute("bodyContent","addProduct");
        model.addAttribute("categories",categories);
        return "master-template";
    }

    @GetMapping("/products/details/{code}")
    public String showProductDetails(@PathVariable Long code, Model model)
    {
        Product product=this.productService.findById(code);
        List<Category> categories=this.categoryService.findAllCategories();
        List<Product> products=this.productService.findProductsOnDiscount();
        model.addAttribute("products",products);
        model.addAttribute("product",product);
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","productDetails");
        return "master-template";
    }

    @GetMapping("/products/manage")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getManagePage(Model model)
    {
        List<Product> products=this.productService.findProductsOrderedByDiscount();
        List<Category> categories=this.categoryService.findAllCategories();
        model.addAttribute("products",products);
        model.addAttribute("categories",categories);
        model.addAttribute("bodyContent","discount");
        return "master-template";
    }

    @PostMapping("/products/discount/{code}")
    public String addDiscount(@PathVariable Long code, @RequestParam String discount)
    {
        Double disc=Double.parseDouble(discount);
        this.productService.updateDiscount(code,disc);
        return "redirect:/products/manage";
    }

    @PostMapping("/products/removeDiscount/{code}")
    public String removeDiscount(@PathVariable Long code)
    {
        this.productService.updateDiscount(code,(double)0);
        return "redirect:/products/manage";
    }
    @PostMapping("/products")
    public String addProduct(@RequestParam Long code,
                             @RequestParam String name,
                             @RequestParam Double price,
                             @RequestParam Integer quantity,
                             @RequestParam String description,
                             @RequestParam String imageUrl,
                             @RequestParam Measure measure,
                             @RequestParam List<Long> category)
    {
        this.productService.addProduct(code,name,price,quantity,description,imageUrl,measure,category);
        return "redirect:/products";
    }

    @PostMapping("/products/{code}")
    public String editProduct(@PathVariable Long code,
                              @RequestParam String name,
                              @RequestParam Double price,
                              @RequestParam Double discount,
                              @RequestParam Integer quantity,
                              @RequestParam String description,
                              @RequestParam String imageUrl,
                              @RequestParam Measure measure)
    {
        this.productService.updateProduct(code,name,price,discount,quantity,description,imageUrl,measure);
        return "redirect:/products";
    }

    @DeleteMapping("/products/delete/{code}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteProduct(@PathVariable Long code)
    {
        this.productService.deleteProduct(code);
        return "redirect:/products";
    }
}
