# starswap-api

## 文件说明

test-data.sql 中包含测试用的数据。


## API 示例说明

取 Token 的列表：

```
http://localhost:8600/v1/starswap/tokens
```

取一个 Token 的的详细信息：

```
http://localhost:8600/v1/starswap/tokens/Bot
```

取 Token Pair 的列表：

```
http://localhost:8600/v1/starswap/tokenPairs
```

取一个 TokenPair 的信息：

```
http://localhost:8600/v1/starswap/tokenPairs/Bot:Ddd
```

获取交易池子的列表：

```
http://localhost:8600/v1/starswap/tokenPairPools
```

获得某个交易池子的信息：

```
http://localhost:8600/v1/starswap/tokenPairPools/0x07fa08a855753f0ff7292fdcbe871216::Bot:Ddd
```

取得某个账号地址注入的流动性：

```
http://localhost:8600/v1/starswap/liquidityAccounts?accountAddress=0x07fa08a855753f0ff7292fdcbe871216
```




