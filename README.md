# Spigot Application
![](https://img.shields.io/badge/dynamic/json?color=green&label=build&query=status&url=https%3A%2F%2Fjitpack.io%2Fapi%2Fbuilds%2Fcom.github.Ashengaurd%2FSpigotApplication%2FlatestOk)
![](https://img.shields.io/github/license/Ashengaurd/SpigotApplication)
![](https://img.shields.io/github/v/release/Ashengaurd/SpigotApplication)
![](https://img.shields.io/github/downloads/Ashengaurd/SpigotApplication/total)
![](https://img.shields.io/discord/690930221930643467?label=discord)

This build allows you to add a openable window for your spigot resource.  
It allows your users to check your plugin and all requirements of your plugin. (Check [#Sceenshots](#Screenshots))

## Features
* Full automated by on the Spigot api
* Linked to resource page
* Linked to support link provided on spigot
* Linked to additional information link provided on spigot
* Version check & Update button
* Customizable description
* Logo & Avatar capture
* Connection log

## Installation
You can import the project through jitpack:  
https://jitpack.io/#Ashengaurd/SpigotApplication/

Create a class and paste the following example code in the class.
This application has some more configs which you can find at [#Customization](#Customization)
```java
import me.ashenguard.api.utils.versions.Version;

public class ExampleEXE extends SpigotPanel {
    // The spigot page ID
    private final static int spigotID = 83245;
    // The current version based on https://en.wikipedia.org/wiki/Software_versioning
    private final static Version version = new Version(1, 2);

    public ExampleEXE() {
        super(spigotID, version);
        // Add Customizations here
    }

    public static void main(String[] args) {
        launch();
    }
}
```

After that you need to define this class before build. ([Maven](#Maven), [Gradle](#Gradle))

### Maven
In the maven you need to add this plugin to your build (pom.xml) replacing *path.to.class* with the class you created.
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>2.4</version>
    <configuration>
        <archive>
            <manifest>
                <mainClass>path.to.class</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

### Gradle
In gradle you need to add this to your build (build.gradle) replacing *path.to.class* with the class you created
```groovy
application {
    mainClass = 'path.to.class'
}
```

## Customization
This application has some customization which you can make.
### Description
Allow you to change the description panel.  
Code: `setDescription(String description)`
> Default: Empty
### Support Link
Allow you to change the support link.  
Code: `setSupport(String URL)`
> Default: Link provided in spigot page
### Requirements
Allow you to add requirements to application.  
Code: `addDependency(int... SpigotID)`  
Alternative: `setDependencies(List<Integer> dependencies)`
> Default: Empty  
> Note: You need to provide spigot resource ID
### Enable connection log
Another panel will pop up and show connections log.  
Code: `enableConnectionLog()`
> Suggestion: Don't enable it for build.
### Disable credits
There is a small credit of the design which is removable.  
Code: `disableCredits()`
> Suggestion: Don't remove it to support this application

### Example based on AGMCore plugin
```java
import me.ashenguard.api.utils.versions.Version;

public class EXE extends SpigotPanel {
    public EXE() {
        super(83245, new Version(1, 2));
        this.addDependency(6245);
        // If you want to support this project don't add this line
        this.disableCredits(); 
        // Don't activate in final build
        this.enableConnectionLog();
        this.setDescription(
                "Installation:\n" +
                        "To Install this plugin, Move the jar file to your plugins folder.\n\n" +
                        "This plugin is just a library for other plugins and there is no commands and events management"
        );
    }

    public static void main(String[] args) {
        launch();
    }
}
```

## Screenshots
![](https://i.ibb.co/Kz2cbwt/MAin.jpg)
![](https://i.ibb.co/xztgPRb/Requirement.jpg)