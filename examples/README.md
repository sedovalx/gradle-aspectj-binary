Simple example of using AspectJ in a Kotlin project. 

It expects `1.0-SNAPSHOT` version of the plugin in the local Maven repository or a value in the 
`TREVIS_BUILD_NUMBER` environment variable. Suppose you have `20` as a value for the env variable, 
 the `1.0.20` plugin version is used than. Of course, you can edit the `build.config` and specify 
 the version directly. 
 
 Run
   
    ./gradlew clean run
    
to see the result. Note that you have to use AspectJ annotations to 
describe aspects.     