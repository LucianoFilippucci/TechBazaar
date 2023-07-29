# TechBazaar-API
TechBazaar is an E-Commerce Project made for "Piattaforme Software per Applicazioni su Web" course at University of Calabria.

## API-Endpoints
Every endpoint will return a ResponseEntity<ResponseMessage<>> Inside  the ResponseMessage we will have 2 attributes:
1) message (TEXT)
2) isError (BOOLEAN)
This with ResponseEntity.HttpStatus can give you info about the error.

| Endpoint                                             | Use                         | Return Type | Type  |
|------------------------------------------------------|-----------------------------|-------------|-------|
| [/user/cart](#usercart)                              | Get User Cart ID            |             | GET   |
| [/user/cart/add](#usercartadd)                       | Add Element to Cart         |             | POST  |
| [/user/cart/update-element](#usercartupdate-element) | Update Single Cart Element  |             | POST |
| [/user/cart/place-order](#usercartplace-order)       | Complete Order and place it |             | POST  |




### /user/cart

The Request need the parameter "cartId". (with RequestParamenter is intended something like cart?cartId=090720231108470039f6ce67eefe64a0e965aa28437d09797)

An example of the call:
```JSON
{
    "message": {
        "cartId": "090720231108470039f6ce67eefe64a0e965aa28437d09797",
        "createdAt": 1688893727062,
        "updatedAt": 1688893727062,
        "productsInCart": {}
    },
    "error": false
}
```

as we can see we get, inside the message, every info about that cart. When there are elements inside the "productsInCart" we found that parameter as:

```JSON
{
  "productsInCart": {
    "3": 5
  }
}
```
To be changed.


### /user/cart/add

The RequestParameter are (example)

| Name      | Value                                             |
|-----------|---------------------------------------------------|
| cartId    | 090720231108470039f6ce67eefe64a0e965aa28437d09797 |
| productId | 3                                                 |
| qty       | 5                                                 |

while the response will be just a message with a boolean value (true/false) obv if an error occurred the "message" property will have the necessary info's

### /user/cart/update-element
This endpoint is useful when changing the quantity of a product it will set the new quantity, 0 mean remove.

| Name      | Value                                             |
|-----------|---------------------------------------------------|
| cartId    | 090720231108470039f6ce67eefe64a0e965aa28437d09797 |
| productId | 3                                                 |
| qty       | 2                                                 |


### /user/cart/place-order
As the endpoint says, this will place the order.

| Name          | Value                                             |
|---------------|---------------------------------------------------|
| cartId        | 090720231108470039f6ce67eefe64a0e965aa28437d09797 |
| userAddressId | 1 (0 = default)                                   |

