How to run this test

1. download the source code from github
2. put in the desired folder
3. make sure maven is installed before run the test
4. If maven is not installed or doesnt exist please install it in your machine and register it in the environment variable
5. if maven is installed, open terminal or cmd and go to the project folder from step 2
6. before run test, make sure device is connected and developer option is on
7. if you run in emulator make sure your device developer option is enabled and device is recognized in adb, to check it enter this command "adb devices"
8. your device id should be appeared in the terminal or cmd
9. after steps above is clear go open the terminal and point the terminal to the correct project folder, 
10. download APK and put in "APK" folder, then run this command
	"mvn clean install"
11. after the console is done checking the library next is run the test by enter this command in terminal
	"mvn clean verify"
11. wait until the apps is running on your device
12. device will ask prompt to run the apps, please standby
13. wait until all steps is finish the steps  
14. check the terminal after the steps is succesfully executed and alseo to see the result if the test is finish and the result can be passed or failed
16. if failed check the logs what step is failing, and fix the steps