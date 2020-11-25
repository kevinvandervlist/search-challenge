package com.ing.sea.pdeng.graph.search.rensink;

import com.ing.sea.pdeng.graph.Vertex;

import java.util.Iterator;

/**
 * Algorithm interface for the search
 *
 * @author Arend Rensink
 */
public interface Search {
    Iterator<Solution> search(Vertex product);
}
