Dependz
=======

![Stable](http://i4.photobucket.com/albums/y123/Freaklotr4/stage_stable.png)

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
        
DependencyGraph<Integer> graph = new DependencyGraph<Integer>();
graph.analyze( map.getDependencyNodes() );

assertTrue( graph.isValid() );
assertEquals( Arrays.asList( 3, 4, 1, 0, 2 ), graph.getOutput() );
```

#### Builds
- [Dependz-1.0.jar](https://github.com/ClickerMonkey/Dependz/raw/master/build/Dependz-1.0.jar)
- [Dependz-src-1.0.jar](https://github.com/ClickerMonkey/Dependz/raw/master/build/Dependz-1.0-src.jar) - includes source code

#### Projects using Dependz:

- [Rekord](https://github.com/ClickerMonkey/Rekord)
