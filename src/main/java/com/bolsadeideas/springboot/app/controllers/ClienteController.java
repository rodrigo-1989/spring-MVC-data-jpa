package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
    @Autowired
    private IClienteService clienteService;


    @RequestMapping(value = "/listar")
    public String listar(Model modelo){
        modelo.addAttribute("titulo","Listado de clientes");
        modelo.addAttribute("clientes", clienteService.findAll());
        return "listar";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar (@PathVariable (value = "id")Long id,Map<String,Object>modelo){
        Cliente cliente = null;
        if (id > 0){
            cliente = clienteService.findOne(id);
        }else{
            return "redirect:/listar";
        }
        modelo.put("titulo","Editar cliente");
        modelo.put("cliente",cliente);
        return "form";
    }

    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> modelo){
        Cliente cliente = new Cliente();
        modelo.put("cliente",cliente);
        modelo.put("titulo","Formulario de Cliente");
        return "form";
    }

    @RequestMapping(value = "/form",method = RequestMethod.POST)
    public String guardar(@Valid Cliente cliente, BindingResult result, Model modelo, SessionStatus status){

        if(result.hasErrors()){
            modelo.addAttribute("titulo","Formulario de cliente");
            return "form";
        }

        clienteService.save(cliente);
        status.setComplete();
        return "redirect:listar";
    }
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar (@PathVariable ( value = "id" )Long id){
        if (id > 0){
            clienteService.delete(id);
        }
        return "redirect:/listar";
    }

}
