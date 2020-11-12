package com.bolsadeideas.springboot.app.controllers;

import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
@SessionAttributes("cliente")
public class ClienteController {
    @Autowired
    private IClienteService clienteService;


    @RequestMapping(value = "/listar", method = RequestMethod.GET)
    public String listar(@RequestParam(name="page", defaultValue="0") int page, Model model) {

        Pageable pageRequest = PageRequest.of(page, 4);

        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<Cliente>("/listar", clientes);
        model.addAttribute("titulo", "Listado de clientes");
        model.addAttribute("clientes", clientes);
        model.addAttribute("page", pageRender);
        return "listar";
    }

    @RequestMapping(value = "/form/{id}")
    public String editar (@PathVariable (value = "id")Long id,Map<String,Object>modelo,RedirectAttributes flash){
        Cliente cliente = null;
        if (id > 0){
            cliente = clienteService.findOne(id);
            if (cliente == null){
                flash.addFlashAttribute("error","El id del cliente no existe en la base de datos");
                return "redirect:/listar";
            }
        }else{
            flash.addFlashAttribute("error","El id del cliente no puede ser 0");
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
    public String guardar(@Valid Cliente cliente, BindingResult result, Model modelo,
                          RedirectAttributes flash, SessionStatus status){

        if(result.hasErrors()){
            modelo.addAttribute("titulo","Formulario de cliente");
            return "form";
        }
        String mensajeFlash = (cliente.getId() != null ) ? "Cliente editado con exito" : "Cliente creado con exito";
        clienteService.save(cliente);
        status.setComplete();
        flash.addFlashAttribute("success",mensajeFlash);
        return "redirect:listar";
    }
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar (@PathVariable ( value = "id" )Long id,RedirectAttributes flash){
        if (id > 0){
            clienteService.delete(id);
            flash.addFlashAttribute("success","Cliente eliminado con exito!!");
        }
        return "redirect:/listar";
    }

}
