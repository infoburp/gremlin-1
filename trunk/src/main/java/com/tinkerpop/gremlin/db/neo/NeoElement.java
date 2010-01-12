package com.tinkerpop.gremlin.db.neo;

import com.tinkerpop.gremlin.model.Element;
import com.tinkerpop.gremlin.model.Index;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.PropertyContainer;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 * @version 0.1
 */
public class NeoElement implements Element {

    protected PropertyContainer element;
    protected final Index index;

    public NeoElement(Index index) {
        this.index = index;
    }

    public void setProperty(String key, Object value) {
        if (this instanceof NeoVertex) {
            Object value2 = this.getProperty(key);
            if (null != value2)
                this.index.remove(key, value2, this);
        }

        this.element.setProperty(key, value);
        if (this instanceof NeoVertex)
            this.index.put(key, value, this);
    }

    public Object getProperty(String key) {
        if (this.element.hasProperty(key))
            return this.element.getProperty(key);
        else
            return null;
    }

    public Set<String> getPropertyKeys() {
        Set<String> keys = new HashSet<String>();
        for (String key : this.element.getPropertyKeys()) {
            keys.add(key);
        }
        return keys;
    }

    public Object removeProperty(String key) {
        try {
            if (this instanceof NeoVertex) {
                Object value2 = this.getProperty(key);
                if (null != value2)
                    this.index.remove(key, value2, this);
            }
            Object value = this.element.removeProperty(key);
            return value;
        } catch (NotFoundException e) {
            return null;
        }
    }

    public int hashCode() {
        return this.element.hashCode();
    }

    public PropertyContainer getRawElement() {
        return this.element;
    }

    public boolean equals(Object object) {
        if (object instanceof Element)
            return this.hashCode() == object.hashCode();
        else
            return false;
    }

    public Object getId() {
        if (this.element instanceof Node) {
            return ((Node) this.element).getId();
        } else { //if(this.element instanceof Relationship) {
            return ((Relationship) this.element).getId();
        }
    }
}
