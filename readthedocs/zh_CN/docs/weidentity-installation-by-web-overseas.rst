.. role:: raw-html-m2r(raw)
   :format: html

.. _weidentity-installation-by-sourcecode:

WeIdentity 安装部署工具使用文档（部署WeIdentity的服务器在海外的情况）
======================================================================


1.下载安装脚本
"""""""""""""""

.. code-block:: shell

  wget -c https://github.com/WeBankFinTech/weid-build-tools/raw/master/common/script/install/weid-install.sh


2.执行安装脚本
""""""""""""""""

.. code-block:: shell

  chmod u+x weid-install.sh
  ./weid-install.sh -t en
  cd weid-build-tools
  
.. note::
     
     - 默认会下载最新版本的可视化安装工具，如果想指定安装版本，可以执行：:code:`./weid-install.sh -v 1.0.12 -t en`。
     
     - `-t参数`说明：指定依赖下载源，en : 指向海外的源， cn : 指向中国内地的源，国内用户建议使用 cn 或者不指定依赖源，国外用户建议使用 en。默认值是cn。


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

通过 IP 访问 :code:`http://ip:6102/index.html` ，将开启可视化安装部署。
