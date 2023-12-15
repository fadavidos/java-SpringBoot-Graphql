# GRAPHQL

## Tools

We will use [Altair](https://altairgraphql.dev/#download)

### Query example:

We should put the url in Altair `http://localhost:8080/graphql` 

---
#### Hello schema

You can write this query:

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

---
#### Book schema

You can write this query if you want to filter by the name of the author:

```
query books {
  books(author: "hi"){
    title
    author {
      name
      originCountry
    }
    released {
      year
      printedEdition
      releasedCountry
    }
  }
}
```

Or you can remove the `(author: "hi")` part to get all books.

We can also query using variables and default values, in this case the `releaseYear` has a default value (`2020`)

```
query booksByRelease($releaseYear: Int = 2020, $releasePrintedEdition: Boolean) {
  booksByReleased(
    releasedInput: {
      year: $releaseYear
      printedEdition: $releasePrintedEdition
    }
  ) {
    title
    released {
      year
    }
    author {
      name
    }
  }
}
```

In this case you will have to send the variables, you can define them like this:

```
{
  "releasePrintedEdition": true
}
```