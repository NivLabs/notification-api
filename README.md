
  Notify API
===================
API provedora de serviços de notificações com assinaturas únicas. API Rest desenvolvida em Java com Spring Boot como tecnologias principais.

Motivação
---------
Após o desenvolvimento de várias integrações com sistemas de envios de notificações como SMS, E-mail, WhatsApp e algumas outras, percebemos que o trabalho se repetia e que se fazia interessante uma padronização das assinaturas de comunicação utilizando o padrão rest com JSON independente do provedor do serviço. A API foi criada pensando nisso, uma só assinatura para inúmeros fornecedores, defininco apenas alguns parâmetros para a disponibilização de alguns serviços.

Building
--------
Para buildar o projeto é necessário ter o Java 21.
A versão necessária do Java encontra-se no `pom.xml` na propriedade `maven.compiler.source`.
A partir do terminal, rode `mvn clean package`.
O jar da aplicação será disponilizado na pasta `target` do projeto.

Contribuindo
------------
Nós aceitamos Pull Requests via GitHub.
Segue algumas recomendações para que possas aplicar PRs com mais facilidade:
+ Mantenha o código sempre identado.
+ Respeite a existência do estilo do código (style code).
+ Criar diffs mínimos - disabilite funções de auto save como formatações automáticas e organização de importações. Se você está sentindo necessidade de formatação, crie um novo PR apenas com esta atividade.
+ Providencie testes unitários com JUnit para suas mudanças e verifique se nenhum teste existente foi quebrado.

Licença
-------
Este código está licenciado por [Apache License v2](https://www.apache.org/licenses/LICENSE-2.0)
Verifique o arquivo `NOTICIA.txt` para informações necessárias e atribuições.