# Sistema de Autenticação com JWT usando Spring Boot

Este projeto é um sistema de autenticação desenvolvido com **Spring Boot**, que utiliza **JSON Web Tokens (JWT)** para gerenciar a autenticação de usuários. O JWT é um padrão de segurança amplamente utilizado para troca de informações seguras entre partes, como autenticação de usuários em aplicações web.

## 🔑 O que é JWT e sua importância?

**JSON Web Token (JWT)** é um padrão aberto (RFC 7519) que define uma maneira compacta e autocontida de transmitir informações de forma segura entre as partes como um objeto JSON. Essas informações podem ser verificadas e confiáveis porque são assinadas digitalmente. O uso de JWTs é importante pois:

- **Segurança**: Garante a integridade e autenticidade das informações trocadas entre o cliente e o servidor.
- **Escalabilidade**: Facilita a autenticação em arquiteturas distribuídas, sem a necessidade de manter o estado no servidor.
- **Desempenho**: Os tokens são compactos e podem ser transmitidos facilmente entre cliente e servidor, o que melhora o desempenho da aplicação.

## ⚙️ Tecnologias e Metodologias Utilizadas

### 1. Exceções Personalizadas
O uso de exceções personalizadas permite um melhor controle sobre os erros que ocorrem na aplicação, oferecendo respostas mais significativas e amigáveis para o usuário final, além de melhorar a manutenção e depuração do código.

### 2. Data Transfer Objects (DTO)
**DTOs** são objetos que transferem dados entre diferentes partes de um sistema. Eles são utilizados para encapsular os dados e reduzir o número de chamadas entre cliente e servidor, melhorando o desempenho e a segurança.

### 3. Banco de Dados em Memória H2
O **H2** é um banco de dados em memória leve e rápido, ideal para testes e desenvolvimento. Ele permite que o projeto seja executado sem a necessidade de instalar ou configurar um banco de dados externo, facilitando a configuração e a execução.

### 4. Swagger
O **Swagger** é uma ferramenta para documentação de APIs RESTful. Ele permite que você visualize e teste facilmente todos os endpoints disponíveis na aplicação, melhorando a experiência do desenvolvedor e garantindo que a API seja bem documentada.

### 5. Spring Security
**Spring Security** é um framework que fornece autenticação e controle de acesso robustos para aplicações Java. Ele oferece uma integração poderosa com o Spring Boot e é altamente configurável para atender às necessidades de segurança de qualquer aplicação.

## 🚀 Como Executar o Projeto

### Método 1: Executando Localmente

1. Clone este repositório:

   ```bash

   https://github.com/Mauro-Pereira/Jwt-with-SpringBoot.git

   ```

2. Abra o projeto em sua IDE de preferência (Eclipse, IntelliJ, etc.).

3. Certifique-se de que você tenha Java e Maven instalados.

4. Execute o projeto usando sua IDE ou o comando Maven:


   ```bash
    mvn spring-boot:run
   ```

### Método 2: Executando com Docker

1. Certifique-se de que você tenha o Docker instalado.

2. Navegue até o diretório do projeto e execute o comando:

   ```bash
    docker-compose up
   ```

### Testando a API

Assim que o servidor estiver em execução, você pode testar os endpoints usando uma ferramenta como o Postman ou diretamente pelo Swagger acessando:

http://localhost:8080/swagger-ui/index.html


### O que consiste esse projeto?

Este projeto tem o objetivo principal o estudo de como fazer autenticação no Spring Boot, usando JWT. Para isso, este consiste em ser um sistema de usuário simples. No caso, o usuário pode ser de dois tipos: usuário comum e usuario admin. Os dois possuem neste sistemas responsabilidades compartilhadas e responsabilidades que são exclusivo de um usuário ADMIN. Exemplos de responsabilidades compartilhadas são: todo usuário consigue se cadastrar no sistema (sem se autenticar), pode ver detalhes de seu registro (precisa estar autenticado), pode atualizar seu registro (precisa estar autenticado), pode deletar seu registro (precisa estar autenticado). No entanto, existe coisas que só um usuário ADMIN pode fazer e esses são: criar um outro admin e retirar a permissão de admin de um usuário (precisa estar auteticado). O projeto já é criado com usuário admin default, segue a baixo:

```bash
usuario admin name: root
usuario admin email: root@email.com
usuario admin password: password
```

Usando esse usuário default, você consiguirá dar permissão a outros usuários comuns.

Ao testar o token JWT de um usuário no Swagger, adicione somente o token sem o "Bearer" na pop-up que aparece quando você clica no cadeado dos enp-points. Uma vez adicionado o token em um daqueles cadeados, o token será configurado para todo o projeto.

Ao usar esse projeto, tenha em mente que ele precisa ser **melhorado**. Existe lacunas de segurança importantes nele ainda que preciso melhorar. Caso você queira contribuir, ficarei feliz.
