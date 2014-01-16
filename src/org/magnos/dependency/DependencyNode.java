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
 * A node in a dependency graph and tree that has a set of dependencies (edges
 * on the graph) and when structured into a tree with a
 * {@link DependencyAnalyzer} has an index and depth in the tree.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The type of value stored in the node.
 */
public class DependencyNode<T> implements Serializable
{

	private static final long serialVersionUID = 1L;

	private T value;

	private int depth;

	private int index;

	private final Set<DependencyNode<T>> dependencies = new HashSet<DependencyNode<T>>();

	/**
	 * Instantiates a new DependencyNode without a value.
	 */
	public DependencyNode()
	{
	}

	/**
	 * Instantiates a new DependencyNode.
	 * 
	 * @param value
	 *        The value of the node.
	 */
	public DependencyNode( T value )
	{
		this.value = value;
	}

	/**
	 * Adds a dependency to this node.
	 * 
	 * @param dependency
	 *        The dependency to add.
	 */
	public void addDependency( DependencyNode<T> dependency )
	{
		dependencies.add( dependency );
	}

	/**
	 * Removes the given dependency from this node.
	 * 
	 * @param dependency
	 *        The dependency to remove.
	 */
	public void removeDependency( DependencyNode<T> dependency )
	{
		dependencies.remove( dependency );
	}

	/**
	 * Whether this node has dependencies.
	 * 
	 * @return True if this node has dependencies, otherwise false.
	 */
	public boolean hasDependencies()
	{
		return !dependencies.isEmpty();
	}

	/**
	 * The value which has dependencies.
	 * 
	 * @return The reference to the value.
	 */
	public T getValue()
	{
		return value;
	}

	/**
	 * Sets the value which has dependencies.
	 * 
	 * @param value
	 *        The new value.
	 * @return
	 */
	public void setValue( T value )
	{
		this.value = value;
	}

	/**
	 * The depth of this node in the dependency tree. The lower the depth, the
	 * least number of dependencies. If the depth is zero, it has no
	 * dependencies.
	 * 
	 * @return The depth of this node in the dependency tree.
	 */
	public int getDepth()
	{
		return depth;
	}

	/**
	 * Sets the depth of this node.
	 * 
	 * @param depth
	 *        The depth of this node in the dependency tree.
	 */
	protected void setDepth( int depth )
	{
		this.depth = depth;
	}

	/**
	 * The index of this node in the dependency tree. The lower the index the
	 * lower number of dependencies. All nodes with a lesser index are potential
	 * dependencies.
	 * 
	 * @return The index of this node in the dependency tree.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * Sets the index of this node.
	 * 
	 * @param index
	 *        The index of this node in the dependency tree.
	 */
	protected void setIndex( int index )
	{
		this.index = index;
	}

	/**
	 * The set of dependencies.
	 * 
	 * @return The reference to the set of dependencies.
	 */
	public Set<DependencyNode<T>> getDependencies()
	{
		return dependencies;
	}

	/**
	 * @return The number of dependencies the node has.
	 */
	public int getDependencyCount()
	{
		return dependencies.size();
	}

}
