
package org.magnos.dependency;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * A {@link DependencyMap} enables you to build a collection of
 * {@link DependencyNode}s based on a key instead of the instance of a
 * DependencyNode.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <K>
 *        The key of the map.
 * @param <V>
 *        The value in the node.
 */
public class DependencyMap<K, V>
{

    private Map<K, Set<K>> dependents;
    private Map<K, V> values;

    /**
     * Instantiates a new DependencyMap without values or dependents.
     */
    public DependencyMap()
    {
        this.dependents = new LinkedHashMap<K, Set<K>>();
        this.values = new LinkedHashMap<K, V>();
    }

    /**
     * Adds a dependency to the map.
     * 
     * @param child
     *        The key that is a dependent of the given dependency.
     * @param dependency
     *        The key that is the parent of the child dependent.
     */
    public void addDependency( K child, K dependency )
    {
        addToListMap( child, dependency, dependents );
    }

    /**
     * Adds a dependent to the map.
     * 
     * @param parent
     *        The key that is the parent of the child dependent.
     * @param dependent
     *        The key that is a child of the given dependency.
     */
    public void addDependent( K parent, K dependent )
    {
        addToListMap( dependent, parent, dependents );
    }

    public void add( K key, V value )
    {
        values.put( key, value );
    }

    /**
     * 
     * @param key
     * @param value
     * @param map
     */
    private <A, B> void addToListMap( A key, B value, Map<A, Set<B>> map )
    {
        Set<B> list = map.get( key );

        if (list == null)
        {
            list = new HashSet<B>();
            map.put( key, list );
        }

        list.add( value );
    }

    public Collection<DependencyNode<V>> getDependencyNodes()
    {
        Map<K, DependencyNode<V>> nodeMap = new HashMap<K, DependencyNode<V>>();

        for (Entry<K, V> e : values.entrySet())
        {
            nodeMap.put( e.getKey(), new DependencyNode<V>( e.getValue() ) );
        }

        for (Entry<K, DependencyNode<V>> e : nodeMap.entrySet())
        {
            DependencyNode<V> dn = e.getValue();

            Set<K> deps = dependents.get( e.getKey() );

            if (deps != null)
            {
                for (K d : deps)
                {
                    dn.addDependent( nodeMap.get( d ) );
                }
            }
        }

        return nodeMap.values();
    }
}
