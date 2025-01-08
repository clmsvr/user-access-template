

### Template para criação de aplicação Web com gerenciamento de usuários.

Recursos usados:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring MVC](https://docs.spring.io/spring-framework/reference/web.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.0/reference/web/spring-security.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/3.4.0/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [Herança de Templates - Thymeleaf](https://www.treinaweb.com.br/blog/thymeleaf-heranca-de-templates)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.4.0/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)
* [Validation](https://docs.spring.io/spring-boot/3.4.0/reference/io/validation.html)
* [MySQL](https://www.mysql.com/)
* [Lombok](https://projectlombok.org/)
* javax.mail (com.sun.mail)


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
* [Configure uma conta para envio de email](https://support.google.com/accounts/answer/185833?hl=pt-BR)
* Configure as propriedades: "mail.sender" e "mail.password"

