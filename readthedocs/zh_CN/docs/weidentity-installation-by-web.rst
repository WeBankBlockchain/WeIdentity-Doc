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

  wget -c  https://github.com/WeBankFinTech/weid-build-tools/raw/master/script/install_build_tool.sh

2.执行安装脚本
""""""""""

.. code-block:: shell

  chmod u+x install_build_tool.sh
  ./install_build_tool.sh
  cd weid-build-tools

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

操作说明
--------

通过内网ip访问http://ip:6102/index.html将开启可视化操作之旅。

1. 配置admin账户
""""""""""

此步骤将配置您在 weid-build-tools 里面的账户，后续的部署等操作将使用该秘钥，请妥善保管，谨防丢失。

2. 配置区块链节点
""""""""""

此步骤将配置连接的区块链节点，您需要提前部署区块链节点。

.. note::
     1. 请选择合适的部署环境，联盟链成员的环境需要一致。
     2. 配置区块链节点端口时请使用channel端口。
     3. 配置chainId时，联盟链成员的chainId需要一致
     4. 配置主群组时，联盟链成员的主群组Id需要一致

3. 配置数据库
""""""""""

此步骤将配置连接的数据库环境，请提前准备数据库名称。

4. 主群组部署合约
""""""""""

完成以上3步操作即完成了 weid-build-tools 的基础配置工作，此步骤将区分联盟链管理员和非管理员进行操作。

* 联盟链管理员

如果您是联盟链管理员，请点击页面的合约部署功能按钮，此步骤将在联盟链的当前主群组中部署您的WeID智能合约, 并且将会自动启用该合约的CNS地址。


* 非管理员

如果您不是联盟链管理员，请先联系联盟链管理员部署WeId智能合约，联盟链管理员部署完WeId智能合约后，请刷新当前页面并确认智能合约的CNS地址，请启用该CNS地址。

.. note::
     当您启用完CNS地址后，weid-build-tools 将自动帮您在第一步配置的账户注册成WeID, 如果有需要请将WeId地址发给联盟链管理员进行权威机构注册，可以通过WeId管理菜单查看您的WeId地址。

5. 部署Evidence合约
""""""""""

此步骤可根据群组Id部署Evidence合约，如果您的机构不需要部署Evidence合约，只需要启用群组管理员部署的Evidence合约，请跟当前群组Id的管理员机构确认CNS编码，确认后再点击启用即可。


附
""""""""""""""""""""""""""""""""

1. 请访问主页通过指引来完成配置和部署合约等相关操作。

* 需要提前准备区块链节点

* 需要有可访问的MySql数据库


2. 合约部署完成将会生成相关的配置文件:

* admin密钥文件目录 : ./output/admin/

* 资源文件目录(用于应用集成SDK): ./resources/
