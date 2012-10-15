scala-logic
===========

scala-logic is a library for logic variables support in Scala. 
It is currently in its very early development stage, so don't hold your breath.

The library will supported typed logic variables and support for unification. It does not
add a full-fledged Prolog-like search engine or constraint solving engine, but rather acts
as a core library if one wanted to implement these.

Usage
=====

Creating logic variables
----

Logic variables can be created simply as follows:

    Var[Int]("X") // create a Int-variable called X

Variables are stored in an implicit variable store, which keeps track of all performed unifications.
Hence, another call of the above code would result in the same object being returned, as there can
only be one variable `X` in a store.

Unification
----

A logic variable can be bound to a term as follows:   

    Var("X") =:= 3 // bind the previously created variable to the term 3
    Var("X") =:= 4 // throws a UnificationError because X was bound to 3 earlier
    3 =:= Var("X") // equivalent to above, except now X is already bound to 3, so nothing is done
   
    Var("X") =:= Var("Y") // unifies the two variables
    Var("Y").value == Some(3) // true
    Var[Int]("X") =:= Var[String]("Y") // compile error
   
Terms
----

A term can be created via the TermN (analogous to FunctionN) traits as follows:

    new Term1[Int, Int] {
      val symbol = "f"
      val arg1 = Constant(3)
    } // constructs the term "f(3)"
    
Terms can also be evaluatable to their type as follows:

    new Term1[Int, Int] extends Function0[Int] {
      val symbol = "f"
      val arg1 = Constant(3)
      override val apply = 3
    }.value == Some(3)
    
Variables and constants are also Term0 instances.

Variable store
====

All variables are assigned to an implicit variable store at their creation. This store keeps track
of the unifications and provides faster lookup of values, as explained by the following example:

    Var("X") =:= Var("Y") =:= Var("Z")
    Var("Z") =:= 1
    Var("X").value == Some(1)
    Var("X").value == Var("Y").value
   
Looking up the value of `X` for the first time requires to look up the values of `Y` and `Z`, before
reaching the actual value 1. The two subsequent requests, however, are optimized by the variable
store to find the value 1 in constant time. 