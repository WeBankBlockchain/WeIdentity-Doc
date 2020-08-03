.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity 安装部署工具使用文档（可视化安装部署方式）
=====================================================

整体介绍
--------

可视化版本只需要简单的几步就可以完成安装，其他的操作都可以在网页上面进行完成，大大降低了操作难度。

.. note::
     注意: 建议在内网环境搭建安装部署。启动的可视化安装工具默认会监听6102端口，不能对外网开放访问。

服务器在中国内地的用户安装步骤：
---------------------------------


.. note::
     服务器在海外的用户，可以参考这个文档，安装速度可能会更快： `WeIdentity 安装部署工具海外用户安装步骤： <./weidentity-installation-by-web-overseas.html>`_ 。


1.下载安装脚本
"""""""""""""""

.. code-block:: shell

  wget -c https://www.fisco.com.cn/cdn/weevent/weidentity/download/releases/weid-install.sh; chmod u+x weid-install.sh


2.执行安装脚本
"""""""""""""""

.. code-block:: shell

  ./weid-install.sh
  cd weid-build-tools

.. note::
     - 默认会下载最新版本的可视化安装工具，如果想指定安装版本，可以通过 :code:`-v` 指定”WeIdentity 安装部署工具“的版本号（注意，不是 WeIdentity Java SDK 的版本号），例如执行： :code:`./weid-install.sh -v 1.0.12`。

3.启动Web服务
""""""""""""""

.. code-block:: shell

  ./start.sh

出现下列输出，则表示Web服务启动成功。

.. code-block:: shell

    the server start successfully.
    the server url:  http://127.0.0.1:6102/index.html


4.安装完成
""""""""""""""

安装已经完成，接下来：

* 可以使用可视化的方式完成 WeIdentity 的部署：通过 :code:`http://IP:6102/index.html` （IP 替换为自己服务器的IP）访问部署工具的 Web 页面，详见文档：\ `部署文档（可视化部署方式） <./deploy-via-web.html>`_\，完成 WeIdentity 的部署和其他相关配置。

* 也可以使用命令行的方式完成部署，详见文档：\ `部署文档（命令行部署方式） <./deploy-via-commandline.html>`_\ ，完成 WeIdentity 的部署和其他相关配置。
