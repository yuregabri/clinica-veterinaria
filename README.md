# Clinica Veterinaria

Sistema desktop para gestao de clinicas veterinarias, desenvolvido em Java com interface grafica (Swing).

## Funcionalidades

- Cadastro e gestao de animais, tutores, veterinarios e funcionarios
- Consultas, diagnosticos e receitas medicas
- Controle de vacinas e agendamentos de retorno
- Servicos de banho e tosa
- Vendas e controle de estoque

## Diagrama de Classes

![Diagrama UML](assets/uml.jpeg)

## Arquitetura

```
┌──────────────────────────────────────┐
│       GUI (Java Swing)               │
├──────────────────────────────────────┤
│       Service (SistemaService)       │
├──────────────────────────────────────┤
│       Model (Entidades)              │
├──────────────────────────────────────┤
│       Persistencia (.dat)            │
└──────────────────────────────────────┘
```

## Estrutura do Projeto

```
src/com/clinica/
├── Main.java
├── gui/          # Interface grafica (12 paineis)
├── model/        # Entidades de dominio (14 classes + 2 enums)
├── service/      # Logica de negocio
└── util/         # Gerador de IDs
```

## Como Executar

```bash
compilar.bat
java -cp src com.clinica.Main
```

## Tecnologias

- Java
- Java Swing
- Persistencia em arquivo binario (.dat)

## Integrantes

- **Yure Gabriel Alves Cruz** - [yuregabri](https://github.com/yuregabri)
- **Lidia Emanuela Barros**
- **Breno Henrique**
- **Gabriel Lucas Almeida**
