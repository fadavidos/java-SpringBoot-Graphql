# GRAPHQL

## Tools

We will use [Altair](https://altairgraphql.dev/#download)

### Query example:

We should put the url in Altair `http://localhost:8080/graphql` and then write this query:

```
query {
  oneHello {
    ...helloInformation
  }
  allHellos {
    ...helloInformation
  }
}

fragment helloInformation on Hello {
  text
  randomNumber
}
```