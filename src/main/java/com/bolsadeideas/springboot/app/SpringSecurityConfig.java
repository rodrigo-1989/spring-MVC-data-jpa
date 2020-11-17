package com.bolsadeideas.springboot.app;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

//Otra forma de darle seguridad es agregando prePostEnabled = true
//asi de esta manera el controlador se anota con  @PreAuthorize("hasRole('ROLE_USER')")
@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginSuccessHandler successHandler;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    DataSource dataSource;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/","/css/**","/js/**","/images/**","/listar").permitAll()
        /* La idea es de remplazar todas estas autorixzaciones desde los controladores con anotaciones
        .antMatchers("/ver/**").hasAnyRole("USER")
        .antMatchers("/uploads/**").hasAnyRole("USER")
        .antMatchers("/form/**").hasAnyRole("ADMIN")
        .antMatchers("/eliminar/**").hasAnyRole("ADMIN")
        .antMatchers("/factura/**").hasAnyRole("ADMIN")*/
        .anyRequest().authenticated()
        .and()
        .formLogin().successHandler(successHandler).loginPage("/login").permitAll()
        .and()
        .logout().permitAll()
        .and()
        .exceptionHandling().accessDeniedPage("/error_403");

    }


    @Autowired
    public void configurerGlobal(AuthenticationManagerBuilder builder )throws Exception{
        //Aqui ya estamos obteniendo los usuarios y roles desde la base de datos
        builder.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder)
                .usersByUsernameQuery("select username, password,enabled from users where username=?")
                .authoritiesByUsernameQuery("select u.username,a.authority from authorities a inner join users u on (a.user_id=u.id) where u.username=?");


        /*
        Todo este bloque de codigo era para hacer autenticacion gusrdando usuarios y contraseñas
        en memoria
        PasswordEncoder encoder = passwordEncoder;
        User.UserBuilder users = User.builder().passwordEncoder( encoder::encode );

        builder.inMemoryAuthentication()
                .withUser(users.username("admin").password("12345").roles("ADMIN","USER"))
                .withUser(users.username("julio").password("12345").roles("USER"));*/

    }
}
