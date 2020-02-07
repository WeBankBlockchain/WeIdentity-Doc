WeIdentity Java SDK 与各版本JDK的兼容性
=======================================

备注：本页面所有测试由rockyxia完成。

1.CentOS
--------

::

    测试操作系统： CentOS Linux release 7.2.1511 (Core)

Open JDK 各版本的兼容性 (推荐使用)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Open JDK版本号
     - 测试结果
   * - 1.8.0
     - 验证成功✅
   * - 9.0.1
     - 验证成功✅
   * - 10
     - 验证成功✅
   * - 11
     - 验证成功✅
   * - 12
     - 验证成功✅
   * - 13
     - 验证成功✅

通过 yum 命令下载安装的Open JDK
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

不兼容。

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - yum open JDK版本号
     - 测试结果
   * - 1.8.0_232
     - 验证失败❌
   * - 11.0.5
     - 验证失败❌

不兼容的原因：由于CentOS的yum仓库的OpenJDK缺少JCE(Java Cryptography
Extension)

Oracle JDK 各版本的兼容性
~~~~~~~~~~~~~~~~~~~~~~~~~

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Oracle JDK版本号
     - 测试结果
   * - 1.8.141
     - 验证成功✅
   * - 1.8.181
     - 验证成功✅
   * - 1.8.231
     - 验证成功✅
   * - 11.0.5
     - 验证成功✅
   * - 13.0.1
     - 验证成功✅

------------

2.Ubuntu
--------

::

    测试操作系统：Ubuntu 16.04.1 LTS

.. _open-jdk-各版本的兼容性-推荐使用-1:

Open JDK 各版本的兼容性 (推荐使用)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Open JDK版本号
     - 测试结果
   * - 1.8.0
     - 验证成功✅
   * - 9.0.1
     - 验证成功✅
   * - 10
     - 验证成功✅
   * - 11
     - 验证成功✅
   * - 12
     - 验证成功✅
   * - 13
     - 验证成功✅



通过 apt-get 命令下载的 Open JDK 各版本的兼容性 (推荐使用)
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - apt-get Open JDK版本号
     - 测试结果
   * - 1.8.0_242
     - 验证成功✅
   * - 9.0.4
     - 验证成功✅
   * - 10.0.2
     - 验证成功✅
   * - 11.0.5
     - 验证成功✅
   * - 12.0.2
     - 验证成功✅
   * - 13.0.1
     - 验证成功✅


部署过程中有告警，告警信息如下：

WARNING: An illegal reflective access operation has occurred WARNING:
Illegal reflective access by io.netty.util.internal.ReflectionUtil
(...)
to constructor java.nio.DirectByteBuffer(long,int) WARNING: Please
consider reporting this to the maintainers of
io.netty.util.internal.ReflectionUtil WARNING: Use –illegal-access=warn
to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future
release


.. _oracle-jdk-各版本的兼容性-1:

Oracle JDK 各版本的兼容性
~~~~~~~~~~~~~~~~~~~~~~~~~

.. list-table::
   :header-rows: 1
   :widths: 30 50

   * - Oracle JDK版本号
     - 测试结果
   * - 1.8.141
     - 验证成功✅
   * - 1.8.181
     - 验证成功✅
   * - 1.8.231
     - 验证成功✅
   * - 11.0.5
     - 验证成功✅
   * - 13.0.1
     - 验证成功✅


告警信息如下：

WARNING: An illegal reflective access operation has occurred WARNING:
Illegal reflective access by io.netty.util.internal.ReflectionUtil
(...)
to constructor java.nio.DirectByteBuffer(long,int) WARNING: Please
consider reporting this to the maintainers of
io.netty.util.internal.ReflectionUtil WARNING: Use –illegal-access=warn
to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future
release
