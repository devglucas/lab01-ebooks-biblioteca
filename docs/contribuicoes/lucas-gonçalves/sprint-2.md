
## Semana 1 
Contribuição: modelei o diagrama de classes do sistema em PlantUML,identificando as classes, atributos, métodos, enumerações e os relacionamentos entre elas (herança, associação, composição e dependência). Gerei o diagrama nos formatos .puml e .png.

Decisões: optei por criar a classe ItemEstante como classe associativa entre EstantePessoal e EBook, para representar o tipo de leitura (obrigatória ou livre) de cada eBook na estante do aluno, em vez de usar uma associação direta entre Aluno e EBook.

## Semana 2
Contribuição: criei a estrutura de pacotes Java do projeto (src/br/edu/pucminas/biblioteca/modelo/) e implementei todas as classes do diagrama de classes UML, a classe abstrata Usuario, as subclasses Aluno e Bibliotecario, os enums FormatoArquivo, Categoria e TipoLeitura, e as classes EBook, Licenca, ItemEstante, EstantePessoal, Catalogo, EquipeBiblioteca e SistemaEstatisticas. Cada classe inclui atributos, construtores, getters/setters e stubs dos métodos para implementação na Sprint 3.

Decisões: optei por já instanciar a EstantePessoal dentro do construtor de Aluno (composição 1:1 conforme o diagrama) e por limitar maxAcessosSimultaneos a 60 diretamente no construtor de Licenca, respeitando a restrição {<= 60} do diagrama.
