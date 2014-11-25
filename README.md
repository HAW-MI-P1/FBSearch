<h1>Components and Responsabilities</h1>
GUI: Daniel (Fenja)
Controller: Hagen (René)
API-Wrapper: Raimund (Lotte, Vitalij)
Parser: Lukas (Nora)
DB-Controller: Florian
Bildabgleich: René (Hagen)
Ontologie: Fenja (Daniel)
Fuzzy: Florian


<h1>Short import for different IDEs</h1>
You need Gradle! Its awesome! Its for dependence control for multiple workstations with different IDEs. In new versions of eclipse you could find the Gradle plugin in the marketplace.

Eclipse: File -> Import -> Gradle -> Gradle Project

Intellij IDEA: File -> Import -> build.gradle

NetBeans: File -> Open Project...

<h1>How to install the project</h1>
<ol>
<li>Install gradle if not already done: Eclipse -> Help -> Install new Software...</li>
<li>(Eclipse 3.7 - 4.2) Work with: Gradle - http://dist.springsource.com/release/TOOLS/update/e3.7/</li>
<li>Choose: Core - Eclipse Integration for Gradle</li>
<li>Click Finish</li>
<li>Import: Eclipse -> File -> Import...</li>
<li>Choose: Gradle -> Gradle Project</li>
<li>Click Browse... (to Project directory)</li>
<li>Click Build Model</li>
<li>Choose: FBSearch</li>
<li>Click Finish</li>
</ol>

Note: Eventually you should clean and refresh the imported project or right click on Project and go to Gradle -> Refresh Dependencies. Also check your JRE and java compiler level. The project is created with Java 1.8.

<h1>How to get the appId and appSecret</h1>
<ol>
<li>Get a facebook account</li>
<li>Get a facebook developer account</li>
<li>Create new app</li>
<li>Insert app domains: localhost</li> 
<li>Add new platform: website</li>
<li>Site-url: http://localhost:8080/connect/facebook</li>
<li>Mobile-site-url: http://localhost:8080/connect/facebook</li>
<li>Save</li>
</ol>

<h1>How to setup the project</h1>
<ol>
<li>Place your appId and appSecret in /src/main/ressources/application.properties</li>
<li>Start the application main function in /src/main/java/hello/Application.java</li>
<li>Open your browser and visit: http://localhost:8080/connect/facebook</li>
<li>Connect to facebook</li>
<li>Get your fabulous news feed!</li>
</ol>

<h1>How to setup DB-Controller</h1>
<ol>
<li>Install a mySQL server. E.g. <a href="http://dev.mysql.com/downloads/mysql/">MySQL.com</a></li>
<li>Create database <b>mip</b></li>
<li>Create table <b>api_results</b><br/>
<code>
CREATE TABLE `mip`.`api_results` (
`k` VARCHAR(45) NOT NULL,
`v` VARCHAR(3255) NULL,
PRIMARY KEY (`k`));
</code></li>
<li>
  Execute code:<br>
  <ul>
    <li>For Test-Setup change uname and password to <b>root</b> (not very safe but purpose rational)</li>
    <li>Use operation <code>DB.connect(String url, String user, String pass);</code></li>
  </ul>
</li>
</ol>
<h3>Necessary Libs in Build Path</h3>
<ul><li><a href="http://dev.mysql.com/downloads/connector/j/">mysql-connector-java-5.1.33-bin.jar</a></li></ul>
