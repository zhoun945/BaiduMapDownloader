#!/bin/sh
cd ..
java_opts="-server -Xms1024m -Xmx1024m -Duser.timezone=GMT+08"
classpath=.:conf:BaiduMapDownloader.jar
for i in lib/*.jar ; do
	classpath=$classpath:$i
done
export classpath

echo "java_opts=$java_opts"
echo "classpath=$classpath"
java -cp $classpath $java_opts com.bfd.map.Main > logs/std.log 2>&1 &
