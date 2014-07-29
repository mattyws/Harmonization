/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufmt.harmonizacao.interfaces;

import java.util.List;
import org.apache.lucene.analysis.Analyzer;

/**
 *
 * @author mattyws
 */
public interface SearcherInterface {
    
    public String getDirectoryName();
    
    public void setPath(String path);
    
    public List<String> search(String field, String value);
    
    public void close();
    
}
