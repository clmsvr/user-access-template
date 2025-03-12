

### Demonstração de aplicação Web com gerenciamento de usuários.

Recursos usados:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring MVC](https://docs.spring.io/spring-framework/reference/web.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.0/reference/web/spring-security.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/3.4.0/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [Herança de Templates - Thymeleaf](https://www.treinaweb.com.br/blog/thymeleaf-heranca-de-templates)
* [Database Initialization](https://docs.spring.io/spring-boot/how-to/data-initialization.html)
* [Validation](https://docs.spring.io/spring-boot/3.4.0/reference/io/validation.html)
* [MySQL](https://www.mysql.com/)
* [Lombok](https://projectlombok.org/)
* [Jakarta Mail Api](https://jakarta.ee/specifications/mail/)
* [commons-codec](https://commons.apache.org/proper/commons-codec/)

### Funções
* Controle de acesso.
* Usuário com acesso controlado por "Roles" que são também um conjunto de "Permissões" cadastradas no banco de dados.
* Interface para Criação de Usuários.
* Interface para Reset de Senha esquecida.
* Interface para atualização de dados do usuário.

#### Comece por:
* Configure o arquivo "application.properties"
* Instancie um servidor MySQL (a base de dados será gerada ao instanciar a aplicação).
* Configure as propriedades: "spring.datasource.*"
* Email:
* [Configure uma conta para envio de email no Gmail](https://support.google.com/accounts/answer/185833?hl=pt-BR)
* [Outra Possibilidade é uma conta gratuita no SendGrid (mas desligue o "Click Tracking")](https://sendgrid.com/)
* Configure as propriedades: "mail.sender", "mail.user-id", "mail.password", "mail.smtp-host" e "mail.smtp-port"

