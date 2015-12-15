# LoyCore

##Important Notes:
Only run the LoyCore on the loy server, its hard coded into our database atm, so running it anywhere else will break stuff, also since its hard coded, dont share the source since it has passwords in it...

##Git Ignore File:
If your using InteliJ, I set the gitignore file to exclude InteliJ files. But if your using something like eclips please update the gitignore file so local ide settings are not synced.

Please update the gitignore to exclude any compiled code, or just put it in a folder called "output", I have that set to ignore.

##How to properly compile:
Compile on JDK 8 or higher

InteliJ:
* Create an artifact called "LoyCore" type JAR containing:
* LoyCore compiled source
* config.yml
* plugin.yml
* (You can also create a META-INF folder)
* Check "Build on make"
* Set the output to whereever you want the jar to be made.
* Make the project, the Jar should be created.
