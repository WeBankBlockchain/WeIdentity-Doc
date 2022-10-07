# 权威发布者管理（AuthorityIssuer）

AuthorityIssuer是用来管理发布者的合约，又分为Data和Controller两个合约文件。根据经验，应该先去观察Data对应的合约文件。

## AuthorityIssuerData合约

### 错误码和数据结构

在AuthorityIssuerData合约中，先定义了若干错误码，在不同的错误产生时返回不同的结果。

```js
		// Error codes
    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ALREADY_EXISTS = 500201;
    uint constant private RETURN_CODE_FAILURE_NOT_EXIST = 500202;
    uint constant private RETURN_CODE_NAME_ALREADY_EXISTS = 500203;
    uint constant private RETURN_CODE_UNRECOGNIZED = 500204;
```

在AuthorityIssuerData合约中，定义了一个特别的数据结构AuthorityIssuer，而且在其内部设置了特殊的编码协议以表达权限、命名、时间、标志位等。attribBytes32是16个哈希值组成的数组，前2个哈希值代表了名称和描述，2～11用于附加的信息。attribInt定义了16个属性，前2个分别是创建日期和修改日期，2～11用于附加信息，最后一个属性是用来记录是否为认证状态，1-代表已认证，0-代表未认证。accValue代表账户值。

```js
    struct AuthorityIssuer {
        // [0]: name, [1]: desc, [2-11]: extra string
        bytes32[16] attribBytes32;
        // [0]: create date, [1]: update date, [2-11]: extra int
        // [15]: flag for recognition status (0: unrecognized, 1: recognized)
        int[16] attribInt;
        bytes accValue;
    }
```

### 构造函数和成员变量

接下来是合约的成员变量定义和初始化部分，authorityIssuerMap用于记录发布者及其属性，authorityIssuerArray记录了发布者地址的数组，uniqueNameMap记录名字的唯一映射关系，recognizedIssuerCount用来记录已认证发布者的数量，roleController则是我们熟悉的角色控制器合约对象，它仍然在构造函数内被初始化。

```js
	mapping (address => AuthorityIssuer) private authorityIssuerMap;
    address[] private authorityIssuerArray;
    mapping (bytes32 => address) private uniqueNameMap;
    uint recognizedIssuerCount = 0;

    RoleController private roleController;

    // Constructor
    function AuthorityIssuerData(address addr) public {
        roleController = RoleController(addr);
    }
```

AuthorityIssuerData合约中的函数较多，我们还是先整体说一下。具体函数列表如下：

- isAuthorityIssuer  检查某地址是否为发布者
- isNameDuplicate  名称是否冲突
- addAuthorityIssuerFromAddress  从地址添加发布者
- recognizeAuthorityIssuer  认证一个发布者
- deRecognizeAuthorityIssuer  取消认证发布者
- deleteAuthorityIssuerFromAddress  从地址删除发布者
- getDatasetLength  获取发布者动态数组的元素个数
- getAuthorityIssuerFromIndex  通过索引获得发布者的地址
- getAuthorityIssuerInfoNonAccValue  获取发布者除了AccValue外的属性
- getAuthorityIssuerInfoAccValue  获取发布者的accValue属性
- getAddressFromName  通过名字获得地址
- getRecognizedIssuerCount  获取认证发布者的数量

### isAuthorityIssuer

***isAuthorityIssuer***函数用来验证地址是否是一个发布者，在合约里会做2个检测，第一个判断addr如果不是一个发布者则返回false；如果addr的attribBytes32[0]没有设置属性，那么也会false。默认情况下，就会返回true。

```js
    function isAuthorityIssuer(
        address addr
    ) 
        public 
        constant 
        returns (bool) 
    {
        if (!roleController.checkRole(addr, roleController.ROLE_AUTHORITY_ISSUER())) {
            return false;
        }
        if (authorityIssuerMap[addr].attribBytes32[0] == bytes32(0)) {
            return false;
        }
        return true;
    }
```

### isNameDuplicate

***isNameDuplicate***函数用来判断名称是否重复，这个检测只需要通过判断uniqueNameMap中是否存在初始化的数据就可以了。

```js
    function isNameDuplicate(
        bytes32 name
    )
        public
        constant
        returns (bool) 
    {
        if (uniqueNameMap[name] == address(0x0)) {
            return false;
        }
        return true;
    }
```

### addAuthorityIssuerFromAddress

***addAuthorityIssuerFromAddress***函数用于添加一个发布者，它需要检测该账户未添加认证，并且名称没有冲突。检测通过后，可以维护authorityIssuerMap、authorityIssuerArray、uniqueNameMap。authorityIssuerMap记录地址和发布者的映射，authorityIssuerArray记录发布者的全部地址，uniqueNameMap则是记录名字与地址的映射关系。

```js
    function addAuthorityIssuerFromAddress(
        address addr,
        bytes32[16] attribBytes32,
        int[16] attribInt,
        bytes accValue
    )
        public
        returns (uint)
    {
        if (authorityIssuerMap[addr].attribBytes32[0] != bytes32(0)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        if (isNameDuplicate(attribBytes32[0])) {
            return RETURN_CODE_NAME_ALREADY_EXISTS;
        }

        // Actual Role must be granted by calling recognizeAuthorityIssuer()
        // roleController.addRole(addr, roleController.ROLE_AUTHORITY_ISSUER());

        AuthorityIssuer memory authorityIssuer = AuthorityIssuer(attribBytes32, attribInt, accValue);
        authorityIssuerMap[addr] = authorityIssuer;
        authorityIssuerArray.push(addr);
        uniqueNameMap[attribBytes32[0]] = addr;
        return RETURN_CODE_SUCCESS;
    }
```

### recognizeAuthorityIssuer

***recognizeAuthorityIssuer***函数用于认证一个发布者，也就是将一个地址设置为发布者。它和addAuthorityIssuerFromAddress函数的区别是对执行者有权限检测。它首先通过roleController来检测权限，接着检查该地址是否已经设置了属性。检测通过后，需要维护recognizedIssuerCount、authorityIssuerMap，同时也用roleController来添加角色。authorityIssuerMap[addr].attribInt[15] = int(1);这行代码是根据前面定义的结构协议来决定的。

```js
    function recognizeAuthorityIssuer(address addr) public returns (uint) {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        if (authorityIssuerMap[addr].attribBytes32[0] == bytes32(0)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        // Set role and flag
        roleController.addRole(addr, roleController.ROLE_AUTHORITY_ISSUER());
        recognizedIssuerCount = recognizedIssuerCount + 1;
        authorityIssuerMap[addr].attribInt[15] = int(1);
        return RETURN_CODE_SUCCESS;
    }
```

### deRecognizeAuthorityIssuer

***deRecognizeAuthorityIssuer***函数用于取消认证一个发布者，它需要判断执行者是否具备MODIFY_AUTHORITY_ISSUER的角色权限，接下来就是通过roleController删除角色，并且调整recognizedIssuerCount和authorityIssuerMap。

```js
    function deRecognizeAuthorityIssuer(address addr) public returns (uint) {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        // Remove role and flag
        roleController.removeRole(addr, roleController.ROLE_AUTHORITY_ISSUER());
        recognizedIssuerCount = recognizedIssuerCount - 1;
        authorityIssuerMap[addr].attribInt[15] = int(0);
        return RETURN_CODE_SUCCESS;
    }
```

### deleteAuthorityIssuerFromAddress

***deleteAuthorityIssuerFromAddress***函数用于删除一个发布者，它需要验证该地址的属性没有设置过，并且执行者具备MODIFY_AUTHORITY_ISSUER的权限。接下来几个动作分别是removeRole删除角色，维护recognizedIssuerCount、authorityIssuerMap，由于authorityIssuerArray是一个数组，因此需要将该元素置换到最后一个元素，然后delete掉它。

```js
    function deleteAuthorityIssuerFromAddress(
        address addr
    ) 
        public 
        returns (uint)
    {
        if (authorityIssuerMap[addr].attribBytes32[0] == bytes32(0)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_AUTHORITY_ISSUER())) {
            return roleController.RETURN_CODE_FAILURE_NO_PERMISSION();
        }
        roleController.removeRole(addr, roleController.ROLE_AUTHORITY_ISSUER());
        
        if (authorityIssuerMap[addr].attribInt[15] == int(1)) {
            recognizedIssuerCount = recognizedIssuerCount - 1;
        }
        
        uniqueNameMap[authorityIssuerMap[addr].attribBytes32[0]] = address(0x0);
        delete authorityIssuerMap[addr];
        uint datasetLength = authorityIssuerArray.length;
        for (uint index = 0; index < datasetLength; index++) {
            if (authorityIssuerArray[index] == addr) { 
                break; 
            }
        } 
        if (index != datasetLength-1) {
            authorityIssuerArray[index] = authorityIssuerArray[datasetLength-1];
        }
        delete authorityIssuerArray[datasetLength-1];
        authorityIssuerArray.length--;
        return RETURN_CODE_SUCCESS;
    }
```

### getDatasetLength

***getDatasetLength***函数用于获取authorityIssuerArray数组的元素个数。

```js
    function getDatasetLength() 
        public 
        constant 
        returns (uint) 
    {
        return authorityIssuerArray.length;
    }
```

### getAuthorityIssuerFromIndex

***getAuthorityIssuerFromIndex***函数用于通过索引获得发布者数组中的地址。

```js
    function getAuthorityIssuerFromIndex(
        uint index
    ) 
        public 
        constant 
        returns (address) 
    {
        return authorityIssuerArray[index];
    }
```

### getAuthorityIssuerInfoNonAccValue

***getAuthorityIssuerInfoNonAccValue***函数用于获取authorityIssuerMap这个mapping中设置的属性，但是它不返回accValue这个属性。

```js
    function getAuthorityIssuerInfoNonAccValue(
        address addr
    )
        public
        constant
        returns (bytes32[16], int[16])
    {
        bytes32[16] memory allBytes32;
        int[16] memory allInt;
        for (uint index = 0; index < 16; index++) {
            allBytes32[index] = authorityIssuerMap[addr].attribBytes32[index];
            allInt[index] = authorityIssuerMap[addr].attribInt[index];
        }
        return (allBytes32, allInt);
    }
```

### getAuthorityIssuerInfoAccValue

***getAuthorityIssuerInfoAccValue***函数用于获取该地址的accValue属性。

```js
    function getAuthorityIssuerInfoAccValue(
        address addr
    ) 
        public 
        constant 
        returns (bytes) 
    {
        return authorityIssuerMap[addr].accValue;
    }
```

### getAddressFromName

***getAddressFromName***函数通过name来获取其对应的地址。

```js
    function getAddressFromName(
        bytes32 name
    )
        public
        constant
        returns (address)
    {
        return uniqueNameMap[name];
    }
```

### getRecognizedIssuerCount

***getRecognizedIssuerCount***函数用于获取认证的发布者数量。

```js
    function getRecognizedIssuerCount() 
        public 
        constant 
        returns (uint) 
    {
        return recognizedIssuerCount;
    }
```

上述是AuthorityIssuerData合约的全部内容，接下来我们介绍AuthorityIssuerController合约。

## AuthorityIssuerController合约

### 构造函数和初始化

AuthorityIssuerController合约包含了AuthorityIssuerData和RoleController，所以在合约一开始定义了authorityIssuerData和roleController两个对象，并在构造函数内对其进行了初始化。像大多数控制器一样，在合约内定义了event用来处理日志，并为事件定义了2个返回码OPERATION_ADD和OPERATION_REMOVE。EMPTY_ARRAY_SIZE虽然和事件响应码放在了一起，它的作用是用来初始化数组的大小。

```js
    AuthorityIssuerData private authorityIssuerData;
    RoleController private roleController;

    // Event structure to store tx records
    uint constant private OPERATION_ADD = 0;
    uint constant private OPERATION_REMOVE = 1;
    uint constant private EMPTY_ARRAY_SIZE = 1;

    event AuthorityIssuerRetLog(uint operation, uint retCode, address addr);
		// Constructor.
    function AuthorityIssuerController(
        address authorityIssuerDataAddress,
        address roleControllerAddress
    ) 
        public 
    {
        authorityIssuerData = AuthorityIssuerData(authorityIssuerDataAddress);
        roleController = RoleController(roleControllerAddress);
    }
```

### 函数综述

阅读AuthorityIssuerController合约中的函数，发现大部分都和AuthorityIssuerData合约有关（调用AuthorityIssuerData合约中的函数），以addAuthorityIssuer函数为例来说，它调用了authorityIssuerData合约的addAuthorityIssuerFromAddress函数，并且输出一个AuthorityIssuerRetLog事件。recognizeAuthorityIssuer、deRecognizeAuthorityIssuer、removeAuthorityIssuer、getTotalIssuer、getAuthorityIssuerInfoNonAccValue、isAuthorityIssuer、getAddressFromName、getRecognizedIssuerCount等函数都是如此，也就不再展开说了。

### getAuthorityIssuerAddressList

这里重点要说getAuthorityIssuerAddressList这个函数，它的目标是获取发布者地址列表，但它实现了分页功能。比如整个地址列表有100个，要求每次取10个，就需要这样的设计来实现分页。输入参数的startPos代表起始位置，num则代表本次要获取的数量。先通过比较总量和startPos以及num的关系来决定返回数据的数量，然后在发布者数组列表中按照偏移+编号的方式获取目标的地址列表返回。

```js
    function getAuthorityIssuerAddressList(
        uint startPos,
        uint num
    ) 
        public 
        constant 
        returns (address[]) 
    {
        uint totalLength = authorityIssuerData.getDatasetLength();

        uint dataLength;
        // Calculate actual dataLength
        if (totalLength < startPos) {
            return new address[](EMPTY_ARRAY_SIZE);
        } else if (totalLength <= startPos + num) {
            dataLength = totalLength - startPos;
        } else {
            dataLength = num;
        }

        address[] memory issuerArray = new address[](dataLength);
        for (uint index = 0; index < dataLength; index++) {
            issuerArray[index] = authorityIssuerData.getAuthorityIssuerFromIndex(startPos + index);
        }
        return issuerArray;
    }
```



