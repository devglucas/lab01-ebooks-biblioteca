## Semana 1
Contribuição: modelei os casos de uso "Manter informacoes de alunos", "Manter informacoes de bibliotecários", "Verificar disponibilidade de licença" e "Notificar sistema de estatísticas" em PlantUML e escrevi as histórias de usuário correspondentes (HU07, HU08, HU09 e HU10). Atualizei o README.md com a descrição do sistema, os links para a documentação e a URL do repositório.

Decisões: optei por modelar "Verificar disponibilidade de licença" (HU09) e "Notificar sistema de estatísticas" (HU10) como casos de uso <<include>> de "Adicionar eBook à estante", pois são comportamentos obrigatórios que sempre ocorrem quando um aluno adiciona um eBook, em vez de modelá-los como casos de uso independentes.
