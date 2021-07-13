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


## 数据结构的序列化/反序列化

生成数据结构的 BCS 序列化/反序列化代码（Java 语言版本）：

```
serdegen --language java --module-name org.starcoin.base --with-runtimes=Serde --target-source-dir ./src/main/java ./generate-format/starcoin_types.yaml
serdegen --language java --module-name org.starcoin.base --with-runtimes=Bcs --target-source-dir ./src/main/java ./generate-format/starcoin_types.yaml
```

相关链接：

https://crates.io/crates/serde-generate/0.9.0
https://lib.rs/crates/bcs
https://github.com/novifinancial/serde-reflection/blob/master/serde-generate/README.md#quick-start-with-python-and-bincode

