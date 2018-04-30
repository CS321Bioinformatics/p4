# p4

Pertinent Information Regarding File Updates:

*Created git repo                       3/29/18

*created java files to test Git repo    3/29/18

*added .gbk files                       3/29/18

#Team Programming Project: Bioinformatics
#CS321
#Team: Ryley Studer, Jason Smith
#Spring 2018

Included files:
  BTree.java - Implementation of B-Tree specialized for this project
  BTreeCache.java - Cache used by BTree.java
  BTreeNode.java - Node class used by BTree.java
  BTreeTest.java - Simple test for BTree.java
  GeneBankConvert.java - Class to convert between String and long integer
    representation of gene sequences
  GeneBankCreateBTree.java - Main class for GeneBankCreateBTree program; creates
    a BTree from a .gbk file
  GeneBankSearch.java - Main class for GeneBankSearch program; searches a BTree
    file created by GeneBankCreateBTree for sequences specified in a query file
  README - This file
  TreeObject.java - Object class used by BTree.java

Compiling and running:
  Compiling:
    javac *.java

  Running GeneBankCreateBTree:
    java GeneBankCreateBTree <cache> <degree> <gbk file> <sequence length> [<cache size>] [<debug level>]

    <cache>: 1 to use a cache, 0 to use no cache.
    <degree>: Degree to use for the BTree. If 0, the default degree (optimal for
      block size 4096) is used.
    <gbk file>: .gbk file to create a BTree from.
    <sequence length>: Length of gene sequences to store in the BTree.
    <cache size>: Size of the cache (maximum number of nodes to store).
    <debug level>: 1 to write a dump of the BTree data to a file named "dump". 0
      to not write a dump.

    GeneBankCreateBTree will create a BTree file in the same directory as the
    gbk file with the naming format:
      <gbk filename>.btree.data.<sequence length>.<degree>

  Running GeneBankSearch:
    To run GeneBankSearch you need a finished BTree file as created by
    GeneBankCreateBTree. The sequence length in the BTree should be the same as
    the sequence length you are searching for.

    java GeneBankSearch <cache> <btree file> <query file> [<cache size>] [<debug level>]

    <cache>: 1 to use a cache, 0 to use no cache.
    <btree file>: Filename of the BTree file to search.
    <query file>: Filename of the query file to use.
    <cache size>: Size of the cache (maximum number of nodes to store).
    <debug level>: If included, must be an integer. Currently has no effect.

    GeneBankSearch will print a summary of the search results showing how many
    times each sequence was found.

Timing results:
   GeneBankCreateBTree:
    $ time java GeneBankCreateBTree 0 0 data/test3.gbk 7
    real        
    user        
    sys         

    $ time java GeneBankCreateBTree 1 0 data/test3.gbk 7 100
    real        
    user        
    sys         

    $ time java GeneBankCreateBTree 1 0 data/test3.gbk 7 500
    real        
    user        
    sys         

  
  GeneBankSearch:
    $ time java GeneBankSearch 0 data/test3.gbk.btree.data.7.128 queries/query7 > /dev/null
    real        
    user        
    sys         

    $ time java GeneBankSearch 1 data/test3.gbk.btree.data.7.128 queries/query7 100 > /dev/null
    real        
    user        
    sys         

    $ time java GeneBankSearch 1 data/test3.gbk.btree.data.7.128 queries/query7 500 > asdf
    real        
    user        
    sys         

    


Explanation of the BTree file format:
Index 0: BTree Metadata
0: Degree int
4: Node Size int
8: Root Offset int
Index 12: Location of First Node
Node Metadata (5 bytes)
12: isLeaf boolean (1 byte)
13: number of objects int (4 bytes)
Node contents
17: parent offset int (4 bytes)
21: TreeObject(1) (12 bytes per)
DNAString long (8 bytes)
Frequency int (4 bytes)
33: child(1) pointer int (4 bytes per)
