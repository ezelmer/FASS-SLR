~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
About this project:
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
This project started with a list of DOIs, with each DOI being a related work to the original slr. The related works were then populated with additional metadata using pubmed apis. This work is all listed in the 
"FASS-SLR\Source\mavenproject1\src\main\java\com\mycompany\mavenproject1\Main.java"
File.

Originally, DOIs were also searched for through the access of other APIs, such as the elsevier api, springer api, CORE api, MEDRXIV, and Crossref (which includes wiley, as well as others). The ability to populate a reference object using one of these services still exists in the code for documentation purposes, however the methods themselves have been deprecated. 

Data was retrieved and stored in a Reference object, which held fields for all relevant information. From there, object information was dumped to excel files. 

The convToJson.java file converts any data in the excel files to json files. 

ProvideMoreSLRInfo.java performs the same search method used on related works, but allows you to populate SLRs with their abstracts, publishers and more.

Most other files were simply added for the sake of data formatting: VerifyChatGPT, createQrel, chatGPTREquestFormatter, generateElasticFile, and filterPubmedFromElastic were all used to slightly modify data based on individual needs.


~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Some important things to note if you plan on making edits:
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
FOLDS:
Folds are easily defined by the RefLoc integer array: RefLoc[slrnum-2] returns the fold number associated with the SLR. This array is stored in Main.

INITIALIZE:
Initialize is a very helpful tool for initializing the set of SLRs in an arraylist that can be easily modified. It's a good starting point to work with. It's located in main.

DUMPDATA VS DUMPSLRDATA:
DumpData dumps a single SLR's data with the name 'k.txt' where k is an integer passed as a parameter
DumpSLRData dumps all slrs data given an arraylist of SLRs. 
