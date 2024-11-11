# Concept Locator

Concept Locator is to search for the concept in source code. The input is a query representining a concept and output is the ranked list of
code locations based on similarity of the concept and the code portion. The implementation is based on the works of [Marcus et. al](http://www.cs.wayne.edu/~severe/publications/Marcus.WCRE.2004.IRApproach.pdf)
titled "An information retrieval approach to concept location in source code" on 2004 and [Gregory et. al.](http://ai2-s2-pdfs.s3.amazonaws.com/cca2/439b3ebaa377f34a942f4826c559b0c9eafb.pdf)
titled "On the use of relevance feedback in IR-based concept location" on 2009. 

## Abstract
Concept Location is a part of incremental change. The developers frequently undertake the concept location process for software maintenance and evolution. When a change request or bug report comes, the developers first find out the place in source code where they should work on to mitigate the change request or bug report. They should have enough knowledge about the source code for finding the place in source code. But it becomes difficult for a large system to know where the concept related to that change or bug request being implemented in source. It can be seen as they search for specific information in source code. Marcus and others proposed Information Retrieval (IR) approach for concept location. In this approach, the source code is treated as collection of documents. The developers search for information with a query representing a concept. IR returns a set of documents from collection of documents best matched with the query. IR-based concept location has several advantages over traditional grep based technique and dependence graph. IR approach saves time. It also helps the developers to know about an unknown software system. Several extensions are proposed in IR-based approach including relevance feedback, clustering, and formal concept analysis. So, a concept location tool is developed using the IR approach and IR with Relevance Feedback approach to solve the search problem for concept location. Relevance Feedback produces better result than IR approach.


# Tool's UI
![A sample Search Result in Concept Locator Tool](img/Search%20Result.PNG)
