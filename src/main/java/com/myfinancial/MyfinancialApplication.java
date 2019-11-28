package com.myfinancial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
/*
	EnableWebMvc com a implementação  WebMvcConfigurer, permite que a aplicação
	possa receber requisições de URLs externas.

	O método addCorsMappings() configura as permissões de acesso aos end points
	do backand.

	.addMapping() -> mapeia os and points que podem ser acessados,
	'(/**)' -> todas podem ser acessados.

	.allowedMethods() -> verbos html que são permitidos fazerem requisições para
	o backand.

	.allowedOrigins() -> URLs com permissão para fazerem requisições aos and points
	do backand (todas permitidas por padrão).
*/
@EnableWebMvc
public class MyfinancialApplication implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        final String[] methods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
        registry.addMapping("/**").allowedMethods(methods);
    }

    public static void main(String[] args) {
        SpringApplication.run(MyfinancialApplication.class, args);
    }

}
