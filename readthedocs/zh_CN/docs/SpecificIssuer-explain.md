# SpecificIssuer

> WeIdentity支持为每位Authority Issuer在链上声明所属类型，即Specific Issuer。您可以指定某位Authority Issuer的具体类型属性，如学校、政府机构、医院等。当前，此属性与其对应的权限没有直接关系，仅作记录之目的。

SpecificIssuer合约也是由Data和Controller来组成，按照惯例，还是先看一下Data文件。

## SpecificIssuerData合约
### 错误码和结构定义

在SpecificIssuerData合约一开始，预先定义了一些错误码。

```js
		// Error codes
    uint constant private RETURN_CODE_SUCCESS = 0;
    uint constant private RETURN_CODE_FAILURE_ALREADY_EXISTS = 500501;
    uint constant private RETURN_CODE_FAILURE_NOT_EXIST = 500502;
    uint constant private RETURN_CODE_FAILURE_EXCEED_MAX = 500503;
    uint constant private RETURN_CODE_FAILURE_NO_PERMISSION = 500000;
    uint constant private RETURN_CODE_FAILURE_DEL_EXIST_ISSUER = 500504;
```

SpecificIssuerData合约的数据定义部分包含IssuerType结构体，用typeName的哈希值来做主键，在IssuerType结构内还包含同伴数组、附加信息、属主和创建时间等。并用一个issuerTypeMap来记录哈希值主键和IssuerType结构的映射关系。

```js
    struct IssuerType {
        // typeName as index, dynamic array as getAt function and mapping as search
        bytes32 typeName;
        address[] fellow;
        mapping (address => bool) isFellow;
        bytes32[8] extra;
        address owner;
        uint256 created;
    }

    mapping (bytes32 => IssuerType) private issuerTypeMap;
    bytes32[] private typeNameArray;
```

接下来说说Data合约内的函数，包含以下功能：

- registerIssuerType  注册一个类型
- removeIssuerType  移除一个类型
- getTypeNameSize   获得类型定义的个数
- getTypInfoByIndex  通过编号获得类型信息
- addExtraValue  添加附加信息
- getExtraValue  获取附加信息
- isIssuerTypeExist  判断类型是否存在
- addIssuer  添加同伴
- removeIssuer  移除同伴
- isSpecificTypeIssuer  判断是否为特定类型
- getSpecificTypeIssuers  分页方式获取同伴
- getSpecificTypeIssuerLength 获得同伴数量

### registerIssuerType

***registerIssuerType***函数用于注册一个特定类型，先要检查该类型之前并未注册过，调用了合约内的isIssuerTypeExist函数，接下来就是维护issuerTypeMap和typeNameArray的数据，该类型的所有者是原始执行者，同伴和附加信息都为空。

```js
    function registerIssuerType(bytes32 typeName) public returns (uint) {
        if (isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        address[] memory fellow;
        bytes32[8] memory extra;
        IssuerType memory issuerType = IssuerType(typeName, fellow, extra, tx.origin, now);
        issuerTypeMap[typeName] = issuerType;
        typeNameArray.push(typeName);
        return RETURN_CODE_SUCCESS;
    }
```

### removeIssuerType
***removeIssuerType***函数用于移除一个已经注册的类型，移除前要检查该类型已经注册过，并且该类型的同伴为空，同时仅有类型的所有者可以移除。对于issuerTypeMap直接使用delete语句就可以删除，对于typeNameArray的元素删除同样需要将目标元素置换到数组末尾，然后执行delete操作。

```js
    function removeIssuerType(bytes32 typeName) public returns (uint) {
        if (!isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        if (issuerTypeMap[typeName].fellow.length != 0) {
            return RETURN_CODE_FAILURE_DEL_EXIST_ISSUER;
        }
        if (issuerTypeMap[typeName].owner != tx.origin) {
            return RETURN_CODE_FAILURE_NO_PERMISSION;
        }
        delete issuerTypeMap[typeName];
        uint datasetLength = typeNameArray.length;
        for (uint index = 0; index < datasetLength; index++) {
            if (typeNameArray[index] == typeName) {
                break;
            }
        }
        if (index != datasetLength-1) {
            typeNameArray[index] = typeNameArray[datasetLength-1];
        }
        delete typeNameArray[datasetLength-1];
        typeNameArray.length--;
        return RETURN_CODE_SUCCESS;
    }
```

### getTypeNameSize
***getTypeNameSize***函数用于获得已经注册的类型个数。

```js
    function getTypeNameSize() public returns (uint) {
        return typeNameArray.length;
    }
```

### getTypInfoByIndex
***getTypInfoByIndex***函数用于通过索引编号获得类型注册的相关信息，包括名字哈希、所有者和创建时间。

```js
    function getTypInfoByIndex(uint index) public returns (bytes32, address, uint256) {
      bytes32 typeName = typeNameArray[index];
      IssuerType memory issuerType = issuerTypeMap[typeName];
      return (typeName, issuerType.owner, issuerType.created);
    }
```

### addExtraValue
***addExtraValue***函数用于添加附加信息，需要传入类型哈希和一条附加信息，由于每个类型的附加信息最多有8条，所以添加上限为8，如果数组内元素已经满了则不能再添加。

```js
    function addExtraValue(bytes32 typeName, bytes32 extraValue) public returns (uint) {
        if (!isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        IssuerType storage issuerType = issuerTypeMap[typeName];
        for (uint index = 0; index < 8; index++) {
            if (issuerType.extra[index] == bytes32(0)) {
                issuerType.extra[index] = extraValue;
                break;
            }
        }
        if (index == 8) {
            return RETURN_CODE_FAILURE_EXCEED_MAX;
        }
        return RETURN_CODE_SUCCESS;
    }
```
### getExtraValue

***getExtraValue***函数用于获得附加信息，函数里使用了extraValues作为临时内存区来存储返回的结果。

```js
    function getExtraValue(bytes32 typeName) public constant returns (bytes32[8]) {
        bytes32[8] memory extraValues;
        if (!isIssuerTypeExist(typeName)) {
            return extraValues;
        }
        IssuerType memory issuerType = issuerTypeMap[typeName];
        for (uint index = 0; index < 8; index++) {
            extraValues[index] = issuerType.extra[index];
        }
        return extraValues;
    }
```

### isIssuerTypeExist

***isIssuerTypeExist***函数用于判断一个类型是否注册过，它的检测依据是mapping取出数据的typeName（哈希值）是否被初始化过。

```js
    function isIssuerTypeExist(bytes32 name) public constant returns (bool) {
        if (issuerTypeMap[name].typeName == bytes32(0)) {
            return false;
        }
        return true;
    }
```

### addIssuer

***addIssuer***函数的作用是添加同伴，添加同伴没有数量上限，具体就是操作issuerTypeMap中的fellow数组，同时维护isFellow这个mapping。

```js
    function addIssuer(bytes32 typeName, address addr) public returns (uint) {
        if (isSpecificTypeIssuer(typeName, addr)) {
            return RETURN_CODE_FAILURE_ALREADY_EXISTS;
        }
        if (!isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        issuerTypeMap[typeName].fellow.push(addr);
        issuerTypeMap[typeName].isFellow[addr] = true;
        return RETURN_CODE_SUCCESS;
    }
```

### removeIssuer

***removeIssuer***函数的作用是移除一个同伴，由于是动态数组，它同样需要将目标地址移动到数组末尾，然后执行delete操作。

```js
    function removeIssuer(bytes32 typeName, address addr) public returns (uint) {
        if (!isSpecificTypeIssuer(typeName, addr) || !isIssuerTypeExist(typeName)) {
            return RETURN_CODE_FAILURE_NOT_EXIST;
        }
        address[] memory fellow = issuerTypeMap[typeName].fellow;
        uint dataLength = fellow.length;
        for (uint index = 0; index < dataLength; index++) {
            if (addr == fellow[index]) {
                break;
            }
        }
        if (index != dataLength-1) {
            issuerTypeMap[typeName].fellow[index] = issuerTypeMap[typeName].fellow[dataLength-1];
        }
        delete issuerTypeMap[typeName].fellow[dataLength-1];
        issuerTypeMap[typeName].fellow.length--;
        issuerTypeMap[typeName].isFellow[addr] = false;
        return RETURN_CODE_SUCCESS;
    }
```

### isSpecificTypeIssuer

***isSpecificTypeIssuer***函数的作用是判断某地址是否是一个同伴，同伴等同于发行者的角色。由于之前添加同伴时已经维护了isFellow，所以在这里直接判断isFellow就可以了。

```js
    function isSpecificTypeIssuer(bytes32 typeName, address addr) public constant returns (bool) {
        if (issuerTypeMap[typeName].isFellow[addr] == false) {
            return false;
        }
        return true;
    }
```

### getSpecificTypeIssuers

***getSpecificTypeIssuers***函数的作用是分页查询给定类型下的同伴，每次最多查询50个，从给定的起始位置开始查询。它的处理思路和之前介绍的分页方式类似，只是这里面固定了最多取50个。

```js
    function getSpecificTypeIssuers(bytes32 typeName, uint startPos) public constant returns (address[50]) {
        address[50] memory fellow;
        if (!isIssuerTypeExist(typeName)) {
            return fellow;
        }

        // Calculate actual dataLength via batch return for better perf
        uint totalLength = getSpecificTypeIssuerLength(typeName);
        uint dataLength;
        if (totalLength < startPos) {
            return fellow;
        } else if (totalLength <= startPos + 50) {
            dataLength = totalLength - startPos;
        } else {
            dataLength = 50;
        }

        // dynamic -> static array data copy
        for (uint index = 0; index < dataLength; index++) {
            fellow[index] = issuerTypeMap[typeName].fellow[index + startPos];
        }
        return fellow;
    }
```

### getSpecificTypeIssuerLength

***getSpecificTypeIssuerLength***函数的作用是获取给定类型下同伴的数量。

```js
    function getSpecificTypeIssuerLength(bytes32 typeName) public constant returns (uint) {
        if (!isIssuerTypeExist(typeName)) {
            return 0;
        }
        return issuerTypeMap[typeName].fellow.length;
    }
```



## SpecificIssuerController合约



### 构造函数和初始化

在SpecificIssuerController合约中，定义了SpecificIssuerData和RoleController对象，用于控制权限和数据操作的调用。在合约一开始，定义了2个动作分别是添加和移除。合约内还定义了SpecificIssuerRetLog事件，来记录操作时的返回信息。

```js
		SpecificIssuerData private specificIssuerData;
    RoleController private roleController;

    // Event structure to store tx records
    uint constant private OPERATION_ADD = 0;
    uint constant private OPERATION_REMOVE = 1;

    event SpecificIssuerRetLog(uint operation, uint retCode, bytes32 typeName, address addr);

    // Constructor.
    function SpecificIssuerController(
        address specificIssuerDataAddress,
        address roleControllerAddress
    )
        public
    {
        specificIssuerData = SpecificIssuerData(specificIssuerDataAddress);
        roleController = RoleController(roleControllerAddress);
    }
```



### 函数概述

在Controller合约内部，大部分函数都是通过Data合约调用完成，有部分写函数同时需要验证执行者的操作权限是否符合角色要求。

以registerIssuerType和isIssuerTypeExist为例，registerIssuerType函数内调用了Data合约的registerIssuerType函数，并且触发了SpecificIssuerRetLog事件。isIssuerTypeExist函数则是直接调用Data合约的isIssuerTypeExist函数即可，像这样的函数不需要角色权限检查。

```js
function registerIssuerType(bytes32 typeName) public {
        uint result = specificIssuerData.registerIssuerType(typeName);
        SpecificIssuerRetLog(OPERATION_ADD, result, typeName, 0x0);
    }

    function isIssuerTypeExist(bytes32 typeName) public constant returns (bool) {
        return specificIssuerData.isIssuerTypeExist(typeName);
    }
```

以addIssuer为例，这类函数需要检查角色权限。它需要先检查执行者是否具备MODIFY_KEY_CPT权限，检查通过后，再调用Data合约的addIssuer函数，同时也会触发SpecificIssuerRetLog的OPERATION_ADD操作事件。

```js
    function addIssuer(bytes32 typeName, address addr) public {
        if (!roleController.checkPermission(tx.origin, roleController.MODIFY_KEY_CPT())) {
            SpecificIssuerRetLog(OPERATION_ADD, roleController.RETURN_CODE_FAILURE_NO_PERMISSION(), typeName, addr);
            return;
        }
        uint result = specificIssuerData.addIssuer(typeName, addr);
        SpecificIssuerRetLog(OPERATION_ADD, result, typeName, addr);
    }
```

其它类似函数套路基本也是如此，接下来特别介绍一下两个分页查询的函数。

### getSpecificTypeIssuerList

***getSpecificTypeIssuerList***函数用来获取特定类型中发布者的列表，由于发布者可能会比较多，所以采用分页技术来处理。前面先检查类型名字是否已经注册过，接下来获取发布者的总人数，根据这个总人数，结合传入参数的起始位置和数量来决定返回列表的数量。

resultArray结构用来存储要返回的结果，Data合约内的getSpecificTypeIssuers函数调用的返回结果将会添加到resultArray中，每次最多也仅会返回50个地址。

```js
    function getSpecificTypeIssuerList(bytes32 typeName, uint startPos, uint num) public constant returns (address[]) {
        if (num == 0 || !specificIssuerData.isIssuerTypeExist(typeName)) {
            return new address[](50);
        }

        // Calculate actual dataLength via batch return for better perf
        uint totalLength = specificIssuerData.getSpecificTypeIssuerLength(typeName);
        uint dataLength;
        if (totalLength < startPos) {
            return new address[](50);
        } else {
            if (totalLength <= startPos + num) {
                dataLength = totalLength - startPos;
            } else {
                dataLength = num;
            }
        }

        address[] memory resultArray = new address[](dataLength);
        address[50] memory tempArray;
        tempArray = specificIssuerData.getSpecificTypeIssuers(typeName, startPos);
        uint tick;
        if (dataLength <= 50) {
            for (tick = 0; tick < dataLength; tick++) {
                resultArray[tick] = tempArray[tick];
            }
        } else {
            for (tick = 0; tick < 50; tick++) {
                resultArray[tick] = tempArray[tick];
            }
        }
        return resultArray;
    }
```



### getIssuerTypeList

getIssuerTypeList函数用于获取特定类型的名称列表、所有者列表和创建时间列表。这里也需要使用分页技术，根据起始位置和数量来决定要返回的数据段，在数据范围确定了之后，就可以根据index来提取对应的特定类型背后的名称、所有者和时间，将它们填写到返回结果数组中就可以了。

```js
    function getIssuerTypeList(
        uint startPos,
        uint num
    )
        public
        constant
        returns (bytes32[] typeNames, address[] owners, uint256[] createds)
    {
        uint totalLength = specificIssuerData.getTypeNameSize();

        uint dataLength;
        // Calculate actual dataLength
        if (totalLength < startPos) {
          return (new bytes32[](0), new address[](0), new uint256[](0));
        } else if (totalLength <= startPos + num) {
          dataLength = totalLength - startPos;
        } else {
          dataLength = num;
        }

        typeNames = new bytes32[](dataLength);
        owners = new address[](dataLength);
        createds = new uint256[](dataLength);
        for (uint index = 0; index < dataLength; index++) {
          (bytes32 typeName, address owner, uint256 created) = specificIssuerData.getTypInfoByIndex(startPos + index);
          typeNames[index] = typeName;
          owners[index] = owner;
          createds[index] = created;
        }
        return (typeNames, owners, createds);
    }
```

