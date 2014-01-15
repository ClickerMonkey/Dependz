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


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * An object which has a value and a list of dependencies and dependents.
 * 
 * @author Philip Diffenderfer
 *
 * @param <T>
 * 		The value type.
 */
public class DependencyNode<T> implements Serializable
{
	
    private static final long serialVersionUID = 1L;
    
	private T value;
	
	private int depth;
	
	private final Set<DependencyNode<T>> dependents = new HashSet<DependencyNode<T>>();
	
	private final Set<DependencyNode<T>> dependencies = new HashSet<DependencyNode<T>>();
	
	/**
	 * Instantiates a new DependencyItem without a value.
	 */
	public DependencyNode()
	{
	}
	
	/**
	 * Instantiates a new DependencyNode.
	 * 
	 * @param value
	 * 		The value.
	 */
	public DependencyNode( T value )
	{
		this.value = value;
	}
	
	/**
	 * Adds a dependency and adds this as a dependent to the dependency.
	 * 
	 * @param dependency
	 * 		The dependency to add.
	 */
	public void addDependency( DependencyNode<T> dependency )
	{
		if ( dependency == this )
		{
			throw new RuntimeException( "A DependencyNode cannot depend on itself" );
		}
		
		dependencies.add( dependency );
		dependency.dependents.add( this );
	}
	
	/**
	 * Adds a dependent and adds this as a dependency to the dependent.
	 * 
	 * @param dependent
	 * 		The dependent to add.
	 */
	public void addDependent( DependencyNode<T> dependent )
	{
		if ( dependent == this )
		{
			throw new RuntimeException( "A DependencyNode cannot depend on itself" );
		}
		
		dependents.add( dependent );
		dependent.dependencies.add( this );
	}
	
	/**
	 * Removes the given dependency and removes this as a dependent from the dependency.
	 * 
	 * @param dependency
	 * 		The dependency to remove.
	 */
	public void removeDependency( DependencyNode<T> dependency )
	{
		dependencies.remove( dependency );
		dependency.dependents.remove( this );
	}
	
	/**
	 * Removes the given dependent and removes this as a dependency from the dependent.
	 * 
	 * @param dependent
	 * 		The dependent to remove.
	 */
	public void removeDependent( DependencyNode<T> dependent )
	{
		dependents.remove( dependent );
		dependent.dependencies.remove( this );
	}
	
	/**
	 * Clears all dependents and removes this as a dependency from each one.
	 * 
	 * @return
	 * 		A set of the dependents cleared.
	 */
	public Set<DependencyNode<T>> clearDependents()
	{
		Set<DependencyNode<T>> dependentsCopy = new HashSet<DependencyNode<T>>( dependents );
		
		for ( DependencyNode<T> dependent : dependentsCopy )
		{
			dependent.removeDependency( this );
		}
		
		return dependentsCopy;
	}
	
	/**
	 * Clears all dependencies and removes this as a dependent from each one.
	 * 
	 * @return
	 * 		A set of the dependencies cleared.
	 */
	public Set<DependencyNode<T>> clearDependencies()
	{
		Set<DependencyNode<T>> dependenciesCopy = new HashSet<DependencyNode<T>>( dependencies );
		
		for ( DependencyNode<T> dependency : dependenciesCopy )
		{
			dependency.removeDependent( this );
		}
		
		return dependenciesCopy;
	}
	
	/**
	 * Whether this node has dependents.
	 * 
	 * @return
	 * 		True if this node has dependents, otherwise false.
	 */
	public boolean hasDependents()
	{
		return !dependents.isEmpty();
	}
	
	/**
	 * Whether this node has dependencies.
	 * 
	 * @return
	 * 		True if this node has dependencies, otherwise false.
	 */
	public boolean hasDependencies()
	{
		return !dependencies.isEmpty();
	}
	
	/**
	 * The value which has dependents and dependencies.
	 * 
	 * @return
	 * 		The reference to the value.
	 */
	public T getValue()
	{
		return value;
	}
	
	/**
	 * Sets the value which has dependents and dependencies.
	 * 
	 * @param value
	 * 		The new value.
	 * @return
	 */
	public void setValue( T value )
	{
		this.value = value;
	}
	
	/**
	 * The depth of this node in the dependency tree. The lower
	 * the depth, the least number of dependencies. If the depth is
	 * zero, it has no dependencies.
	 * 
	 * @return
	 * 		The depth of this node in the dependency tree.
	 */
	public int getDepth()
	{
		return depth;
	}
	
	/**
	 * Sets the depth of this node.
	 * 
	 * @param depth
	 * 		The depth of this node in the dependency tree.
	 */
	protected void setDepth( int depth )
	{
		this.depth = depth;
	}
	
	/**
	 * The set of dependents.
	 * 
	 * @return
	 * 		The reference to the set of dependents.
	 */
	public Set<DependencyNode<T>> getDependents()
	{
		return dependents;
	}
	
	/**
	 * The set of dependencies.
	 * 
	 * @return
	 * 		The reference to the set of dependencies.
	 */
	public Set<DependencyNode<T>> getDependencies()
	{
		return dependencies;
	}
	
	@Override
    public int hashCode()
    {
		return value == null ? 0 : value.hashCode();
    }

	@Override
	@SuppressWarnings("unchecked")
    public boolean equals( Object obj )
    {
	    if ( this == obj )
		    return true;
	    if ( obj == null )
		    return false;
	    if ( getClass() != obj.getClass() )
		    return false;
	    
	    DependencyNode<T> other = (DependencyNode<T>)obj;
	    
	    return ( value == other.value || ( value != null && other.value != null && value.equals( other.value ) ) );
    }
	
}
