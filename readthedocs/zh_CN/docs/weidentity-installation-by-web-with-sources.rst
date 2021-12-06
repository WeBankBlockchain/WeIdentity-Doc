.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-web-with-sources:

‘WeIdentity 部署工具’安装文档（下载源代码进行安装）
=====================================================

整体介绍
--------

可视化版本只需要简单的几步就可以完成安装，其他的操作都可以在网页上面进行完成，大大降低了操作难度。

.. note::
     注意: 建议在内网环境搭建安装部署。启动的可视化安装工具默认会监听6021端口，不能对外网开放访问。

服务器在中国内地的用户安装步骤：
---------------------------------


1.下载源码
"""""""""""""""

.. code-block:: shell

    git clone https://gitee.com/WeBank/WeIdentity-Build-Tools.git


.. note::
    服务器在海外的用户，可以从github上下载： :code:`git clone git@github.com:WeBankBlockchain/WeIdentity-Build-Tools.git`


2.切换分支并给脚本授权
"""""""""""""""""""""""""""

.. code-block:: shell

    cd WeIdentity-Build-Tools
    chmod u+x *.sh


3.编译项目
"""""""""""""""

.. code-block:: shell

    ./build.sh


.. note::
   此步骤将自动下载 gradle 版本, 如果此步骤很慢可自行安装 gradle5.6+ 并执行： :code:`gradle build`


3.启动Web服务
""""""""""""""

.. code-block:: shell

  ./start.sh

出现下列输出，则表示Web服务启动成功。

.. code-block:: shell

    -----------------------------------------------
    The weid-build-tools web server started successfully.
    The weid-build-tools web server url : http://127.0.0.1:6021
    -----------------------------------------------


4.安装完成
""""""""""""""

安装已经完成，接下来：

* 可以使用可视化的方式完成 WeIdentity 的部署：通过 :code:`http://127.0.0.1:6021` （IP 替换为自己服务器的IP）访问部署工具的 Web 页面，详见文档：\ `部署文档（可视化部署方式） <./deploy-via-web.html>`_\，完成 WeIdentity 的部署和其他相关配置。


插件安装
--------

安装fisco-bcos-browser
""""""""""""""""""""""""""""""""

安装fisco-bcos-browser前请使用可视化工具完成数据库配置，并以根目录WeIdentity-Build-Tools为基准做如下操作:

1.进入安装目录，并给予脚本权限

.. code-block:: shell

    cd common/script/install
    chmod u+x *.sh

2.执行安装脚本

.. code-block:: shell

    ./plugin_install.sh -n webase

出现下列输出，则表示Web服务启动成功。

.. code-block:: shell

    --------------------------------------------------------------------------
    fisco-bcos-browser is installed successfully, please go to the fisco-bcos-browser/server directory and start the server.
    Example: cd fisco-bcos-browser/server && ./start.sh
    --------------------------------------------------------------------------

3.进入fisco-bcos-browser服务目录，并且启动服务

.. code-block:: shell

    cd fisco-bcos-browser/server
    ./start.sh

4.可以通过可视化工具进行访问fisco-bcos-browser
