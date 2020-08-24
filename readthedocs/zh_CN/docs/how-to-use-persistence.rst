如何使数据库
================

前提条件
~~~~~~~~

使用数据库需要提前配置数据库所需环境，请提前自行安装数据库并创建数据库实例及用户，数据库配置请参考 
\ `使用 WeIdentity 部署工具完成部署（可视化部署方式） <./deploy-via-web.html#id8>`__\
安装部署相应数据库。

生成配置文件
~~~~

安装部署后，在 ``weid-build-tools/resource`` 目录下会自动生成 ``weidentity.properties``，所需的数据库配置已完成,其部分数据库参数配置如下：

.. code:: shell

    persistence_type=${PERSISTENCE_TYPE}

    # MySQL connection config
    # Support multiple data source configurations with comma-separated multiple data sources.
    datasource.name=datasource1

    # The configuration of each data source is prefixed by the name of the data source.
    datasource1.jdbc.url=jdbc:mysql://${MYSQL_ADDRESS}/${MYSQL_DATABASE}?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&rewriteBatchedStatements=true&serverTimezone=Asia/Shanghai
    datasource1.jdbc.driver=com.mysql.cj.jdbc.Driver
    datasource1.jdbc.username=${MYSQL_USERNAME}
    datasource1.jdbc.password=${MYSQL_PASSWORD}

    # If you want to configure redis in cluster mode, enter multiple node addresses separated by commas.
    redis.url=${REDIS_ADDRESS}
    redis.password=${REDIS_PASSWORD}

使用说明
~~~~

当前数据库支持mysql及redis，配置数据库时只能选择其中一个，在 ``weidentity.properties`` 中以persistence_type标识数据库类型，persistence具体使用示例如下：

.. code:: java

    //从weidentity.properties中读取数据库类型为mysql还是redis
    String type = PropertyUtils.getProperty("persistence_type");
        if (type.equals("mysql")) {
            persistenceType = PersistenceType.Mysql;
        } else if (type.equals("redis")) {
            persistenceType = PersistenceType.Redis;
        }
        persistence = PersistenceFactory.build(persistenceType);
    }

至此，数据库接口实例化完成，persistence接口具体信息请参考
\ `WeIdentity JAVA SDK文档 <./projects/javasdk/weidentity-java-sdk-doc.html>`__ 。

