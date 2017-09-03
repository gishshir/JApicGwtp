/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.dao;

import java.util.List;

import fr.tsadeo.app.japicgwtp.server.domain.IModelItem;

/**
 *
 * @author sylvie
 * @param <T>
 */
public interface IITemDao<T extends IModelItem> extends IDao {
	
	public static String COL_ID = "id";
    
     public T getById(Long id, boolean withDatas);

     
     public void delete(T item);
     
      public List<T> listAll();
      
      // to be tested
      public void attachAndSave(T item);
      public void attachUnmodifiedItem(T item);
      public void attachAndUpdate(T item);
//      public void mergeAndUpdateItem(T item);
}
