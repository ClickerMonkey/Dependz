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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


/**
 * Analyzes a graph of {@link DependencyNode}s and attempts to sort it into a
 * dependency tree structure (using a topological sort). The depth of the tree
 * is given as {@link #getMaximumDepth()} (inclusive), and the tree levels can
 * be attained with {@link #getLevelNodes()} and {@link #getLevels()}. Tree
 * level n depends on all nodes at depth less than n. <br/>
 * <br/>
 * If a cycle is detected in the graph {@link #isValid()} returns false and
 * {@link #isCyclic()} returns true. {@link #getCycleSize()} returns the number
 * of nodes involved in the cycle and that number of nodes in
 * {@link #getCycle()} the nodes in the cycle.<br/>
 * <br/>
 * If a cycle isn't detected, {@link #getOrdered()} and
 * {@link #getOrderedNodes()} will be an array of values and nodes ordered so
 * that the i'th element depends on all elements less than i.
 * 
 * @author Philip Diffenderfer
 * 
 * @param <T>
 *        The {@link DependencyNode} value type.
 */
public class DependencyAnalyzer<T>
{

	private DependencyNode<T>[] nodes;
	private DependencyNode<T>[] cycle;
	private int cycleSize = 0;
	private DependencyNode<T>[] orderedNodes;
	private int orderedSize = 0;
	private T[] ordered;
	private int maximumDepth = -1;
	private boolean valid = false;

	/**
	 * Analyzes the graph of nodes and attempts to construct a tree.
	 * 
	 * @param nodeCollection
	 *        The collection of {@link DependencyNode}s.
	 * @return True if the graph was sorted, false if there was a cycle.
	 * @see DependencyAnalyzer
	 */
	public boolean analyze( Collection<DependencyNode<T>> nodeCollection, T ... emptyArray )
	{
		final int N = nodeCollection.size();

		nodes = nodeCollection.toArray( new DependencyNode[N] );

		cycle = new DependencyNode[N];
		cycleSize = 0;

		ordered = Arrays.copyOf( emptyArray, N );
		orderedNodes = new DependencyNode[N];
		orderedSize = 0;

		maximumDepth = 0;

		valid = true;

		// Initialize the nodes which have no dependencies. 
		for (int i = 0; i < N; i++)
		{
			DependencyNode<T> dn = nodes[i];

			if (dn.hasDependencies())
			{
				dn.setDepth( -1 );
				cycle[cycleSize++] = dn;
			}
			else
			{
				addOrdered( 0, dn );
			}
		}
		
		// If all nodes have dependencies, there definitely is a cycle involving
		// all nodes.
		if (orderedSize == 0)
		{
			return ( valid = false );
		}

		// While not all nodes have been placed in the ordered array.
		while (orderedSize < N)
		{
			int newlyCycled = 0;

			// Determine for each node whether all of it's dependencies have
			// been placed on the tree, if so place it on the tree, otherwise
			// keep it on the cycle array.
			for (int i = 0; i < cycleSize; i++)
			{
				DependencyNode<T> dn = cycle[i];

				int min = N;
				int max = -1;

				for (DependencyNode<T> dependency : dn.getDependencies())
				{
					min = Math.min( min, dependency.getDepth() );
					max = Math.max( max, dependency.getDepth() );
				}

				// If all dependencies have been placed on the tree, add this
				// node to the tree and mark it's depth and index.
				if (min != -1)
				{
					addOrdered( max + 1, dn );

					maximumDepth = Math.max( maximumDepth, dn.getDepth() );
				}
				// If there was a dependency not yet placed on the tree, 
				// keep it on the cycle array.
				else
				{
					cycle[newlyCycled++] = dn;
				}
			}

			// If no nodes have been added to ordered (or taken from cycle)
			// there is a circular dependency that exists in cycle.
			if (newlyCycled == cycleSize)
			{
				return ( valid = false );
			}

			cycleSize = newlyCycled;
		}

		return ( valid = true );
	}

	/**
	 * Adds an ordered {@link DependencyNode} at the given depth.
	 * 
	 * @param depth
	 *        The depth of the node.
	 * @param dn
	 *        The node to add to the ordered list.
	 */
	private void addOrdered( int depth, DependencyNode<T> dn )
	{
		dn.setDepth( depth );
		dn.setIndex( orderedSize );
		ordered[orderedSize] = dn.getValue();
		orderedNodes[orderedSize] = dn;
		orderedSize++;
	}

	/**
	 * Returns an array of node lists where the n'th list contains all nodes
	 * with the depth of n. The n'th level depends on all levels less than n.
	 * 
	 * @return A reference to a newly created array of DependencyNode lists.
	 */
	public List<DependencyNode<T>>[] getLevelNodes()
	{
		List<DependencyNode<T>>[] levelNodes = new ArrayList[maximumDepth + 1];

		for (int i = 0; i <= maximumDepth; i++)
		{
			levelNodes[i] = new ArrayList<DependencyNode<T>>();
		}

		for (int i = 0; i < orderedSize; i++)
		{
			DependencyNode<T> dn = orderedNodes[i];

			levelNodes[dn.getDepth()].add( dn );
		}

		return levelNodes;
	}

	/**
	 * Returns an array of values where the n'th list contains all values with
	 * the depth of n. The n'th level depends on all levels less than n.
	 * 
	 * @return A reference to a newly created array of value lists.
	 */
	public List<T>[] getLevels()
	{
		List<T>[] levels = new ArrayList[maximumDepth + 1];

		for (int i = 0; i <= maximumDepth; i++)
		{
			levels[i] = new ArrayList<T>();
		}

		for (int i = 0; i < orderedSize; i++)
		{
			levels[orderedNodes[i].getDepth()].add( ordered[i] );
		}

		return levels;
	}

	/**
	 * @return The array of nodes analyzed.
	 */
	public DependencyNode<T>[] getNodes()
	{
		return nodes;
	}

	/**
	 * @return The array of nodes in the cycle, where {@link #getCycleSize()} is
	 *         the number of nodes in the cycle.
	 */
	public DependencyNode<T>[] getCycle()
	{
		return cycle;
	}

	/**
	 * @return The number of nodes in the cycle, if zero the graph has no
	 *         cycles.
	 */
	public int getCycleSize()
	{
		return cycleSize;
	}

	/**
	 * @return The array of nodes ordered by their dependencies.
	 */
	public DependencyNode<T>[] getOrderedNodes()
	{
		return orderedNodes;
	}

	/**
	 * @return The number of elements in {@link #getOrderedNodes()} that are a
	 *         part of the ordered tree.
	 */
	public int getOrderedSize()
	{
		return orderedSize;
	}

	/**
	 * @return The array of values ordered by their dependencies.
	 */
	public T[] getOrdered()
	{
		return ordered;
	}

	/**
	 * @return The maximum depth of a node (inclusive) in the tree.
	 */
	public int getMaximumDepth()
	{
		return maximumDepth;
	}

	/**
	 * @return True if there was a cycle detected when analyzing the graph.
	 */
	public boolean isCyclic()
	{
		return !valid;
	}

	/**
	 * Whether the last analyze method ran was circular dependency free.
	 * 
	 * @return True if there were no circular dependencies, otherwise false.
	 */
	public boolean isValid()
	{
		return valid;
	}

}
