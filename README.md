# File-storage-uploader
This simple single-user file storage project serves my needs to upload many files from various devices via HTTP and browsers through my home intranet to my PC and store them without using any cloud service. Sorry for Russian-only localization, but looking at the Java code you'll understand what any function does. Also subfolders are not supported. Maybe someone diligent will fix it.
I used NetBeans 7.0.1, JDK 6, Glassfish JPS. In essence this is JSP NetBeans project.
All third-party JARs are stored in "[my_project_folder]/web/WEB-INF/lib/" folder.
Place storage_root.txt file into your config Glassfish domain folder. Like this in my case: "C:\Program Files\glassfish-3.1.1\glassfish\domains\domain1\config\storage_root.txt".
The contents of this file (storage_root.txt) is the path of your folder where you will store uploaded files. For example:
D:\WebServices\FileStorageJSP\web\files
Deploy it and use it in your household.
