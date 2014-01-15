Dependz
=======

A dependency analysis utility in Java which uses topological sorting to order values based on their dependencies.

There are two methods to build a DependencyGraph, one with DependencyNodes (which requires all DependencyNode references to be created ahead of time) or by using DependencyMap which can use an arbitrary key to map dependencies between values.

#### Example

```java
DependencyMap<String, Integer> map = new DependencyMap<String, Integer>();
map.add( "value0", 0 );
map.add( "value1", 1 );
map.add( "value2", 2 );
map.add( "value3", 3 );
map.add( "value4", 4 );
map.addDependency( "value0", "value1" ); // value0 depends on value1
map.addDependency( "value2", "value0" ); 
map.addDependency( "value1", "value3" );
map.addDependency( "value1", "value4" );

DependencyGraph<Integer> analyzer = new DependencyGraph<Integer>();
analyzer.analyze( map.getDependencyNodes() );

assertTrue( analyzer.isValid() );
assertEquals( 3, analyzer.getOutput().get( 0 ).intValue() );
assertEquals( 4, analyzer.getOutput().get( 1 ).intValue() );
assertEquals( 1, analyzer.getOutput().get( 2 ).intValue() );
assertEquals( 0, analyzer.getOutput().get( 3 ).intValue() );
assertEquals( 2, analyzer.getOutput().get( 4 ).intValue() );
```