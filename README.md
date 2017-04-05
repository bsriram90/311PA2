# 311PA2
This is the grader for Programming Assignment 2 for COMS 311 - Spring 2017. There are 36 tests in total including the performance test.

The contents of this Read me is split into 2 sections
1. TestCase Setup and Details
2. Grading details

## TestCase Setup and Details
The following steps should get the TestCases working
1. The tests are Junit tests. The necessary external jars have been added to [lib](https://github.com/bsriram90/311PA2/tree/master/lib).
  1. These tests must be added to the project library if your IDE does not do it yourself.
1. GraphProcessor and WikiCrawler must be replaced with student submission along with any other helper classes that they have used.
1. RefGraphProcessor is our reference class implemented by Pavan used to compare the student's run time. It uses VertexList as a helper class.
1. Homework5Test contains all the tests to be run.
  1. Upon completion of testing, the points and comments will be written into points.csv and runtimes will be written into runtimes.csv.
1. Graph-**.txt will be generated as output for each test. This can be referenced later if needed.
1. Only the performance test has a time bound. The timeout is defined by TIMEOUT_MINUTES in Homework5Test.
1. I also needed to set the project compile output and the test output folder in my IDE(Intellj) for it to work properly.

The following steps should be followed before running the TestCases for each student
1. Replace the value for 'studentName' in Homework5Test.
2. Copy the submission and overwrite WikiCrawler and GraphProcessor.
3. Edit WikiCrawler such that base url is 'http://web.cs.iastate.edu/%7Epavan'.
3. Run all tests in Homework5Test.

## Grading details
Total points is 500. It's split into the following
1. Report - 100 points
2. Crawler - 150 points
   1. Extract links - 25 points
   2. Crawling - 125 points
3. GraphProcessor - 200 points
   1. outdegree - 9 points
   2. sameComponents - 60 points
   3. componentVertices - 45 points
   4. numComponents - 21 points
   5. largestComponent - 21 points
   6. bfsPath - 44 points
4. Efficiency - 50 points

### Report
Report carries more points this time because we expect them to use the best datastructures for the Crawler, BFS and SCC algorithms and explain their reason for picking them here. 

### Crawler
We run the submitted Crawlers against a series of web pages uploaded to Pavan's department webpage. Each time an incorrect graph is returned, we add a comment that contains a tag that can be used to identify the exact graph where it went wrong. I will upload the graphs here later. 

We test the crawlers on how they handle 
* Self edges
* Duplicate edges
* Simple web graph
* Valid cycle in web graph
* Stopping when they hit the max nodes

### GraphProcessor
We have a series of Graphs defined as text files in the [input](https://github.com/bsriram90/311PA2/tree/master/input) folder. There are 7 graphs, and for each graph we test outDegree, sameComponent, componentVertices, numComponents, largestComponent, and bfsPath. Each test of each of those methods except bfsPath carries 3 points, whereas bfsPath carries 5.5 points.

### Efficiency
We only time their GraphProcessor methods, specifically, the constructor, numComponents, sameComponent and componentVertices. The test graph has 120,000 vertices and about 17 million edges. We test sameComponent and componentVertices on 10,000 different inputs. We compare it against Pavan's implementation and deduct points if the submission is more than 3 times slower than the reference. 
