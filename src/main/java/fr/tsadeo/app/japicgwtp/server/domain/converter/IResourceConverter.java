/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.tsadeo.app.japicgwtp.server.domain.converter;

import org.springframework.core.io.Resource;

/**
 *
 * @author sylvie
 */
public interface IResourceConverter {

  public String toUrl(Resource resource);    
  
  //public UrlState getUrlState(Resource resource);
  
  public Resource toResource(String url);
  
  public boolean valideUrl(String url);
}
