如何运行单元测试
================

LINUX 系统上通过 gradle 执行单元测试
------------------------------------

前提条件
~~~~~~~~

单元测试的运行需要提前安装部署好WeIdentity JAVA SDK，请参考 
\ `安装部署工具方式 <./weidentity-installation-by-sourcecode.html>`__\
中的源码方式安装部署WeIdentity。

流程
~~~~

下载源代码进行安装部署后，以 ``WeIdentity`` 根目录为起点：

1, 拷贝秘钥文件到 ``src/test/resources`` 目录。

.. code:: shell

   cp ecdsa_key src/test/resources/

1, 进入dist/conf目录。

.. code:: shell

   cd dist/conf

2, 将生成好的 ``weidentity.properties`` 和 ``fisco.properties`` 复制到 ``src/test/resources``
目录，WeIdentity 安装部署完会自动生成并配置好
``weidentity.properties`` 和 ``fisco.properties``\ ，所需的节点配置和合约地址配置已完成,可以直接使用。

.. code:: shell

   cp fisco.properties weidentity.properties  ../../src/test/resources/

3, 将证书文件复制到 ``src/test/resources`` 目录，证书是 WeIdentity 运行所需要的 SDK 证书。

* 如果您使用的是 FISCO BCOS2.X 版本，请将您的 ``ca.crt``、``node.crt`` 和 ``node.key`` 复制到 ``src/test/resources`` 目录。

.. code:: shell

   cp ca.crt node.crt node.key  ../../src/test/resources/

4, 将字体 ``NotoSansCJKtc-Regular.ttf`` 复制到 ``src/test/resources`` 目录，并进行字体安装。

注：字体安装请参照 \ `技术问题列表 <./faq.html>`__\  中的字体安装说明。

5, 回到项目根目录，执行测试命令。

.. code:: shell

   cd ../../
   ./gradlew test

