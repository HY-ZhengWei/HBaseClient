# HBaseClient
HBase客户端数据管理软件


![image](images/HBaseClient_Main.png)
HBaseClient -- 客户端工具
	1: put支持中文
	2: 支持文件形式的批量put命令执行
	3: 支持扫描目录下所有文件的批量put命令执行
	4: 支持put命令字符的执行
	5: 支持文件编码自动识别
	6: 支持图形化界面管理

命令格式：
	HBaseClient <IP=xxx> <[File=xxx [FileType=GBK]] | [cmd=xxx]> [-window]

命令参数说明：
	IP         HBase数据库地址
	File       加载命令文件的路径或目录的路径
	FileType   加载命令文件的编码格式。可自动识别。当识别不到时，默认为GBK
	CMD        加载命令字符
	Language   选择语言。cn:简体中文、 en:English
	-window    启动图形化管理界面
	/v         显示版本信息
	/?         显示帮助信息


---
#### 本项目引用Jar包，其源码链接如下
引用 https://github.com/HY-ZhengWei/hy.common.base 类库

引用 https://github.com/HY-ZhengWei/hy.common.file 类库

引用 https://github.com/HY-ZhengWei/hy.common.hbase.1.x 类库

引用 https://github.com/HY-ZhengWei/hy.common.ui 类库

引用 https://github.com/HY-ZhengWei/XJava 类库