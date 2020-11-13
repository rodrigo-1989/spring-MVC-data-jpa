package com.bolsadeideas.springboot.app;

import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



//Se implementa CommandLineRunner para asi crear la carpeta de uploads cada vez que se
//arranca el servido para asi eliminar todas las fotos que nos quedan cuando bajamos el servidor
@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {
    @Autowired
    IUploadFileService uploadFileService;
    public static void main(String[] args) {
        SpringApplication.run(SpringBootDataJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        uploadFileService.deleteAll();
        uploadFileService.init();
    }
}
