/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 *              Open Software License (OSL 3.0)
 */

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

    private Map<K, Set<K>> dependencies;
    private Map<K, V> values;

    /**
     * Instantiates a new DependencyMap without values or dependents.
     */
    public DependencyMap()
    {
        this.dependencies = new LinkedHashMap<K, Set<K>>();
        this.values = new LinkedHashMap<K, V>();
    }

    /**
     * States that <code>a</code> depends on <code>b</code>.
     */
    public void addDependency( K a, K b )
    {
        getDependencies( a ).add( b );
    }

    /**
     * States that <code>b</code> depends on <code>a</code>.
     */
    public void addDependent( K a, K b )
    {
        getDependencies( b ).add( a );
    }

    /**
     * Returns a {@link Set} of all things dependent on the given parent.
     * 
     * @param parent
     *        The parent to get the dependents of.
     * @return The non-null set of dependencies.
     */
    public Set<K> getDependencies( K parent )
    {
        Set<K> set = dependencies.get( parent );

        if (set == null)
        {
            set = new HashSet<K>();
            dependencies.put( parent, set );
        }

        return set;
    }

    /**
     * Sets the value of the given key.
     * 
     * @param key
     *      The key.
     * @param value
     *      The value attached to the key.
     */
    public void put( K key, V value )
    {
        values.put( key, value );
    }

    /**
     * Converts the values and dependencies in the map into a {@link Collection}
     * of {@link DependencyNode}s.
     * 
     * @return The reference to the collection of {@link DependencyNode}s.
     */
    public Collection<DependencyNode<V>> toNodes()
    {
        Map<K, DependencyNode<V>> nodeMap = new HashMap<K, DependencyNode<V>>();

        for (Entry<K, V> e : values.entrySet())
        {
            nodeMap.put( e.getKey(), new DependencyNode<V>( e.getValue() ) );
        }

        for (Entry<K, DependencyNode<V>> e : nodeMap.entrySet())
        {
            DependencyNode<V> dn = e.getValue();

            Set<K> deps = dependencies.get( e.getKey() );

            if (deps != null)
            {
                for (K d : deps)
                {
                    dn.addDependency( nodeMap.get( d ) );
                }
            }
        }

        return nodeMap.values();
    }
    
    /**
     * Attempts to convert the value and dependencies in the map to a dependency
     * tree and returns the {@link DependencyAnalyzer}.
     * 
     * @return The reference of the DependencyAnalyzer storing the tree.
     */
    public DependencyAnalyzer<V> toAnalyzer()
    {
    	DependencyAnalyzer<V> analyzer = new DependencyAnalyzer<V>();
    	
    	analyzer.analyze( toNodes() );
    	
    	return analyzer;
    }
    
}
