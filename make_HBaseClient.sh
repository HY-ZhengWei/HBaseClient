#!/bin/sh

cd bin


jar cvfm hy.hbase.jar MANIFEST.MF LICENSE org
cp hy.hbase.jar ..
rm hy.hbase.jar
cd ..
