# 在FISCO-BCOS上指定群组ID并部署WeIdentity

WeIdentity支持FISCO-BCOS 2.x的跨群组功能。您可以在确定被访问的节点存在多个群组的前提下，通过指定群组ID，达到跨群组部署、执行WeIdentity功能的目的。具体场景和步骤如下。

注意：您必须首先确认被连接的节点确实处于需要连接的群组里。否则，WeIdentity会报错，错误为“群组不存在”。

## 安装部署工具跨群组部署WeIdentity

您只需在下载部署工具之后，修改位于`\script\tpl`中的`fisco.properties.tpl`，找到其中的：

```
group.id=1
```

并将其中的1修改为您想要连接的群组，之后，正常运行`compile.sh`及`deploy.sh`以完成编译和部署。

## 源码方式跨群组部署WeIdentity

您只需在下载源码之后，修改位于源代码目录下`\src\main\resource`中的`fisco.properties.tpl`，找到其中的：

```
group.id=1
```

并将其中的1修改为您想要连接的群组，之后，正常运行`run.sh`以完成编译和部署。

## 切换群组

在FISCO-BCOS的多群组架构中，不同群组之间的链上智能合约和WeID是完全无法互通的。在不同的群组上部署的WeIdentity智能合约会被存入当前的`fisco.properties`中。
如果您在多个群组上都部署了WeIdentity合约，那么建议您在切换群组之前，备份本群组特定的`fisco.properties`。未来，只需替换此配置文件，就可以达到切换群组的目的。