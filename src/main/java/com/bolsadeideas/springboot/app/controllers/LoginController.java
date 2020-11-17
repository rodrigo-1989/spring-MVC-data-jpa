package com.bolsadeideas.springboot.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam (value = "error",required = false)String error,
                        @RequestParam (value = "logout",required = false)String logout,
                        Model modelo, Principal principal, RedirectAttributes flash){
        if( principal != null ){
            flash.addFlashAttribute("info","Ya ha iniciado sesion anteriormente");
            return "redirect:/";
        }
        if (error!=null){
            modelo.addAttribute("error","ERROR: El nombre del ususario o contraseña es invalido vuelve a intentarlo");
        }
        if (logout != null){
            modelo.addAttribute("success","Has cerrado sesion con exito");
        }
        return "login";
    }

}
