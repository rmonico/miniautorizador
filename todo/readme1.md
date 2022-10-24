# Desafio VR Desenvolvimento

1. Recebemos um código desenvolvido por terceiros de um sistema que possui alto volume de lógica de negócio e apresenta
   as seguintes características:
    - O sistema recebe requisições REST, está dividido em camadas e possui classes de domínio;
    - O controller recebe a requisição e está com toda lógica de negócio. Monta e repassa o domínio para a aplicação;
    - A aplicação tem a responsabilidade de repassar o objeto pronto para o repositório;
    - O repositório apenas persiste os objetos mapeados do hibernate através de spring data;
    - O domínio apenas faz o mapeamento para o BD;
    - Nenhum teste unitário foi escrito.
    - O sistema está escrito em java para rodar como spring boot.

- Apresente observações/problemas sobre essa solução.**
- Comente qual(is) a(s) sua(s) estratégia(s) para melhorar este sistema em termos de qualidade e manutenção. Justifique
  suas decisões.

2. Descreva quais são as principais limitações ao se adotar servidores de aplicação em uma arquitetura orientada a
   microsserviços.

3. Atualmente, diversas aplicações escritas em Java estão deixando de serem desenvolvidas para rodarem em servidores (
   JBoss, Tomcat), adotando ferramentas que disponibilizam um servidor embutido na própria ferramenta. Quais são os
   principais desafios ao se tomar uma decisão dessas? Justifique sua resposta.

4. Teste prático (em anexo)
