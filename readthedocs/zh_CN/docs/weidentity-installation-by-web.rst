.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity JAVA SDK安装部署文档（可视化版本）
=================================================

整体介绍
--------

可视化版本只需要简单的几步就可以完成安装，其他的操作都可以在网页上面进行完成，大大降低了操作难度。

.. note::
     注意: 建议在内网安装部署。


1.下载安装脚本
""""""""""
.. code-block:: shell

  wget -c  https://github.com/WeBankFinTech/weid-build-tools/raw/develop/script/install_build_tool.sh

2.执行安装脚本
""""""""""

.. code-block:: shell

  chmod u+x install_build_tool.sh
  ./install_build_tool.sh

3.执行构建脚本
""""""""""

.. code-block:: shell

  ./build.sh

4.启动Web服务
""""""""""

.. code-block:: shell

  ./start.sh

出现下列输出，则表示Web服务启动成功。

.. code-block:: shell

    the server start successfully.
    the server url:  http://localhost:6102/index.html

.. note::
     如果您想使用命令版本, 可以按照 `命令版本部署 - 1.2章节 <./weidentity-build-with-deploy.html>`_ 来操作。


附
""""""""""""""""""""""""""""""""

1. 请访问主页通过指引来完成配置和部署合约等相关操作。

* 需要提前准备区块链节点

* 需要有可访问的MySql数据库


2. 合约部署完成将会生成相关的配置文件:

* admin密钥文件目录 : ./output/admin/

* 资源文件目录(用于应用集成SDK): ./resources/
