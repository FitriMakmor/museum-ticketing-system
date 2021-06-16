# museum-ticketing-system :classical_building: :label: :walking_woman: :walking_man:
A Museum Ticketing and Gate Control System using Concurrent / Multithreaded Programming in Java

## Project Description

A museum reopens after months of closure due to the COVID-19 pandemic. As a safety measure, the museum’s management decided to limit the number of visitors in the museum at one time. The following rules have been set:

* The museum opens from 9.00 a.m. to 6.00 p.m. daily.
* The museum receives not more than 900 visitors per day.
* Not more than 100 visitors are allowed in the museum at one time.
* The museum has 2 entrances – South Entrance (SE) and North Entrance (NE); and two exits – East Exit (EE) and West Exit (WE).
* Each entrance and exit has 4 turnstiles (T1-T4) for visitors to access through. The turnstiles have sensors to detect visitors entering or leaving the museum.
* Visitors use the museum’s mobile app to purchase tickets. The ticketing system refuses the purchase when the daily limit of visitors (900) has been reached.
* Tickets will be sold from 8.00 a.m. to 5.00 p.m. daily.
* A control system that integrates with the ticketing system will be built to control visitors’ access to the museum.

We are required to build a multithreaded program to simulate operations of the control system. The following are assumptions for our simulation:

* First request to purchase tickets will be made at 8.00 a.m.
* Subsequent purchase will be made every 1-4 minutes. Each purchase will be for 1-4 tickets.
* Every ticket has an ID in the form of T####, where #### are running numbers from 0001 to 9999. There is also a timestamp of purchase on each ticket.
* Visitors enter the museum based on the timestamps on their tickets, i.e. visitors who purchased their tickets earlier enter the museum before those purchase their tickets later.
* Each visitor randomly uses a turnstile at either South Entrance or North Entrance to enter the museum. 
* Each visitor stays in the museum for 50-150 minutes. The duration of stay will be randomly assigned to the visitor when the visitor is entering the museum.
* After the duration of stay is over, the visitor randomly uses a turnstile at either East Exit or West Exit to leave the museum. 
