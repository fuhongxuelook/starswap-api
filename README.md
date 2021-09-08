# starswap-api

## 文件说明

test-data.sql 中包含测试用的数据。


## API 示例说明

取 Token 的列表：

```
http://localhost:8600/v1/starswap/tokens
```

取一个 Token 的的详细信息（假设 TokenId 为 `Bot`）：

```
http://localhost:8600/v1/starswap/tokens/Bot
```

取 Liquidity Token 的列表：

```
http://localhost:8600/v1/starswap/liquidityTokens
```

取一个 Liquidity Token 的信息（目前一个 Token Pair 只支持对应一个 Liquidity Token，故可以使用参数 `{TokenId_X}:{TokenId_Y}` 获取）：

```
http://localhost:8600/v1/starswap/liquidityTokens/Bot:Ddd
```

获取流动性池子的列表：

```
http://localhost:8600/v1/starswap/liquidityPools
```

获得某个交易池子的信息（目前一个 Token Pair 只支持一个池子，故可以使用参数 `{TokenId_X}:{TokenId_Y}` 获取）：

```
http://localhost:8600/v1/starswap/liquidityPools/Bot:Ddd
```

取得某个账号地址注入的流动性列表：

```
http://localhost:8600/v1/starswap/liquidityAccounts?accountAddress=0x598b8cbfd4536ecbe88aa1cfaffa7a62
```

取得 Farm 的列表：

```
http://localhost:8600/v1/starswap/lpTokenFarms
```

取得某个 Farm 的信息：

```
http://localhost:8600/v1/starswap/lpTokenFarms/Bot:Ddd
```

取得某个账号地址抵押的 Farm 的列表：

```
http://localhost:8600/v1/starswap/lpTokenFarmAccounts?accountAddress=0x598b8cbfd4536ecbe88aa1cfaffa7a62
```

取得所有 Farm 的 TVL：

```
http://localhost:8600/v1/starswap/farmingTvlInUsd
```

更多 API 见 Swagger UI：

```
http://localhost:8600/swagger-ui/index.html
```

## 数据结构的序列化/反序列化

相关链接：

* https://crates.io/crates/serde-generate/0.9.0
* https://lib.rs/crates/bcs
* https://github.com/novifinancial/serde-reflection/blob/master/serde-generate/README.md#quick-start-with-python-and-bincode

