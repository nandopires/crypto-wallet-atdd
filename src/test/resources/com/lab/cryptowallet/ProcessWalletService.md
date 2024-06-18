# Acceptance Process Wallet test

## GIVEN

###
| [add-asset][][id][assetId] | symbol | [quantity][assetQuantity] | [price][assetWalletPrice] |
| --- | --- | ------- | ---------- |
|bitcoin| BTC |0.12345|37870.5058|
|ethereum| ETH |4.89532|2004.9774|

[add-asset]: - "addAsset(#assetId,#assetQuantity,#assetWalletPrice)"
[assetId]: - "#assetId"
[assetQuantity]: - "#assetQuantity"
[assetWalletPrice]: - "#assetWalletPrice"

## WHEN

- Using Coincap Assets API, we get the asset ids [bitcoin](- "#bitcoin") and [ethereum](- "#ethereum") respectively
- Next,	using the Coincap Assets History API, we get the prices	"[56999.9728252053067291](- "#bitcoinCoincapPrice")"[ ](- "setPrice(#bitcoin, #bitcoinCoincapPrice)") and "[2032.1394325557042107](- "#ethereumCoincapPrice")"[ ](- "setPrice(#ethereum, #ethereumCoincapPrice)") respectively

## THEN
- If BTC quantity is [ ](- "#bitcoinObj=getAsset(#bitcoin)")[ ](- "c:echo=#bitcoinObj.quantity") the current position is [7036.646645271595115707395](- "?=calcPosition(#bitcoin)")
- If ETH quantity is [ ](- "#ethereumObj=getAsset(#ethereum)")[ ](- "c:echo=#ethereumObj.quantity") the current position is [9947.972806978589936723924](- "?=calcPosition(#ethereum)")
- Total = Current BTC Position + Current ETH Position, that is equal to [16984.619452250185052431319](- "?=calcTotalWallet()")
- BTC had an expected performance __[151.00](- "?=#bitcoinObj.performance")%__ compared to original price of __"[ ](- "c:echo=#bitcoinObj.price")"__ and 
- ETH had an performance __[101.00](- "?=#ethereumObj.performance")%__ compared to original price of __"[ ](- "c:echo=#ethereumObj.price")"__


## GIVEN

WalletProcessService class

## WHEN

Process method invoked [ ](- "#outputLine=processWallet()")

## THEN

- The expected result is [ ](- "c:echo=#outputLine")