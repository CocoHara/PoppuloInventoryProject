# Poppulo Inventory Project

## My Set-up:

Eclipse Oxygen.3a Release (4.7.3a)

Apache Tomcat 8.0.51

## How to Install and Run the Application:

Start by starting the database. When you open the project the **Ant** view should be on the right side of the code view. If Ant is not visible simple go to the **Window tab -> Show View -> Ant**.
 
When Ant is visible on the right side of the code open the **hsqldb** file. Simply double left click the **start** button and the database should be running.
 
To run the server: Right click on the **InventoryServer** Java Dynamic Web Project. Highlight **Run As** and then Left Click **Run on Server**.

When the Run on Server window pops-up choose an existing server and choose the **Tomcat v8.0 Server** at localhost. Click **Next** and make sure the **InventoryServer** Jar is in Configured. Then click **Finish**.      

If this option is not available you have to download the **Apache Tomcat 8.0.51** server online and install it onto your PC. In eclipse you should get a window like this
 
Select the **Tomcat v8.0 Server** and click **Next**. The next window should look like this:

                 
Click the **Browse** button and search for where you installed the **Apache Tomcat 8.0.51** server and click **OK**. Run on Server will then take you to the window in the image below where you should make sure the **InventoryServer** Jar is in Configured and then click **Finish**
 
To run the Application itself open the **MainWindow** Java class in the **InventoryClient** Java Project and click run or the **green play button**.
