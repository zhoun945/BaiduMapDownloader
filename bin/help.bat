@echo off

cd ..
set java_opts=-server -Xms1024m -Xmx1024m

setlocal enabledelayedexpansion
set classpath=.;conf;BaiduMapDownloader.jar;
for %%c in (lib\*.jar) do set classpath=!classpath!;%%c
set classpath=%classpath%;

echo java_opts = %java_opts%
echo classpath = %classpath%
java %java_opts% -cp %classpath% com.bfd.map.TileHelper

pause