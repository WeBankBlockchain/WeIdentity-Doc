.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity 安装部署工具使用文档（可视化安装部署方式）
=====================================================

整体介绍
--------

可视化版本只需要简单的几步就可以完成安装，其他的操作都可以在网页上面进行完成，大大降低了操作难度。

.. note::
     注意: 这里的部署是针对依赖区块链的部署方式，不依赖区块链的WeIdentity版本（3.1.0-rc.1及以上）无需经过可视化部署。建议在内网环境搭建安装部署。启动的可视化安装工具默认会监听6021端口，不能对外网开放访问。

服务器在中国内地的用户安装步骤：
---------------------------------


.. note::
     服务器在海外的用户，可以参考这个文档，安装速度可能会更快： `WeIdentity 安装部署工具海外用户安装步骤： <./weidentity-installation-by-web-overseas.html>`_ 。


1.下载安装脚本
"""""""""""""""

.. code-block:: shell

  wget -c https://gitee.com/WeBank/WeIdentity-Build-Tools/raw/master/common/script/install/weid_install.sh


2.执行安装脚本
"""""""""""""""

.. code-block:: shell

  chmod u+x weid_install.sh
  ./weid_install.sh
  cd weid-build-tools && mkdir -p resources/conf/amop && cp common/script/consumer_p* resources/conf/amop/

.. note::
     - 默认会下载最新版本的可视化安装工具，如果想指定安装版本，可以通过 :code:`-v` 指定”WeIdentity 安装部署工具“的版本号（注意，不是 WeIdentity Java SDK 的版本号），例如执行： :code:`./weid_install.sh -v 1.0.12`。
     - 查看\ `“WeIdentity 部署工具”历史版本 <https://search.maven.org/artifact/com.webank/weid-build-tools>`_\

3.启动Web服务
""""""""""""""

.. code-block:: shell

  ./start.sh

出现下列输出，则表示Web服务启动成功。

.. code-block:: shell

    the server start successfully.
    the server url:  http://127.0.0.1:6021


4.安装完成
""""""""""""""

安装已经完成，接下来：

* 可以使用可视化的方式完成 WeIdentity 的部署：通过 :code:`http://IP:6021` （IP 替换为自己服务器的IP）访问部署工具的 Web 页面，详见文档：\ `部署文档（可视化部署方式） <./deploy-via-web.html>`_\，完成 WeIdentity 的部署和其他相关配置。

* 也可以使用命令行的方式完成部署，详见文档：\ `部署文档（命令行部署方式） <./deploy-via-commandline.html>`_\ ，完成 WeIdentity 的部署和其他相关配置。


插件安装
----

安装fisco-bcos-browser
""""""""""""""""""""""""""""""""

安装fisco-bcos-browser前请使用可视化工具完成数据库配置。

1.执行安装脚本

.. code-block:: shell

    ./weid_install.sh -n webase

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
