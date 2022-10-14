How to make a release
=====================

* Run the following command to deploy the artifact:

  ```
  mvn release:clean release:prepare release:perform
  ```
  
* push all changes
* update Maven artifact version in [README.md](README.md#maven)