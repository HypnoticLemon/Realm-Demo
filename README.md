# Simple Realm Database implimentation with JAVA

Data will get from API and store in local database (Realm) and when phone is not connect to internet it'll retrieve data from databse.

## Installation

add this dependencies  `classpath 'io.realm:realm-gradle-plugin:2.2.1'` in project level build.gradle file

next step is add `apply plugin: 'realm-android'` in application level build.gradle.

also add this line to application gradle.

  `realm {
        syncEnabled = true
    }`


## Contributing

1. Fork it!
2. Create your feature branch: `git checkout -b my-new-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin my-new-feature`
5. Submit a pull request :D


## License

This Project is under apache licence v2.0
