## Visa Journey Data Analyzer

### Overview

This is a little program which goes to the visajourney.com website and extracts relevant timeline data for EAD applicants.

It runs and takes 50 pages of previous timelines' data and outputs three files. Normally this equates to about 6 months of recent data. Three text files will be output to the VJ directory (from which it's being run).

### Running the program

Must have sbt installed in the path of the system. But to kick it off it's simply a case of running 

```
sbt run
```
You should see an output giving you the farthest back date the data was taken from like this:

```
> [info] Running vj.Main
Data taken from present day to yyyy-mm-ddT00:00:00.000-07:00 
```

### Analysis

If you look in the VJ folder you'll find three files: approvalToCard.txt, biometricsToApproval.txt and filedToApproval.txt. The names of the files are self-explanatory. The contents are lists of number of days between those dates found on the website.

The program deals with missing dates or incorrectly formatted dates, so the values are correct. You can import the files into Excel, Matlab, Igor Pro, R etc to do your own statistical analysis.

Let me know if you use it!