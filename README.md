# Cryptocurrencies exchange rates API
* Uses the public [CryptoCompare price API](https://min-api.cryptocompare.com/documentation?key=Price&cat=singleSymbolPriceEndpoint) instead of the unavailable Cryptonator API.
* The information should be updated every 5 minutes.
* Ensure that your project architecture is as clean as possible
    * **Note:**
        * `Repository` implements [CRUD](https://en.wikipedia.org/wiki/Create,_read,_update_and_delete) functionality.
        * `Service` implements business logic by manipulating a composite `Repository`
        * `Controller` hosts client-accessible [End Points](https://stackoverflow.com/questions/10799198/what-are-rest-resources)
        * `Domain` or `Model` are object-representations of our data 

## Endpoint

`GET /cryptocurrencies/{base}/{quote}`

Example:

`GET /cryptocurrencies/btc/usd`

Sample response:

```json
{
  "base": "BTC",
  "quote": "USD",
  "amount": 64321.12
}
```

## Configuration

By default the app calls:

`https://min-api.cryptocompare.com/data/price`

You can override the upstream API base URL with the `CRYPTO_PRICE_API_BASE_URL` environment variable:

```bash
export CRYPTO_PRICE_API_BASE_URL=https://min-api.cryptocompare.com/data/price
```

No startup fetch is performed anymore, so the application can start even if the external price API is temporarily unavailable. The external API is only called when `/cryptocurrencies/{base}/{quote}` is requested.
