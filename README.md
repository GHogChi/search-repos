# search-repos: Rewards Network coding exercise

## Rationale

This is opinionated code. Here are the opinions that have driven this project:

### Automated Testing
_Test automation is software development._ I'm treating this exercise as a 
development project and applying "best practices" (a much-abused term) from 
"normal" software development (I should say "functional+OOP server-side 
development", 
because browser-side development, in spite of all the over-hyped frameworks 
and Node.js empire-building, is not ready for prime time).

_Tests are not just pass/fail._ When they fail, they need to provide all  
possible information for rapidly locating, analyzing and fixing problems.

### Language 
I used Java 8 because I can write it faster and more fluently than Scala, 
which I would prefer otherwise. To mitigate some of the well-known weaknesses
 of Java (exceptions and nulls), I've incorporated a **Result** monad and 
 auxiliary **SafeError** classes.

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