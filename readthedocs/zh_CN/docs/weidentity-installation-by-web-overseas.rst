.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity 安装部署工具使用文档（部署WeIdentity的服务器在海外的情况）
======================================================================


1.下载安装脚本
"""""""""""""""

.. code-block:: shell

  wget -c https://github.com/WeBankFinTech/weid-build-tools/raw/master/common/script/install/weid_install.sh


2.执行安装脚本
""""""""""""""""

.. code-block:: shell

  chmod u+x weid_install.sh
  ./weid_install.sh -t en
  cd weid-build-tools

.. note::

     - 默认会下载最新版本的可视化安装工具，如果想指定安装版本，可以执行：:code:`./weid_install.sh -v 1.0.12 -t en`。

     - `-t参数`说明：指定依赖下载源，en : 指向海外的源， cn : 指向中国内地的源，国内用户建议使用 cn 或者不指定依赖源，国外用户建议使用 en。默认值是cn。


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

通过 IP 访问 :code:`http://ip:6021` ，将开启可视化安装部署。


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
