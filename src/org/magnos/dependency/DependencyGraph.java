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


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Analyzes a collection of nodes and creates an ordered list such that no
 * node in the list comes before a dependency of that node. If circular
 * dependencies exist, the {@link #analyze(Collection)} method returns false
 * and the analyzer is marked invalid.
 * 
 * @author pdiffenderfer
 *
 * @param <T>
 * 		The type of node that has dependencies.
 */
public class DependencyGraph<T>
{
	
	private final List<DependencyNode<T>> outputNodes = new ArrayList<DependencyNode<T>>();
	
	private final List<T> output = new ArrayList<T>();
	
	private boolean valid;
	
	/**
	 * Resets the Analyzer.
	 */
	public void clear()
	{
		outputNodes.clear();
		output.clear();
		valid = false;
	}
	
	/**
	 * Analyzes a collection of Dependency nodes and creates an ordered list of
	 * nodes such that no node in the list is before one if it's dependencies.
	 * If the list contains circular dependencies, false will be returned and
	 * this analyzer will be marked invalid.
	 * 
	 * @param nodeCollection
	 * 		The collection of dependency nodes.
	 * @return
	 * 		The validity of the nodeCollection. An nodeCollection is valid if
	 * 		no circular dependencies exist.
	 */
	public boolean analyze( Collection<DependencyNode<T>> nodeCollection )
	{
		List<DependencyNode<T>> nodeList = new ArrayList<DependencyNode<T>>( nodeCollection );
		Queue<DependencyNode<T>> sourceQueue = new ArrayDeque<DependencyNode<T>>();
		
		// Initialize depth of all nodes to -1 to mark "not placed"
		for ( DependencyNode<T> node : nodeList )
		{
			node.setDepth( -1 );
		}
		
		// Build a queue of all nodes which don't have dependents.
		for ( DependencyNode<T> node : nodeList )
		{
			if ( !node.hasDependencies() )
			{
				node.setDepth( 0 );
				
				sourceQueue.offer( node );
			}
		}
		
		// How to quickly tell if a circular dependency exists:
		//		1. The queue is empty.
		//		2. All nodes have depth -1.
		if ( sourceQueue.isEmpty() )
		{
			return (valid = false);
		}
		
		// Clear outputNodes and output in preparation.
		outputNodes.clear();
		output.clear();
		
		while ( !sourceQueue.isEmpty() )
		{
			DependencyNode<T> node = sourceQueue.poll();
			
			// The node no longer has dependencies, it can be added to output.
			outputNodes.add( node );
			output.add( node.getValue() );
			
			// Copy the list of dependents, and then clear it. This is to ensure
			// that the current node is removed as a dependency from its dependents.
			Set<DependencyNode<T>> dependentSet = node.clearDependents();
			
			for ( DependencyNode<T> dependent : dependentSet )
			{
				// Don't add nodes that aren't ready, all their dependencies
				// need to come first!
				if ( !dependent.hasDependencies() )
				{
					dependent.setDepth( node.getDepth() + 1 );
					sourceQueue.offer( dependent );
				}
			}
		}
		
		// How to tell if a circular dependency remains:
		//		1. An node exists in nodeList that has depth -1.
		//		2. An node still has dependencies/dependents.
		//		3. The size of outputNodes != nodeList.
		if ( nodeList.size() != outputNodes.size() )
		{
			return (valid = false);
		}
		
		return (valid = true);
	}
	
	/**
	 * Returns a list of lists such that the nth list consists of all output
	 * nodes that have a depth of n. 
	 * 
	 * @return
	 * 		A reference to a newly created list of DependencyNode lists.
	 */
	public List<List<DependencyNode<T>>> getDepthGroupNodes()
	{
		return getDepthGroupNodes( outputNodes );
	}
	
	/**
	 * Returns a list of lists such that the nth list consists of all output
	 * nodes that have a depth of n. 
	 * 
	 * @param nodeList
	 * 		The list of nodes to group by depth.
	 * @return
	 * 		A reference to a newly created list of DependencyNode lists.
	 */
	public List<List<DependencyNode<T>>> getDepthGroupNodes( List<DependencyNode<T>> nodeList )
	{
		List<List<DependencyNode<T>>> groups = new ArrayList<List<DependencyNode<T>>>();
		
		for ( DependencyNode<T> node : nodeList )
		{
			while ( groups.size() <= node.getDepth() )
			{
				groups.add( new ArrayList<DependencyNode<T>>() );
			}
			
			groups.get( node.getDepth() ).add( node );
		}
		
		return groups;
	}

	
	/**
	 * Returns a list of lists such that the nth list consists of all output
	 * nodes that have a depth of n. 
	 * 
	 * @return
	 * 		A reference to a newly created list of DependencyNode lists.
	 */
	public List<List<T>> getDepthGroups()
	{
		return getDepthGroups( outputNodes );
	}
	
	/**
	 * Returns a list of lists such that the nth list consists of all output
	 * nodes that have a depth of n. 
	 * 
	 * @param nodeList
	 * 		The list of nodes to group by depth.
	 * @return
	 * 		A reference to a newly created list of DependencyNode lists.
	 */
	public List<List<T>> getDepthGroups( List<DependencyNode<T>> nodeList )
	{
		List<List<T>> groups = new ArrayList<List<T>>();
		
		for ( DependencyNode<T> node : nodeList )
		{
			while ( groups.size() <= node.getDepth() )
			{
				groups.add( new ArrayList<T>() );
			}
			
			groups.get( node.getDepth() ).add( node.getValue() );
		}
		
		return groups;
	}
	
	/**
	 * The ordered list of DependencyNodes created by the analyze method.
	 * 
	 * @return
	 * 		The reference to the DependencyNode output.
	 */
	public List<DependencyNode<T>> getOutputNodes()
	{
		return outputNodes;
	}
	
	/**
	 * The ordered list of nodes created by the analyze method.
	 * 
	 * @return
	 * 		The reference to the node output.
	 */
	public List<T> getOutput()
	{
		return output;
	}
	
	/**
	 * Whether the last analyze method ran was circular dependency free.
	 * 
	 * @return
	 * 		True if there were no circular dependencies, otherwise false.
	 */
	public boolean isValid()
	{
		return valid;
	}
	
}
