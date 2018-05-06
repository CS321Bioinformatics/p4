#Team Programming Project: Bioinformatics
*   CS321
*   Team: Ryley Studer, Jason Smith
*   Spring 2018

Included files:
  BTree.java - Implementation of B-Tree specialized for this project
  BTreeCache.java - Cache used by BTree.java
  BTreeNode.java - Node class used by BTree.java
  RAM.java - Class to convert between String and long integer representation of gene sequences
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
    $ time java GeneBankCreateBTree 0 0 test3.gbk 7
    real        
    user        20.399
    sys         

    $ time java GeneBankCreateBTree 1 0 test3.gbk 7 100
    real        
    user        20.387
    sys         

    $ time java GeneBankCreateBTree 1 0 test3.gbk 7 500
    real        
    user        20.328
    sys         

  
  GeneBankSearch:
    $ time java GeneBankSearch 0 test3.gbk.btree.data.7.127 queries/query7
    real        
    user        
    sys         

    $ time java GeneBankSearch 1 test3.gbk.btree.data.7.127 queries/query7 100 
    real        
    user        
    sys         

    $ time java GeneBankSearch 1 test3.gbk.btree.data.7.127 queries/query7 500
    real        
    user        
    sys         

    


Explanation of the BTree file format:
*   Index 0: BTree Metadata
*   0: Degree int
*   4: Node Size int
*   8: Root Offset int
*   Index 12: Location of First Node
*   Node Metadata (9 bytes)
*   12: isLeaf boolean (1 byte)
*   13: number of objects int (4 bytes)
*   17: node offset (4 bytes)
*   Node contents
*   21: parent offset int (4 bytes)
*   25: TreeObject(1) (12 bytes per)
*       DNAString long (8 bytes)
*       Frequency int (4 bytes)
*   37: child(1) pointer int (4 bytes per)

We split the BTree up by first parsing the file in the CreateBTree driver class. This process needed
a way to convert the data that we extracted from the GeneBank files. We passed this through a convert
from string to long and created the BTree structure by using the next bit of data. Depending on how 
large the specified sequence length, this was changed to accommodate the size of the sequence that we 
use to write to the tree. After the BTree file was built, we worked on searching back through the file 
and reading the contents back from the disk, the offset of these nodes was the challenge for this 
section of our project. After we wrote out the diagrams of the BTree structure, we were able to 
conceptualize this much more efficiently.

In our second driver class we had to search back through the tree and compare our keys we saved to the 
outputs of the correct query test-file that was provided. In this process, we were able to observe the 
frequency of the node-data that the searchBTree returned. If a node showed up once, and had no 
duplicates, it would have a frequency of 0, if it showed up again, we would increment that node.


Known Issues:
Our GeneBankCreateBTree driver does compile (with cache option) and will produce a btree binary file, as well as a dump 
of the TreeObjects in the BTree and their frequencies. Our GeneBankBTreeSearch driver will compile (with cache option) 
as well. We are aware that our GeneBankBTreeSearch output is incorrect. We have not been able to locate the source of 
the issue.  
