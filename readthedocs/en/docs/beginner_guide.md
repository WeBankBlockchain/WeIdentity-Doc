# Beginner's Guide
---
## Installation Guide
This guide gives an instruction on installing and running WeIdentity
in simple ways.
### 1. Prerequisites and Check List
+ Please prepare a server with internet access. You may find more details from below check list.

| NO.| Type          | Entry                     | Version/Configuration                                              | Remarks                                         |
| :--| :------------- | :----------------------- | :--------------------------------------------------------- | :----------------------------------------------- |  
| 1. | Infrastructure | Server Operation System  | Ubuntu(18.04.1 64bit)                                      | See more [supported operation systems](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/hardware_requirements.html).            |
| 2. | Infrastructure | Server Configuration     | 1 virtual machine with 2 cores CPU,4G memory,50G data disk | See more [recommended configurations](https://fisco-bcos-documentation.readthedocs.io/zh_CN/latest/docs/manual/hardware_requirements.html).              |
| 3. | Infrastructure | Network Configuration    | See Network Requirements below.                            |  |
| 4. | Dependency     | Software Installation    | Latest version of OPENJDK, 8+.                             | Required by WeIdentity. See more [supported JDK versions](https://weidentity.readthedocs.io/zh_CN/latest/docs/weid-compatibility-test.html).|
| 5. | Dependency     | Software Installation    | Gradle v6.4                                                | Required by WeIdentity JAVA SDK. Visit [Gradle website](https://gradle.org/) for more details if needed. |
| 6. | Dependency     | Software Installation    | Mysql v5.7.30                                              | Required by WeIdentity. Visit [Mysql website](https://www.mysql.com/) for more details if needed. |
| 7. | Blockchain     | FISCO BCOS Installation  | FISCO BCOS v2.5                                            |   |
| 8. | WeIdentity     | WeIdentity Installation  | Latest version of WeIdentity Web Tool, v1.0+                 | For WeIdentity Deployment |
| 9. | WeIdentity     | WeIdentity Deployment    | Latest version of WeIdentity, v1.6+.                       | Including  WeIdentity JAVA SDK and WeIdentity Contract. |

*Network Requirements:*
 + *Enable server to download related installation files from internet.*
 + *Enable your browser to visit WeIdentity Web Tool GUI via http.*
 + *Enable communication among WeIdentity and FISCO BCOS blockchain nodes.*

### 2. Installation
#### 1) Install Dependencies
+ Run below commands one by one to install Openssl,Curl,Git,Openjdk,Mysql,Sdkman and Gradle on the server.
```
sudo apt install -y openssl curl
sudo apt install -y git
sudo apt install -y default-jdk
sudo apt-get install mysql-server-5.7
sudo apt install -y unzip
sudo apt install -y zip
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install gradle 6.4
```
+  Run below commands to setup Mysql database.
```
sudo mysql -u root
```
```
create database weid;
GRANT ALL PRIVILEGES ON weid.* TO weid@"%" IDENTIFIED BY "password";
exit;
```
+ Run below commands to check if the depandencies have been installed successfully.
```
sdk version
java -version
gradle -v
sudo mysql -v
```

#### 2) Install FISCO BCOS and WeIdentity

| NO. | Module               | Version | Description                            |
| :-- | :------------------- | :------ | :------------------------------------  |
| 1.  | FISCO BCOS           | v2.5    | Latest version by 2020.07              |
| 2.  | Web3SDK              | v2.4.1  | Latest version by 2020.07              |
| 3.  | FISCO BCOS Console   | v1.0.9  | Latest version by 2020.07              |
| 4.  | WeIdentity Contract  | v1.2.21 | Latest version by 2020.07              |
| 5.  | WeIdentity JAVA SDK  | v1.6.4  | Latest version by 2020.07              |
| 6.  | WeIdentity Web Tool  | v1.0.12 | Latest version by 2020.07              |

+ Run below command to install FISCO BCOS and FISCO BCOS Console.
```
cd ~ && mkdir -p fisco && cd fisco &&
curl -LO https://github.com/FISCO-BCOS/FISCO-BCOS/releases/download/v2.5.0/build_chain.sh && chmod u+x build_chain.sh &&
bash build_chain.sh -l "127.0.0.1:4" -p 30300,20200,8545 -v 2.5.0 &&
bash nodes/127.0.0.1/start_all.sh &&
cd ~/fisco && curl -LO https://github.com/FISCO-BCOS/console/releases/download/v1.0.9/download_console.sh && bash download_console.sh &&
cp -n console/conf/applicationContext-sample.xml console/conf/applicationContext.xml &&
cp nodes/127.0.0.1/sdk/* console/conf/
```
+ Run below commands one by one to check if FISCO BCOS has been installed successfully.
```
ps -ef | grep -v grep | grep fisco-bcos
tail -f ~/fisco/nodes/127.0.0.1/node0/log/log*  | grep connected
tail -f ~/fisco/nodes/127.0.0.1/node0/log/log*  | grep +++
```
+ Run below command to install WeIdentity Web Tool.
```
cd ~ && mkdir -p weid && cd weid &&
sudo apt install git &&
wget -c https://github.com/WeBankBlockchain/weid-build-tools/raw/master/common/script/install/weid-install.sh &&
chmod u+x weid-install.sh &&
cd ~/weid && ./weid-install.sh -t en &&
cd weid-build-tools &&
./start.sh
```   
There are two ways to deploy WeIdentity JAVA SDK and WeIdentity Contracts.   
+ (Option 1) Use command line to deploy.   
 + Update configurations in `~/weid/weid-build-tools/run.config` as below.
```
blockchain_address=127.0.0.1:20200
blockchain_fiscobcos_version=2
org_id=demo
chain_id=1
group_id=1
mysql_address=127.0.0.1:3306
mysql_database=weid
mysql_username=weid
mysql_password=password
cns_profile_active=test
```
  + Copy FISO BCOS blockchain key files
```
cd ~/weid/weid-build-tools &&
cp -f ~/fisco/nodes/127.0.0.1/sdk/ca.crt ~/weid/weid-build-tools/resources &&
cp -f ~/fisco/nodes/127.0.0.1/sdk/node.crt ~/weid/weid-build-tools/resources &&
cp -f ~/fisco/nodes/127.0.0.1/sdk/node.key ~/weid/weid-build-tools/resources
```   
  + Deploy WeIdentity JAVA SDK and WeIdentity Contract.   
```
cd ~/weid/weid-build-tools &&
chmod +x compile.sh &&
./compile.sh &&
chmod +x deploy.sh &&
./deploy.sh
```
The output should be similar to below.
```
contract is deployed with success.
===========================================.
weid contract address is 0x4ba81103afbd5fc203db14322c3a48cd1abb7770
cpt contract address is 0xb1f3f13f772f3fc04b27ad8c377def5bc0c94200
authority issuer contract address is 0xabb97b3042d0f50b87eef3c49ffc8447560faf76
evidence contract address is 0x8cc0de880394cbde18ca17f6ce2cf7af5c51891e
specificIssuer contract address is 0xca5fe4a67da7e25a24d76d24efbf955c475ab9ca
===========================================.
```   
+ (Option 2) Use WeIdentity Web Tool for deployment (in Chinese language).   
 + Open URL `http://{public ip of the server}:6102/guide.html` via browser and follow the guide shown on the web page. (You may copy the values from Option 1)
```
http://127.0.0.1:6102/guide.html
```
 + Choose a role "As a Committee Member"(recommended) or "As a non Committee Member".
 + Setup FISCO BCOS.
 + Define main group id.
 + Setup Mysql (Optional).
 + Click button to generate admin account.
 + Click button to deploy WeIdentity Contracts and WeIdentity JAVA SDK as a Committee Member only.
