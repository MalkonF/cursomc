# Loja virtual

Projeto(parte de backend) para criacao de uma loja virtual.

##Antes de utilizar

Tenha instalado o JDK e as variáveis JAVA_HOME e PATH configuradas.

##Como abrir o projeto

**Windows**

O projeto pode ser abertos pelo **STS(Spring Tools Suite)** na opção *File - Open Projects from File System*.

**Linux**

##Execução

Clique com botão direito sobre o projeto e escolha . A partir daí os endpoints já podem ser acessados tanto pelo brownser como pelo Postman.

Para utilizar no modo test(configurar no *application.properties*) não e necessário ter db configurado. Acesso pela URL
localhost:8080/h2-console para acessar o banco de dados.
Para utilizar no modo de produção ou desenvolvimento instale mysql e crie uma base de dados de nome *curso_spring*,
depois configure as credenciais de acesso no arquivo *.properties* correspondente.
