# eindopdracht2023-Mario-Daoud
Application that helps you explore, discover and pick out wintersport locations to visit.

## Database 
![diagram](https://github.com/Mario-Daoud/backend-alpineer/assets/113902874/bb390086-1f82-40ee-b097-376774e447f1)

#### Country
Deze tabel bevat alle landen. Een land kan meerdere locaties hebben.
#### Location
Deze tabel bevat alle locaties waar er gesport kan worden. Een location is altijd verbonden aan 1 country. 
#### Review
Deze tabel bevat alle reviews. Een review is altijd verbonden aan 1 user en aan 1 location.
#### User
Deze tabel bevat alle gebruikers.

## Documentation
<a href="https://alpineer-backend.azurewebsites.net/swagger-ui/index.html#/location-controller/getAllLocationsWithCountries">API docs</a>
