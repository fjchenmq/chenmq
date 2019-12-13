@echo off

set M2_HOME=E:\Chenmq\maven\maven-3.3.9\
set JAVA_HOME=E:\Chenmq\Plugins\jdk1.8.0_162
call set path=%M2_HOME%\bin;%JAVA_HOME%\bin;%Path%

set MAVEN_OPTS=-Xms1024m -Xmx1368m

echo %path%;
call mvn   -Dmaven.test.skip=true clean install

pause

:end
exit /b 0
