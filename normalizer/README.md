# Value Normalizer Backend
The backend code of the application. This backed runs on the port 8080 and can be accessed using the APIs at http://localhost:4200/api/<API>

## APIs
1) To upload the file to the server.
```
POST - http://localhost:4200/api/upload
Param - {file:<actual file>}
Post Body - include the file with name *file* and value *actual csv file to upload*
```

2) Get file contents.
```
GET - http://localhost:8080/api/file?name=<file_name.csv>
Output - Json of file data
```

3) Get column names in the csv file.
```
GET - http://localhost:8080/api/file/header?name=<file_name.csv>
Output - Column headers with their index

Example - {"0":"Name","1":"Price"}
```

4) Get data for local merging. Output is of type [KeyValue](https://github.com/anhaidgroup/value_normalizer/blob/master/normalizer/src/main/java/edu/wisc/entity/normalizer/model/KeyValue.java)
```
GET - http://localhost:8080/api/file/local?name=<file_name.csv>&column=<column_to_perform_local_merge>&sort=1
Output - List of KeyValue pairs where index is the the exact location at which this data occurs in the csv file and value is the column data.

Example - If *hello* occurs at location 5, *hellos* at location 6, *bye* at location 7, *byes* at location 8 then data would be:-
Example Output - [{"index":5,"value":"hello"},{"index":6,"value":"hellos"},{"index":7,"value":"bye"},{"index":8,"value":"byes"}]
```

5) Save local merge diff to disk. Input is of type [KeyValue](https://github.com/anhaidgroup/value_normalizer/blob/master/normalizer-ui/src/app/models/custom.interface.ts)
```
Post - http://localhost:8080/api/file/local/diff/save?name=<file_name.csv>&column=<column_to_perform_normalization>
Param - {keyval:<data of type List<List<KeyValue>>}
Input - Data of type List<List<KeyValue>>. Each list in this data is the list of values that are same and have to be normalized. 

Example - If *hello* which occurs at location 5 is same as *hellos* at location 6 and *bye* which occurs at location 7 is same as *byes* at location 8 and they have to be normalized. The data would be:-
Example Input - [[{"index":5,"value":"hello"},{"index":6,"value":"hellos"}],[{"index":7,"value":"bye"},{"index":8,"value":"byes"}]] 
```

6) Get data for global merging. Output is of type [KeyList](https://github.com/anhaidgroup/value_normalizer/blob/master/normalizer/src/main/java/edu/wisc/entity/normalizer/model/KeyList.java)
```
GET - http://localhost:8080/api/file/global?name=<file_name.csv>&column=<column_to_perform_global_merge>
Output - List of KeyList pairs where key is the actual column data and value is the list of locations at which this data occurs in the csv file.

Example - If *hello* occurs at location 5, *hellos* at location 6, *bye* at location 7, *byes* at location 8, *hi* at location 9 and 10 then data would be:- 
Example Output - [{"key":"hello","values":[5]},{"key":"hellos","values":[6]},{"key":"bye","values":[7]},{"key":"byes","values":[8]}, {"key":"hi","values":[9,10]}]
```

7) Save global merge diff to disk. Input is of type `List<List<Integer>>`.
```
Post - http://localhost:8080/api/file/global/diff/save?name=<file_name.csv>&column=<column_to_perform_normalization>
Param - {keyval:<data of type List<List<Integer>>}
Input - Data of type List<List<Integer>>. Each list in this data is the list of indexes of values that are same and have to be normalized. 

Example -If *hello* which occurs at location 5 is same as *hellos* at location 6 and *bye* which occurs at location 7 is same as *byes* at location 8 and they have to be normalized. The data would be:-
Example input - [[5,6],[7,8]] 
```

For more information about the APIs and how they have been implemented, check [controller]( https://github.com/anhaidgroup/value_normalizer/blob/master/normalizer/src/main/java/edu/wisc/entity/normalizer/controller/FileViewController.java)
