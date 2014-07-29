/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufmt.harmonizacao.interfaces;

import org.apache.lucene.document.Document;

/**
 *
 * @author mattyws
 */
public interface IndexerInterface {
    
    public String getDirectoryName();
    
    public void setPath(String path);
    
    public void index(Document doc);
    
    public void close();
    
}
