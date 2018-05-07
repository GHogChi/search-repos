# search-repos: PoC for integrated test automation

### Overview
(See doc/Overview.png - markdown plugin won't load it.)

Focussing on two aspects of the GitHub API for searching repositories, this 
project does not attempt to be exhaustive but to provide a library of support
 functions for easy extension of test coverage.
 
A problem for these tests is that we don't have an independent way of 
verifying repository information other than the GitHub API. We can't tell 
if a given query returns all matching repositories, only that the ones it 
does return actually do match. Under normal development
circumstances, this would be covered by whitebox testing. 

#### Name matching
- using a string known to be part of the names of multiple repositories 
("tetri" - based on an example in the documentation), 
verify that every repository returned in the responses (multiple pages) 
contains that string in its name.

- verify that a nonsense string evokes a graceful response indicating that no
 repositories match.

##### proposed
- generative test: given a random word chosen from a dictionary, verify that 
either no repository name contains the string or all repositories in 
the response do.

#### Pagination
- verify (using fruitful names like "tetris" and randomly generated numbers 
with a reasonable upper bound that the the _page_ and _per_page_ query 
parameters produce the correct results.
- verify that out-of-range page numbers (based on Link header info) result in
 graceful responses and informative error info.
- verify that non-numeric _page_ values result in
 graceful responses and informative error info.
 
## Running tests
I run all the tests in Intellij IDEA. They can also be run from the 
[Jupiter Console Launcher](https://junit.org/junit5/docs/current/user-guide/#running-tests-console-launcher).

Tests of support code are in the **src/test** folders. Tests of the domain core 
(i.e. the GitHub API) are in **src/main**. In IDEA, selecting the
**Run All Tests** menu item will run both sets, regardless of the level at 
which the menu is invoked.

 ## The code
 ### Pragmatic/Evolutionary coding
 Following the XP principle of minimalism, this code implements only what is 
 needed for the current tasks. It's definitely skeletal.
 
 This is code that is written to be refactored. For example, the 
 **ApacheHttpPort**, the **WebRequestAdapter**, and the test classes that use
  them should undergo two types of refactoring:
 - _Horizontal_: separation of concerns means that certain functionalities need
  to be moved among these classes and existing functions need to be split. 
  Example: URI and HTTP header generation, which currently conflate domain 
  semantics and communication support.
 - _Vertical_: Abstract ports and adapters should be extracted and perhaps 
 moved into a general utility project. Headers passed into the port with each
  request are redundant, and an immutable subclass of the port could have 
  reusable headers injected at construction.
 
 Because the tests are really the "production code", the Junit test classes 
 are merely the reporting "shell" and the core functionality of the tests is 
 in classes that are not dependent on Junit. The core Junit tests should 
 eventually be replaced by a custom framework.

## Rationale

This is opinionated code. Here are the opinions that have driven this project:

### Automated Testing
**_Test automation is software development._** I'm treating this exercise as a 
development project and applying "best practices" (a much-abused term) from 
"normal" software development (I should say "functional+OOP server-side 
development", 
because browser-side development, in spite of all the over-hyped frameworks 
and Node.js empire-building, is not ready for prime time).

**_Tests are not just pass/fail._** When they fail, they need to provide all  
possible information for rapidly locating, analyzing and fixing problems.

### Language 
I used Java 8 because I can write it faster and more fluently than Scala, 
which I would prefer otherwise. To mitigate some of the well-known weaknesses
 of Java (exceptions and nulls), I've incorporated a **Result** monad and 
 auxiliary **SafeError** classes. (Scala is too permissive - it allows 
 us to throw exceptions; it should only allow us to catch them.)

I believe that OOP brings support for 
evolution through refinement of the metaphors that constitute the 
[domain model](https://en.wikipedia.org/wiki/Domain-driven_design), and that 
functional programming provides the basis for the cleanest and most efficient 
code. 
### Architecture
I have applied a refinement of [Hexagonal Architecture](http://alistair.cockburn.us/Hexagonal+architecture) 
as follows:

#### Fractality
Every software object from the largest 
distributed system down to a single line of code or even machine instruction 
can 
be logically analyzed into 
-  a core - the [bounded context](https://martinfowler.com/bliki/BoundedContext.html) 
,
 and 
- support elements 
  - internal (pure functions) 
  - and external 
(ports and adapters).
#### Core
The core is an explicit domain model: the classes and functions/methods that 
explicitly represent the domain
 (== the objects and arrows of the domain-as-[category](https://en.wikipedia.org/wiki/Category_theory)). 
#### Internal support
[Pure functions](https://en.wikipedia.org/wiki/Pure_function), custom-written
 for this object or from linked-in libraries.
#### External support
- Adapters: translators between the core domain and external domains, 
connected to
- Ports: IO handlers

The pairing of an adapter and a port is a **Ujoint** (universal joint - 
metaphor courtesy of David Wollman).

Technically, even a function that returns the time is implicitly a Ujoint: it
 communicates with an external object (the "system"), and it is not a pure 
 function.