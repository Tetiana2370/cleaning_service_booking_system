package com.csms.server.api;

import com.csms.server.dto.AppUserDto;
import com.csms.server.exception.ValidationException;
import com.csms.server.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController
{
    @Autowired
    AppUserService appUserService;
    
    @GetMapping("/")
    public String getHomePage(){
        return "index.html";
    }
    
    @GetMapping("/login")
    public String getLoginForm(){
        return "login.html";
    }
    
    @GetMapping("/register")
    public String getRegisterForm(Model model){
        model.addAttribute("appUserDto", new AppUserDto());
        return "register.html";
    }
    
    @PostMapping("/register")
    public String register(@ModelAttribute AppUserDto appUserDto, Model model)
    {
        try {
            appUserService.insert(appUserDto);
        } catch (ValidationException validationException){
            return "/register";
        }
        return "customerOrders";
    }
    
    @GetMapping("customerOrders")
    public String getCustomerOrdersView(){
        return "customerOrders";
    }
    
   
}

