package com.eburtis.ElephantBet.domain;

import org.hibernate.annotations.Type;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;

import static com.eburtis.ElephantBet.domain.ERole.*;
import static java.util.Collections.singleton;

/**
 * Entité utilisateur
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = Utilisateur.TABLE_NAME)
public class Utilisateur {

    public static final String TABLE_NAME = "utilisateur";
    public static final String TABLE_ID = TABLE_NAME + "_id";
    public static final String TABLE_SEQ = TABLE_ID + "_seq";

    @Id
    @Column(name = "id")
    @SequenceGenerator(name = TABLE_SEQ, sequenceName = TABLE_SEQ)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = TABLE_SEQ)
    private Long id;

    private String nom;

    private String prenoms;

    private String username;

    private boolean actif;

    private String password;

    @Enumerated(EnumType.STRING)
    private ERole role;

    public Utilisateur(String password,ERole role) {
        this.password = password;
        this.actif = true;
        this.role= role;
    }

    /**
     * Constructeur par  défaut
     */
    public Utilisateur() {
    }

    public Utilisateur(Long id, String nom, String prenoms, String username, boolean actif, String password, ERole role) {
        this.id = id;
        this.nom = nom;
        this.prenoms = prenoms;
        this.username = username;
        this.actif = actif;
        this.password = password;
        this.role = role;
    }

    /**
     * Mets à jour le utilisateur
     *
     * @param nom     le nom de l'utilisateur
     * @param prenoms les prénoms de l'utilisateur
     * @param username   l'email
     */
    public void mettreAJour(String nom,
                            String prenoms,
                            String username
    ) {
        this.nom = nom;
        this.prenoms = prenoms;
        this.username = username;
    }


    public void miseAjourGlobal(String nom, String prenoms, String username, String password, String role) {
        this.nom = nom;
        this.prenoms = prenoms;
        this.username = username;
        this.password = password;
        this.role = ERole.valueOf(role);
    }

    public void miseAjourSansMotDePasse(String nom, String prenoms, String username, String role) {
        this.nom = nom;
        this.prenoms = prenoms;
        this.username = username;
        this.role = ERole.valueOf(role);
    }


    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenoms() {
        return prenoms;
    }

    public String getUsername() {
        return username;
    }

    public boolean isActif() {
        return actif;
    }

    public String getPassword() {
        return password;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }


    public ERole getRole() {
        return role;
    }

    public User buildUser() {
        return new User(username, password, singleton(new SimpleGrantedAuthority(role.name())));
    }
}
