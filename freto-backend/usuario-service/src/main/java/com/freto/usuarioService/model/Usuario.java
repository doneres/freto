package com.freto.usuarioService.model;

@Entity
@Table(name = "Usuario")
public class Usuario {

    @id
    
    private UUID id;
    private String nome;
    private String email;
    private String senha;

}
