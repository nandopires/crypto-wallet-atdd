# Reading Coincap Assets

## GIVEN

The Coincap API provides the endpoint __[http://api.coincap.io/v2/assets](- "#assetEndpoint")__ to get asset information.
For authentication, we must use the Header Authorization with Bearer token __[eaa33432-5fff-4d6b-8e85-52e9897071e9](- "#token")__

## WHEN

Using Coincap API and the query string [search](- "#queryString") with value [BTC](- "#assetSymbol")

## THEN
[ ](- "configure(#assetEndpoint,#token,#queryString,#assetSymbol)")
[ ](- "#btcAsset=getAsset()")
We get __[ ](- "c:echo=#btcAsset.id")__ with USD __[ ](- "c:echo=#btcAsset.coincapPrice")__ price.