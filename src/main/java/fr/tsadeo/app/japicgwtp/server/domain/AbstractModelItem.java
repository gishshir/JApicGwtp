/*
 * Classe mère de tous les objets du domain
 */
package fr.tsadeo.app.japicgwtp.server.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author sylvie
 */
@MappedSuperclass
public abstract class AbstractModelItem implements IModelItem, Serializable {

    public static final char SEPARATOR = '.';
    
    

    @Id
    @GeneratedValue
    private Long id;

    @Override
    public Long getId() {
        return this.id;
    }
   
}
