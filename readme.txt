The string search algorithm is implemented in this application to search through the information files of all countries in the world. By doing that, the statistics like population, area land, median age of male or female, birth rate, death rate, literacy of female and number of airpors extracted and compared among countries.

*The information files of countries and input and output file examples can be found in the files above.

### Query Format
<category 1>, [top/last], <integer> [+ category 2, [top/last], <integer> [+ category 3, [top/last], <integer>  ]]

For instance:
#### area-total,top,3
This query returns the three countries in the world which have the largest area in alphabetical order.
Output is 'Canada, Russia, United States'.

#### area-land,top,2+airports,top,4
This query returns both the two countries which have the largest land area and the four countries that have the maximum number of airports.

Output is 'Brazil, Canada, China, Mexico, Russia, United States'. 

## Motivation
This application is developed to comprehend the tree data structure and the string search algorithm. 
