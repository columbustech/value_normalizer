# Value Normalizer
Cloud microservice for normalizing values of an attribute.

## Overview
Value Normalizer is a microservice which can be used to normalize values in the column of a csv file. This tool allows you to upload csv file to the server, select a column and then normalize it based on user feedback. This repository contains both backend code(normalizer) and the UI code (normalizer-ui) which can be hosted together or seperately in different machines. Once the application has been hosted, it can be used via a normal web browser, or it can be queried via the APIs. 
For more details about the APIs, click on the normalizer project.

## Installing on local machine
### Requirements
Java 8+ (For the backend), Apache Maven, Node 11+ with Angular cli (For the UI)
### Steps
1) Clone the repository 
```
git clone git@github.com:anhaidgroup/value_normalizer.git
```
#### Run Backend Code
2) Create storage folder (where CSV files will be stored in the server) based on your platform. Check [ConfigurationService](https://github.com/anhaidgroup/value_normalizer/blob/master/normalizer/src/main/java/edu/wisc/entity/normalizer/services/ConfigurationService.java) for exact folder locations. Change these locations based on your filesystem. 
By default for windows platform, the location is `src/main/resources/csv/`, for linux platform, the location is `/normalizer/csv/`.

3) Run maven clean install in the backend code. 
```
cd normalizer
mvn clean install
```

4) In the target folder, there'll be a normalizer-0.0.1-SNAPSHOT.jar created. Run that jar file. The server will start on the port 8080 and can be accessed using http://localhost:8080
```
java -jar normalizer-0.0.1-SNAPSHOT.jar 
```

5) If you are using an IDE like Eclipse/Intellij, you can directly run the backend using their internal run command as well. 

#### Run the UI Code
6) To run the ui code, just run the command ng serve and the UI server will start on port 4200 which can be accessed using http://localhost:4200
```
cd normalizer-ui
ng serve
```

7) If your backed server is running in a different machine than the UI, you'll have to configure the proxy.conf.json accordingly.
```
https://github.com/anhaidgroup/value_normalizer/blob/master/normalizer-ui/proxy.conf.json
```

## Installing on Aws using docker
### Steps
1) Login to the ec2 console and launch an ec2 instance. Our application was tested on Ubuntu 16.04, hence we recommend using the same platform. Also, as both UI and backend servers run on the same machine, we recommend using atleast 16 GB of memory and 120 GB of storage. 

2) Once the instance is created, AWS will give you a public key (mykey.pem) which can be used to login into your newly created machine. 

3) As our application uses ports 8080 and 4200, make sure you allow All TCP/UDP traffic to these ports. Go to “Network & Security”, click on the security group your instance is part of. Click on "Inbound Rules" and Add TCP/UDP traffic. Add for both ports 8080 and 4200. 

4) Note the public DNS name of the instance you created from the dashboard.

5) Login into the aws instance. NOTE: Sometimes the user is "ec2-user, root". It depends on the instance you choose.
```
ssh -i /path_to_key/mykey.pem ubuntu@public_dns_name
```

6) Install Docker using the following commands
```
sudo apt-get update
sudo apt-get install \
    apt-transport-https \
    ca-certificates \
    curl \
    software-properties-common
```
```
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository \
   "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
   $(lsb_release -cs) \
   stable"
sudo apt-get update
sudo apt-get install docker-ce
```
Refer [docker documentation](https://docs.docker.com/install/linux/docker-ce/ubuntu/#install-docker-ce) in case there are any problems with the installation. 

7) After you have successfully installed docker, pull the latest image of *valuenormalizer* from dockerhub.
```
sudo docker pull dgera/valuenormalizer
```

8) Run the docker image. Note: Make you press either CTRL+q or CTRL+p to exit the docker. This will keep running the docker image in the background and will take you back to the instance shell. 
```
sudo docker run -it -p 8080:8080 -p 4200:4200 dgera/valuenormalizer
cd ~
sh init.sh
Press CTRL+q or CTRL+p to run the docker image in background.
```
9) Navigate to http://public_dns_name:4200 to access the tool. 

10) You can stop (or terminate) the service from the EC2 instance dashboard. Select your EC2 instance, click on “Actions” and then click on stop (or terminate)

## Usage
To use this tool, navigate to the location where its deployed (either http://localhost:4200 or http://public_dns_name:4200). This opens up the main page where you can upload a CSV file to start normalizing. Once you select the csv file and click on upload, the tool will ask you to select a column. Choose the column you want to normalize and the tool will navigate to the local merge UI. 
1) Local Merging - The first step in merging process is the local merging. On each page of local merging, select the entities which are similar in the list. Click on merge once the selection is complete. Do this for all the pages. 
2) Global Merging - Clicking *next* on the last page of local merging, will take you to the global merging UI. In this section, you'll see three columns on the top (which will be 3 of the values of the column you selected for normalizing). Select the options where the column value is same as the row value (values you want to normalize). Do this for all the pages. Once you click next on the last page, the top 3 column values will change. This will repeat until all the values are exhausted.
After finishing the global merge process, you'll be left with the normalized values.

## API
For API documentation, refer the [Readme](https://github.com/anhaidgroup/value_normalizer/blob/master/normalizer/README.md) of backend code.

