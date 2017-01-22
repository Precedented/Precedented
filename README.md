
## Precedented

In rural areas and small towns experienced attorneys range from $100 to $200 an hour and can even rise to a staggering $400 in major metropolitan areas. While the average American may choose to only leverage attorneys for major complex lawsuits, the use of a lawyer for a relatively minor civil lawsuit is often impractical both in time and resources. This leaves a large segment of the American population in the precarious dilemma of deciding whether or not they should pursue a lawsuit amid the daunting legal fees. For small claims and conciliation courts (settles disputes under $5,000), many American choose to represent themselves in order to spare themselves of massive attorney fees. More often than not, however, these individuals are entering court without the legal advise necessary to prepare themselves for their case, inevitably leading to unfavorable results.

We believe that if a person does not have the resources to perform an inalienable right, he or she has essentially forfeited that right. _Precedented_ aims to restore these rights to every American, enabling users to easily search relevant legal code and precedent, guiding them through the process of taking legal action to protect their home, property, and family.

## What is _Precedented_

_Precedented_ is an intuitive legal search engine incorporating cognitive computing to allow users to ask questions about legal precedent from both state and federal courts. After the user searches a question, _Precedented_ utilizes IBM Watson's sophisticated natural language processing SDK to interpret the query and execute a basic search. _Precedented_ then uses IBM Watson's Ranking learning algorithm, an enhanced information retrieval library with machine learning, to order over 3.3 million precedential opinions provided by Court Listener based on the contents of the court opinion. These court opinions are sorted by relevance and conveniently displayed to the user.

This repository is the Java back-end application for Precedented. Uses IBM Watson API and Courtlistener.com's legal database to receive general user queries and return a ranking of related court cases.

## How we built _Precedented_

To develop _Precedented_ we utilized Apache Solr, Core, and Client Libraries to create a basic query mechanism over the corpus stored on the Court Listener severs.  When then ranked these results using IBM Watson’s Retrieve and Rank SDK.  Using training data we produced with a python algorithm with ground truths we taught the program to parse the English language and search for key legal terms. The ranking algorithm than ordered the results, providing the user with a series of links to the most relevant cases.

## Challenges we ran into

While IBM Watson is an impressive tool for unstructured data analysis, the SDK suffers from sever versioning issues and poor interaction with the Apache Solr library.  Court Listener’s varying formatting for their data structures compounded this issue.  As a result, much of the project was used to determine the formatting and find deprecated jar versions of our libraries. 

## Accomplishments that we're proud of`

We are proud of our ability to facilitate the access of critical legal information to individuals of all socio-economic backgrounds. We also successfully established a learning algorithm to efficiently search over 3.3 million precedential opinions in seconds.

## What we learned

We learned how to fuse IBM Watson's natural language processing SDK, Retrieve and Rank SDK, Apache Solr library, and Court Listener to create a platform with a profound societal impact.

## What's next for _Precedented_

We intend on implementing additional features, such as the ability to login into the search engine. By asking for a user profile _Precedented_ will be able to determine the user's address, enabling _Precedented_ to tailor court case by user location from town and county to state laws. We are also interested in expanding to nearby countries such as Canada.

